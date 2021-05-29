package com.xiaot.protocol.pojo;

import lombok.Data;

/**
 * <p>
 * 协议消息
 * </p>
 *
 * @author lzy
 * @since 2021/5/26.
 */
@Data
public class XiaotMessage {

    private XiaotHeader header;
    private Object body;

}
