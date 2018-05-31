package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KuluAgentHandlerTest extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(KuluAgentHandlerTest.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        /*buf.writeBytes((ByteBuf) msg);
        System.out.println(buf);
        buf.readerIndex(0);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);*/
        /*ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        System.out.println(new String(data));
        buf.readerIndex(0);*/
        logger.info(ctx.channel().isWritable() + "==" + msg);
        /*ByteBuf buf = (ByteBuf)msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);*/
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("channelInactive");
    }
}
