package com.app.dns_lookup;

import com.app.dns_lookup.packets.DnsPacket;
import com.app.dns_lookup.packets.DnsQuestionPacket;
import com.app.user_interface.TextUI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DnsClient {

    /**
     *
     * @param args
     * @return
     * @throws IOException
     */
    public static DnsQuery parseArgsForDnsQuery(String[] args) throws IOException
    {
        DnsQuery query = new DnsQuery();

        String timeout = "";
        String maxRetries = "";
        String port = "";
        String requestType = "";
        String serverIp = "";
        String domainName = "";

        if (args.length < 3) {
            throw new IOException("DNS Client missing arguments!");
        }

        // parse arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (args[i].equals("-t")) {
                    timeout = args[i++ + 1];
                }
                else if (args[i].equals("-r")) {
                    maxRetries = args[i++ + 1];
                }
                else if (args[i].equals("-p")) {
                    port = args[i++ + 1];
                }
                else if(args[i].equals("-mx") || args[i].equals("-ns")){
                    requestType = args[i++].substring(1).toUpperCase();
                }
            }
            //@ server name
            if (args[i].startsWith("@")) {
                serverIp = args[i].substring(1);
                if (i == args.length - 1) {
                    TextUI.printError(2, "Domain name is missing!" );
                }
                else {
                    domainName = args[i + 1];
                }
            }
        }

        if (!timeout.equals(""))
            query.setTimeout(timeout);
        if (!maxRetries.equals(""))
            query.setMaxRetries(maxRetries);
        if (!port.equals(""))
            query.setPort(port);
        if (!requestType.equals(""))
            query.setRequestType(requestType);
        if (!serverIp.equals(""))
            query.setServerIp(serverIp);
        if (!domainName.equals(""))
            query.setDomainName(domainName);

        return query;
    }

    /**
     *
     * @param query
     */
    public static void validateDnsQuery(DnsQuery query){
        // check if this is a working query (not missing anything)

        if (query.getServerIp().equals("") || query.getDomainName().equals("")) {
            TextUI.printError(2, "There is no input for server or domain name.");
        }

        // TODO
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void performDnsLookup(String[] args) throws IOException{
        DnsQuery query;
        try {
            query = parseArgsForDnsQuery(args);
            validateDnsQuery(query);
            performDnsLookup(query);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param query
     * @throws IOException
     */
    public static void performDnsLookup(DnsQuery query) throws IOException{
        DnsPacket questionPacket = new DnsQuestionPacket(query);
        DatagramSocket socket = new DatagramSocket();
        try {
            TextUI.print("Sending question...");
            socket.send(questionPacket.getDatagramPacket());
            TextUI.print("Sending question complete.");
        } catch (IOException ie) {
            System.out.println("ERROR\tCould not send packet.");
            return;
        }

        byte[] answerBuffer = new byte[DnsPacket.MAX_PACKET_SIZE];
        DatagramPacket answerPacket = new DatagramPacket(answerBuffer, answerBuffer.length);
        try {
            TextUI.print("Waiting for response...");
            socket.receive(answerPacket);
            TextUI.print("Receieved response.");
        } catch (IOException ie) {
            System.out.println("ERROR\tCould not receive packet.");
            return;
        }

        socket.close();
    }
}