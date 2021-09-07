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
    BIZ_REQ('3', "业务消息"),
    HEARTBEAT_REQ('5', "心跳请求消息"),
    HEARTBEAT_RESP('6', "心跳应答消息"),


    ;


    private char val;
    private String desc;

    Command(char val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static Command of(char val){
        for (Command value : Command.values()) {
            if (value.getVal() == val){
                return value;
            }
        }
        return null;
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
