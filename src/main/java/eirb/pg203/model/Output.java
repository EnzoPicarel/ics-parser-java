package eirb.pg203.model;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Output {

  public abstract String header();

  public abstract String footer();

  public abstract String displayEvent(Event E);

  public abstract String displayTodo(Todo T);

  public void displayCalendar(Calendar C, String file) {
    StringBuilder sb = new StringBuilder();

    sb.append(header());

    for (CalendarComponent CD : C.getAllComponents()) {
      sb.append(CD.printWith(this));
    }

    sb.append(footer());

    String finalOutput = sb.toString();

    if (file == null || file.isEmpty()) {
      System.out.print(finalOutput);
    } else {
      try (FileWriter writer = new FileWriter(file)) {
        writer.write(finalOutput);
      } catch (IOException e) {
        System.err.println("An error occurred while writing the file: " + file);
        e.printStackTrace();
      }
    }
  }
}
