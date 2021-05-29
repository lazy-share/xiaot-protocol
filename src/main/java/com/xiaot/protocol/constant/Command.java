package com.xiaot.protocol.constant;

/**
 * <p>
 * 协议指令枚举
 * </p>
 *
 * @author lzy
 * @since 2021/5/27.
 */
public enum Command {

    HANDSHAKE_REQ('1', "握手请求消息"),
    HANDSHAKE_RESP('2', "握手应答消息"),
    BIZ_REQ('3', "业务请求消息"),
    BIZ_RESP('4', "业务应答消息"),
    HEARTBEAT_REQ('5', "心跳请求消息"),
    HEARTBEAT_RESP('6', "心跳应答消息"),


    ;


    /**
     * 1、ACK请求消息
     * 2、ACK应答消息
     * 3、业务请求消息
     * 4、业务应答消息
     * 5、心跳请求消息
     * 6、心跳应答消息
     */
    private char val;
    private String desc;

    Command(char val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public char getVal() {
        return val;
    }

    public void setVal(char val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
