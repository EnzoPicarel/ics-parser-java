package eirb.pg203.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class ParserURL extends AbstractParser {

  @Override
  public Calendar parse(String urlString, String type) {
    try { // gère les erreurs réseau/globales (ex: connexion qui échoue)
      URL url = URI.create(urlString).toURL();
      URLConnection connection = url.openConnection(); // ouvre la connexion réseau
      // ici, on ouvre le flux de lecture sur la connexion et il sera fermé
      // automatiquement
      try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
        return parseStream(reader, type); // délègue le parsing du flux à la méthode générique
      }
    } catch (IOException e) {
      // en cas d'erreur réseau ou lecture, on affiche un message et on retourne un
      // calendrier vide
      System.err.println("Erreur de téléchargement depuis l'URL : " + e.getMessage());
      return new Calendar();
    }
  }
}
