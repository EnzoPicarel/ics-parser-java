package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MixedComponentsFilteringTest {

  private static final String MIXED_PATH = "src/test/resources/mixed_events_todos.ics";

  @Test
  @DisplayName("Calendar parsed from mixed file contains both events and todos")
  void parsedBothTypes() {
    Calendar cal = AbstractParser.chooseParser(MIXED_PATH, "events");
    assertEquals(2, cal.getEvents().size(), "Should parse exactly 2 events");
    assertEquals(2, cal.getTodos().size(), "Should parse exactly 2 todos from mixed file");
  }

  @Test
  @DisplayName("Event summaries correct and not polluted by VTODO lines")
  void eventDataIntegrity() {
    Calendar cal = AbstractParser.chooseParser(MIXED_PATH, "events");
    assertTrue(cal.getEvents().stream().anyMatch(e -> ((Event) e).summary.equals("Event One")));
    assertTrue(cal.getEvents().stream().anyMatch(e -> ((Event) e).summary.equals("Event Two")));
  }

  @Test
  @DisplayName("Todo summaries correct and not polluted by VEVENT lines")
  void todoDataIntegrity() {
    Calendar cal = AbstractParser.chooseParser(MIXED_PATH, "todos");
    assertTrue(cal.getTodos().stream().anyMatch(t -> ((Todo) t).summary.equals("Todo One")));
    assertTrue(cal.getTodos().stream().anyMatch(t -> ((Todo) t).summary.equals("Todo Two")));
  }
}
