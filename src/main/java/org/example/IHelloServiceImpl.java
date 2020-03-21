package org.example;

public class IHelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("request in sayHello:" + content);
        return "say Hello:"+content;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("request in saveUser:" + user);
        return "SUCCESS";
    }
}
