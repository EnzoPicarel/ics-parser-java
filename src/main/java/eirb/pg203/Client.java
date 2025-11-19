package eirb.pg203;

import java.util.ArrayList;
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

    ArrayList<CalendarComponent> components = Parser.parse(file, type);
    for (CalendarComponent c : components) {
      System.out.println(c);
    }
  }
}
