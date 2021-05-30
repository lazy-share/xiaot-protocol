package com.xiaot.protocol.support;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.custom.XiaotBizRespCallbackProvide;
import io.netty.channel.ChannelHandlerContext;
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
public class DefaultXiaotBizRespCallbackProvide implements XiaotBizRespCallbackProvide {


    @Override
    public void execute(Object body, ChannelHandlerContext ctx) {
        log.info("client biz resp callback: {}", JSONObject.toJSONString(body));

    }


}
