package com.app;

import com.app.dns_lookup.DnsClient;
import com.app.dns_lookup.DnsQuery;

import java.io.IOException;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class Driver {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            DnsClient.performDnsLookup(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
