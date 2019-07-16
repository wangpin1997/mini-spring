Spring IOC和AOP功能的完成步骤

一.IOC容器
  IOC最关键的两个类，BeanDefinition和BeanFactory
  1.BeanDefinition是spring中我们所说的bean，所有的类都会被spring包装成一个BeanDefinition,存在BeanFactory的实现中。
  2.BeanFactory是所有spring获取bean入口的父接口，如（AnnotationConfigApplicationContext，ClassPathXmlApplicationContext）注解和xml配置等
  在spring中惯用一个套路，以Default开头的都是封装的基本功能，如DefaultListableBeanFactory,他里边就包含了beanFactory所有默认的实现
  
  看看 DefaultListableBeanFactory类中，有一个这个，这个就是存放我们所有bean的Map,键为名称，值为BeanDefinition对象
  private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
  所以我们需要存值取值就只需put()和get()操作,这个当然只是口头理解
  registerBeanDefinition(String str,BeanDefinition bean){}方法就是注册bean，也就是我们所说的put操作
  getBean(String name)方法就是我们所说的获取bean，也就是get()操作，他实现于BeanFactory接口

二.具体调用流程
  1.xml配置
##1.调用源头
```java
public class test{
    ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
}
```    
    

##2.进入ClassPathXmlApplicationContext类的构造方法
```java
public class ClassPathXmlApplicationContext{
     public ClassPathXmlApplicationContext(String configLocation){
            this(configLocation,new AutowireCapableBeanFactory());
        }
        public ClassPathXmlApplicationContext(String configLocation, AbstractBeanFactory beanFactory) {
            super(beanFactory);
            this.configLocation = configLocation;
            refresh();
        }
}
```   
##3.进入AbstractApplicationContext类的refresh()方法
```java
public abstract class AbstractApplicationContext{
    
     public void refresh()  {
            //先加载默认自动装配的bean
            loadBeanDefinitions(beanFactory);
            //再注册bean
            registerBeanPostProcessors(beanFactory);
            //准备工作，记录下容器的启动时间、标记“已启动”状态、处理配置文件中的占位符
            onRefresh();
        }
}
```
## 4.loadBeanDefinitions(AbstractBeanFactory factory)方法调用可以得知这个factory是父类实例的一个new AutowireCapableBeanFactory()；
    它是一个自动装配的类，在spring中常见的如Environment类，Properties类等。
    他交由子类ClassPathXmlApplicationContext来实现
```java
public class ClassPathXmlApplicationContext{
        @Override
        protected void loadBeanDefinitions(AbstractBeanFactory factory){
            XmlBeanDefinitionReader reader=new XmlBeanDefinitionReader(new ResourceLoader());
            reader.loadBeanDefinitions(configLocation);
            for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : reader.getRegistry().entrySet()) {
                beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
            }
        }
}
```    
##    5.在该方法中定义了一个XmlBeanDefinitionReader,可以看出，这就是一个xml解析类，在其的构造中需要传入一个资源解析器

```java
public class ResourceLoader {

    public Resource getResource(String config){
        URL url=this.getClass().getClassLoader().getResource(config);
        return new UrlResource(url);
    }
}

public class UrlResource implements Resource{
    
        private final URL url;
    
        public UrlResource(URL url) {
            this.url = url;
        }
        public InputStream getInputStream() throws IOException {
            URLConnection connection=url.openConnection();
            connection.connect();
            return connection.getInputStream();
        }
}
```    
##   6.就是根据文件URL解析出对应的字节流.接下来调用reader.loadBeanDefinitions(configLocation);  
    
```java
public class XmlBeanDefinitionReader{
        //拿到解析URL得到的字节流，解析bean
        public void loadBeanDefinitions(String config) {
            InputStream inputStream=getResourceLoader().getResource(config).getInputStream();
            doLoadBeanDefinitions(inputStream);
        }
        //解析bean真正的操作，通过dom解析，输出一个Document，去将bean注册到容器中，也就是那个beanMap<String,BeanDefinition>中
        protected void doLoadBeanDefinitions(InputStream inputStream) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);
            // 解析bean
            registerBeanDefinitions(doc);
            inputStream.close();
        }
            //注册bean，拿到document根节点
        public void registerBeanDefinitions(Document doc) {
                Element root = doc.getDocumentElement();
        
                parseBeanDefinitions(root);
            }
            //开始解析节点
        protected void parseBeanDefinitions(Element root) {
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node instanceof Element) {
                        Element ele = (Element) node;
                        processBeanDefinition(ele);
                    }
                }
            }
        
        //拿到beanName和id，这个可以自己拓展，比如没有定义id和name的情况，
        //spring就是如果没有定义name，就取class拼接#+数字作为name（例子：cn.wpin.business.HelloServiceImpl#0）id存在就会作为别名
        //很关键的一步，解析，然后封装成BeanDefinition，再注册
        protected void processBeanDefinition(Element ele) {
                String name = ele.getAttribute("id");
                String className = ele.getAttribute("class");
                BeanDefinition beanDefinition = new BeanDefinition();
                processProperty(ele, beanDefinition);
                beanDefinition.setBeanClassName(className);
                getRegistry().put(name, beanDefinition);
            }
            
            /*  <bean id="helloService" name="helloService" class="cn.wpin.business.HelloServiceImpl">
                      <property name="text" value="Hello World! 1234565"></property>
                      <property name="orderService" ref="orderService"></property>
                  </bean>*/
            //解析bean里边的property标签
        private void processProperty(Element ele, BeanDefinition beanDefinition) {
                NodeList propertyNode = ele.getElementsByTagName("property");
                for (int i = 0; i < propertyNode.getLength(); i++) {
                    Node node = propertyNode.item(i);
                    if (node instanceof Element) {
                        Element propertyEle = (Element) node;
                        String name = propertyEle.getAttribute("name");
                        String value = propertyEle.getAttribute("value");
                        if (value != null && value.length() > 0) {
                            beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
                        } else {
                            String ref = propertyEle.getAttribute("ref");
                            if (ref == null || ref.length() == 0) {
                                throw new IllegalArgumentException("Configuration problem: <property> element for property '"
                                        + name + "' must specify a ref or value");
                            }
                            BeanReference beanReference = new BeanReference(ref);
                            beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
                        }
                    }
                }
            }
}
```    

##    7.再回到AbstractApplicationContext类的refresh()方法（第四步之前）看看registerBeanPostProcessors(beanFactory)方法，调用过程如下
```java
public abstract class AbstractApplicationContext{
    //注册bean的方法
    protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory){
        //此方法是获取是否实现于BeanPostProcessor类的类，也就是增强类,点击进去看看
        List beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
        }
    }   
}
/**
* 这是一个非常重要的类，里边实例化了三个集合
*/
public abstract class AbstractBeanFactory{
        /**
         * 非常关键，这就是存放所有bean的Map
         */
        private Map<String, BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<String, BeanDefinition>(256);
        /**
         * 存放所有beanName的list
         */
        private final List<String> beanDefinitionNames = new ArrayList<String>();
        /**
         * 存放所有增强操作的list
         */
        private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
        
        //遍历，拿到实现于BeanPostProcessor类，并返回
        public  List getBeansForType(Class<?> type) {
            List beans = new ArrayList<Object>();
            for (String beanDefinitionName : beanDefinitionNames) {
                if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                    beans.add(getBean(beanDefinitionName));
                }
            }
            return beans;
        }
        //接上面，在AbstractApplicationContext的registerBeanPostProcessors方法中，
        // 遍历拿到的list，并调用此方法添加到beanPostProcessors中存起来
        public  void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
            this.beanPostProcessors.add(beanPostProcessor);
        }
}
```
##     8.到这一步再回到AbstractApplicationContext类的onRefresh()方法
```java
public abstract class AbstractApplicationContext{
    //继续看，里边就调用了一个preInstantiateSingletons(),见名知意，优先实例化单例。
    protected void onRefresh(){
        beanFactory.preInstantiateSingletons();
    }
} 
public abstract class AbstractBeanFactory{
    //上面的调用到这了，迭代器遍历beanDefinitionNames这个list，然后getBean()
     public void preInstantiateSingletons(){
         for (Iterator it = this.beanDefinitionNames.iterator(); it.hasNext();) {
             String beanName = (String) it.next();
             getBean(beanName);
         }
     }
     //getBean(beanName)方法到这里了，
    public Object getBean(String beanName) {
         //根据beanName,从map中取出对应的bean，为空则抛出异常，这是我自定义的异常
         BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
         if (beanDefinition==null){
             throw new NoSuchBeanDefinitionException(beanDefinition.getBeanClassName());
         }
         //拿到bean对象
         Object bean=beanDefinition.getBean();
         //如果是为空，则创建bean且初始化
         if (bean==null){
             bean =doCreateBean(beanDefinition);
             bean=initializeBean(bean,beanName);
             beanDefinition.setBean(bean);
         }
         return bean;
     }
     
     //接上边，bean为空，创建bean
    protected Object doCreateBean(BeanDefinition beanDefinition){
         //初始化bean，创建其实例
        Object bean=createBeanInstance(beanDefinition);
        beanDefinition.setBean(bean);
        applyPropertyValues(bean,beanDefinition);
        return bean;
    }    
    //再进入到applyPropertyValues方法，这是个钩子方法(模板方法)，交由子类AutowireCapableBeanFactory实现
    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition){
    
    }
    //接下来就进入了此方法，这是对BeanPostProcessor的方法进行执行，前置和后置增强，TODO部分可以自己操作
    //接下来是Aop部分先不说，到此getBean()方法完毕，getBean()的调用处preInstantiateSingletons()方法也完毕
    //onRefresh()方法也执行完毕，refresh()也执行完毕,ClassPathXmlApplicationContext类的构造方法也执行完毕
    //整个IOC容器就起来了
    protected Object initializeBean(Object bean,String name) {
        //分别进行前置和后置增强
        for (BeanPostProcessor processor : beanPostProcessors) {
            bean = processor.postProcessBeforeInitialization(bean, name);
        }
        //todo:
        for (BeanPostProcessor processor : beanPostProcessors) {
            bean = processor.postProcessAfterInitialization(bean, name);
        }
        return bean;
    }    
}

public class AutowireCapableBeanFactory extends AbstractBeanFactory {
    
    //将bean的属性值填充在bean中，具体参考HelloServiceImpl，里边的两个字段值就是通过xml方式解析填充
    //方法执行完往上看
    public void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValueList()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }

            try {
                Method declaredMethod = bean.getClass().getDeclaredMethod(
                        "set" + propertyValue.getName().substring(0, 1).toUpperCase()
                                + propertyValue.getName().substring(1), value.getClass());
                declaredMethod.setAccessible(true);

                declaredMethod.invoke(bean, value);
            } catch (NoSuchMethodException e) {
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }
}

```

##    9.继续看AOP接着上面的方法，点进去方法postProcessBeforeInitialization看其实现类AspectJAwareAdvisorAutoProxyCreator
```java
//见名知意，切面自动装配代理创建器
public class AspectJAwareAdvisorAutoProxyCreator implements BeanFactoryAware, BeanPostProcessor {

    private AbstractBeanFactory beanFactory;


    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory= (AbstractBeanFactory) beanFactory;
    }
    //前置增强，没有做任何修饰
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
    //后置增强，很关键
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //判断bean是否实现于AspectJExpressionPointcutAdvisor类，实现则返回
        if (bean instanceof AspectJExpressionPointcutAdvisor) {
            return bean;
        }
        //判断bean是否实现于MethodInterceptor，实现则返回
        if (bean instanceof MethodInterceptor) {
            return bean;
        }
        //拿到AspectJExpressionPointcutAdvisor的所有实现类
        List<AspectJExpressionPointcutAdvisor> advisors = beanFactory
                .getBeansForType(AspectJExpressionPointcutAdvisor.class);
        //遍历
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            //如果根据切点的匹配原则，能匹配到当前bean
            if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
                //创建一个代理对象
                ProxyFactory advisedSupport = new ProxyFactory();
                //set进去代理增强
                advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                //匹配到的方法
                advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
                //目标对象，被代理的对象
                TargetSource targetSource = new TargetSource( bean.getClass(),bean,bean.getClass().getInterfaces());
                advisedSupport.setTargetSource(targetSource);
                //代理
                return advisedSupport.getProxy();
            }
        }
        return bean;
    }
}

public class ProxyFactory extends AdvisedSupport implements AopProxy{

    //通过上面的getProxy跳到了这，可以看出这里默认是Cglib代理
    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    protected final AopProxy createAopProxy() {
        return new CglibAopProxy(this);
    }
}

public class CglibAopProxy{
    //通过上面的调用，看到这边cglib代理的真实操作
    public Object getProxy() {
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetSource().getTargetClass());
        enhancer.setInterfaces(advisedSupport.getTargetSource().getInterfaces());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advisedSupport));
        Object o=enhancer.create();
        return o;
    }    
}
```
 ##   10.到上面就差不多了。AOP是织入在IOC里边的，   
