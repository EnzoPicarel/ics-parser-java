package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ParserTest {

  private static final String ICS_PATH = "src/test/resources/i2.ics";

  @Test
  public void testParseSimpleICS() throws Exception {
    ArrayList<CalendarComponent> events = Parser.parse(ICS_PATH, "events");

    assertFalse(events.isEmpty());
    assertNotNull(events.get(0));
  }

  @Test
  public void testParseFalsePath() throws Exception {
    ArrayList<CalendarComponent> events = Parser.parse("nonexistent.ics", "events");
    assertNotNull(events);
    assertEquals(0, events.size());
  }

  @Test
  void testFirstEvent() throws Exception {
    ArrayList<CalendarComponent> events = Parser.parse(ICS_PATH, "events");
    Event e = (Event) events.get(0);

    assertEquals(e.creation_date, Parser.parseIcsDate("20251104T215832Z"));
    assertTrue(e.summary.equals("Présentation PFA"));
    assertTrue(e.location.equals("EA- (AMPHI A)"));

    assertTrue(e.description.contains("JANIN David"));
    assertTrue(e.description.contains("Exporté le:04/11/2025 22:58)"));
  }

  @Test
  void testLastEvent() throws Exception {
    ArrayList<CalendarComponent> events = Parser.parse(ICS_PATH, "events");
    Event e = (Event) events.get(events.size() - 1);

    assertEquals(e.creation_date, Parser.parseIcsDate("20251104T215832Z"));
    assertTrue(e.summary.equals("Présentation options S7"));
    assertTrue(e.location.equals("EA- (AMPHI D)"));
    assertTrue(e.description.contains("LOMBARDY Sylvain"));
    assertTrue(e.description.contains("(Exporté le:04/11/2025 22:58)"));
  }

  @Test
  public void testMutipleLineDescription() throws Exception {
    ArrayList<CalendarComponent> events = Parser.parse(ICS_PATH, "events");
    Event e = (Event) events.get(1);

    assertEquals(e.creation_date, Parser.parseIcsDate("20251104T215832Z"));
    assertEquals(e.uid, "ADE60323032352d323032362d3536342d302d33");
    assertTrue(e.description.contains("EIN7-PROG"));
    assertTrue(e.description.contains("FALLERI-VIALARD Jean-Remy"));
    assertTrue(e.description.contains("Programmation Orientée Objets"));
    assertTrue(e.description.contains("(Exporté le:04/11/2025 22:58)"));
  }
}
