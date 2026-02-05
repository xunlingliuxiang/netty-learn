package com.cosco.chat.handler.client;

import com.cosco.chat.protocal.request.LoginRequestPacket;
import com.cosco.chat.serialize.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端开始登录");
        LoginRequestPacket login = new LoginRequestPacket();
        login.setUserName("admin");
        login.setPassword("admin");
        login.setUserId(1);
        ByteBuf buffer = PacketCodeC.encode(ctx.alloc().buffer(), login);
        ctx.channel().writeAndFlush(buffer);
    }
}
