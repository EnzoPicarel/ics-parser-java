package eirb.pg203;

import java.time.Instant;

public abstract class CalendarComponent {

  // Propriétés communes
  protected String uid;

  protected String summary;

  protected String location;

  protected Instant creation_date;

  public CalendarComponent(String uid, String summary, String location, Instant creation_date) {
    this.uid = uid;
    this.summary = summary;
    this.location = location;
    this.creation_date = creation_date;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Instant getCreationDate() {
    return creation_date;
  }

  public void setCreationDate(Instant creationDate) {
    this.creation_date = creation_date;
  }
}
