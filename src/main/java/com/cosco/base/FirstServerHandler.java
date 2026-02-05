package com.cosco.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(getByteBuf(ctx,":客户端接收到连接"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + "： 服务端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
        ctx.channel().writeAndFlush(getByteBuf(ctx,null));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx,String msg){
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(Charset.defaultCharset().encode(new Date() + (msg == null
                ?":服务端收到了消息,返回ack给客户端"
                : msg)));
        return buffer;

    }
}
