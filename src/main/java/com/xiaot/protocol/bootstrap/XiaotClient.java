package com.xiaot.protocol.bootstrap;

import com.xiaot.protocol.codec.XiaotMessageDecoder;
import com.xiaot.protocol.codec.XiaotMessageEncoder;
import com.xiaot.protocol.constant.Command;
import com.xiaot.protocol.handler.BizReqHandler;
import com.xiaot.protocol.handler.HandshakeReqHandler;
import com.xiaot.protocol.handler.HeartBeatReqHandler;
import com.xiaot.protocol.handler.TailHandler;
import com.xiaot.protocol.pojo.XiaotHeader;
import com.xiaot.protocol.pojo.XiaotMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
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


    /**
     * channel
     */
    private volatile Channel channel;
    private volatile boolean init = false;
    /**
     *
     */
    private LinkedBlockingQueue<Object> sendQueue = new LinkedBlockingQueue<>(10000);

    public XiaotClient(String host, int port) {
        new Thread(() -> connection(host, port)).start();
    }

    /**
     * 建立连接
     *
     * @param host 远程主机
     * @param port 远程端口
     */
    public synchronized void connection(String host, int port) {

        if (init) {
            return;
        }
        init = true;
        //
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(group).channel(NioSocketChannel.class)
                    //禁用nagle算法.tips:Nagle算法就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ReadTimeoutHandler(10,  TimeUnit.SECONDS))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    //消息解码器
                                    .addLast("XiaotMessageDecoder", new XiaotMessageDecoder(1024 * 1024, 0, 4))
                                    //消息编码器
                                    .addLast("XiaotMessageEncoder", new XiaotMessageEncoder())
                                    //握手请求处理器
                                    .addLast("HandshakeReqHandler", new HandshakeReqHandler())
                                    //心跳请求处理器
                                    .addLast("HeartBeatReqHandler", new HeartBeatReqHandler())
                                    //业务请求处理器
                                    .addLast("BizReqHandler", new BizReqHandler(sendQueue))
                                    //末尾处理器
//                                    .addLast("TailHandler", new TailHandler())
                            ;
                        }
                    });
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("client connection time out, retry connection...", e);
        } finally {
            try {
                TimeUnit.SECONDS.sleep(5);
                if (channel != null && (channel.isActive() || channel.isOpen())) {
                    channel.close();
                    channel = null;
                }
                init = false;
                connection(host, port);
            } catch (InterruptedException interruptedException) {
                log.error("client connection time out, retry fail...", interruptedException);
            }
        }
    }

    /**
     * 发送消息
     *
     * @param body 消息体
     */
    public void sendMessage(Object body, Map<String, Object> headerMap) {
        XiaotMessage message = new XiaotMessage();
        XiaotHeader header = new XiaotHeader();
        header.setCommand(Command.BIZ_REQ.getVal());
        header.setAttribute(headerMap);
        message.setBody(body);
        message.setHeader(header);
        sendQueue.add(message);
    }

}
