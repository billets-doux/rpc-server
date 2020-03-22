package org.example;

@RpcService(value = IHelloService.class,version = "v2.0")
public class IHelloService2Impl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("【V2.0】request in sayHello:" + content);
        return "【V2.0】say Hello:"+content;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("【V2.0】request in saveUser:" + user);
        return "【V2.0】SUCCESS";
    }
}
