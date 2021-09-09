package com.xiaot.protocol.support;

import com.xiaot.protocol.custom.XiaotServerBizReqCallbackProvide;
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
public class DefaultXiaotServerBizReqCallbackProvide implements XiaotServerBizReqCallbackProvide {


    @Override
    public void execute(Object body, Map<String, Object> headerMap, ChannelHandlerContext ctx) {

        //Do nothing by default
    }


}
