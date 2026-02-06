package com.cosco.chat.handler.server;

import com.cosco.chat.protocal.response.LoginResponsePacket;
import com.cosco.chat.session.Session;
import com.cosco.chat.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
//        System.out.println("收到登录响应: success=" + msg.isSuccess() + ", userId=" + msg.getUserId() + ", userName=" + msg.getUserName());
        if (msg.isSuccess()){
            Session session = new Session(msg.getUserId(), msg.getUserName());
            SessionUtil.bindSession(session, ctx.channel());
//            System.out.println("Session已绑定: " + session);
            System.out.println("客户端登录成功,用户:" + msg.getUserName());
        }
        else{
            System.out.println("客户端登录失败");
        }
    }
}
