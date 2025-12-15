package eirb.pg203.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ParserFile extends AbstractParser {

  @Override
  public Calendar parse(String filePath, String type) {
    try (FileReader fileReader = new FileReader(filePath)) {
      // lit le contenu du fichier avec un FileReader (lecture caractère par
      // caractère)
      return parseStream(fileReader, type); // délègue le parsing du flux à la méthode générique
    } catch (FileNotFoundException e) {
      // si le fichier n'existe pas, affiche un message et retourne un calendrier vide
      System.err.println("Erreur : Le fichier " + filePath + " est introuvable.");
      return new Calendar();
    } catch (IOException e) {
      // en cas d'erreur de lecture, affiche un message et retourne un calendrier vide
      System.err.println("Erreur de lecture du fichier : " + e.getMessage());
      return new Calendar();
    }
  }
}
