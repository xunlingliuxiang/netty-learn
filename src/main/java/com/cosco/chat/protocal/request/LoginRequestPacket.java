package com.cosco.chat.protocal.request;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends Packet {
    private String userId;
    private String userName;
    private String password;
    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
