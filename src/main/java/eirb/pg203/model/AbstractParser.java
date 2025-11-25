package eirb.pg203.model;

import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractParser { // abstract donc on peut pa l'instancier
  public static Calendar chooseParser(
      String source,
      String type) { // static car sert de factory => fabrique l'instance, doit être accessible sans
    // instance préalable
    // redirection vers le parser adéquat
    if (source.startsWith("http://") || source.startsWith("https://")) {
      ParserURL parserURL = new ParserURL();
      return parserURL.parse(source, type);
    } else {
      ParserFile parserFile = new ParserFile();
      return parserFile.parse(source, type);
    }
  }

  // partie commune du parsing
  protected Calendar parseStream(Reader reader, String type) throws IOException {
    Calendar calendar = new Calendar(); // Assure-toi d'avoir un constructeur vide ou par défaut

    try (IcsReader icsReader =
        new IcsReader(
            reader)) { // utilisation de la classe IcsReader pour gérer les lignes continues ->
      // intéret de AutoCloseable
      String line;
      while ((line = icsReader.readLine()) != null) {
        // On ne parse que les composants correspondant au type demandé.
        // Ancienne version lisait tout et supposait une homogénéité du fichier.
        if (type.equals("events") && line.equals("BEGIN:VEVENT")) {
          Event event = parseVEvent(icsReader); // icsReader est positionné après BEGIN:VEVENT
          if (event != null) calendar.addComponent(event);
        } else if (type.equals("todos") && line.equals("BEGIN:VTODO")) {
          Todo todo = parseVTodo(icsReader);
          if (todo != null) calendar.addComponent(todo);
        }
      }
    }
    return calendar;
  }

  // parsing des event
  private Event parseVEvent(IcsReader r) throws IOException {
    Map<String, String> map = parseComponentProperties(r, "VEVENT");
    if (map.isEmpty()) return null;
    return new Event(
        map.get("UID"),
        map.get("SUMMARY"),
        map.get("LOCATION"),
        parseIcsDate(map.get("DTSTAMP")),
        parseIcsDate(map.get("DTSTART")),
        parseIcsDate(map.get("DTEND")),
        map.get("DESCRIPTION"),
        null);
  }

  // parsing des todos
  private Todo parseVTodo(IcsReader r) throws IOException {
    Map<String, String> map = parseComponentProperties(r, "VTODO");
    if (map.isEmpty()) return null;
    return new Todo(
        map.get("UID"),
        map.get("SUMMARY"),
        map.get("LOCATION"),
        map.get("PRIORITY"),
        map.get("PERCENT-COMPLETE"),
        parseIcsDate(map.get("COMPLETED")),
        parseIcsDate(map.get("DUE")),
        parseIcsDate(map.get("LAST-MODIFIED")),
        parseIcsDate(map.get("DTSTAMP")),
        map.get("STATUS"),
        map.get("CLASS"),
        map.get("SEQUENCE"));
  }

  // remplit une map tant qu'on n'a pas atteint la balise de fin
  private Map<String, String> parseComponentProperties(IcsReader r, String componentName)
      throws IOException {
    Map<String, String> map = new HashMap<>();
    String line;
    String endTag = "END:" + componentName;

    while ((line = r.readLine()) != null) {
      if (line.equals(endTag)) {
        break;
      }
      // découpage clé/valeur
      // on limite le split à 2 pour ne pas couper s'il y a des ':' dans la
      // description
      String[] parts = line.split(":", 2);
      if (parts.length == 2) {
        String key =
            parts[0]
                .split(";")[
                0]; // On ignore les paramètres (ex: DTSTART;TZID=...) pour simplifier l'iter 1&2
        map.put(key, parts[1]);
      }
    }
    return map;
  }

  protected Instant parseIcsDate(String s) {
    if (s == null || s.isEmpty()) return null;
    try {
      // gestion basique du format Z (UTC)
      // pour une gestion complète des Timezones, c'est plus complexe, mais suffisant
      // pour ce
      // projet.
      String cleanDate = s.replace("Z", "");
      DateTimeFormatter f =
          DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss").withZone(ZoneId.of("UTC"));
      return Instant.from(f.parse(cleanDate));
    } catch (Exception e) {
      // fallback ou gestion de date courte (yyyyMMdd) si nécessaire
      return null;
    }
  }

  public abstract Calendar parse(String source, String type); // méthode abstraite
}
