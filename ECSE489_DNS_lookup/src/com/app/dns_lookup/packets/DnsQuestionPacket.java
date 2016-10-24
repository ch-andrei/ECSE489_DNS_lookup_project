package com.app.dns_lookup.packets;

import com.app.dns_lookup.DnsLookupRequest;
import com.app.user_interface.TextUI;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsQuestionPacket extends DnsPacket{

    static Random random;

    public DnsQuestionPacket(DnsLookupRequest request) {
        this.packetDataBuffer = ByteBuffer.allocate(computePacketLength(request));
        this.random = new Random(System.currentTimeMillis());
        initPacket(request); // setup packetDataBuffer
    }

    /**
     *
     * @param request
     * @return
     */
    private int computePacketLength(DnsLookupRequest request){
        // header + QNAME + 1 empty byte (end of QNAME) + 4 bytes for QCLASS and QTYPE + 1 for length adjustement
        return HEADER_SIZE + request.getDomainName().length() + 6;
    }

    /**
     *
     * @param request
     */
    private void initPacket(DnsLookupRequest request) {
        initPacketHeader();
        initPacketData(request);
        try {
            InetAddress lookupServer = InetAddress.getByAddress(request.getServerIpAsByteArray());
            this.datagramPacket = new DatagramPacket(packetDataBuffer.array(), 0,
                    packetDataBuffer.array().length, lookupServer, Integer.valueOf(request.getPort()));
        } catch (NumberFormatException nfe) {
            TextUI.printError(3, "Invalid lookup format.");
        } catch (UnknownHostException e) {
            TextUI.printError(2, "Unknown host exception.");
        }
    }

    /**
     *
     */
    private void initPacketHeader() {
        // write id
        byte[] id = new byte[2];
        random.nextBytes(id);
        packetDataBuffer.put(id);
        // write QR|Opcode|AA|TC|RD|RA|Z|RCODE
        packetDataBuffer.put((byte)0x1);
        packetDataBuffer.put((byte)0x0);
        // write 1 to QDCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x1);
        // write 0 to ANCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
        // write 0 to NSCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
        // write 0 to ARCOUNT
        packetDataBuffer.put((byte)0x0);
        packetDataBuffer.put((byte)0x0);
    }

    /**
     *
     * @param request
     */
    private void initPacketData(DnsLookupRequest request) {
        // write QNAME
        List<char[]> labels = request.getDomainNameAsList();
        for(char[] label : labels){
            packetDataBuffer.put((byte)(label.length));
            for (char c : label){
                packetDataBuffer.put((byte)c);
            }
        }
        // write 00 to indicate end of labels
        packetDataBuffer.put((byte)0x00);

        // write QTYPE (type-A, type-NS, type-MX)
        switch(request.getRequestType()){
            default: // this should not happen; but write 0x0001 as if A-type
                packetDataBuffer.put((byte)0x00);
                packetDataBuffer.put((byte)0x01);
                break;
            case "A": // write 0x0001
                packetDataBuffer.put((byte)0x00);
                packetDataBuffer.put((byte)0x01);
                break;
            case "NS": // write 0x0002
                packetDataBuffer.put((byte)0x00);
                packetDataBuffer.put((byte)0x02);
                break;
            case "MX": // write 0x000f
                packetDataBuffer.put((byte)0x00);
                packetDataBuffer.put((byte)0x0f);
                break;
        }
        // write 0x0001 to QCLASS
        packetDataBuffer.put((byte)0x00);
        packetDataBuffer.put((byte)0x01);
    }

   public String toString() {
       String out = "";
       String str = bytesToHex(this.packetDataBuffer.array());
       for (int i = 0; i < str.length() / 2; i++) {
           out += str.charAt(2 * i) + "" + str.charAt(2 * i + 1) + " ";
           if (i != 0 && i % 15 == 0)
               out += "\n";
       }
       return out;
   }
}



