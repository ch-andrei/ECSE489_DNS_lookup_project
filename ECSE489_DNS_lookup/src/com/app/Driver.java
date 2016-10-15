package com.app;

import com.app.dns_lookup.DnsClient;
import com.app.dns_lookup.DnsQuery;
import com.app.user_interface.TextUI;

import java.io.IOException;
import java.util.ArrayList;


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
