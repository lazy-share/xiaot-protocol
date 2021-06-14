package com.xiaot.protocol.example;

import com.xiaot.protocol.bootstrap.XiaotClient;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
@Slf4j
public class XiaotClientExample {

    public static void main(String[] args) throws InterruptedException {

        //实例化小T协议客户端
        XiaotClient client = new XiaotClient("localhost", 9000);

        //客户端启动三条线程同时通过小T协议客户端向服务端发送数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        client.sendMessage("zhangsan sayHello33333 .................................", null, new GenericFutureListener<Future<? super Void>>() {
                            @Override
                            public void operationComplete(Future<? super Void> future) throws Exception {
                                if (future.isSuccess()) {
//                                    log.info("send success...");
                                } else {
                                    log.error("send fail...", future.cause());
                                }
                            }
                        });
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        TimeUnit.MILLISECONDS.sleep(7);
                        client.sendMessage("wangwu sayHello11111 .................................", null);
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        TimeUnit.MILLISECONDS.sleep(2);
                        client.sendMessage("zhaoliu sayHello222222 .................................", null);
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }).start();

        //阻塞主线程
        TimeUnit.HOURS.sleep(10000);
    }

}
