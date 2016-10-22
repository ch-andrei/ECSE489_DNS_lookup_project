package com.app.user_interface;

public class TextUI {

    public static void print(String s){
        System.out.println(s);
    }

    //Summarize the query that has been sent
    public static void printRequest(String name, String server_IP, String request_type) {
        System.out.println("DnsLookup sending request for " + name +
                "\nServer: " + server_IP +
                "\nRequest type: " + request_type);
    }

    //When a valid response is received
    public static void printResponseTime(String time, String num_retries) {
        System.out.println("Response received after " + time + " milliseconds (" + num_retries + new String((Integer.valueOf(num_retries) > 1) ? " retries)." : " retry)."));
    }

    //When the response contains records in the Answer Section
    public static void printAnswerSection(String num_answer) {
        System.out.println("***Answer Section (" + num_answer + " records)***");
    }

    //A (IP address) records
    public static void printRecordsA(String request_type, String ip_addr, int seconds_can_cache, String auth) {
        System.out.println("IP " + " \t " + ip_addr + " \t " + seconds_can_cache + " \t " + auth);
    }

    //CNAME, NS records
    public static void printRecordsCNAMEorNS(String request_type, String alias, int seconds_can_cache, String auth) {
        System.out.println(request_type + " \t " + alias + " \t " + seconds_can_cache + " \t " + auth);
    }

    //MX records
    public static void printRecrodsMX(String request_type, String aliaspref, int seconds_can_cache, String auth) {
        System.out.println(request_type + " \t " + aliaspref + " \t " + seconds_can_cache + " \t " + auth);
    }

    //When response contains records in Additional Section
    public static void printAdditionalSection(String num_additional) {
        System.out.println("***Additional Section (" + num_additional + " records)***");
    }

    //When response contains records in Additional Section
    public static void printAuthoritySection(String num_authority) {
        System.out.println("***Authority Section (" + num_authority + " records)***");
    }

    public static void printError(int error, String description) {
        switch(error) {
            //If no record found
            case 1:
                System.out.println("NOTFOUND \t " + description);
                break;
            //any error occur during execution
            case 2:
                System.out.println("ERROR \t " + description);
                break;
            //Be specific with the error messages input syntax
            case 3:
                System.out.println("ERROR\tIncorrect input syntax: " + description);
                break;
            //Be specific with the error messages max retries
            case 4:
                System.out.println("ERROR\tMaximum number of retries " + description + " exceeded");
                break;
            //Be specific with the error messages unexpected response
            case 5:
                System.out.println("ERROR\tUnexpected response: " + description);
                break;
        }
    }
}