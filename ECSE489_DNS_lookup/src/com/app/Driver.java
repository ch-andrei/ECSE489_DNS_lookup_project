package com.app;

import com.app.dns_lookup.DnsClient;

import java.io.IOException;


/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class Driver {

    public static void main(String[] args) {
        try {
            DnsClient.performDnsLookup(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
