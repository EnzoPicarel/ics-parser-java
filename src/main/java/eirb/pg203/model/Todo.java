package eirb.pg203.model;

import java.time.Instant;

public class Todo extends CalendarComponent {
  String priority;
  String progress;
  Instant completed_date;
  Instant due_date;
  Instant modification_date;
  String completed;
  String attendance;
  String sequence;

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
      String completed,
      String attendance,
      String sequence) {
    super(uid, summary, location, creation_date);
    this.priority = priority;
    this.progress = progress;
    this.completed_date = completed_date;
    this.due_date = due_date;
    this.modification_date = modification_date;
    this.completed = completed;
    this.attendance = attendance;
    this.sequence = sequence;
  }
}
