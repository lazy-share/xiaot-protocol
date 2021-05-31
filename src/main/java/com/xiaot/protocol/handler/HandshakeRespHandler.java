package com.xiaot.protocol.handler;

import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.constant.Const;
import com.xiaot.protocol.custom.XiaotSecurityAuthProvide;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import com.xiaot.protocol.pojo.XiaotSecurity;
import com.xiaot.protocol.util.ChannelWriteUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
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
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.HANDSHAKE_REQ.getVal() == receiveMsg.getHeader().getCommand()) {
            //检查协议魔数
            if (Const.MAJOR != receiveMsg.getHeader().getMajor()) {
                ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol major check fail，" + receiveMsg.getHeader().getMajor()));
                return;
            }

            //检查主版本
            else if (Const.MAIN_VERSION != receiveMsg.getHeader().getMainVersion()) {
                ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol main version check fail，" + receiveMsg.getHeader().getMainVersion()));
            }

            //检查次版本
            else if (Const.MINOR_VERSION != receiveMsg.getHeader().getMinorVersion()) {
                ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol minor version check fail，" + receiveMsg.getHeader().getMinorVersion()));
            }

            //安全认证 || 握手应答
            else {

                XiaotSecurity security = new XiaotSecurity();
                security.setHeader(receiveMsg.getHeader());
                security.setRemoteAddress((InetSocketAddress) ctx.channel().remoteAddress());
                ServiceLoader<XiaotSecurityAuthProvide> loader = ServiceLoader.load(XiaotSecurityAuthProvide.class);
                boolean isOk = true;
                for (XiaotSecurityAuthProvide service : loader) {
                    String errorMsg = service.isAllow(security);
                    if (errorMsg != null) {
                        isOk = false;
                        ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol security check fail, " + errorMsg));
                        break;
                    }
                }

                if (isOk) {
                    log.debug("server receive handshake request...");
                    //应答握手
                    XiaotMessage respMsg = new XiaotMessage();
                    XiaotHeader respHeader = new XiaotHeader();
                    respHeader.setSuccess(Const.SUCCESS);
                    respHeader.setCommand(Command.HANDSHAKE_RESP.getVal());
                    respMsg.setHeader(respHeader);
                    ChannelWriteUtil.write(ctx.channel(), respMsg, new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                log.debug("server send handshake response");
                            } else {
                                log.error("server send handshake response fail...", future.cause());
                            }
                        }
                    });
                }
            }
        }

        ctx.fireChannelRead(msg);
    }

    private XiaotMessage buildFailMessage(String body) {
        XiaotMessage respMsg = new XiaotMessage();
        XiaotHeader respHeader = new XiaotHeader();
        respHeader.setCommand(Command.HANDSHAKE_RESP.getVal());
        respHeader.setSuccess(Const.FAIL);
        respMsg.setHeader(respHeader);
        respMsg.setBody(body);
        log.error(body);
        return respMsg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
