package eirb.pg203.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Calendar {

  private List<CalendarComponent> components;

  public Calendar() {
    this.components = new ArrayList<>();
  }

  public void addComponent(CalendarComponent component) {
    if (component != null) {
      this.components.add(component);
    }
  }

  public List<CalendarComponent> getAllComponents() {
    return components;
  }

  public List<Event> getEvents() {
    return components.stream()
        .filter(component -> component instanceof Event)
        .map(component -> (Event) component)
        .collect(Collectors.toList());
  }

  public List<Todo> getTodos() {
    return components.stream()
        .filter(component -> component instanceof Todo)
        .map(component -> (Todo) component)
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return "Calendar{" +
        "nombre d'éléments=" + components.size() +
        '}';
  }
}