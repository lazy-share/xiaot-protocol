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
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //类型转换
        XiaotMessage receiveMsg = (XiaotMessage) msg;

        //参数检查
        if (receiveMsg == null || receiveMsg.getHeader() == null) {
            //放过，进入pipeline下一个处理器
            ctx.fireChannelRead(msg);
            return;
        }

        //检查指令，这里只处理握手应答指令
        if (Command.HANDSHAKE_RESP.getVal() != receiveMsg.getHeader().getCommand()) {
            ctx.fireChannelRead(msg);
            return;
        }

        //检查应答标记位
        int flag = (int) receiveMsg.getBody();
        //
        if (Const.SUCCESS == flag) {
            log.debug("client receive handshake response...");
            ctx.fireChannelRead(msg);
        } else {
            //关闭连接
            ctx.close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.fireExceptionCaught(cause);
    }
}
