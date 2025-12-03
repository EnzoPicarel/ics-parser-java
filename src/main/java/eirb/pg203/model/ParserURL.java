package eirb.pg203.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class ParserURL extends AbstractParser {

  @Override
  public Calendar parse(String urlString, String type) {
    try {
      URL url = URI.create(urlString).toURL();
      URLConnection connection = url.openConnection();
      try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
        return parseStream(reader, type);
      }
    } catch (IOException e) {
      System.err.println("Erreur de téléchargement depuis l'URL : " + e.getMessage());
      return new Calendar();
    }
  }
}
