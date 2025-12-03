package eirb.pg203;

import eirb.pg203.model.*;
import java.util.List;

public class CalendarApplication {
  private final ArgumentParser argumentParser;

  public CalendarApplication() {
    this.argumentParser = new ArgumentParser();
  }

  public int run(String[] args) {
    CliConfig config = argumentParser.parse(args);
    if (config == null) {
      return 1;
    }

    try {
      Calendar calendar = AbstractParser.chooseParser(config.inputFile, config.componentType);

      Calendar filteredCalendar = applyFilters(calendar, config);

      config.outputGenerator.displayCalendar(filteredCalendar, config.outputFile);
      return 0;
    } catch (Exception e) {
      System.err.println("Error processing calendar: " + e.getMessage());
      return 1;
    }
  }

  private Calendar applyFilters(Calendar calendar, CliConfig config) {
    Calendar filtered = new Calendar();

    if (config.componentType.equals("events")) {
      List<Event> events = calendar.getEvents();
      List<Event> filteredEvents = config.eventFilter.filter(events);
      filteredEvents.forEach(filtered::addComponent);
    } else if (config.componentType.equals("todos")) {
      List<Todo> todos = calendar.getTodos();
      List<Todo> filteredTodos = config.todoFilter.filter(todos);
      filteredTodos.forEach(filtered::addComponent);
    }

    return filtered;
  }
}
