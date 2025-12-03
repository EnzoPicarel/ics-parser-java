package eirb.pg203;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests de base pour couvrir la classe Client (argument parsing + génération). */
public class ClientTest {

  @Test
  @DisplayName("Client main events -> TXT console output")
  void clientMainEventsTxt() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream original = System.out;
    try {
      System.setOut(new PrintStream(out));
      Client.main(new String[] {"src/test/resources/events_minimal.ics", "events"});
    } finally {
      System.setOut(original);
    }
    String content = out.toString();
    assertTrue(content.startsWith("["));
    assertTrue(content.contains("Event{"));
    assertTrue(content.endsWith("]"));
  }

  @Test
  @DisplayName("Client main todos -> ICS file output")
  void clientMainTodosIcsFile() throws Exception {
    Path tmp = Files.createTempFile("client-todos", ".ics");
    Client.main(
        new String[] {"src/test/resources/todos_mixed.ics", "todos", "-ics", "-o", tmp.toString()});
    String content = Files.readString(tmp);
    assertTrue(content.contains("BEGIN:VCALENDAR"));
    assertTrue(content.contains("BEGIN:VTODO"));
    assertTrue(content.contains("END:VCALENDAR"));
  }

  @Test
  @DisplayName("Client main todos -> HTML file output")
  void clientMainTodosHtmlFile() throws Exception {
    Path tmp = Files.createTempFile("client-todos", ".ics");
    Client.main(
        new String[] {
          "src/test/resources/mixed_events_todos.ics", "todos", "-html", "-o", tmp.toString()
        });
    String content = Files.readString(tmp);
    assertTrue(content.contains("<tr class=\"todo\">"));
    assertTrue(content.contains("<span class=\"badge badge-todo\">"));
    assertFalse(content.contains("<tr class=\"event\">"));
    assertFalse(content.contains("<span class=\"badge badge-event\">"));
  }

  @Test
  @DisplayName("Client main event -> HTML file output")
  void clientMainEventHtmlFile() throws Exception {
    Path tmp = Files.createTempFile("client-todos", ".ics");
    Client.main(
        new String[] {
          "src/test/resources/mixed_events_todos.ics", "events", "-html", "-o", tmp.toString()
        });
    String content = Files.readString(tmp);
    assertFalse(content.contains("<tr class=\"todo\">"));
    assertFalse(content.contains("<span class=\"badge badge-todo\">"));
    assertTrue(content.contains("<tr class=\"event\">"));
    assertTrue(content.contains("<span class=\"badge badge-event\">"));
  }

  @Test
  @DisplayName("Client main override format back to TXT after -ics")
  void clientMainOverrideFormat() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream original = System.out;
    try {
      System.setOut(new PrintStream(out));
      Client.main(new String[] {"src/test/resources/events_minimal.ics", "events", "-ics", "-txt"});
    } finally {
      System.setOut(original);
    }
    String content = out.toString();
    // Doit être au format TXT (crochets + Event{) et ne doit pas contenir
    // BEGIN:VCALENDAR
    assertTrue(content.startsWith("["));
    assertTrue(content.contains("Event{"));
    assertFalse(content.contains("BEGIN:VCALENDAR"));
  }

  @Test
  @DisplayName("Client main todos default txt console")
  void clientMainTodosDefaultTxt() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream original = System.out;
    try {
      System.setOut(new PrintStream(out));
      Client.main(new String[] {"src/test/resources/todos_mixed.ics", "todos"});
    } finally {
      System.setOut(original);
    }
    String content = out.toString();
    assertTrue(content.contains("Todo{"));
    assertTrue(content.startsWith("["));
  }
}
