package eirb.pg203.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ParserURL extends AbstractParser {

  @Override
  public Calendar parse(String urlString, String type) {
    try {
      URL url = new URL(urlString);
      URLConnection connection = url.openConnection(); // connection à l'url
      // on utilise InputStreamReader pour convertir les bytes du réseau en caractères
      try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
        return parseStream(reader, type);
      }
    } catch (IOException e) {
      System.err.println("Erreur de téléchargement depuis l'URL : " + e.getMessage());
      return new Calendar();
    }
  }
}
