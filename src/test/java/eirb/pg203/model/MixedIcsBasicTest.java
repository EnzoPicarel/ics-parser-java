package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MixedIcsBasicTest {

  private static final String ICS_PATH = "src/test/resources/mixed.ics";

  @Test
  void eventsSelectionLoadsOnlyEvents() {
    Calendar cal = AbstractParser.chooseParser(ICS_PATH, "events");
    assertNotNull(cal, "Le calendrier ne doit pas être null");
    assertFalse(cal.getEvents().isEmpty(), "Doit contenir au moins un VEVENT");
    assertTrue(cal.getTodos().isEmpty(), "Aucun VTODO attendu quand on demande 'events'");
  }

  @Test
  void todosSelectionLoadsOnlyTodos() {
    Calendar cal = AbstractParser.chooseParser(ICS_PATH, "todos");
    assertNotNull(cal, "Le calendrier ne doit pas être null");
    assertFalse(cal.getTodos().isEmpty(), "Doit contenir au moins un VTODO");
    assertTrue(cal.getEvents().isEmpty(), "Aucun VEVENT attendu quand on demande 'todos'");
  }

  @Test
  void knownEventSummaryIsPresent() {
    Calendar cal = AbstractParser.chooseParser(ICS_PATH, "events");
    assertTrue(
        cal.getEvents().stream()
            .anyMatch(c -> ((Event) c).summary.equals("Présentation PFA")),
        "L'événement 'Présentation PFA' doit être présent dans les VEVENTs");
  }
}
