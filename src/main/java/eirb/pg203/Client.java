package eirb.pg203;

import java.util.List;

public class Client {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("ADD PATH");
      System.exit(1);
    }

    Calendar C = new Calendar(Parser.parse(args[0]));

    List<Event> list_event = C.getEvents();
    System.out.println(list_event);
  }
}
