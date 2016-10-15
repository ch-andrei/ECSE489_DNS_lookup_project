package com.app.dns_lookup.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public abstract class DnsPacket {

    public static final int MAX_PACKET_SIZE = 1024;
    public static final int HEADER_SIZE = 12;

    protected ByteBuffer packetData;
    protected DatagramPacket datagramPacket;

    public DnsPacket() {
    }

    public ByteBuffer getPacketData() {
        return packetData;
    }

    public void setPacketData(ByteBuffer packetData) {
        this.packetData = packetData;
    }

    public DatagramPacket getDatagramPacket() {
        return datagramPacket;
    }

    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.datagramPacket = datagramPacket;
    }
}



