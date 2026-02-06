package com.cosco.chat.handler.server;

import com.cosco.chat.protocal.request.MessageRequestPacket;
import com.cosco.chat.protocal.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) throws Exception {
        System.out.println(new Date() + "： 收到服务端的消息： " + msg.getMessage());
    }
}
