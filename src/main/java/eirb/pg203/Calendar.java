package eirb.pg203;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Calendar {

    //liste qui contient des event et des todos
    private List<CalendarComponent> components = new ArrayList<>();

    //méthode pour ajouter un composant (Event ou Todo)
    public void addComponent(CalendarComponent component) {
        if (component != null) {
            this.components.add(component);
        }
    }

    //méthode pour obtenir tous les composants
    public List<CalendarComponent> getAllComponents() {
        return components;
    }

    //méthode pour obtenir uniquement les events
    public List<Event> getEvents() {
        return components.stream()
                .filter(component -> component instanceof Event) //on garde que les composants qui sont des Events
                .map(component -> (Event) component) //on le convertit en Event
                .collect(Collectors.toList()); //on collecte le tout dans une liste
    }
}