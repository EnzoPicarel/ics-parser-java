package eirb.pg203.model;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Output {

  public abstract String header();

  public abstract String footer();

  public abstract String displayEvent(Event E);

  public abstract String displayTodo(Todo T);

  public void displayCalendar(Calendar C, String file) {
    if (file == null || file.isEmpty()) {

      System.out.print(header());

      for (CalendarComponent CD : C.getAllComponents()) {
        if (CD instanceof Event) {
          System.out.print(displayEvent((Event) CD));
        } else {
          System.out.print(displayTodo((Todo) CD));
        }
      }
      System.out.print(footer());

    } else {
      // Mode Fichier
      try (FileWriter writer = new FileWriter(file)) {
        writer.write(header());

        for (CalendarComponent CD : C.getAllComponents()) {
          String data;
          if (CD instanceof Event) {
            data = displayEvent((Event) CD);
          } else {
            data = displayTodo((Todo) CD);
          }
          writer.write(data);
        }

        writer.write(footer());
        writer.close();
      } catch (IOException e) {
        System.out.println("An error occurred while writing the file.");
        e.printStackTrace();
      }
    }
  }
}
