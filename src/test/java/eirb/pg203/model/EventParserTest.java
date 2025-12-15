package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EventParserTest {

  @Test
  @DisplayName("Parse normal i2.ics events file")
  void parseNormalFile() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/i2.ics", "events");
    assertNotNull(cal);
    List<CalendarComponent> comps = cal.getAllComponents();
    assertFalse(comps.isEmpty());
    Event first = (Event) comps.get(0);
    assertNotNull(first.summary);
    assertNotNull(first.start_date);
    assertNotNull(first.end_date);
  }

  @Test
  @DisplayName("Folded lines are concatenated (events_folded.ics)")
  void foldedLines() throws Exception {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/events_folded.ics", "events");
    assertEquals(1, cal.getEvents().size());
    Event e = cal.getEvents().get(0);
    assertTrue(e.description.contains("Suite sur une nouvelle ligne"));
    assertTrue(e.description.contains("Continuation sur une ligne"));
  }

  @Test
  @DisplayName("Minimal event without optional fields")
  void minimalEvent() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/events_minimal.ics", "events");
    assertEquals(1, cal.getEvents().size());
    Event e = cal.getEvents().get(0);
    assertNull(e.location); // Not provided
    assertNull(e.description); // Not provided
    assertNotNull(e.creation_date);
  }

  @Test
  @DisplayName("Invalid DTSTAMP date returns null creation_date")
  void invalidDate() {
    Calendar cal =
        AbstractParser.chooseParser("src/test/resources/events_invalid_date.ics", "events");
    Event e = cal.getEvents().get(0);
    assertNull(e.creation_date, "Invalid date should parse to null");
    assertNotNull(e.start_date);
  }

  @Test
  @DisplayName("Non existent file returns empty calendar")
  void nonexistentFile() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/does_not_exist.ics", "events");
    assertNotNull(cal);
    assertTrue(cal.getEvents().isEmpty());
  }

  @Test
  @DisplayName("OutputTxt formatting for one event")
  void outputTxtEvent() throws Exception {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/events_minimal.ics", "events");
    Output out = new OutputTxt();
    Path tmp = Files.createTempFile("event-text", ".txt");
    out.displayCalendar(cal, tmp.toString());
    String content = Files.readString(tmp);
    assertTrue(content.startsWith("["));
    assertTrue(content.contains("Event{"));
    assertTrue(content.endsWith("]"));
  }

  @Test
  @DisplayName("OutputIcs formatting for one event")
  void outputIcsEvent() throws Exception {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/events_minimal.ics", "events");
    Output out = new OutputIcs();
    Path tmp = Files.createTempFile("event-ics", ".ics");
    out.displayCalendar(cal, tmp.toString());
    String content = Files.readString(tmp);
    assertTrue(content.contains("BEGIN:VCALENDAR"));
    assertTrue(content.contains("BEGIN:VEVENT"));
    assertTrue(content.contains("END:VEVENT"));
    assertTrue(content.contains("END:VCALENDAR"));
  }
}
