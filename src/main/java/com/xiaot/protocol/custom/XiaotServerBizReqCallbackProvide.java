package com.xiaot.protocol.custom;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
public interface XiaotServerBizReqCallbackProvide {


    void execute(Object body, Map<String, Object> headerMap, ChannelHandlerContext ctx);

}
