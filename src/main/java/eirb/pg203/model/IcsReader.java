package eirb.pg203.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

// classe utilitaire pour lire les fichiers ICS en gérant les lignes continues
public class IcsReader implements AutoCloseable {
  private final BufferedReader reader;
  private String nextLine = null;

  public IcsReader(Reader reader) {
    this.reader = new BufferedReader(reader);
  }

  // lit une ligne complète du fichier ICS
  public String readLine() throws IOException {
    String line = (nextLine != null) ? nextLine : reader.readLine();
    nextLine = null;

    if (line == null) return null;

    while (true) {
      String next = reader.readLine();
      if (next != null && (next.startsWith(" ") || next.startsWith("\t"))) {
        // c'est une suite de ligne, on concatène (en sautant l'espace)
        line += next.substring(1);
      } else {
        // c'est une nouvelle ligne, on la garde pour le prochain appel
        nextLine = next;
        break;
      }
    }
    return line;
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
