package com.xiaot.protocol.example;

import com.xiaot.protocol.bootstrap.XiaotClient;
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
        XiaotClient client = new XiaotClient("localhost", 9000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        TimeUnit.MILLISECONDS.sleep(5);
                        client.sendMessage("zhangsan sayHello .................................", null);
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
                        TimeUnit.MILLISECONDS.sleep(7);
                        client.sendMessage("wangwu sayHello .................................", null);
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
                        client.sendMessage("zhaoliu sayHello .................................", null);
                    }catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }).start();

        TimeUnit.HOURS.sleep(10000);
    }

}
