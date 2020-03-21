package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcProxyServer {

    public void publisher(Object service ,int port){
        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while (true){
                Socket accept = serverSocket.accept();
                executorService.execute(new ProcessorHandler(accept,service));

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
}
