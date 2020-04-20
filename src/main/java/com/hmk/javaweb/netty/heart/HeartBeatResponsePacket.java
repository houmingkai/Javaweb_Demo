package com.hmk.javaweb.netty.heart;


import static com.hmk.javaweb.netty.heart.command.Command.HEARTBEAT_RESPONSE;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
