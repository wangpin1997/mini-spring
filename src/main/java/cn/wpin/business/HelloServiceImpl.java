package cn.wpin.business;

public class HelloServiceImpl implements HelloService,OrderService {

    private String text;

    private OrderService orderService;

    public void setText(String text) {
        this.text = text;
    }

    public void hello() throws InterruptedException {
        System.out.println("hello world");
        orderService.print();
    }

    public void print() {
        System.out.println(text);
    }

    public void setOrderService(OrderService orderService){
       this.orderService=orderService;
    }
}
