package eirb.pg203.model;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Output {

  public abstract String header();

  public abstract String footer();

  public abstract String displayEvent(Event E);

  public abstract String displayTodo(Todo T);

  public void displayCalendar(Calendar C, String file) {
    // 1. Étape de Construction : On accumule tout le texte en mémoire
    StringBuilder sb = new StringBuilder();

    sb.append(header());

    for (CalendarComponent CD : C.getAllComponents()) {
      sb.append(CD.printWith(this));
    }

    sb.append(footer());

    // 2. Étape de Sortie : On décide où envoyer le texte final
    String finalOutput = sb.toString();

    if (file == null || file.isEmpty()) {
      // Mode Console
      System.out.print(finalOutput);
    } else {
      // Mode Fichier
      try (FileWriter writer = new FileWriter(file)) {
        writer.write(finalOutput);
        // Pas besoin de writer.close() ici car tu utilises le "try-with-resources"
      } catch (IOException e) {
        System.err.println("An error occurred while writing the file: " + file);
        e.printStackTrace();
      }
    }
  }
}
