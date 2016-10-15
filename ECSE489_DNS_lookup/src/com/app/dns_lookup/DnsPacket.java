package com.app.dns_lookup;

import java.nio.ByteBuffer;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsPacket {

    public static final int MAX_PACKET_SIZE = 1024;

    private ByteBuffer packetData;

    public DnsPacket(DnsQuery query) {
        initPacket(query);
    }

    private void initPacket(DnsQuery query) {
        // TODO
    }
}



