package eirb.pg203;

import java.util.ArrayList;
import java.util.List;

public class Client {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("ADD PATH");
      System.exit(1);
    }

    List<Event> List_event = new ArrayList<>();
    List_event = Parser.parse(args[0]);
    System.out.println(List_event);
  }
}
