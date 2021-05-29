package com.xiaot.protocol.custom;

import com.xiaot.protocol.pojo.XiaotMessage;

/**
 * <p>
 * 协议加解密接口定义
 * </p>
 *
 * @author lzy
 * @since 2021/5/28.
 */
public interface XiaotCodecProvide {

    /**
     * 加密算法
     *
     * @param message 消息对象
     * @return
     */
    byte[] encode(XiaotMessage message);

    /**
     * 解密算法
     *
     * @param bytes 字节
     * @return
     */
    XiaotMessage decode(byte[] bytes);

}
