package com.xiaot.protocol.handler;

import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 心跳请求处理器
 * </p>
 *
 * @author lzy
 * @since 2021/5/28.
 */
@Slf4j
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> scheduledFuture;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        XiaotMessage receiveMsg = (XiaotMessage) msg;
        //参数检查
        if (receiveMsg == null || receiveMsg.getHeader() == null) {
            //放过，进入pipeline下一个处理器
            ctx.fireChannelRead(msg);
            return;
        }

        //接收到握手成功应答，初始化心跳检查后台任务
        if (Command.HANDSHAKE_RESP.getVal() == receiveMsg.getHeader().getCommand()) {
            log.debug("client init heartbeat check schedule");
            scheduledFuture = ctx.executor().scheduleAtFixedRate(
                    new HeartBeatTask(ctx), 0, 10, TimeUnit.SECONDS
            );
            return;
        }

        //接收到心跳应答，
        if (Command.HEARTBEAT_RESP.getVal() == receiveMsg.getHeader().getCommand()) {
            log.debug("client receive heartbeat response");
            return;
        }

        //放过，进入pipeline下一个处理器
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
        ctx.fireExceptionCaught(cause);
    }

    private static class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            XiaotMessage sendMsg = new XiaotMessage();
            XiaotHeader header = new XiaotHeader();
            header.setCommand(Command.HEARTBEAT_REQ.getVal());
            sendMsg.setHeader(header);
            ctx.writeAndFlush(sendMsg);
            log.debug("client send heartbeat request");
        }
    }

}