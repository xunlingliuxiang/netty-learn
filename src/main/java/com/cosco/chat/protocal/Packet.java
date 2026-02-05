package com.cosco.chat.protocal;

import com.cosco.chat.protocal.command.Command;
import lombok.Data;

@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     * @return
     */
    public abstract Byte getCommand();
}
