package com.xiaot.protocol.codec;

import com.alibaba.fastjson.JSONObject;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 协议解码器
 * </p>
 *
 * @author lzy
 * @since 2021/5/26.
 */
public class XiaotMessageDecoder extends LengthFieldBasedFrameDecoder {


    public XiaotMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //Netty处理粘包和拆包得到完整的消息包之后，会调用这里逻辑
        ByteBuf byteBuf = (ByteBuf) super.decode(ctx, in);
        if (byteBuf == null) {
            return null;
        }
        try {
            int length = byteBuf.readInt();
            //初始化byte数组
            byte[] array = new byte[length];
            //从byteBuf上次读位置开始读取字节数据到byte数组0，length位置
            byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
            //实例化解码工具类
            //
            String json = new String(array, StandardCharsets.UTF_8);
            XiaotMessage message = JSONObject.parseObject(json, XiaotMessage.class);
            return message;
        } finally {
            byteBuf.release();
        }
    }
}
