package com.app;

import com.app.dns_lookup.DnsLookup;
import com.app.user_interface.TextUI;

import java.io.IOException;

/**
 * Created by Andrei-ch on 2016-10-14.
 */
public class DnsClient {

    public static void main(String[] args) {
        try {
            DnsLookup.performDnsLookup(args);
        } catch (IOException e) {
            TextUI.printError(2,"Caught some COMPLETELY UNEXPECTED exception. Printing stack trace!");
            e.printStackTrace();
        }
    }
}
