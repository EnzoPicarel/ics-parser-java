package eirb.pg203.model;

import java.time.Instant;

public abstract class CalendarComponent {

  // propriétés communes
  String uid;

  String summary;

  String location;

  Instant creation_date;

  CalendarComponent(String uid, String summary, String location, Instant creation_date) {
    this.uid = uid;
    this.summary = summary;
    this.location = location;
    this.creation_date = creation_date;
  }

  public abstract String printWith(Output O);
}
