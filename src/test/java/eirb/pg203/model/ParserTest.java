package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ParserTest {

  private static final String ICS_PATH = "src/test/resources/i2.ics";

  private Instant createExpectedDate(String dateStr) {
    try {
      String cleanDate = dateStr.replace("Z", "");
      DateTimeFormatter f =
          DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss").withZone(ZoneId.of("UTC"));
      return Instant.from(f.parse(cleanDate));
    } catch (Exception e) {
      throw new RuntimeException("Erreur format date test", e);
    }
  }

  @Test
  public void testParseSimpleICS() {
    Calendar calendar = AbstractParser.chooseParser(ICS_PATH, "events");

    assertNotNull(calendar, "Le calendrier ne doit pas être null");

    List<CalendarComponent> events = calendar.getAllComponents();

    assertFalse(events.isEmpty(), "La liste ne doit pas être vide");
    assertNotNull(events.get(0));
  }

  @Test
  public void testParseFalsePath() {
    Calendar calendar = AbstractParser.chooseParser("chemin/inexistant.ics", "events");

    assertNotNull(calendar);
    assertTrue(calendar.getAllComponents().isEmpty(), "Devrait être vide sur erreur fichier");
  }

  @Test
  void testFirstEvent() {
    Calendar calendar = AbstractParser.chooseParser(ICS_PATH, "events");
    List<CalendarComponent> events = calendar.getAllComponents();

    Event e = (Event) events.get(0);

    assertEquals(createExpectedDate("20251104T215832Z"), e.creation_date);
    assertEquals("Présentation PFA", e.summary);
    assertEquals("EA- (AMPHI A)", e.location);

    assertNotNull(e.description);
    assertTrue(e.description.contains("JANIN David"));
    assertTrue(e.description.contains("Exporté le:04/11/2025 22:58"));
  }

  @Test
  void testLastEvent() {
    Calendar calendar = AbstractParser.chooseParser(ICS_PATH, "events");
    List<CalendarComponent> events = calendar.getAllComponents();

    Event e = (Event) events.get(events.size() - 1);

    assertEquals(createExpectedDate("20251104T215832Z"), e.creation_date);
    assertEquals("Présentation options S7", e.summary);
    assertEquals("EA- (AMPHI D)", e.location);
    assertTrue(e.description.contains("LOMBARDY Sylvain"));
  }

  @Test
  public void testMutipleLineDescription() {
    Calendar calendar = AbstractParser.chooseParser(ICS_PATH, "events");
    List<CalendarComponent> events = calendar.getAllComponents();

    Event e = (Event) events.get(1);

    assertEquals("ADE60323032352d323032362d3536342d302d33", e.uid);

    assertTrue(e.description.contains("EIN7-PROG"), "Description coupée non recollée ?");
    assertTrue(e.description.contains("FALLERI-VIALARD Jean-Remy"));
    assertTrue(e.description.contains("Programmation Orientée Objets"));
  }

  @Test
  public void testUrlSelection() {
    Calendar calendar = AbstractParser.chooseParser("http://google.com/fake.ics", "events");
    assertNotNull(calendar);
    assertTrue(calendar.getAllComponents().isEmpty());
  }
}
