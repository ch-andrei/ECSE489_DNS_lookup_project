package com.app.dns_lookup.packets;

import com.app.user_interface.TextUI;

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

    public byte[] parseID(){
        return (new byte[] {packetDataBuffer.array()[0], packetDataBuffer.array()[1]});
    }

    public boolean parseAuthority(){
        return (((packetDataBuffer.array()[2] & 0b00000100) == (0b00000100)) ? true : false);
    }

    public boolean parseRecursive(){
        return (((packetDataBuffer.array()[3] & 0b10000000) == (0b10000000)) ? true : false);
    }

    public byte parseRcode(){
        return (byte)(packetDataBuffer.array()[3] & 0b00001111);
    }

    public int parseAncount(){
        byte[] ancount = new byte[] {packetDataBuffer.array()[6], packetDataBuffer.array()[7]};
        ByteBuffer ancount_buf = ByteBuffer.wrap(ancount);
        return ancount_buf.getShort(0);
    }

    public String[] parsePacketInfo(){
        // init array for output
        String[] out = new String[3];

        byte rcode = parseRcode();
        switch (rcode){
            default:
                break;
            case 0:
                break;
            case 1:
                // TODO print error message (see DNS primer document page 2-3)
                break;
            case 2:
                // TODO
                break;
            case 3:
                // TODO
                break;
            case 4:
                // TODO
                break;
            case 5:
                // TODO
                break;
        }

        if (!parseRecursive()){
            TextUI.printError(2, "Contacted server does not allow recursion.");
        }

        int offset = HEADER_SIZE;
        // find the end of NAME section
        while (packetDataBuffer.array()[offset] != 0x00 && offset < MAX_PACKET_SIZE ){
            offset++;
        }
        // TextUI.print("offset " + offset);

        // get TYPE
        byte type = packetDataBuffer.array()[offset+2];
        if (type == 0x01)
            out[0] = "A";
        else if (type == 0x02)
            out[0] = "NS";
        else if (type == 0x0f)
            out[0] = "MX";
        else if (type == 0x05)
            out[0] = "CNAME";
        else {
            TextUI.printError(5, "Unexpected response TYPE {" + bytesToHex(new byte[] {type}) + "}");
        }

        // check CLASS
        byte pclass = packetDataBuffer.array()[offset+4];
        if (pclass != 0x01)
            TextUI.printError(5, "Unexpected response CLASS (not 0x0001): {" + bytesToHex(new byte[] {type}) + "}");

        // get TTL
        byte[] ttl = new byte[4];
        for (int i = 0; i < 4; i++){
            ttl[i] = packetDataBuffer.array()[offset+11+i];
        }
        ByteBuffer ttl_buf = ByteBuffer.wrap(ttl);
        // need to get unsigned value
        out[1] = "" + ((long) ttl_buf.getInt(0) & 0xffffffffL);

        // get RDATA
        byte rdlength[] = new byte[] {packetDataBuffer.array()[offset+15], packetDataBuffer.array()[offset+16]};
        ByteBuffer rdlength_buf = ByteBuffer.wrap(rdlength);
        int RDLENGTH = (rdlength_buf.getShort() & 0xffff);
        String RDATA = "";
        if (out[0].equals("A")){
            // get ip
            byte[] ip = new byte[4];
            for (int i = 0; i < 4; i++){
                ip[i] = packetDataBuffer.array()[offset+17+i];
            }
            out[2] = bytesIpToString(ip);
        } else if (out[0].equals("NS")){

        } else if (out[0].equals("MX")){

        } else if (out[0].equals("CNAME")){

        } else {
            TextUI.printError(5, "Could not interpret answer packet.");
        }
        //for (int i = 0; i < RDLENGTH; i++){
//
        //}

        return out;
    }

    public String bytesIpToString(byte[] bytes){
        String str = "";
        int i = 0;
        for (byte b : bytes) {
            str += ((b & 0b10000000) == 0b10000000) ? (b+256) : b;
            if (i++ != 3) str += ".";
        }
        return str;
    }
}
