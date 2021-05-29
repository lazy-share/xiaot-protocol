package com.xiaot.protocol.handler;

import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 心跳应答处理器
 * </p>
 *
 * @author lzy
 * @since 2021/5/28.
 */
@Slf4j
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        XiaotMessage receiveMsg = (XiaotMessage) msg;
        //参数检查
        if (receiveMsg == null || receiveMsg.getHeader() == null) {
            //放过，进入pipeline下一个处理器
            ctx.fireChannelRead(msg);
            return;
        }

        //接收到非心跳请求，放过，进入pipeline下一个处理器
        if (Command.HEARTBEAT_REQ.getVal() != receiveMsg.getHeader().getCommand()) {
            //放过，进入pipeline下一个处理器
            ctx.fireChannelRead(msg);
            return;
        }
        log.debug("server receive heartbeat request");
        //应答心跳请求
        XiaotMessage sendMsg = new XiaotMessage();
        XiaotHeader header = new XiaotHeader();
        header.setCommand(Command.HEARTBEAT_RESP.getVal());
        sendMsg.setHeader(header);
        ctx.writeAndFlush(sendMsg);
        log.debug("server send heartbeat response");
    }


}
