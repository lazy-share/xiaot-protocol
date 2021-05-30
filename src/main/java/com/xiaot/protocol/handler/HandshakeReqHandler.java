package com.xiaot.protocol.handler;

import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.constant.Const;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import com.xiaot.protocol.util.SidUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 握手请求处理器
 * </p>
 *
 * @author lzy
 * @since 2021/5/27.
 */
@Slf4j
public class HandshakeReqHandler extends ChannelInboundHandlerAdapter {


    /**
     * 在连接通道channel激活时立即发送握手请求
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        XiaotHeader header = new XiaotHeader();
        header.setCommand(Command.HANDSHAKE_REQ.getVal());
        header.setSid(SidUtil.INSTANCE.nextId());
        XiaotMessage sendMsg = new XiaotMessage();
        sendMsg.setHeader(header);
        log.debug("client send handshake request...");
        ctx.writeAndFlush(sendMsg);
        ctx.fireChannelActive();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //类型转换
        XiaotMessage receiveMsg = (XiaotMessage) msg;

        //处理握手应答指令
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.HANDSHAKE_RESP.getVal() == receiveMsg.getHeader().getCommand()) {
            if (Const.SUCCESS == receiveMsg.getHeader().getSuccess()) {
                log.debug("client receive handshake response");
            } else {
                log.error("client receive handshake response fail " + (receiveMsg.getBody() == null ? "" : (String) receiveMsg.getBody()));
                ctx.close();
            }
        }
        //只有成功才继续往下流转
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
