package eirb.pg203;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private static final String USAGE = "Usage: java Client <path_to_ics_file>";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println(USAGE);
            System.exit(1);
        }
        
        List<Event> List_event = new ArrayList<>();
        List_event = Parser.parse(args[0]);
        System.out.println(List_event);
    }
}