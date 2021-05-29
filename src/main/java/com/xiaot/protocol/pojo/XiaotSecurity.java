package com.xiaot.protocol.pojo;

import lombok.Data;

import java.net.InetSocketAddress;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/28.
 */
@Data
public class XiaotSecurity {

    private XiaotHeader header;
    private InetSocketAddress remoteAddress;

}
