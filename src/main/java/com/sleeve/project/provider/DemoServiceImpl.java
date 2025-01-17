package com.sleeve.project.provider;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHello(String name) {
        System.out.println("正在调用api-backend 的sayHello 参数是 " + name);
        return "sleeve" + name;
    }
}
