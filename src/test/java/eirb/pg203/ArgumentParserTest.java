package eirb.pg203;

import static org.junit.jupiter.api.Assertions.*;

import eirb.pg203.model.EventFilter;
import eirb.pg203.model.TodoFilter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// sert à vérifier que le parser d'arguments fonctionne correctement
// i.e. transforme correctement les arguments en un objet CliConfig
// teste une grande majorité des cas : switch , if imbriqués, etc.
class ArgumentParserTest {

  private final ArgumentParser parser = new ArgumentParser();

  // helper pour masquer les System.err lors des tests d'erreurs
  private void runWithSilentErr(Runnable runnable) {
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(new ByteArrayOutputStream()));
    try {
      runnable.run();
    } finally {
      System.setErr(originalErr);
    }
  }

  // --- TESTS SUCCÈS (HAPPY PATH) ---

  @Test
  void testValidEventWithToday() {
    CliConfig config = parser.parse(new String[] {"f.ics", "events", "-today"});
    assertNotNull(config);
    assertTrue(config.eventFilter instanceof EventFilter.TodayFilter);
  }

  @Test
  void testValidTodoWithIncomplete() {
    CliConfig config = parser.parse(new String[] {"f.ics", "todos", "-incomplete"});
    assertNotNull(config);
    assertTrue(config.todoFilter instanceof TodoFilter.IncompleteFilter);
  }

  @Test
  void testValidDateRange() {
    CliConfig config =
        parser.parse(new String[] {"f.ics", "events", "-from", "20250101", "-to", "20250131"});
    assertNotNull(config);
    assertTrue(config.eventFilter instanceof EventFilter.DateRangeFilter);
  }

  // --- TESTS ECHECS (ERROR PATHS) ---

  @Test
  @DisplayName("Erreur : Arguments manquants")
  void testNotEnoughArgs() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {}));
          assertNull(parser.parse(new String[] {"file"}));
        });
  }

  @Test
  @DisplayName("Erreur : Type de composant invalide")
  void testInvalidComponentType() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {"f.ics", "badType"}));
        });
  }

  @Test
  @DisplayName("Erreur : Option inconnue")
  void testUnknownOption() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {"f.ics", "events", "-unknown"}));
        });
  }

  @Test
  @DisplayName("Erreur : -o sans fichier")
  void testMissingOutputFile() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {"f.ics", "events", "-o"}));
        });
  }

  // --- TESTS FILTRES EVENTS SUR TODOS (DOIT ECHOUER) ---

  @Test
  void testEventFiltersOnTodos() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-today"}));
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-tomorrow"}));
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-week"}));
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-from", "20250101"}));
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-to", "20250101"}));
        });
  }

  // --- TESTS FILTRES TODOS SUR EVENTS (DOIT ECHOUER) ---

  @Test
  void testTodoFiltersOnEvents() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {"f.ics", "events", "-incomplete"}));
          assertNull(parser.parse(new String[] {"f.ics", "events", "-all"}));
          assertNull(parser.parse(new String[] {"f.ics", "events", "-completed"}));
          assertNull(parser.parse(new String[] {"f.ics", "events", "-inprocess"}));
          assertNull(parser.parse(new String[] {"f.ics", "events", "-needsaction"}));
        });
  }

  // --- TESTS CUMULATIFS INTERDITS (EVENTS) ---

  @Test
  void testCumulativeTimeFilters() {
    runWithSilentErr(
        () -> {
          // -today + -tomorrow
          assertNull(parser.parse(new String[] {"f.ics", "events", "-today", "-tomorrow"}));
          // -today + -week
          assertNull(parser.parse(new String[] {"f.ics", "events", "-today", "-week"}));
          // -today + -from
          assertNull(parser.parse(new String[] {"f.ics", "events", "-today", "-from", "20250101"}));
          // -today + -to
          assertNull(parser.parse(new String[] {"f.ics", "events", "-today", "-to", "20250101"}));
        });
  }

  // --- TESTS CUMULATIFS INTERDITS (TODOS) ---

  @Test
  void testCumulativeStatusFilters() {
    runWithSilentErr(
        () -> {
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-incomplete", "-completed"}));
          assertNull(parser.parse(new String[] {"f.ics", "todos", "-all", "-needsaction"}));
        });
  }

  // --- TESTS PARSING DATES (-FROM / -TO) ---

  @Test
  void testDateErrors() {
    runWithSilentErr(
        () -> {
          // from sans date
          assertNull(parser.parse(new String[] {"f.ics", "events", "-from"}));
          // to sans date
          assertNull(parser.parse(new String[] {"f.ics", "events", "-to"}));
          // from format invalide
          assertNull(parser.parse(new String[] {"f.ics", "events", "-from", "not-a-date"}));
          // to format invalide
          assertNull(parser.parse(new String[] {"f.ics", "events", "-to", "not-a-date"}));
        });
  }

  @Test
  void testLogicDateErrors() {
    runWithSilentErr(
        () -> {
          // from sans To
          assertNull(parser.parse(new String[] {"f.ics", "events", "-from", "20250101"}));
          // to sans From
          assertNull(parser.parse(new String[] {"f.ics", "events", "-to", "20250101"}));
          // from après To
          assertNull(
              parser.parse(
                  new String[] {"f.ics", "events", "-from", "20250201", "-to", "20250101"}));
        });
  }
}
