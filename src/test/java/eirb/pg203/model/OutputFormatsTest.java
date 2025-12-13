package eirb.pg203.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OutputFormatsTest {

    private Event sampleEvent;
    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        Instant now = Instant.parse("2025-11-04T10:00:00Z");
        
        sampleEvent = new Event(
            "evt-uid-1", "Cours Java", "Amphi A",
            now, now, now.plusSeconds(3600),
            "Description", "OBLIGATOIRE"
        );

        sampleTodo = new Todo(
            "todo-uid-2", "Projet POO", "Maison",
            "1", "50", null, now.plusSeconds(86400),
            now, now, now, "IN-PROCESS", "PUBLIC", "0",
            "CN=\"Prof\":mailto:prof@test.fr"
        );
    }

    @Test
    @DisplayName("OutputTxt : Vérification du format texte brut")
    void testOutputTxt() {
        Output strategy = new OutputTxt();
        
        assertEquals("[", strategy.header());
        assertEquals("]", strategy.footer());

        //test Event
        String outEvent = strategy.displayEvent(sampleEvent);
        assertTrue(outEvent.contains("Event{"));
        assertTrue(outEvent.contains("Summary: Cours Java"));
        assertTrue(outEvent.contains("Location: Amphi A"));
        assertTrue(outEvent.contains("Uid: evt-uid-1"));

        //test Todo
        String outTodo = strategy.displayTodo(sampleTodo);
        assertTrue(outTodo.contains("Todo{"));
        assertTrue(outTodo.contains("Summary: Projet POO"));
        assertTrue(outTodo.contains("Completed: IN-PROCESS"), "Le statut doit être affiché");
    }

    @Test
    @DisplayName("OutputHtml : Vérification structurelle (Contenu + Balises de base)")
    void testOutputHtml() {
        Output strategy = new OutputHtml();

        //header : On vérifie juste qu'on annonce du HTML
        String header = strategy.header();
        assertTrue(header.contains("<!DOCTYPE html>"));
        
        //event : On vérifie le contenu ET la structure de tableau
        String outEvent = strategy.displayEvent(sampleEvent);
        assertTrue(outEvent.contains("Cours Java"), "Le résumé doit être présent");
        assertTrue(outEvent.contains("Amphi A"), "Le lieu doit être présent");
        assertTrue(outEvent.contains("<tr"), "Doit contenir une ligne de tableau");
        assertTrue(outEvent.contains("<td"), "Doit contenir une cellule de tableau");

        //todo
        String outTodo = strategy.displayTodo(sampleTodo);
        assertTrue(outTodo.contains("Projet POO"));
        assertTrue(outTodo.contains("50%"));
        assertTrue(outTodo.contains("<tr"), "Doit être une ligne de tableau");
    }

    @Test
    @DisplayName("OutputIcs : Vérification de la syntaxe RFC 5545")
    void testOutputIcs() {
        Output strategy = new OutputIcs();
        
        String header = strategy.header();
        assertTrue(header.contains("BEGIN:VCALENDAR"));
        assertTrue(header.contains("VERSION:2.0"));

        //event
        String outEvent = strategy.displayEvent(sampleEvent);
        assertTrue(outEvent.contains("BEGIN:VEVENT"));
        assertTrue(outEvent.contains("SUMMARY:Cours Java"));
        assertTrue(outEvent.contains("UID:evt-uid-1"));
        assertTrue(outEvent.contains("DTSTART:20251104T100000Z"));
        assertTrue(outEvent.contains("END:VEVENT"));

        //todo
        String outTodo = strategy.displayTodo(sampleTodo);
        assertTrue(outTodo.contains("BEGIN:VTODO"));
        assertTrue(outTodo.contains("SUMMARY:Projet POO"));
        assertTrue(outTodo.contains("STATUS:IN-PROCESS"));
        //cas particulier ORGANIZER avec point-virgule
        assertTrue(outTodo.contains("ORGANIZER;CN=\"Prof\":mailto:prof@test.fr"));
        assertTrue(outTodo.contains("END:VTODO"));
    }
}