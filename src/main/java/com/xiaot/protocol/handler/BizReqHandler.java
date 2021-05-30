package com.xiaot.protocol.handler;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.custom.XiaotBizReqCallbackProvide;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * 业务请求处理器
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
@Slf4j
public class BizReqHandler extends ChannelInboundHandlerAdapter {

    private LinkedBlockingQueue<Object> sendQueue;
    private volatile Thread thread = null;

    public BizReqHandler(LinkedBlockingQueue<Object> sendQueue) {
        this.sendQueue = sendQueue;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        thread = new Thread(() -> {

            Object msg = null;
            try {
                while (true) {
                    msg = sendQueue.take();
                    ctx.writeAndFlush(msg);
                }
            } catch (Exception e) {
                if (msg != null) {
                    try {
                        sendQueue.put(msg);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //类型转换
        XiaotMessage receiveMsg = (XiaotMessage) msg;

        //握手成功应答指令
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.BIZ_RESP.getVal() == receiveMsg.getHeader().getCommand()) {
            log.debug("client receive biz response: {}", JSONObject.toJSONString(receiveMsg));
            ServiceLoader<XiaotBizReqCallbackProvide> loader = ServiceLoader.load(XiaotBizReqCallbackProvide.class);
            for (XiaotBizReqCallbackProvide service : loader) {
                service.execute(receiveMsg.getBody(), ctx);
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (thread != null){
            thread.interrupt();
        }
        ctx.fireExceptionCaught(cause);
    }
}
