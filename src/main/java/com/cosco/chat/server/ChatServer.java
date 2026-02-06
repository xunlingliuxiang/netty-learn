package com.cosco.chat.server;


import com.cosco.chat.handler.AuthHandler;
import com.cosco.chat.handler.PacketDecoder;
import com.cosco.chat.handler.PacketEncoder;
import com.cosco.chat.handler.Splitter;
import com.cosco.chat.handler.client.LoginRequestHandler;
import com.cosco.chat.handler.client.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {

    public void start(Integer port){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new Splitter());
                            ch.pipeline().addLast(new PacketDecoder());
                            ch.pipeline().addLast(new LoginRequestHandler());
                            ch.pipeline().addLast(new AuthHandler());
                            ch.pipeline().addLast(new MessageRequestHandler());
                            ch.pipeline().addLast(new PacketEncoder());
//                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.addListener((future)->{
                if (future.isSuccess()){
                    System.out.println("服务端启动成功");
                }
            });
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            worker.shutdownGracefully();

            boss.shutdownGracefully();
        }

    }
    public static void main(String[] args) {
        new ChatServer().start(8000);
    }
}
