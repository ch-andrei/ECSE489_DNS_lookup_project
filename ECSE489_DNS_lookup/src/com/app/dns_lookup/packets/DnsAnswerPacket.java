package com.app.dns_lookup.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

/**
 * Created by Andrei on 2016-10-17.
 */
public class DnsAnswerPacket extends DnsPacket{

    public DnsAnswerPacket(DatagramPacket packet) {
        this.datagramPacket = packet;
        this.packetDataBuffer = ByteBuffer.wrap(packet.getData());
    }




}
