package com.cosco.chat.serialize;


import com.alibaba.fastjson.serializer.JSONSerializer;
import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.command.Command;
import com.cosco.chat.protocal.request.LoginRequestPacket;
import com.cosco.chat.protocal.request.MessageRequestPacket;
import com.cosco.chat.protocal.response.LoginResponsePacket;
import com.cosco.chat.protocal.response.MessageResponsePacket;
import com.cosco.chat.serialize.algorithm.SerializerAlgorithm;
import com.cosco.chat.serialize.impl.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {
    public static final int MAGIC_NUMBER = 0x12345678;
    /**
     * 序列化器们
     */
    private static final Map<Byte, Serializer> serializerMap;

    /**
     * 指令们
     */
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;

    public static PacketCodeC INSTANCE = new PacketCodeC();

    static {
        serializerMap = new HashMap<>();
        serializerMap.put(SerializerAlgorithm.JSON, new JsonSerializer());

        packetTypeMap = new HashMap<>();
        // 登录
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
    }
    public ByteBuf encode(ByteBuf out,Packet packet) {
        // 1.创建byteBuf对象
        return writeByteBufInfo(out,packet);
    }


    private static ByteBuf writeByteBufInfo(ByteBuf byteBuf, Packet msg) {
        // 2.序列化java对象
        byte[] serialize = Serializer.DEFAULT.serialize(msg);
        // 3.编码
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(Packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(msg.getCommand());
        byteBuf.writeInt(serialize.length);
        byteBuf.writeBytes(serialize);
        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过魔数
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> requestType = packetTypeMap.get(command);
        Serializer serializer = serializerMap.get(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }
}
