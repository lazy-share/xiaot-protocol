package com.xiaot.protocol.pojo;

import com.xiaot.protocol.constant.Const;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 协议头
 * </p>
 *
 * @author lzy
 * @since 2021/5/26.
 */
@Data
public class XiaotHeader {

    /**
     * 协议魔数，固定值 0xACAFDCBA
     */
    private int major = Const.MAJOR;
    /**
     * 协议主版本：
     */
    private char mainVersion = Const.MAIN_VERSION;
    /**
     * 协议次版本：
     */
    private char minorVersion = Const.MINOR_VERSION;
    /**
     * 消息总长度
     */
    private int length;
    /**
     * 会话id
     */
    private long sid;
    /**
     * 指令
     */
    private char command;
    /**
     * 消息头扩展属性
     */
    private Map<String, Object> attribute = new HashMap<>();

}
