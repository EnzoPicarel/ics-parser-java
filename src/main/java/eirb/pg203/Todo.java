package eirb.pg203;

import java.time.Instant;

public class Todo extends CalendarComponent {
  Integer priority;
  Integer progress;
  Instant completed_date;
  Instant due_date;
  Instant modification_date;
  Boolean completed;
  Attendance attendance;
  Integer sequence;

  Todo(
      String uid,
      String summary,
      String location,
      Integer priority,
      Integer progress,
      Instant completed_date,
      Instant due_date,
      Instant modification_date,
      Instant creation_date,
      Boolean completed,
      Attendance attendance,
      Integer sequence) {
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
