package cn.wpin.mvc.servlet;

import cn.wpin.bean.factory.AbstractBeanFactory;
import cn.wpin.mvc.annotation.Controller;
import cn.wpin.mvc.annotation.RequestMapping;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author wangpin
 */
@WebServlet(value = "dispatcherServlet")
public class DispatcherServlet extends HttpServlet {

    private static Properties properties=new Properties();

    private static Map<String,Method> handleMapping=new HashMap<String,Method>();

    private static Map controllerMap=new HashMap();

    private static List<String> classNames=new ArrayList<String>();


    private static Map<String,Object> IOC=new HashMap<String, Object>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            doDispatcher(req,resp);
        } catch (Exception e) {
            resp.getWriter().write("500 Server Exception");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) {
        //1.加载配置文件
        doLoadConfig(config.getInitParameter("contextConfiguration"));
        //2.初始化所有相关联的类,扫描用户设定的包下面所有的类
        doScanner(properties.getProperty("scanPackage"));
        //3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v  beanName-bean) beanName默认是首字母小写
        doInstance();
        //4.初始化HandlerMapping(将url和method对应上)
        initHandleMapping();
    }

    /**
     *@Author:wangPin
     *@Date:2019/4/27-13:24
     *@Params: String location x需要加载的目录
     *@Description:加载目录下的配置文件
     **/
    private void doLoadConfig(String location){
        //加载目录里边所有文件以流的形式
        InputStream inputStream=getClass().getClassLoader().getResourceAsStream(location);
        //将流加载到Properties，方便取值
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *@Author:wangPin
     *@Date:2019/4/27-13:36
     *@Params:
     *@Description:包扫描，统一方法classNames中
     **/
    private void doScanner(String packageName){
        //将连接符.替换成/
        URL url=getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        //递归遍历包下所有目录
        File dir=new File(url.getFile());
        for (File file:dir.listFiles()){
            if (file.isDirectory()){
                doScanner(packageName+file.getName());
            }else{
                String className=packageName+"."+file.getName().replaceAll(".class","");
                classNames.add(className);
            }
        }
    }

    /**
     *@Author:wangPin
     *@Date:2019/4/27-13:47
     *@Params:
     *@Description:拿到扫描的类，通过反射机制实例化
     **/
    private void doInstance(){
        if (classNames.isEmpty()) {
            return;
        }
        for (String str:classNames){
            try {
                Class<?> cls=Class.forName(str);
                if (cls.isAnnotationPresent(Controller.class)) {
                    IOC.put(toLowerFirstWord(cls.getSimpleName()),cls.newInstance());
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     *@Author:wangPin
     *@Date:2019/4/27-14:25
     *@Params:
     *@Description:初始化handleMapping,将URL和method对应上
     **/
    private void initHandleMapping(){
        if (IOC.isEmpty())
            return;
        for (Map.Entry<String, Object> entry: IOC.entrySet()){
            Class<? extends Object> clazz = entry.getValue().getClass();
            if(!clazz.isAnnotationPresent(Controller.class)){
                continue;
            }
            //拼接URL
            String baseUrl="";
            if (clazz.isAnnotationPresent(RequestMapping.class)){
                RequestMapping wpRequestMapping=clazz.getAnnotation(RequestMapping.class);
                baseUrl+=wpRequestMapping.value();
            }
            Method[] methods=clazz.getDeclaredMethods();
            for (Method method:methods){
                RequestMapping requestMappings=method.getAnnotation(RequestMapping.class);
                String url=requestMappings.value();
                url=(baseUrl+"/"+url).replaceAll("/+","/");
                handleMapping.put("/"+url,method);
                try {
                    controllerMap.put("/"+url,clazz.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    /**
     *@Author:wangPin
     *@Date:2019/4/27-14:26
     *@Params:
     *@Description:作出返回
     **/
    private void doDispatcher(HttpServletRequest request,HttpServletResponse response) throws Exception {
        if (handleMapping.isEmpty()){
            return;
        }
        String url=request.getRequestURI();
        String contextPath=request.getContextPath();
        url=url.replace(contextPath,"").replaceAll("/+","/");
        if (!handleMapping.containsKey(url)){
            response.getWriter().write("404 NOT FOUND");
            return;
        }
        Method method=handleMapping.get(url);
        //获取方法的参数
        Class<?>[] parameterType=method.getParameterTypes();
        Map<String,String[]> parameterMap=request.getParameterMap();
        Object[] paramValues=new Object[parameterType.length];
        for (int i=0;i<parameterType.length;i++){
            //根据参数名，做处理
            String requestParam=parameterType[i].getSimpleName();
            if (requestParam.equalsIgnoreCase("HttpServletRequest")){
                paramValues[i]=request;
                continue;
            }
            if (requestParam.equalsIgnoreCase("HttpServletResponse")){
                paramValues[i]=response;
                continue;
            }
            if (requestParam.equalsIgnoreCase("String")){
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i]=value;
                }
            }
        }
        method.invoke(controllerMap.get(url),paramValues);

    }

    /**
     * 将首字符转成小写
     * @param name
     * @return
     */
    private String toLowerFirstWord(String name){
        char [] chars=name.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }


}
