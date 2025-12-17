package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MixedIcsBasicTest {

  private static final String ICS_PATH = "src/test/resources/mixed.ics";

  @Test
  void parserLoadsEverythingFromMixedFile() {
    Calendar cal = AbstractParser.chooseParser(ICS_PATH, "events");
    assertNotNull(cal, "Le calendrier ne doit pas être null");
    assertFalse(cal.getEvents().isEmpty(), "Le calendrier brut doit contenir les événements");
    assertFalse(cal.getTodos().isEmpty(), "Le calendrier brut doit AUSSI contenir les todos");
  }

  @Test
  void knownComponentsArePresent() {
    Calendar cal = AbstractParser.chooseParser(ICS_PATH, "ignored");
    boolean hasEvent = cal.getEvents().stream().anyMatch(e -> "Présentation PFA".equals(e.summary));
    assertTrue(hasEvent, "L'événement 'Présentation PFA' doit être chargé");
    boolean hasTodo =
        cal.getTodos().stream().anyMatch(t -> "Réviser l'examen de POO".equals(t.summary));
    assertTrue(hasTodo, "Le Todo 'Réviser l'examen de POO' doit être chargé");
  }
}
