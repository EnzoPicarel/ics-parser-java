package eirb.pg203.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class OutputTxt extends Output {

  private final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  @Override
  public String header() {
    return "[";
  }

  @Override
  public String footer() {
    return "]";
  }

  @Override
  public String displayEvent(Event E) {
    return String.format(
        """
            Event{
                Creation Date: %s
                Start Date: %s
                End Date: %s
                Uid: %s
                Summary: %s
                Location: %s
                Description: %s
                Attendance: %s
            }
            """,
        formatDate(E.creation_date),
        formatDate(E.start_date),
        formatDate(E.end_date),
        E.uid,
        E.summary,
        E.location,
        E.description,
        E.attendance);
  }

  @Override
  public String displayTodo(Todo T) {
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
                Organizer: %s
            }
            """,
        formatDate(T.creation_date),
        T.uid,
        T.summary,
        (T.location != null ? T.location : ""),
        T.priority,
        T.progress,
        formatDate(T.completed_date),
        formatDate(T.due_date),
        formatDate(T.modification_date),
        T.status,
        T.attendance,
        T.sequence,
        T.organizer);
  }

  private String formatDate(Instant date) {
    return date != null ? formatter.format(date) : "UNDEFINED";
  }
}
