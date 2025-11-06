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


public class Client {

    public static Instant parseIcsDate(String s) {
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
    public static void main(String[] args) {
        String filePath = args[0];
        try (FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            HashMap<String,String> test = null;
            ArrayList<HashMap<String,String>> rep = new ArrayList<>();
            ArrayList<Event> List_Event= new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("BEGIN:VEVENT")) {
                    test = new HashMap<>();
                }
                if (test != null) {
                    if (line.contains("LOCATION")) {
                        String line3 = line;
                        while (((line = bufferedReader.readLine()) != null) && (!line.contains("DESCRIPTION"))) {
                            if (line != null) {
                                line3 = line3.concat(line);
                            }
                        }
                        String[] parts = line3.split(":");
                        if (parts.length == 1) {
                            test.put(line3.split(":")[0],"");
                        } else {
                            test.put(line3.split(":")[0],line3.split(":")[1]);
                        }
                        
                        //System.out.println(line3);
                        String line2 = line;
                        while (((line = bufferedReader.readLine()) != null) && (!line2.contains("UID"))) {
                            if (line != null) {
                                line2 = line2.concat(line);
                            }
                        }
                        String []splited_line = line2.split("UID");
                        String descr = splited_line[0];
                        String uid = splited_line[1];
                        String []splited_descr = descr.split(":",2);
                        test.put(splited_descr[0],splited_descr[1]);
                        test.put("UID",uid.split(":")[1]);
                    } else {
                        test.put(line.split(":", 2)[0], line.split(":", 2)[1]);
                    }
                }
                if (line.contains("END:VEVENT") && test != null) {
                    rep.add(new HashMap<String,String>(test));
                    
                    Instant creationDate = parseIcsDate(test.get("DTSTAMP"));
                    Instant startDate = parseIcsDate(test.get("DTSTART"));
                    Instant endDate = parseIcsDate(test.get("DTEND"));
                    
                    Event event = new Event(
                        creationDate,
                        startDate,
                        endDate,
                        test.get("SUMMARY"),
                        test.get("LOCATION"),
                        test.get("DESCRIPTION"),
                        null  // attendance à définir selon vos besoins
                    );
                    List_Event.add(event);
                              
                    test = null;
                        }
                    }
            System.out.println(rep);
            //System.out.println(List_Event);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
