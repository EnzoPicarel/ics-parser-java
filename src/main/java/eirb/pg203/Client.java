package eirb.pg203;

import java.util.List;

public class Client {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: clical path/to/file.ics events|todos");
      System.exit(1);
    }
    String file = args[0];
    String type = args[1];
    if (!type.equals("events") && !type.equals("todos")) {
      System.err.println("Second argument must be 'events' or 'todos'");
      System.exit(1);
    }

    Calendar C = new Calendar(Parser.parse(file, type));

    List<Event> list_event = C.getEvents();
    System.out.println(list_event);
  }
}
