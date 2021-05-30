package com.xiaot.protocol.example;

import com.xiaot.protocol.bootstrap.XiaotServer;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
public class XiaotServerExample {

    public static void main(String[] args) throws Exception {
        new XiaotServer().bind(9000);
    }
}
