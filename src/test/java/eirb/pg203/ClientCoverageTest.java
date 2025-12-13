package eirb.pg203;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

//sert à vérifier que la chaîne fonctionne : Arguments -> Parser -> Filtres -> Output
class ClientCoverageTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("Test Application avec arguments valides (Retourne 0)")
    void testAppValidExecution() {
        String validIcsPath = "src/test/resources/i2.ics";
        String[] args = {validIcsPath, "events", "-html"};

        CalendarApplication app = new CalendarApplication();

        int exitCode = app.run(args);
        if (exitCode != 0) {
            System.out.println("Erreur Standard : " + errContent.toString());
        }
        assertEquals(0, exitCode, "L'application devrait retourner 0 en cas de succès");
    }

    @Test
    @DisplayName("Test Application avec fichier invalide (Géré proprement)")
    void testAppInvalidFile() {
        String[] args = {"fichier_inexistant_bidon.ics", "events"};

        CalendarApplication app = new CalendarApplication();
        int exitCode = app.run(args);

        assertEquals(0, exitCode, "Le parser gère l'erreur silencieusement, donc code 0 attendu");
        assertFalse(errContent.toString().isEmpty(), "Un message d'erreur aurait dû être affiché sur stderr");
    }

    @Test
    @DisplayName("Test Application mode HTML (Retourne 0)")
    void testAppHtmlOutput() {
        String validIcsPath = "src/test/resources/i2.ics";
        String[] args = {validIcsPath, "events", "-html"};

        CalendarApplication app = new CalendarApplication();
        int exitCode = app.run(args);

        assertEquals(0, exitCode);
    }
}