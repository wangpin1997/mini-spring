package cn.wpin.business;

public class HelloServiceImpl implements HelloService {

    private String text;


    public void setText(String text) {
        this.text = text;
    }

    public void hello() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("hello world");
    }
}
