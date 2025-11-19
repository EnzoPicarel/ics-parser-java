package eirb.pg203;

import eirb.pg203.model.*;
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
    List<CalendarComponent> list_component = C.getAllComponents();
    System.out.println(list_component);
  }
}
