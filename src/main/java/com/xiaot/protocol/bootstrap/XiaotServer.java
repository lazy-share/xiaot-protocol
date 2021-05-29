package com.xiaot.protocol.bootstrap;

import com.xiaot.protocol.codec.XiaotMessageDecoder;
import com.xiaot.protocol.codec.XiaotMessageEncoder;
import com.xiaot.protocol.constant.Const;
import com.xiaot.protocol.handler.HandshakeReqHandler;
import com.xiaot.protocol.handler.HandshakeRespHandler;
import com.xiaot.protocol.handler.HeartBeatRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议服务端启动程序
 * </p>
 *
 * @author lzy
 * @since 2021/5/29.
 */
@Slf4j
public class XiaotServer {


    public void bind() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast("XiaotMessageDecoder", new XiaotMessageDecoder(1024 * 1024, 0, 4))
                                .addLast("XiaotMessageEncoder", new XiaotMessageEncoder())
                                .addLast("readTimeoutHandler", new ReadTimeoutHandler(20))
                                .addLast("HandshakeRespHandler", new HandshakeRespHandler())
                                .addLast("HeartBeatRespHandler", new HeartBeatRespHandler());
                    }
                });
        bootstrap.bind(Const.SERVER_PORT).sync();
        log.info("Xiaot Server Start on port: " + Const.SERVER_PORT);
    }

    public static void main(String[] args) throws Exception {
        new XiaotServer().bind();
    }

}
