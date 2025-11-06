package eirb.pg203;
import java.time.Instant;

public class Event {
    
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
    public String toString() {
        return String.format("""
            Event{
                Creation Date: %s
                Start Date: %s
                End Date: %s
                Summary: %s
                Location: %s
                Description: %s
                Attendance: %s
            }""",
            creation_date,
            start_date,
            end_date,
            summary,
            location,
            description,
            attendance);
    }
}