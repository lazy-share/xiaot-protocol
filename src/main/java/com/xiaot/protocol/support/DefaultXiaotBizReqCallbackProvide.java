package com.xiaot.protocol.support;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.custom.XiaotBizReqCallbackProvide;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

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
    public void execute(Object body, Map<String, Object> headerMap, ChannelHandlerContext ctx) {
        log.info("client biz resp callback body: {}, headers:{}",
                (body == null ? "null" : JSONObject.toJSONString(body)),
                (headerMap == null ? "null" : JSONObject.toJSONString(headerMap)));
    }


}
