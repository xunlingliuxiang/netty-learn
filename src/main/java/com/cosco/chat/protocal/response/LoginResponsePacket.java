package com.cosco.chat.protocal.response;

import com.cosco.chat.protocal.Packet;
import com.cosco.chat.protocal.command.Command;
import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * 客户端登录Response对象
 *
 * @author FangYuan
 * @since 2023-03-28 20:44:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String userId;

    private String userName;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
