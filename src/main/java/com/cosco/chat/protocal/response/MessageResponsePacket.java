package com.cosco.chat.protocal.response;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageResponsePacket extends Packet {
    private String message;

    private String userId;

    private String userName;

    private String toUserId;

    private String toUserName;
    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
