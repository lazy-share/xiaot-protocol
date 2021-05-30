package com.xiaot.protocol.handler;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
@Slf4j
public class TailHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //
        if (msg != null) {
            XiaotMessage message = (XiaotMessage) msg;
            if (message.getHeader() != null) {
                if (Command.of(message.getHeader().getCommand()) == null) {
                    log.error("receive be not illegal command message: " + JSONObject.toJSONString(message));
                }
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
    }
}
