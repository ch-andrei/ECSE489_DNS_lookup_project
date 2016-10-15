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
    public static DnsLookupRequest parseArgsForDnsQuery(String[] args) throws IOException
    {
        DnsLookupRequest request = new DnsLookupRequest();

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
            if (args[i].contains("-t")) {
                timeout += args[i+1];
            }
            else if (args[i].contains("-r")) {
                maxRetries += args[i+1];
            }
            else if (args[i].contains("-p")) {
                port += args[i+1];
            }
            else if(args[i].contains("-mx") || args[i].contains("-ns")){
                requestType += args[i].substring(1).toUpperCase();
            }
            //@ server name
            else if (args[i].startsWith("@")) {
                serverIp += args[i].substring(1);
                if (i == args.length - 1) {
                    TextUI.printError(2, "Domain name is missing!" );
                }
                else {
                    domainName += args[i + 1];
                }
            }
        }

        if (!timeout.equals(""))
            request.setTimeout(timeout);
        if (!maxRetries.equals(""))
            request.setMaxRetries(maxRetries);
        if (!port.equals(""))
            request.setPort(port);
        if (!requestType.equals(""))
            request.setRequestType(requestType);
        if (!serverIp.equals(""))
            request.setServerIp(serverIp);
        if (!domainName.equals(""))
            request.setDomainName(domainName);

        TextUI.print(request.toString());

        return request;
    }

    /**
     *
     * @param request
     */
    public static void validateDnsQuery(DnsLookupRequest request){
        // check if this is a working request (not missing anything)

        if (request.getServerIp().equals("") || request.getDomainName().equals("")) {
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
        DnsLookupRequest request;
        try {
            request = parseArgsForDnsQuery(args);
            validateDnsQuery(request);
            performDnsLookup(request);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param request
     * @throws IOException
     */
    public static void performDnsLookup(DnsLookupRequest request) throws IOException{
        DatagramSocket socket = new DatagramSocket();
        int timeout = Integer.valueOf(request.getTimeout());
        socket.setSoTimeout(timeout);

        // setup packet
        DnsPacket questionPacket = new DnsQuestionPacket(request);

        TextUI.print(questionPacket.toString());

        int counter = 0;
        boolean receieved = false;
        while (!receieved && counter < Integer.valueOf(request.getMaxRetries())) {
            // send question
            try {
                TextUI.print("[" + (counter + 1) + "] Sending question...");
                socket.send(questionPacket.getDatagramPacket());
                TextUI.print("[" + (counter + 1) + "] Sending question complete.");
            } catch (IOException ie) {
                System.out.println("Error: failed to send packet.");
                return;
            }

            // wait for response
            byte[] answerBuffer = new byte[DnsPacket.MAX_PACKET_SIZE];
            DatagramPacket answerPacket = new DatagramPacket(answerBuffer, answerBuffer.length);
            try {
                TextUI.print("[" + (counter + 1) + "] Waiting for response...");
                socket.receive(answerPacket);
                TextUI.print("Receieved response.");
                receieved = true;
            } catch (IOException ie) {
                //ie.printStackTrace();
                TextUI.print("[" + (counter + 1) + "] Timeout: No response from server.");
            }
            counter++;
        }
        if (!receieved)
            TextUI.print("Max number of retries reached: no response from server.");
        // close the socket
        socket.close();
    }
}