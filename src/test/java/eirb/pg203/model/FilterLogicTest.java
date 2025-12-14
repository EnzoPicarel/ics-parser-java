package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// sert à tester la logique des filtres EventFilter et TodoFilter
// on vérifie que chaque filtre fonctionne comme prévu en filtrant les événements et les tâches
class FilterLogicTest {

  // --- UTILITAIRES DE CONSTRUCTION ---
  private Event createEvent(String summary, Instant startDate) {
    // constructeur Event (8 args) : uid, summary, location, created, start, end, desc, attendance
    return new Event(
        "uid",
        summary,
        "loc",
        Instant.now(),
        startDate, // c'est ce qui nous intéresse pour le filtre
        startDate != null ? startDate.plus(1, ChronoUnit.HOURS) : null,
        "desc",
        "att");
  }

  private Todo createTodo(String summary, String status) {
    // constructeur Todo (14 args) : status est le 11ème
    Instant now = Instant.now();
    return new Todo(
        "uid", summary, "loc", "1", "0", now, now, now, now, now,
        status, // c'est ce qui nous intéresse pour le filtre
        "att", "0", "org");
  }

  // --- TESTS EVENT FILTERs ---
  @Test
  @DisplayName("EventFilter.TodayFilter : Garde uniquement les events d'aujourd'hui")
  void testTodayFilter() {
    Instant now = Instant.now();
    Instant tomorrow = now.plus(1, ChronoUnit.DAYS);

    List<Event> events = new ArrayList<>();
    events.add(createEvent("Aujourd'hui", now));
    events.add(createEvent("Demain", tomorrow));
    events.add(createEvent("Sans date", null));

    EventFilter filter = new EventFilter.TodayFilter();
    List<Event> result = filter.filter(events);

    assertEquals(1, result.size());
    assertEquals("Aujourd'hui", result.get(0).summary);
  }

  @Test
  @DisplayName("EventFilter.TomorrowFilter : Garde uniquement les events de demain")
  void testTomorrowFilter() {
    Instant now = Instant.now();
    Instant tomorrow = now.plus(1, ChronoUnit.DAYS);

    List<Event> events = new ArrayList<>();
    events.add(createEvent("Aujourd'hui", now));
    events.add(createEvent("Demain", tomorrow));

    EventFilter filter = new EventFilter.TomorrowFilter();
    List<Event> result = filter.filter(events);

    assertEquals(1, result.size());
    assertEquals("Demain", result.get(0).summary);
  }

  @Test
  @DisplayName("EventFilter.WeekFilter : Garde les events de la semaine (J à J+7)")
  void testWeekFilter() {
    Instant now = Instant.now();
    Instant in3Days = now.plus(3, ChronoUnit.DAYS);
    Instant in8Days = now.plus(8, ChronoUnit.DAYS); // hors limite

    List<Event> events = new ArrayList<>();
    events.add(createEvent("Maintenant", now));
    events.add(createEvent("Dans 3 jours", in3Days));
    events.add(createEvent("Dans 8 jours", in8Days));

    EventFilter filter = new EventFilter.WeekFilter();
    List<Event> result = filter.filter(events);

    assertEquals(2, result.size());
    assertFalse(result.stream().anyMatch(e -> e.summary.equals("Dans 8 jours")));
  }

  @Test
  @DisplayName("EventFilter.DateRangeFilter : Garde les events dans une plage spécifique")
  void testDateRangeFilter() {
    // définition de la plage : 1er Janvier 2025 -> 31 Janvier 2025
    LocalDate start = LocalDate.of(2025, 1, 1);
    LocalDate end = LocalDate.of(2025, 1, 31);

    // on crée des instants basés sur la zone système (comme le fait ta classe EventFilter)
    ZoneId zone = ZoneId.systemDefault();
    Instant jan10 = start.plusDays(9).atStartOfDay(zone).toInstant();
    Instant feb1 = end.plusDays(1).atStartOfDay(zone).toInstant();

    List<Event> events = new ArrayList<>();
    events.add(createEvent("Janvier", jan10));
    events.add(createEvent("Fevrier", feb1));
    events.add(createEvent("Null", null));

    EventFilter filter = new EventFilter.DateRangeFilter(start, end);
    List<Event> result = filter.filter(events);

    assertEquals(1, result.size());
    assertEquals("Janvier", result.get(0).summary);
  }

  @Test
  @DisplayName("EventFilter.AllEventsFilter : Garde tout")
  void testAllEventsFilter() {
    List<Event> events = new ArrayList<>();
    events.add(createEvent("Un", Instant.now()));
    events.add(createEvent("Deux", null));

    EventFilter filter = new EventFilter.AllEventsFilter();
    List<Event> result = filter.filter(events);

    assertEquals(2, result.size());
  }

  // --- TESTS TODO FILTERS ---
  @Test
  @DisplayName("TodoFilter.CompletedFilter : Garde uniquement COMPLETED")
  void testCompletedFilter() {
    List<Todo> todos = new ArrayList<>();
    todos.add(createTodo("Fait", "COMPLETED"));
    todos.add(createTodo("Pas fait", "IN-PROCESS"));

    TodoFilter filter = new TodoFilter.CompletedFilter();
    List<Todo> result = filter.filter(todos);

    assertEquals(1, result.size());
    assertEquals("Fait", result.get(0).summary);
  }

  @Test
  @DisplayName("TodoFilter.IncompleteFilter : Tout sauf COMPLETED")
  void testIncompleteFilter() {
    List<Todo> todos = new ArrayList<>();
    todos.add(createTodo("Fait", "COMPLETED"));
    todos.add(createTodo("En cours", "IN-PROCESS"));
    todos.add(createTodo("A faire", "NEEDS-ACTION"));

    TodoFilter filter = new TodoFilter.IncompleteFilter();
    List<Todo> result = filter.filter(todos);

    assertEquals(2, result.size());
    assertFalse(result.stream().anyMatch(t -> t.status.equals("COMPLETED")));
  }

  @Test
  @DisplayName("TodoFilter.InProcessFilter : Garde uniquement IN-PROCESS")
  void testInProcessFilter() {
    List<Todo> todos = new ArrayList<>();
    todos.add(createTodo("En cours", "IN-PROCESS"));
    todos.add(createTodo("Autre", "NEEDS-ACTION"));

    TodoFilter filter = new TodoFilter.InProcessFilter();
    List<Todo> result = filter.filter(todos);

    assertEquals(1, result.size());
    assertEquals("En cours", result.get(0).summary);
  }

  @Test
  @DisplayName("TodoFilter.NeedsActionFilter : Garde uniquement NEEDS-ACTION")
  void testNeedsActionFilter() {
    List<Todo> todos = new ArrayList<>();
    todos.add(createTodo("Action", "NEEDS-ACTION"));
    todos.add(createTodo("Autre", "COMPLETED"));

    TodoFilter filter = new TodoFilter.NeedsActionFilter();
    List<Todo> result = filter.filter(todos);

    assertEquals(1, result.size());
    assertEquals("Action", result.get(0).summary);
  }

  @Test
  @DisplayName("TodoFilter.AllTodosFilter : Garde tout")
  void testAllTodosFilter() {
    List<Todo> todos = new ArrayList<>();
    todos.add(createTodo("A", "COMPLETED"));
    todos.add(createTodo("B", "WHATEVER"));

    TodoFilter filter = new TodoFilter.AllTodosFilter();
    List<Todo> result = filter.filter(todos);

    assertEquals(2, result.size());
  }
}
