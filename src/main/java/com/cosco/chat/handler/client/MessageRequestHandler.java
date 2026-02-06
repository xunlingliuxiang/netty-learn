package com.cosco.chat.handler.client;

import com.cosco.chat.protocal.request.MessageRequestPacket;
import com.cosco.chat.protocal.response.MessageResponsePacket;
import com.cosco.chat.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        String toUserName = msg.getToUserName();
        String userName = msg.getUserName();
        Channel toUserChannel = SessionUtil.getChannel(toUserName);
        Channel channel = SessionUtil.getChannel(userName);
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        if (toUserChannel == null || !SessionUtil.hasLogin(toUserChannel)){
            if (channel != null && SessionUtil.hasLogin(channel)){
                messageResponsePacket.setMessage("消息接收人["+ toUserName +"]已离线,消息无法发送");
                channel.writeAndFlush(messageResponsePacket);
            }else{
                System.out.println("消息发送人["+ userName +"]已离线,回复消息无法发送");
            }
        }else{
            messageResponsePacket.setMessage(msg.getMessage());
            toUserChannel.writeAndFlush(messageResponsePacket);
            if (channel != null && SessionUtil.hasLogin(channel)){
                MessageRequestPacket packet = new MessageRequestPacket();
                packet.setMessage("消息已发送给["+ toUserName +"]");
                channel.writeAndFlush(packet);
            }else {
                System.out.println("消息接收人["+ toUserName +"]已发送, 消息发送人["+ userName +"]已离线,回复消息无法发送");
            }
        }
    }


}
