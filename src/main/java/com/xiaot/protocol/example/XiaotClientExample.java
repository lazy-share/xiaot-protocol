package com.xiaot.protocol.example;

import com.xiaot.protocol.bootstrap.XiaotClient;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
public class XiaotClientExample {

    public static void main(String[] args) throws InterruptedException {
        XiaotClient client = new XiaotClient("localhost", 9000);

        while (true){
            TimeUnit.SECONDS.sleep(1);
            client.sendMessage("zhangsan sayHello", null);
        }
    }

}
