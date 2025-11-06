package eirb.pg203;
import java.time.Instant;

class Event {
    
    Instant creation_date;
    Instant start_date;
    Instant end_date;
    String summary;
    String location;
    String description;
    Attendance attendance;


    Event(Instant creation_date, Instant start_date, Instant end_date, String summary, String location, String description, Attendance attendance) {
        this.creation_date = creation_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.summary = summary;
        this.location = location;
        this.description = description;
        this.attendance = attendance;
    }
}