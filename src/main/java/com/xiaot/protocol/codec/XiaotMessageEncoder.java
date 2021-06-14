package com.xiaot.protocol.codec;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 协议编码器
 * </p>
 *
 * @author lzy
 * @since 2021/5/26.
 */
@Slf4j
public class XiaotMessageEncoder extends MessageToByteEncoder<XiaotMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, XiaotMessage message, ByteBuf byteBuf) throws Exception {
        if (message == null || message.getHeader() == null) {
            throw new Exception("encode message be do not null");
        }
        //Serialize
        byte[] raw = JSONObject.toJSONString(message).getBytes(StandardCharsets.UTF_8);
        message.getHeader().setLength(raw.length);
        // 这里严格按照解码器TCP粘包和拆包解决方案要求格式进行编码
        // writeInt表示往消息头写内容长度，占用空间是4个字节
        byteBuf.writeInt(message.getHeader().getLength());
        byteBuf.writeBytes(raw);
    }

}
