package org.example;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class ProcessorHandler implements Runnable {
    private Socket socket;

    private Map<String ,Object> handlerMap;

    public ProcessorHandler(Socket socket,Map<String ,Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream = null;

        ObjectOutputStream outputStream = null;

        try {
            // 输入流中应该有的信息 - 类 方法 参数
            inputStream = new ObjectInputStream(socket.getInputStream());
            // 反序列化
            RpcRequest request = (RpcRequest)inputStream.readObject();
            Object result = invoke(request);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Object invoke(RpcRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = request.getClassName();

        String version = request.getVersion();
        if (!StringUtils.isEmpty(version)){
            serviceName+="="+version;
        }
        Object service = handlerMap.get(serviceName);

        if (service==null){
            throw new RuntimeException("service not found:"+serviceName);
        }
        Object[] args = request.getParameters();
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();

        }
        Class<?> clazz = Class.forName(request.getClassName());//根据请求的类来加载

        Method method = clazz.getMethod(request.getMethodName(), types);

        Object result = method.invoke(service, args);

        return result;


    }
}
