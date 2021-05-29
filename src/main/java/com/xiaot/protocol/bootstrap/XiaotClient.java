package com.xiaot.protocol.bootstrap;

import com.xiaot.protocol.codec.XiaotMessageDecoder;
import com.xiaot.protocol.codec.XiaotMessageEncoder;
import com.xiaot.protocol.handler.HandshakeReqHandler;
import com.xiaot.protocol.handler.HeartBeatReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 协议客户端启动程序
 * </p>
 *
 * @author lzy
 * @since 2021/5/29.
 */
@Slf4j
public class XiaotClient {

    private EventLoopGroup group = new NioEventLoopGroup();
    public void connection(String host, int port) throws Exception {
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("XiaotMessageDecoder", new XiaotMessageDecoder(1024 * 1024, 0, 4))
                                    .addLast("XiaotMessageEncoder", new XiaotMessageEncoder())
                                    .addLast("ReadTimeoutHandler", new ReadTimeoutHandler(20))
                                    .addLast("HandshakeReqHandler", new HandshakeReqHandler())
                                    .addLast("HeartBeatReqHandler", new HeartBeatReqHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(
                    new InetSocketAddress(host, port)
            ).sync();
            future.channel().closeFuture().sync();
        } finally {
            log.error("client connection time out, retry connection...");
            TimeUnit.SECONDS.sleep(5);
            connection(host, port);
        }
    }

    public static void main(String[] args) {
        XiaotClient client = new XiaotClient();
        try {
            client.connection("localhost", 9000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
