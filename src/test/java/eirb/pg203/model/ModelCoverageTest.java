package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

// sert à tester la couverture des classes Event, Todo et Calendar
// jacoco considère qu'une classe n'est pas couverte si on n'a pas appelé son constructeur ou
// si on n'a pas accédé à au moins un de ses champs
class ModelCoverageTest {

  @Test
  void testEventConstructorAndFields() {
    String uid = "event-1";
    String summary = "Event Summary";
    String location = "Event Location";
    Instant now = Instant.now();
    String desc = "Description";
    String att = "Attendance";

    Event event = new Event(uid, summary, location, now, now, now, desc, att);

    assertEquals(uid, event.uid);
    assertEquals(summary, event.summary);
    assertEquals(now, event.creation_date);
    assertEquals(desc, event.description);
    assertEquals(att, event.attendance);
  }

  @Test
  void testTodoConstructorAndFields() {
    String uid = "todo-1";
    String summary = "Faire le ménage";
    String location = "Maison";
    String priority = "1";
    String progress = "50";
    Instant now = Instant.now();
    String status = "IN-PROCESS";
    String attendance = "PUBLIC";
    String sequence = "0";
    String organizer = "mailto:alice@example.com";

    Todo todo =
        new Todo(
            uid,
            summary,
            location,
            priority,
            progress,
            now, // completed_date
            now, // due_date
            now, // modification_date
            now, // creation_date (DTSTAMP - héritée)
            now, // date_start (spécifique Todo ici)
            status,
            attendance,
            sequence,
            organizer);

    assertEquals(uid, todo.uid);
    assertEquals(summary, todo.summary);
    assertEquals(location, todo.location);

    assertEquals(priority, todo.priority);
    assertEquals(progress, todo.progress);
    assertEquals(now, todo.completed_date);
    assertEquals(now, todo.due_date);
    assertEquals(now, todo.modification_date);
    assertEquals(now, todo.creation_date);
    assertEquals(now, todo.date_start);
    assertEquals(status, todo.status);
    assertEquals(attendance, todo.attendance);
    assertEquals(sequence, todo.sequence);
    assertEquals(organizer, todo.organizer);
  }

  @Test
  void testCalendarOperations() {
    Calendar cal = new Calendar();
    assertTrue(cal.getAllComponents().isEmpty());

    Instant now = Instant.now();

    Event e = new Event("1", "S", "L", now, now, now, "D", "A");

    Todo t =
        new Todo("2", "Sum", "Loc", "1", "0", now, now, now, now, now, "Stat", "Att", "0", "Org");

    cal.addComponent(e);
    cal.addComponent(t);

    assertEquals(2, cal.getAllComponents().size());
    assertEquals(1, cal.getEvents().size());
    assertEquals(1, cal.getTodos().size());
  }
}
