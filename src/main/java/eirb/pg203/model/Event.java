package eirb.pg203.model;

import java.time.Instant;

public class Event extends CalendarComponent {

  Instant start_date;
  Instant end_date;
  String description;
  String attendance;

  Event(
      String uid,
      String summary,
      String location,
      Instant creation_date,
      Instant start_date,
      Instant end_date,
      String description,
      String attendance) {
    super(uid, summary, location, creation_date);
    this.start_date = start_date;
    this.end_date = end_date;
    this.description = description;
    this.attendance = attendance;
  }

  public String printWith(Output O) {
    return O.displayEvent(this);
  }
}
