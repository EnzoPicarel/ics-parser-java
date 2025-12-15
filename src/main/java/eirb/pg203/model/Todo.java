package eirb.pg203.model;

import java.time.Instant;

public class Todo extends CalendarComponent {
  String priority;
  String progress;
  Instant completed_date;
  Instant due_date;
  Instant modification_date;
  Instant start_date;
  String status;
  String attendance;
  String sequence;
  String organizer;

  Todo(
      String uid,
      String summary,
      String location,
      String priority,
      String progress,
      Instant completed_date,
      Instant due_date,
      Instant modification_date,
      Instant creation_date,
      Instant start_date,
      String status,
      String attendance,
      String sequence,
      String organizer) {
    super(uid, summary, location, creation_date);
    this.priority = priority;
    this.progress = progress;
    this.completed_date = completed_date;
    this.due_date = due_date;
    this.modification_date = modification_date;
    this.status = status;
    this.attendance = attendance;
    this.sequence = sequence;
    this.organizer = organizer;
    this.start_date = start_date;
  }

  public String printWith(Output O) {
    return O.displayTodo(this);
  }
}
