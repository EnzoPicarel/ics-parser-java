package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// sert à vérifier que la classe ArgumentsCommande fonctionne correctement
// i.e transforme correctement les arguments en un objet ArgumentsCommande
// et gère les erreurs de manière appropriée
// on teste aussi les options de sortie et de format
class ArgumentsCommandeTest {

  @Test
  @DisplayName("Test nominal : Fichier et Type Events")
  void testBasicParsingEvents() {
    String[] args = {"calendar.ics", "events"};
    ArgumentsCommande cmd = new ArgumentsCommande(args);

    assertEquals("calendar.ics", cmd.getInputFile());
    assertEquals(ArgumentsCommande.ExtractionType.events, cmd.getType());

    assertEquals("", cmd.getOutputFile());
    assertTrue(cmd.getOutputGenerator() instanceof OutputTxt);
  }

  @Test
  @DisplayName("Test nominal : Type Todos (insensible à la casse)")
  void testBasicParsingTodosCaseInsensitive() {
    String[] args = {"calendar.ics", "TODOS"};
    ArgumentsCommande cmd = new ArgumentsCommande(args);

    assertEquals("calendar.ics", cmd.getInputFile());
    assertEquals(ArgumentsCommande.ExtractionType.todos, cmd.getType());
  }

  @Test
  @DisplayName("Test option format HTML")
  void testHtmlOption() {
    String[] args = {"file.ics", "events", "-html"};
    ArgumentsCommande cmd = new ArgumentsCommande(args);

    assertTrue(
        cmd.getOutputGenerator() instanceof OutputHtml, "Le générateur devrait être OutputHtml");
  }

  @Test
  @DisplayName("Test option format ICS")
  void testIcsOption() {
    String[] args = {"file.ics", "events", "-ics"};
    ArgumentsCommande cmd = new ArgumentsCommande(args);

    assertTrue(
        cmd.getOutputGenerator() instanceof OutputIcs, "Le générateur devrait être OutputIcs");
  }

  @Test
  @DisplayName("Test option sortie fichier (-o)")
  void testOutputFileOption() {
    String[] args = {"file.ics", "events", "-o", "sortie.txt"};
    ArgumentsCommande cmd = new ArgumentsCommande(args);

    assertEquals("sortie.txt", cmd.getOutputFile());
  }

  @Test
  @DisplayName("Test combinaison format et sortie fichier")
  void testCombinedOptions() {
    // l'ordre ne doit pas compter
    String[] args = {"file.ics", "todos", "-o", "result.html", "-html"};
    ArgumentsCommande cmd = new ArgumentsCommande(args);

    assertEquals("result.html", cmd.getOutputFile());
    assertTrue(cmd.getOutputGenerator() instanceof OutputHtml);
  }

  @Test
  @DisplayName("Erreur : Pas assez d'arguments")
  void testNotEnoughArguments() {
    String[] args = {"file.ics"};
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new ArgumentsCommande(args);
            });
    assertTrue(exception.getMessage().contains("Usage:"));
  }

  @Test
  @DisplayName("Erreur : Type inconnu")
  void testInvalidType() {
    String[] args = {"file.ics", "machin"};
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new ArgumentsCommande(args);
            });
    assertEquals("Second argument must be 'events' or 'todos'", exception.getMessage());
  }

  @Test
  @DisplayName("Erreur : Option -o sans nom de fichier")
  void testMissingOutputFile() {
    String[] args = {"file.ics", "events", "-o"};
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new ArgumentsCommande(args);
            });
    assertEquals("Error: -o requires a filename", exception.getMessage());
  }
}
