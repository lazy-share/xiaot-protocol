package com.xiaot.protocol.support;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.custom.XiaotBizReqCallbackProvide;
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
public class DefaultXiaotBizReqCallbackProvide implements XiaotBizReqCallbackProvide {


    @Override
    public void execute(Object body, ChannelHandlerContext ctx) {
        log.info("client biz req callback: {}", JSONObject.toJSONString(body));
    }


}
