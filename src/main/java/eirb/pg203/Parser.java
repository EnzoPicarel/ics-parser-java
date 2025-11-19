package eirb.pg203;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
  private Parser() {}

  static Instant parseIcsDate(String s) {
    if (s == null || s.isEmpty()) return null;
    try {
      DateTimeFormatter fZulu =
          DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneId.of("UTC"));
      return Instant.from(fZulu.parse(s));
    } catch (Exception e) {
      return null;
    }
  }

  static ArrayList<CalendarComponent> parse(String filePath) {
    ArrayList<CalendarComponent> listEvent = new ArrayList<>();

    try (FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {

      String line;
      HashMap<String, String> map = null;
      String lastKey = null; // Sert à se souvenir de la clé précédente (ex: "DESCRIPTION")

      while ((line = bufferedReader.readLine()) != null) {

        // 1. Début d'un événement
        if (line.contains("BEGIN:VEVENT")) {
          map = new HashMap<>();
          lastKey = null;
          continue;
        }

        // 2. Fin d'un événement : on crée l'objet
        if (line.contains("END:VEVENT") && map != null) {
          Event event =
              new Event(
                  map.get("UID"),
                  map.get("SUMMARY"),
                  map.get("LOCATION"),
                  parseIcsDate(map.get("DTSTAMP")),
                  parseIcsDate(map.get("DTSTART")),
                  parseIcsDate(map.get("DTEND")),
                  map.get("DESCRIPTION"),
                  null);
          listEvent.add(event);
          map = null; // On reset pour le prochain
          continue;
        }

        // 3. Traitement du contenu de l'événement
        if (map != null && !line.isEmpty()) {

          // CAS A : C'est une suite de ligne (commence par un espace)
          if (line.startsWith(" ") && lastKey != null) {
            String suiteDuTexte = line.substring(1); // On enlève l'espace du début
            String ancienTexte = map.get(lastKey);

            // On colle la suite au texte existant
            map.put(lastKey, ancienTexte + suiteDuTexte);

          }
          // CAS B : C'est une nouvelle propriété (ex: "LOCATION:Salle B")
          else {
            String[] parts = line.split(":", 2); // On coupe uniquement au premier ":""
            if (parts[1] != "") {
              String key = parts[0];
              String value = parts[1];

              map.put(key, value);
              lastKey = key; // On mémorise la clé au cas où la ligne suivante est une suite
            } else {
              map.put(parts[0], "UNDEFINED");
            }
          }
        }
      }
      return listEvent;

    } catch (FileNotFoundException e) {
      System.out.println("Fichier introuvable : " + e.getMessage());
      return new ArrayList<>();
    } catch (IOException e) {
      System.out.println("Erreur de lecture : " + e.getMessage());
      return new ArrayList<>();
    }
  }
}
