package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OutputGenerationTest {

  @Test
  @DisplayName("OutputTxt writes file with mixed components")
  void outputTxtMixed() throws Exception {
    Calendar calEvents =
        AbstractParser.chooseParser("src/test/resources/events_minimal.ics", "events");
    Calendar calTodos = AbstractParser.chooseParser("src/test/resources/todos_mixed.ics", "todos");
    Calendar merged = new Calendar();
    calEvents.getAllComponents().forEach(merged::addComponent);
    calTodos.getAllComponents().forEach(merged::addComponent);

    Output out = new OutputTxt();
    Path tmp = Files.createTempFile("mixed", ".txt");
    out.displayCalendar(merged, tmp.toString());
    String content = Files.readString(tmp);
    assertTrue(content.contains("Event{"));
    assertTrue(content.contains("Todo{"));
  }

  @Test
  @DisplayName("OutputIcs writes file with mixed components")
  void outputIcsMixed() throws Exception {
    Calendar calEvents =
        AbstractParser.chooseParser("src/test/resources/events_minimal.ics", "events");
    Calendar calTodos = AbstractParser.chooseParser("src/test/resources/todos_mixed.ics", "todos");
    Calendar merged = new Calendar();
    calEvents.getAllComponents().forEach(merged::addComponent);
    calTodos.getAllComponents().forEach(merged::addComponent);

    Output out = new OutputIcs();
    Path tmp = Files.createTempFile("mixed", ".ics");
    out.displayCalendar(merged, tmp.toString());
    String content = Files.readString(tmp);
    assertTrue(content.contains("BEGIN:VEVENT"));
    assertTrue(content.contains("BEGIN:VTODO"));
  }
}
