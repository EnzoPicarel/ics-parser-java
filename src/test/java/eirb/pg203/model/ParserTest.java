package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ParserTest {

  private static final String ICS_PATH = "src/test/resources/i2.ics";

  /**
   * Utilitaire local pour créer une date attendue (remplace l'ancien
   * Parser.parseIcsDate statique)
   */
  private Instant createExpectedDate(String dateStr) {
    try {
      // Adapte le format à celui de tes dates brutes (ex: 20251104T215832Z)
      String cleanDate = dateStr.replace("Z", "");
      DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss").withZone(ZoneId.of("UTC"));
      return Instant.from(f.parse(cleanDate));
    } catch (Exception e) {
      throw new RuntimeException("Erreur format date test", e);
    }
  }

  @Test
  public void testParseSimpleICS() {
    // 1. Choix du parser via la factory
    Calendar calendar = AbstractParser.chooseParser(ICS_PATH, "events");

    // 2. Vérifications de base
    assertNotNull(calendar, "Le calendrier ne doit pas être null");

    // Utilisation de TA méthode getAllComponents()
    List<CalendarComponent> events = calendar.getAllComponents();

    assertFalse(events.isEmpty(), "La liste ne doit pas être vide");
    assertNotNull(events.get(0));
  }

  @Test
  public void testParseFalsePath() {
    // Doit retourner un calendrier vide (grâce au try/catch dans ParserFile)
    Calendar calendar = AbstractParser.chooseParser("chemin/inexistant.ics", "events");

    assertNotNull(calendar);
    assertTrue(calendar.getAllComponents().isEmpty(), "Devrait être vide sur erreur fichier");
  }

  @Test
  void testFirstEvent() {
    Calendar calendar = AbstractParser.chooseParser(ICS_PATH, "events");
    List<CalendarComponent> events = calendar.getAllComponents();

    // Cast vers Event pour accéder aux champs spécifiques
    Event e = (Event) events.get(0);

    // Accès DIRECT aux champs publics (pas de getters)
    assertEquals(
        createExpectedDate("20251104T215832Z"),
        e.creation_date); // ou e.creation_date selon ton nom
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

    // Vérification de l'UID
    assertEquals("ADE60323032352d323032362d3536342d302d33", e.uid);

    // Vérification du "Unfolding" (IcsReader a bien fait son boulot ?)
    assertTrue(e.description.contains("EIN7-PROG"), "Description coupée non recollée ?");
    assertTrue(e.description.contains("FALLERI-VIALARD Jean-Remy"));
    assertTrue(e.description.contains("Programmation Orientée Objets"));
  }

  @Test
  public void testUrlSelection() {
    // Teste si la logique chooseParser détecte bien une URL
    // Cela va échouer silencieusement (retour vide) car l'URL n'existe pas, mais ça
    // ne doit pas
    // crasher.
    Calendar calendar = AbstractParser.chooseParser("http://google.com/fake.ics", "events");
    assertNotNull(calendar);
    assertTrue(calendar.getAllComponents().isEmpty());
  }
}
