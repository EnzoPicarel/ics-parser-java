package eirb.pg203.model;

import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractParser {
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
    Calendar calendar = new Calendar();

    try (IcsReader icsReader =
        new IcsReader(
            reader)) { // utilisation de la classe IcsReader pour gérer les lignes continues ->
      // intéret de AutoCloseable
      String line;
      while ((line = icsReader.readLogicalLine()) != null) {
        // Parse TOUS les composants, indépendamment du type demandé
        // Le filtrage se fera plus tard (getEvents() ou getTodos())
        if (line.equals("BEGIN:VEVENT")) {
          Event event = parseVEvent(icsReader); // icsReader est positionné après BEGIN:VEVENT
          if (event != null) calendar.addComponent(event);
        } else if (line.equals("BEGIN:VTODO")) {
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
        parseIcsDate(map.get("DTSTART")),
        map.get("STATUS"),
        map.get("CLASS"),
        map.get("SEQUENCE"),
        map.get("ORGANIZER"));
  }

  // remplit une map tant qu'on n'a pas atteint la balise de fin
  private Map<String, String> parseComponentProperties(IcsReader r, String componentName)
      throws IOException {
    Map<String, String> map = new HashMap<>();
    String line;
    String endTag = "END:" + componentName;

    while ((line = r.readLogicalLine()) != null) {
      if (line.equals(endTag)) {
        break;
      }
      // découpage clé/valeur
      // on limite le split à 2 pour ne pas couper s'il y a des ':' dans la
      // description
      String[] parts = line.split(":", 2);
      if (parts[0].startsWith("ORGANIZER")) {
        parts = line.split(";", 2); // ex parts[0] = "ORGANIZER" parts[1] = "CN="Alice":mailto:..."
        map.put(parts[0], parts[1]);
      } else if (parts.length == 2) {
        parts[0] = parts[0].split(";")[0]; // ex parts[0] = DUE;VALUE=DATE -> DUE
        map.put(parts[0], parts[1]);
      }
    }
    return map;
  }

  protected Instant parseIcsDate(String s) {
    if (s == null || s.isEmpty()) return null;
    try {
      // 1. Cas "Date-Heure" (ex: 20251104T204504Z)
      if (s.contains("T")) {
        String cleanDate = s.replace("Z", "");
        DateTimeFormatter f =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss").withZone(ZoneId.of("UTC"));
        return Instant.from(f.parse(cleanDate));
      }
      // 2. Cas "Date Seule" (ex: 20251107) -> format utilisé par DUE
      else {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMdd");
        // On parse en LocalDate (jour), puis on dit que c'est le début de cette journée
        // en UTC
        LocalDate date = LocalDate.parse(s, f);
        return date.atStartOfDay(ZoneId.of("UTC")).toInstant();
      }
    } catch (Exception e) {
      // En cas d'erreur, on l'affiche pour déboguer au lieu de renvoyer null
      // silencieusement
      System.err.println("Erreur parsing date : " + s);
      return null;
    }
  }

  public abstract Calendar parse(String source, String type);
}
