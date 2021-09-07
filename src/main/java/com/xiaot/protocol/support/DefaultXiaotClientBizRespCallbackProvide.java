package com.xiaot.protocol.support;

import com.xiaot.protocol.custom.XiaotClientBizRespCallbackProvide;
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
public class DefaultXiaotClientBizRespCallbackProvide implements XiaotClientBizRespCallbackProvide {


    @Override
    public void execute(Object body, Map<String, Object> headerMap, ChannelHandlerContext ctx) {

        //Do nothing by default
    }


}
