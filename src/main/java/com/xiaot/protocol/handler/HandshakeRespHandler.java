package com.xiaot.protocol.handler;

import com.alibaba.fastjson.JSON;
import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.constant.Const;
import com.xiaot.protocol.custom.XiaotSecurityAuthProvide;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import com.xiaot.protocol.pojo.XiaotSecurity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ServiceLoader;

/**
 * <p>
 * 握手应答处理器
 * </p>
 *
 * @author lzy
 * @since 2021/5/27.
 */
@Slf4j
public class HandshakeRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //类型转换
        XiaotMessage receiveMsg = (XiaotMessage) msg;

        //参数检查
        if (receiveMsg == null || receiveMsg.getHeader() == null){
            //放过，进入pipeline下一个处理器
            ctx.fireChannelRead(msg);
            return;
        }

        //检查指令，这里只处理握手请求指令
        if (Command.HANDSHAKE_REQ.getVal() != receiveMsg.getHeader().getCommand()) {
            ctx.fireChannelRead(msg);
            return;
        }

        //检查协议魔数
        if (Const.MAJOR != receiveMsg.getHeader().getMajor()) {
            log.error("xiaot protocol major check fail");
            ctx.close();
            return;
        }

        //检查主版本
        if (Const.MAIN_VERSION != receiveMsg.getHeader().getMainVersion()) {
            log.error("xiaot protocol main version check fail");
            ctx.close();
            return;
        }

        //检查次版本
        if (Const.MINOR_VERSION != receiveMsg.getHeader().getMinorVersion()) {
            log.error("xiaot protocol minor version check fail");
            ctx.close();
            return;
        }

        //安全认证
        XiaotSecurity security = new XiaotSecurity();
        security.setHeader(receiveMsg.getHeader());
        security.setRemoteAddress((InetSocketAddress) ctx.channel().remoteAddress());
        ServiceLoader<XiaotSecurityAuthProvide> loader = ServiceLoader.load(XiaotSecurityAuthProvide.class);
        for (XiaotSecurityAuthProvide service : loader) {
            if (!service.isAllow(security)) {
                log.error("xiaot protocol security auth fail");
                ctx.close();
                return;
            }
        }
        log.debug("server receive handshake request...");
        //应答握手
        XiaotMessage respMsg = new XiaotMessage();
        XiaotHeader respHeader = new XiaotHeader();
        respHeader.setCommand(Command.HANDSHAKE_RESP.getVal());
        respMsg.setHeader(respHeader);
        respMsg.setBody(Const.SUCCESS);
        ctx.writeAndFlush(respMsg);
        log.debug("server send handshake response...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
