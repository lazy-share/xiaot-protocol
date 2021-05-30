package com.xiaot.protocol.handler;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.custom.XiaotBizRespCallbackProvide;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

/**
 * <p>
 * 业务应答处理器
 * </p>
 *
 * @author lzy
 * @since 2021/5/30.
 */
@Slf4j
public class BizRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //类型转换
        XiaotMessage receiveMsg = (XiaotMessage) msg;

        //业务应答指令
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.BIZ_REQ.getVal() == receiveMsg.getHeader().getCommand()) {
            log.debug("server receive biz request: {}", JSONObject.toJSONString(receiveMsg));
            ServiceLoader<XiaotBizRespCallbackProvide> loader = ServiceLoader.load(XiaotBizRespCallbackProvide.class);
            for (XiaotBizRespCallbackProvide service : loader) {
                service.execute(receiveMsg.getBody(), ctx);
            }
        }
        ctx.fireChannelRead(msg);
    }
}
