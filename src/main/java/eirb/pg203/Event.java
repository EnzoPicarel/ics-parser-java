package eirb.pg203;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Event {

  Instant creation_date;
  Instant start_date;
  Instant end_date;
  String uid;
  String summary;
  String location;
  String description;
  Attendance attendance;

  Event(
      Instant creation_date,
      Instant start_date,
      Instant end_date,
      String summary,
      String uid,
      String location,
      String description,
      Attendance attendance) {
    this.creation_date = creation_date;
    this.start_date = start_date;
    this.end_date = end_date;
    this.uid = uid;
    this.summary = summary;
    this.location = location;
    this.description = description;
    this.attendance = attendance;
  }

  public String toString() {
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
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
            }""",
        formatter.format(creation_date),
        formatter.format(start_date),
        formatter.format(end_date),
        uid,
        summary,
        location,
        description,
        attendance);
  }
}
