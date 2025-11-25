package eirb.pg203;

import eirb.pg203.model.*;
import java.util.List;

public class Client {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: clical path/to/file.ics events|todos optional-args");
      System.exit(1);
    }

    String file = args[0];
    String type = args[1];

    if (!type.equals("events") && !type.equals("todos")) {
      System.err.println("Second argument must be 'events' or 'todos'");
      System.exit(1);
    }

    Calendar C =  AbstractParser.chooseParser(file, type);
  }
}
