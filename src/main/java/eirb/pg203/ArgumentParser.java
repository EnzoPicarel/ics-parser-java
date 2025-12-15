package eirb.pg203;

import eirb.pg203.model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ArgumentParser {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  public CliConfig parse(String[] args) {
    if (args.length < 2) {
      printUsage();
      return null;
    }

    String inputFile = args[0];
    String componentType = args[1];

    if (!isValidComponentType(componentType)) {
      System.err.println("Second argument must be 'events' or 'todos'");
      return null;
    }

    Output outputGenerator = new OutputTxt();
    String outputFile = "";
    EventFilter eventFilter = new EventFilter.TodayFilter();
    TodoFilter todoFilter = new TodoFilter.IncompleteFilter();

    LocalDate fromDate = null;
    LocalDate toDate = null;
    boolean hasTimeFilter = false;
    boolean hasStatusFilter = false;

    for (int i = 2; i < args.length; i++) {
      switch (args[i]) {
        case "-ics":
          outputGenerator = new OutputIcs();
          break;
        case "-txt":
          outputGenerator = new OutputTxt();
          break;
        case "-html":
          outputGenerator = new OutputHtml();
          break;
        case "-o":
          if (i + 1 < args.length) {
            outputFile = args[i + 1];
            i++;
          } else {
            System.err.println("Error: -o requires a filename");
            return null;
          }
          break;

        case "-today":
          if (!componentType.equals("events")) {
            System.err.println("Error: -today is only valid with 'events'");
            return null;
          }
          if (hasTimeFilter) {
            System.err.println("Error: time filters are not cumulative");
            return null;
          }
          eventFilter = new EventFilter.TodayFilter();
          hasTimeFilter = true;
          break;

        case "-tomorrow":
          if (!componentType.equals("events")) {
            System.err.println("Error: -tomorrow is only valid with 'events'");
            return null;
          }
          if (hasTimeFilter) {
            System.err.println("Error: time filters are not cumulative");
            return null;
          }
          eventFilter = new EventFilter.TomorrowFilter();
          hasTimeFilter = true;
          break;

        case "-week":
          if (!componentType.equals("events")) {
            System.err.println("Error: -week is only valid with 'events'");
            return null;
          }
          if (hasTimeFilter) {
            System.err.println("Error: tTomorrowFilterime filters are not cumulative");
            return null;
          }
          eventFilter = new EventFilter.WeekFilter();
          hasTimeFilter = true;
          break;

        case "-from":
          if (!componentType.equals("events")) {
            System.err.println("Error: -from is only valid with 'events'");
            return null;
          }
          if (hasTimeFilter && fromDate == null) {
            System.err.println("Error: -from/-to cannot be combined with other time filters");
            return null;
          }
          if (i + 1 < args.length) {
            try {
              fromDate = LocalDate.parse(args[i + 1], DATE_FORMATTER);
              i++;
              hasTimeFilter = true;
            } catch (Exception e) {
              System.err.println("Error: invalid date format for -from (expected YYYYMMDD)");
              return null;
            }
          } else {
            System.err.println("Error: -from requires a date");
            return null;
          }
          break;

        case "-to":
          if (!componentType.equals("events")) {
            System.err.println("Error: -to is only valid with 'events'");
            return null;
          }
          if (hasTimeFilter && toDate == null && fromDate == null) {
            System.err.println("Error: -from/-to cannot be combined with other time filters");
            return null;
          }
          if (i + 1 < args.length) {
            try {
              toDate = LocalDate.parse(args[i + 1], DATE_FORMATTER);
              i++;
              hasTimeFilter = true;
            } catch (Exception e) {
              System.err.println("Error: invalid date format for -to (expected YYYYMMDD)");
              return null;
            }
          } else {
            System.err.println("Error: -to requires a date");
            return null;
          }
          break;

        case "-incomplete":
          if (!componentType.equals("todos")) {
            System.err.println("Error: -incomplete is only valid with 'todos'");
            return null;
          }
          if (hasStatusFilter) {
            System.err.println("Error: status filters are not cumulative");
            return null;
          }
          todoFilter = new TodoFilter.IncompleteFilter();
          hasStatusFilter = true;
          break;

        case "-all":
          if (!componentType.equals("todos")) {
            System.err.println("Error: -all is only valid with 'todos'");
            return null;
          }
          if (hasStatusFilter) {
            System.err.println("Error: status filters are not cumulative");
            return null;
          }
          todoFilter = new TodoFilter.AllTodosFilter();
          hasStatusFilter = true;
          break;

        case "-completed":
          if (!componentType.equals("todos")) {
            System.err.println("Error: -completed is only valid with 'todos'");
            return null;
          }
          if (hasStatusFilter) {
            System.err.println("Error: status filters are not cumulative");
            return null;
          }
          todoFilter = new TodoFilter.CompletedFilter();
          hasStatusFilter = true;
          break;

        case "-inprocess":
          if (!componentType.equals("todos")) {
            System.err.println("Error: -inprocess is only valid with 'todos'");
            return null;
          }
          if (hasStatusFilter) {
            System.err.println("Error: status filters are not cumulative");
            return null;
          }
          todoFilter = new TodoFilter.InProcessFilter();
          hasStatusFilter = true;
          break;

        case "-needsaction":
          if (!componentType.equals("todos")) {
            System.err.println("Error: -needsaction is only valid with 'todos'");
            return null;
          }
          if (hasStatusFilter) {
            System.err.println("Error: status filters are not cumulative");
            return null;
          }
          todoFilter = new TodoFilter.NeedsActionFilter();
          hasStatusFilter = true;
          break;

        default:
          System.err.println("Unknown option: " + args[i]);
          return null;
      }
    }

    if (fromDate != null || toDate != null) {
      if (fromDate == null) {
        System.err.println("Error: -to requires -from");
        return null;
      }
      if (toDate == null) {
        System.err.println("Error: -from requires -to");
        return null;
      }
      if (fromDate.isAfter(toDate)) {
        System.err.println("Error: -from date must be before -to date");
        return null;
      }
      eventFilter = new EventFilter.DateRangeFilter(fromDate, toDate);
    }

    return new CliConfig(
        inputFile, componentType, outputGenerator, outputFile, eventFilter, todoFilter);
  }

  private boolean isValidComponentType(String type) {
    return "events".equals(type) || "todos".equals(type);
  }

  private void printUsage() {
    System.err.println("Usage: clical file.ics events|todos [options]");
    System.err.println("Options for events:");
    System.err.println("  -today          Show today's events (default)");
    System.err.println("  -tomorrow       Show tomorrow's events");
    System.err.println("  -week           Show this week's events");
    System.err.println("  -from DATE      Show events from date (use with -to)");
    System.err.println("  -to DATE        Show events until date (use with -from)");
    System.err.println("Options for todos:");
    System.err.println("  -incomplete     Show incomplete todos (default)");
    System.err.println("  -all            Show all todos");
    System.err.println("  -completed      Show completed todos");
    System.err.println("  -inprocess      Show in-process todos");
    System.err.println("  -needsaction    Show todos needing action");
    System.err.println("Output options:");
    System.err.println("  -txt            Text output (default)");
    System.err.println("  -ics            ICS format output");
    System.err.println("  -html           HTML format output");
    System.err.println("  -o FILE         Output to file (default: stdout)");
  }
}
