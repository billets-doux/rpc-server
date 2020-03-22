package org.example;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class QSRpcServer implements ApplicationContextAware, InitializingBean {
    ExecutorService executorService = Executors.newCachedThreadPool();

    private int port;
    private Map<String ,Object> handlerMap = new HashMap<>();

    public QSRpcServer(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket accept = serverSocket.accept();
                executorService.execute(new ProcessorHandler(accept,handlerMap));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!serviceBeanMap.isEmpty()){
            serviceBeanMap.forEach((key,value) ->{
                //拿到注解
                RpcService rpcService = value.getClass().getAnnotation(RpcService.class);
                //拿到名称
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();//拿到版本号
                if (!StringUtils.isEmpty(version)){
                    serviceName += "="+version;
                }
                handlerMap.put(serviceName, value);
            });
        }


    }
}
