package eirb.pg203;

import eirb.pg203.model.*;

public class Client {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: clical file.ics events|todos [-ics|-txt] [-o output_file]");
      System.exit(1);
    }

    String file = args[0];
    String type = args[1];

    if (!type.equals("events") && !type.equals("todos")) {
      System.err.println("Second argument must be 'events' or 'todos'");
      System.exit(1);
    }

    Output generator = new OutputTxt(); // Par défaut en TXT
    String outputFile = ""; // Par défaut vide = Console

    // 2. Analyse des arguments optionnels (à partir de l'index 2)
    for (int i = 2; i < args.length; i++) {
      switch (args[i]) {
        case "-ics":
          generator = new OutputIcs();
          break;
        case "-txt":
          generator = new OutputTxt();
          break;
        case "-o":
          if (i + 1 < args.length) {
            outputFile = args[i + 1];
            i++;
          } else {
            System.err.println("Error: -o require a filename");
            System.exit(1);
          }
          break;
      }
    }

    Calendar C = AbstractParser.chooseParser(file, type);

    generator.displayCalendar(C, outputFile);
  }
}
