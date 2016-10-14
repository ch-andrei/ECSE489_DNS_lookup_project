package com.app;

import com.app.UserInterface.TextUI;

import java.io.IOException;

public class DnsClient {

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

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                //TODO
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

    public static void checkDnsQuery(DnsQuery query){
        // check if this is a working query (not missing anything)

        if (query.getServerIp().equals("") || query.getDomainName().equals("")) {
            TextUI.printError(2, "There is no input for server or domain name.");
        }

        // TODO
    }

    public static void main(String[] args) {
        DnsQuery query;
        try {
            query = parseArgsForDnsQuery(args);
            checkDnsQuery(query);

            // TODO


        } catch (IOException e){
            e.printStackTrace();
        }
    }
}