package com.xiaot.protocol.util;

import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * <p>
 * channel write util
 * </p>
 *
 * @author lzy
 * @since 2021/5/31.
 */
public class ChannelWriteUtil {


    public static ChannelFuture write(Channel channel,
                                      XiaotMessage message) {

        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        if (!channel.isWritable()) {
            throw new ChannelException("channel not writable");
        }

        ChannelFuture future = channel.writeAndFlush(message);
        if (!future.isSuccess()) {
            throw new ChannelException("channel write fail", future.cause());
        }
        return future;
    }

    public static ChannelFuture write(Channel channel,
                                      XiaotMessage message,
                                      GenericFutureListener<? extends Future<? super Void>> futureListener) {

        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        if (!channel.isWritable()) {
            throw new ChannelException("channel not writable");
        }

        ChannelFuture future = channel.writeAndFlush(message);
        if (future.cause() != null) {
            throw new ChannelException("channel write fail", future.cause());
        }
        if (futureListener != null) {
            future.addListener(futureListener);
        }
        return future;
    }
}
