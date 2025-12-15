package eirb.pg203.model;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class OutputIcs extends Output {

  private static final DateTimeFormatter ICS_DATETIME =
      DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC);

  private static final DateTimeFormatter ICS_DATE_ONLY =
      DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);

  @Override
  public String header() {
    return """
        BEGIN:VCALENDAR\r
        VERSION:2.0\r
        PRODID:-//ADE/version 6.0\r
        CALSCALE:GREGORIAN\r
        METHOD:PUBLISH\r
        """;
  }

  @Override
  public String footer() {
    return "END:VCALENDAR\r\n";
  }

  @Override
  public String displayEvent(Event e) {
    StringBuilder sb = new StringBuilder();
    sb.append("BEGIN:VEVENT\n");

    sb.append(generateLine("DTSTAMP", fmt(e.creation_date)))
        .append(generateLine("UID", e.uid))
        .append(generateLine("DTSTART", fmt(e.start_date)))
        .append(generateLine("DTEND", fmt(e.end_date)))
        .append(generateLine("SUMMARY", e.summary))
        .append(generateLine("LOCATION", e.location))
        .append(generateLine("DESCRIPTION", e.description));

    sb.append("END:VEVENT\n");
    return sb.toString();
  }

  @Override
  public String displayTodo(Todo t) {
    StringBuilder sb = new StringBuilder();
    sb.append("BEGIN:VTODO\n");

    sb.append(generateLine("DTSTAMP", fmt(t.creation_date)))
        .append(generateLine("UID", t.uid))
        .append(generateLine("SUMMARY", t.summary))
        .append(generateLine("STATUS", t.status))
        .append(generateLine("DSTART;VALUE=DATE", fmtDate(t.date_start)))
        .append(generateLine("DUE;VALUE=DATE", fmtDate(t.due_date)))
        .append(generateLine("COMPLETED", fmt(t.completed_date)))
        .append(generateLine("LAST-MODIFIED", fmt(t.modification_date)))
        .append(generateLine("PRIORITY", t.priority))
        .append(generateLine("PERCENT-COMPLETE", t.progress))
        .append(generateLine("SEQUENCE", t.sequence))
        .append(generateLine("CLASS", t.attendance))
        .append(generateLine("ORGANIZER", t.organizer));

    sb.append("END:VTODO\n");
    return sb.toString();
  }

  private String generateLine(String key, String value) {
    if (value != null && !value.isEmpty()) {
      if (key.equals("ORGANIZER")) {
        return key + ";" + value + "\n";
      }
      return key + ":" + value + "\n";
    }
    return "";
  }

  public static String fmt(Instant i) {
    return (i == null) ? null : ICS_DATETIME.format(i);
  }

  public static String fmtDate(Instant i) {
    return (i == null) ? null : ICS_DATE_ONLY.format(i);
  }
}
