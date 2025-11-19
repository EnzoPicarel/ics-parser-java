package eirb.pg203.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

  public String toString() {
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
    return String.format(
        """
            Todo{
                Creation Date: %s
                Uid: %s
                Summary: %s
                Location: %s
                Priority: %s
                Progress: %s
                Completed Date: %s
                Due Date: %s
                Modification Date: %s
                Completed: %s
                Attendance: %s
                Sequence: %s
            }""",
        formatter.format(creation_date),
        uid,
        summary,
        location != null ? location : "UNDEFINED",
        priority,
        progress,
        completed_date != null ? formatter.format(completed_date) : "UNDEFINED",
        due_date != null ? formatter.format(due_date) : "UNDEFINED",
        modification_date != null ? formatter.format(modification_date) : "UNDEFINED",
        completed,
        attendance,
        sequence);
  }
}
