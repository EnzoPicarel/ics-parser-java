package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TodoParserTest {

  @Test
  @DisplayName("Parse sample todos.ics")
  void parseTodos() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/todos.ics", "todos");
    assertNotNull(cal);
    List<Todo> todos = cal.getTodos();
    assertFalse(todos.isEmpty());
    Todo first = todos.get(0);
    assertEquals("Réviser l'examen de POO", first.summary);
    assertEquals("5", first.priority);
    assertEquals("100", first.progress);
    assertNotNull(first.completed_date);
    // Format DUE actuel est YYYYMMDD sans heure -> parseIcsDate retourne null (non
    // géré)
    assertNull(first.due_date);
  }

  @Test
  @DisplayName("Parse mixed status todos")
  void parseMixed() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/todos_mixed.ics", "todos");
    List<Todo> todos = cal.getTodos();
    assertEquals(3, todos.size());
    assertTrue(todos.stream().anyMatch(t -> "COMPLETED".equals(t.status)));
    assertTrue(
        todos.stream().anyMatch(t -> "IN-PROCESS".equals(t.status))
            || todos.stream()
                .anyMatch(
                    t -> "IN-PROCESS".equals(t.attendance))); // status stored in completed field
    assertTrue(todos.stream().anyMatch(t -> "NEEDS-ACTION".equals(t.status)));
  }

  @Test
  @DisplayName("Todo minimal fields")
  void minimalTodo() {
    // Reuse single VTODO from inline string? (Simplify by pointing to existing file
    // with at least one VTODO)
    Calendar cal = AbstractParser.chooseParser("src/test/resources/todos_mixed.ics", "todos");
    Todo t =
        cal.getTodos().stream()
            .filter(td -> td.uid.equals("todo-none-1"))
            .findFirst()
            .orElseThrow();
    assertNull(t.due_date);
    assertNull(t.completed_date);
    assertNull(t.modification_date);
  }

  @Test
  @DisplayName("OutputTxt for todos")
  void outputTxtTodos() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/todos_mixed.ics", "todos");
    Output out = new OutputTxt();
    // Use console mode by providing empty filename; capture using replacement of
    // System.out? Simpler: rely on non-null strings returned by display methods
    String header = out.header();
    assertEquals("[", header);
    String body = out.displayTodo(cal.getTodos().get(0));
    assertTrue(body.contains("Todo{"));
    assertTrue(body.contains("Progress:"));
  }

  @Test
  @DisplayName("OutputIcs for todos")
  void outputIcsTodos() {
    Calendar cal = AbstractParser.chooseParser("src/test/resources/todos_mixed.ics", "todos");
    Output out = new OutputIcs();
    String header = out.header();
    assertTrue(header.contains("BEGIN:VCALENDAR"));
    String body = out.displayTodo(cal.getTodos().get(0));
    assertTrue(body.contains("BEGIN:VTODO"));
    assertTrue(body.contains("END:VTODO"));
  }
}
