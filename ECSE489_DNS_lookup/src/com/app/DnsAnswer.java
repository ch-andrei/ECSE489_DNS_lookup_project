package com.app;

public class DnsAnswer {

    public static void printRequest(String name, String server_IP, String request_type) {
        System.out.println("DnsClient sending request for " + name +
                "\nServer: " + server_IP +
                "\nRequest type: " + request_type);
    }

    public static void printResponseTime(String time, String num_retries) {
        System.out.println("Response received after " + time + " seconds (" + num_retries + " retries)");
    }

    public static void printResponseSection(String num_answer) {
        System.out.println("***Answer Section (" + num_answer + " records)***");
    }

    //A (IP address) records
    public static void printResponseA(String request_type, String ip_addr, int seconds_can_cache, String auth) {
        System.out.println("IP " + "\t" + ip_addr + "\t" + seconds_can_cache + "\t" + auth);
    }

    //CNAME, NS records
    public static void printResponse(String request_type, String alias, int seconds_can_cache, String auth) {
        System.out.println(request_type + "\t" + alias + "\t" + seconds_can_cache + "\t" + auth);
    }

    //MX records
    public static void printResponseMX(String request_type, String alias, String pref, int seconds_can_cache, String auth) {
        System.out.println(request_type + "\t" + alias + "\t" + pref + "\t" + seconds_can_cache + "\t" + auth);
    }

    public static void printAdditionalSection(String num_additional) {
        System.out.println("***Additional Section (" + num_additional + " records)***");
    }

    public static void printError(int error, String description) {
        switch(error) {
            case 1:
                System.out.println("NOTFOUND");
                break;

            case 2:
                System.out.println("ERROR\t" + description);
                System.exit(0);
                break;

            //Be specific with the error messages
            case 3:
                System.out.println("ERROR\tIncorrect input syntax: " + description);
                System.exit(0);
                break;

            case 4:
                System.out.println("ERROR\tMaximum number of retries " + description + " exceeded");
                System.exit(0);
                break;

            case 5:
                System.out.println("ERROR\tUnexpected response " + description);
                System.exit(0);
                break;
        }
    }
}