package eirb.pg203;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
    private static Instant parseIcsDate(String s) {
        if (s == null) return null;

        // This formatter NOW knows to apply the UTC zone when it parses
        DateTimeFormatter fZulu = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
                                                .withZone(ZoneId.of("UTC"));
        
        DateTimeFormatter fLocal = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

        if (s.endsWith("Z")) {
            // This will now work
            return Instant.from(fZulu.parse(s));
        }
        
        // This part was already correct for local times
        return LocalDateTime.parse(s, fLocal)
                .atZone(ZoneId.systemDefault())
                .toInstant();
    }
    public static ArrayList<Event> parse(String filePath) {
        try (FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            HashMap<String,String> line_array = null;
            ArrayList<Event> List_Event= new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("BEGIN:VEVENT")) {
                    line_array = new HashMap<>();
                }
                if (line_array != null) {
                    if (line.contains("LOCATION")) { //LOCATION can be on two line 
                        String line_location = line;
                        while (((line = bufferedReader.readLine()) != null) && (!line.contains("DESCRIPTION"))) {
                            if (line != null) {
                                if (line.startsWith(" ")) {
                                    line = line.substring(1,line.length());
                                }
                                line_location = line_location.concat(line);
                            }
                        }
                        String[] parts = line_location.split(":"); //[0] = LOCATION and [1] data
                        if (parts.length == 1) {
                            line_array.put(parts[0],"UNDEFINED");
                        } else {
                            line_array.put(parts[0],parts[1]);
                        }
                        
                        String line_description = line;
                        while (((line = bufferedReader.readLine()) != null) && (!line.contains("UID"))) {
                            if (line != null) {
                                if (line.startsWith(" ")) {
                                    line = line.substring(1,line.length());
                                }
                                line_description = line_description.concat(line);
                            }
                        }
                        String []splited_descr = line_description.split(":",2);
                        line_array.put(splited_descr[0],splited_descr[1]);

                        String []splited_uid = line.split(":"); // splited_uid[0] = "UID" and splited_uid[1] = data
                        line_array.put(splited_uid[0],splited_uid[1]);
                    } else { //add other field that are on 1 line only
                        line_array.put(line.split(":")[0], line.split(":")[1]);
                    }
                }
                if (line.contains("END:VEVENT") && line_array != null) {                  
                    Instant creationDate = parseIcsDate(line_array.get("DTSTAMP"));
                    Instant startDate = parseIcsDate(line_array.get("DTSTART"));
                    Instant endDate = parseIcsDate(line_array.get("DTEND"));
                    
                    Event event = new Event(
                        creationDate,
                        startDate,
                        endDate,
                        line_array.get("SUMMARY"),
                        line_array.get("UID"),
                        line_array.get("LOCATION"),
                        line_array.get("DESCRIPTION"),
                        null  // attendance à définir selon vos besoins
                    );
                    List_Event.add(event);
                              
                    line_array = null;
                        }
                    }
            return List_Event;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
