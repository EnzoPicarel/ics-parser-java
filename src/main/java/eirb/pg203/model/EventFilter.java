package eirb.pg203.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventFilter {

  public abstract List<Event> filter(List<Event> events);

  public static class TodayFilter extends EventFilter {
    @Override
    public List<Event> filter(List<Event> events) {
      LocalDate today = LocalDate.now();
      return events.stream()
          .filter(e -> e.start_date != null && isSameDay(e.start_date, today))
          .collect(Collectors.toList());
    }
  }

  public static class TomorrowFilter extends EventFilter {
    @Override
    public List<Event> filter(List<Event> events) {
      LocalDate tomorrow = LocalDate.now().plusDays(1);
      return events.stream()
          .filter(e -> e.start_date != null && isSameDay(e.start_date, tomorrow))
          .collect(Collectors.toList());
    }
  }

  public static class WeekFilter extends EventFilter {
    @Override
    public List<Event> filter(List<Event> events) {
      LocalDate now = LocalDate.now();
      LocalDate weekEnd = now.plusDays(7);
      return events.stream()
          .filter(
              e ->
                  e.start_date != null
                      && !toLocalDate(e.start_date).isBefore(now)
                      && toLocalDate(e.start_date).isBefore(weekEnd))
          .collect(Collectors.toList());
    }
  }

  public static class DateRangeFilter extends EventFilter {
    private final LocalDate from;
    private final LocalDate to;

    public DateRangeFilter(LocalDate from, LocalDate to) {
      this.from = from;
      this.to = to;
    }

    @Override
    public List<Event> filter(List<Event> events) {
      return events.stream()
          .filter(
              e -> {
                if (e.start_date == null) return false;
                LocalDate eventDate = toLocalDate(e.start_date);
                return !eventDate.isBefore(from) && !eventDate.isAfter(to);
              })
          .collect(Collectors.toList());
    }
  }

  public static class AllEventsFilter extends EventFilter {
    @Override
    public List<Event> filter(List<Event> events) {
      return events;
    }
  }

  private static boolean isSameDay(Instant instant, LocalDate date) {
    return toLocalDate(instant).equals(date);
  }

  private static LocalDate toLocalDate(Instant instant) {
    return instant.atZone(ZoneId.systemDefault()).toLocalDate();
  }
}
