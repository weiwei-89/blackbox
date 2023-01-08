package org.edward.blackbox.tcp.server.handler;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.edward.blackbox.config.SystemConfig;
import org.edward.blackbox.runner.ProtocolServiceRunner;
import org.edward.blackbox.tcp.netty.util.ByteBufUtil;
import org.edward.turbosnail.decoder.ProtocolDecoder;
import org.edward.turbosnail.decoder.model.Info;
import org.edward.turbosnail.util.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDecryptHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DataDecryptHandler.class);

    private final ProtocolDecoder protocolDecoder;

    public DataDecryptHandler() {
        this.protocolDecoder = new ProtocolDecoder(ProtocolServiceRunner.getPapers(),
                SystemConfig.getProtocolConfig().getId());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("DataDecryptHandler added");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if(msg instanceof ByteBuf) {
                ByteBuf data = (ByteBuf) msg;
                byte[] bytes = ByteBufUtil.getBytes(data);
                logger.info("hex:{}", DataUtil.toHexString(bytes));
                Info info = this.protocolDecoder.decode(bytes);
                logger.info("info:{}", JSON.toJSONString(info));
            }
        } finally {
            ReferenceCountUtil.release(msg, ReferenceCountUtil.refCnt(msg));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("DataDecryptHandler goes wrong", cause);
    }
}