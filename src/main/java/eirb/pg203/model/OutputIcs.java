package eirb.pg203.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OutputIcs extends Output {

  // Formatteur standard pour ICS : YYYYMMDDTHHMMSSZ (en UTC)
  private static final DateTimeFormatter ICS_DATETIME =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC);

  // Formatteur pour les dates seules (sans heure) : YYYYMMDD
  private static final DateTimeFormatter ICS_DATE_ONLY =
      DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);

  @Override
  public String header() {
    String s = "";
    s += "BEGIN:VCALENDAR\r\n";
    s += "VERSION:2.0\r\n";
    s += "PRODID:-//ADE/version 6.0\r\n";
    s += "CALSCALE:GREGORIAN\r\n";
    s += "METHOD:PUBLISH\r\n";
    return s;
  }

  @Override
  public String footer() {
    return "END:VCALENDAR\r\n";
  }

  @Override
  public String displayEvent(Event e) {
    String s = "BEGIN:VEVENT\r\n";

    s += generateLine("DTSTAMP", fmt(e.creation_date != null ? e.creation_date : Instant.now()));
    s += generateLine("UID", e.uid);

    s += generateLine("DTSTART", fmt(e.start_date));
    s += generateLine("DTEND", fmt(e.end_date));

    s += generateLine("SUMMARY", escape(e.summary));
    s += generateLine("LOCATION", escape(e.location));
    s += generateLine("DESCRIPTION", escape(e.description));

    s += "END:VEVENT\r\n";
    return s;
  }

  @Override
  public String displayTodo(Todo t) {
    String s = "BEGIN:VTODO\r\n";

    s += generateLine("DTSTAMP", fmt(t.creation_date != null ? t.creation_date : Instant.now()));
    s += generateLine("UID", t.uid);

    s += generateLine("SUMMARY", escape(t.summary));

    if (t.due_date != null) {
      s += "DUE;VALUE=DATE:" + fmtDate(t.due_date) + "\r\n";
    }

    s += generateLine("COMPLETED", fmt(t.completed_date));
    s += generateLine("LAST-MODIFIED", fmt(t.modification_date));

    // Propriétés entières / états
    if (t.priority != null) s += "PRIORITY:" + t.priority + "\r\n";
    if (t.progress != null) s += "PERCENT-COMPLETE:" + t.progress + "\r\n";
    if (t.sequence != null) s += "SEQUENCE:" + t.sequence + "\r\n";

    if (Boolean.TRUE.equals(t.completed)) {
      s += "STATUS:COMPLETED\r\n";
    }

    s += "END:VTODO\r\n";
    return s;
  }

  // --- MÉTHODES UTILITAIRES PRIVÉES ---

  /** Génère une ligne "CLE:VALEUR\r\n" ou renvoie une chaîne vide si la valeur est nulle. */
  private String generateLine(String key, String value) {
    if (value != null && !value.isEmpty()) {
      return key + ":" + value + "\r\n";
    }
    return "";
  }

  private static String fmt(Instant i) {
    if (i == null) return null;
    return ICS_DATETIME.format(i);
  }

  private static String fmtDate(Instant i) {
    if (i == null) return null;
    return ICS_DATE_ONLY.format(i);
  }

  /** Échappe les caractères spéciaux selon la RFC 5545. */
  private static String escape(String s) {
    if (s == null) return null;
    return s.replace("\\", "\\\\")
        .replace(";", "\\;")
        .replace(",", "\\,")
        .replace("\n", "\\n")
        .replace("\r", "");
  }
}
