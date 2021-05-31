package com.xiaot.protocol.handler;

import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.constant.Const;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import com.xiaot.protocol.util.ChannelWriteUtil;
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
        if (receiveMsg != null && receiveMsg.getHeader() != null) {
            //接收到心跳请求，
            if (Command.HEARTBEAT_REQ.getVal() == receiveMsg.getHeader().getCommand()) {
                log.debug("server receive heartbeat request");
                //应答心跳请求
                XiaotMessage sendMsg = new XiaotMessage();
                XiaotHeader header = new XiaotHeader();
                header.setCommand(Command.HEARTBEAT_RESP.getVal());
                header.setSuccess(Const.SUCCESS);
                sendMsg.setHeader(header);
                ChannelWriteUtil.write(ctx.channel(), sendMsg, future -> {
                    if (future.isSuccess()) {
                        log.debug("server send heartbeat response");
                    } else {
                        log.error("server send heartbeat response fail..", future.cause());
                    }
                });
            }
        }

        ctx.fireChannelRead(msg);
    }


}
