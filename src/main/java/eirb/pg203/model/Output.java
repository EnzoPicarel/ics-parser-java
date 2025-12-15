package eirb.pg203.model;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Output {

  // début du bloc d'affichage (ex: [ ou BEGIN:VCALENDAR)
  public abstract String header();

  // fin du bloc d'affichage (ex: ] ou END:VCALENDAR)
  public abstract String footer();

  // affiche un event dans le format choisi
  public abstract String displayEvent(Event E);

  // affiche un todo dans le format choisi
  public abstract String displayTodo(Todo T);

  // affiche tout le calendrier (events et todos), soit sur la sortie standard,
  // soit dans un fichier
  public void displayCalendar(Calendar C, String file) {
    StringBuilder sb = new StringBuilder();

    sb.append(header()); // ajoute l'en-tête

    for (CalendarComponent CD : C.getAllComponents()) {
      sb.append(CD.printWith(this)); // affiche chaque composant avec le bon format
    }

    sb.append(footer()); // ajoute le pied de page

    String finalOutput = sb.toString();

    // si pas de nom de fichier, affiche sur la sortie standard, sinon écrit dans le
    // fichier
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
