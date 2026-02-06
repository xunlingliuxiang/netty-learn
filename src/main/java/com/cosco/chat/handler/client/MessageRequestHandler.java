package com.cosco.chat.handler.client;

import com.cosco.chat.protocal.request.MessageRequestPacket;
import com.cosco.chat.protocal.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        ctx.channel().writeAndFlush(receiveMessage(msg));
    }

    private Object receiveMessage(MessageRequestPacket msg) {
        System.out.println(new Date() + ":服务端收到消息:" + msg.getMessage());
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复【" +
                msg.getMessage() + "】");
        return messageResponsePacket;
    }
}
