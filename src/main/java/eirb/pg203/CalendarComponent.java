package eirb.pg203;

import java.time.Instant;

public abstract class CalendarComponent {

  // Propriétés communes
  String uid;

  String summary;

  String location;

  Instant creation_date;

  public CalendarComponent(String uid, String summary, String location, Instant creation_date) {
    this.uid = uid;
    this.summary = summary;
    this.location = location;
    this.creation_date = creation_date;
  }
}
