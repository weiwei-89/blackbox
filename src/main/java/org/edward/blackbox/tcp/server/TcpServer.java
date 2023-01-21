package org.edward.blackbox.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import org.edward.blackbox.config.TcpServerConfig;
import org.edward.blackbox.tcp.netty.codec.CachedFrameDecoder;
import org.edward.blackbox.tcp.netty.codec.FrameDecoder;
import org.edward.blackbox.tcp.netty.codec.HeartbeatHandler;
import org.edward.blackbox.tcp.netty.codec.InputLogger;
import org.edward.blackbox.tcp.server.handler.DataDecryptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServer {
    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);

    private final TcpServerConfig tcpServerConfig;

    public TcpServer(TcpServerConfig tcpServerConfig) {
        this.tcpServerConfig = tcpServerConfig;
    }

    private Channel bindChannel;

    public void startup() throws Exception {
        logger.info("starting up tcp server[type:netty,port:{}]......", this.tcpServerConfig.getPort());
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            this.bindChannel = new ServerBootstrap()
                    .group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
//                                    .addLast(new LineBasedFrameDecoder(9))
//                                    .addLast(new CachedFrameDecoder(new byte[]{0x23}, 9))
                                    .addLast(new HeartbeatHandler(1500L))
                                    .addLast(new InputLogger())
                                    .addLast(new FrameDecoder(new byte[]{0x23}, 9))
                                    .addLast(new DataDecryptHandler());
                        }
                    }).bind(this.tcpServerConfig.getPort()).sync().channel();
            logger.info("done");
            this.bindChannel.closeFuture().sync();
            logger.info("tcp server stopped[port:{}]", this.tcpServerConfig.getPort());
        } finally {
            logger.info("cleaning up......", this.tcpServerConfig.getPort());
            parentGroup.shutdownGracefully().sync();
            childGroup.shutdownGracefully().sync();
            logger.info("done");
        }
    }

    public void shutdown() throws Exception {
        logger.info("shutting down tcp server[port:{}]......", this.tcpServerConfig.getPort());
        if(this.bindChannel == null) {
            return;
        }
        this.bindChannel.close().sync();
        logger.info("done");
    }
}