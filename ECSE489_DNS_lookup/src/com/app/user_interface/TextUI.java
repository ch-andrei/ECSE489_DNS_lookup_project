package com.app.user_interface;

public class TextUI {

    public static void print(String s){
        System.out.println(s);
    }

    //Summarize the query that has been sent
    public static void printRequest(String name, String server_IP, String request_type) {
        print("DnsClient sending request for " + name +
                "\nServer: " + server_IP +
                "\nRequest type: " + request_type);
    }

    //When a valid response is received
    public static void printResponseTime(String time, String num_retries) {
        print("Response received after " + time + " seconds (" + num_retries + " retries)");
    }

    //When the response contains records in the Answer Section
    public static void printAnswerSection(String num_answer) {
        print("***Answer Section (" + num_answer + " records)***");
    }

    //A (IP address) records
    public static void printRecordsA(String request_type, String ip_addr, int seconds_can_cache, String auth) {
        print("IP " + "\t" + ip_addr + "\t" + seconds_can_cache + "\t" + auth);
    }

    //CNAME, NS records
    public static void printRecordsCNAMEorNS(String request_type, String alias, int seconds_can_cache, String auth) {
        print(request_type + "\t" + alias + "\t" + seconds_can_cache + "\t" + auth);
    }

    //MX records
    public static void printRecrodsMX(String request_type, String alias, String pref, int seconds_can_cache, String auth) {
        print(request_type + "\t" + alias + "\t" + pref + "\t" + seconds_can_cache + "\t" + auth);
    }

    //When response contains records in Additional Section
    public static void printAdditionalSection(String num_additional) {
        print("***Additional Section (" + num_additional + " records)***");
    }

    public static void printError(int error, String description) {
        switch(error) {
            //If no record found
            case 1:
                print("NOTFOUND");
                break;
            //any error occur during execution
            case 2:
                print("ERROR\t" + description);
                System.exit(0);
                break;
            //Be specific with the error messages input syntax
            case 3:
                print("ERROR\tIncorrect input syntax: " + description);
                System.exit(0);
                break;
            //Be specific with the error messages max retries
            case 4:
                print("ERROR\tMaximum number of retries " + description + " exceeded");
                System.exit(0);
                break;
            //Be specific with the error messages unexpected response
            case 5:
                print("ERROR\tUnexpected response " + description);
                System.exit(0);
                break;
        }
    }
}