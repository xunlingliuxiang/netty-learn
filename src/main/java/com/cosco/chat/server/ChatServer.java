package com.cosco.chat.server;


import com.cosco.chat.handler.server.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {

    public void start(Integer port){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ServerHandler());
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
