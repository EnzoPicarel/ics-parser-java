package eirb.pg203.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParserFile extends AbstractParser {

    @Override
    public Calendar parse(String filePath, String type) {
        try (FileReader fileReader = new FileReader(filePath)) {
            return parseStream(fileReader,type);
        } catch (FileNotFoundException e) {
            System.err.println("Erreur : Le fichier " + filePath + " est introuvable.");
            return new Calendar(); //retourne un calendrier vide pour éviter le crash
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
            return new Calendar();
        }
    }
}