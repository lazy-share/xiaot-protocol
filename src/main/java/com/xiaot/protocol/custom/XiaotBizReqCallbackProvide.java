package com.xiaot.protocol.custom;

import io.netty.channel.ChannelHandlerContext;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
public interface XiaotBizReqCallbackProvide {


    void execute(Object body, ChannelHandlerContext ctx);

}
