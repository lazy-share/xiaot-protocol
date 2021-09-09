package com.xiaot.protocol.bootstrap;

import com.xiaot.protocol.codec.XiaotMessageDecoder;
import com.xiaot.protocol.codec.XiaotMessageEncoder;
import com.xiaot.protocol.handler.BizReqHandler;
import com.xiaot.protocol.handler.HandshakeRespHandler;
import com.xiaot.protocol.handler.HeartBeatRespHandler;
import com.xiaot.protocol.handler.TailHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
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

    /**
     * 绑定端口
     *
     * @throws Exception
     */
    public void bind(int port) throws Exception {

        //ReactSelect主线程组 boss线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //work线程池
        EventLoopGroup workGroup = new NioEventLoopGroup();
        //实例化引导启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //1、通过引导启动类传入TCP相关配置，2、通过引导启动类传入内部handler处理链配置
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                // 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                // 如果未设置或所设置的值小于1，Java将使用默认值50。
                // 当长度大于Backlog时，新的连接就会被TCP内核拒绝掉
                .option(ChannelOption.SO_BACKLOG, 100)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                //消息解码器，继承LengthFieldBasedFrameDecoder可以处理TCP粘包/拆包问题
                                .addLast("XiaotMessageDecoder", new XiaotMessageDecoder(1024 * 1024, 0, 4))
                                //消息编码器
                                .addLast("XiaotMessageEncoder", new XiaotMessageEncoder())
                                //握手应答处理器
                                .addLast("HandshakeRespHandler", new HandshakeRespHandler())
                                //心跳应答处理器
                                .addLast("HeartBeatRespHandler", new HeartBeatRespHandler())
                                //业务请求处理器
                                .addLast("BizReqHandler", new BizReqHandler())
                                //末尾处理器
                                .addLast("TailHandler", new TailHandler())
                        ;
                    }
                });
        //资源泄露监控日志级别
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        bootstrap.bind(port).sync();
        log.info("Xiaot Server Start on port: " + port);
    }


}
