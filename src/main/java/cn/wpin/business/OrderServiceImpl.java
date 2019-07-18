package cn.wpin.business;

import cn.wpin.bean.annotation.Component;

@Component
public class OrderServiceImpl implements OrderService {

    public void print() {
        System.out.println("order order");
    }
}
