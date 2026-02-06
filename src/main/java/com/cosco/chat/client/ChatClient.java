package com.cosco.chat.client;

import com.cosco.chat.command.ConsoleCommandManger;
import com.cosco.chat.handler.PacketDecoder;
import com.cosco.chat.handler.PacketEncoder;
import com.cosco.chat.handler.Spliter;
import com.cosco.chat.handler.client.ClientHandler;
import com.cosco.chat.handler.server.LoginResponseHandler;
import com.cosco.chat.handler.server.MessageResponseHandler;
import com.cosco.chat.protocal.request.MessageRequestPacket;
import com.cosco.chat.serialize.PacketCodeC;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ChatClient {

    private static final Integer MAX_RETRY = 5;
    public void start(Integer port){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new Spliter());
                            ch.pipeline().addLast(new PacketDecoder());
                            ch.pipeline().addLast(new ClientHandler());
                            ch.pipeline().addLast(new LoginResponseHandler());
                            ch.pipeline().addLast(new MessageResponseHandler());
                            ch.pipeline().addLast(new PacketEncoder());
//                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });
//            bootstrap.connect("localhost",port).sync();
            connect(bootstrap,"localhost",port,5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(Bootstrap bootstrap,String host,Integer port, int retry){
        bootstrap.connect(host,port).addListener(future -> {
            if (future.isSuccess()){
                System.out.println(new Date() + ":连接成功,准备登录");
                // 连接成功后开启控制台读取消息
                startConsoleThread(((ChannelFuture) future).channel());
            }else if (retry<=0){
                System.err.println("重试次数已用尽,无法连接");
            }else{
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 定时任务下次执行重连的时间
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");

                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1),
                        delay, TimeUnit.SECONDS);
            }
        });
    }

    private void startConsoleThread(Channel channel) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!Thread.interrupted() && channel.isActive()) {
                // 执行命令
//                ConsoleCommandManger consoleCommandManger = new ConsoleCommandManger();
//                consoleCommandManger.execCommand(scanner, channel);
                System.out.println("输入消息发送至服务端： ");
                Scanner sc = new Scanner(System.in);
                String line = sc.nextLine();
                MessageRequestPacket packet = new MessageRequestPacket();
                packet.setMessage(line);
                ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc().buffer(), packet);
                channel.writeAndFlush(byteBuf);
            }
            if (!channel.isActive()) {
                System.out.println("连接已断开，请重启客户端重试");
            }
        }).start();
    }

    public static void main(String[] args) {
        new ChatClient().start(8000);
    }
}
