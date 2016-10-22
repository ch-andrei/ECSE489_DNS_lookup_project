package com.app;

import com.app.dns_lookup.DnsLookup;

import java.io.IOException;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsClient {

    public static void main(String[] args) {
        try {
            DnsLookup.performDnsLookup(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
