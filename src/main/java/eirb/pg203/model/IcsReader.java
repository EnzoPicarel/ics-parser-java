package eirb.pg203.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class IcsReader implements AutoCloseable {
  // lecture efficace
  private final BufferedReader reader;
  // stocke la prochaine ligne à traiter si besoin
  private String bufferedLine = null;

  // construit avec un Reader générique (fichier, url, ...)
  public IcsReader(Reader reader) {
    this.reader = new BufferedReader(reader);
  }

  // lit une ligne logique (gère les retours à la ligne avec espace/tabulation)
  public String readLogicalLine() throws IOException {
    String logicalLine = (bufferedLine != null) ? bufferedLine : reader.readLine();
    bufferedLine = null;

    if (logicalLine == null) return null;

    // boucle pour concaténer les lignes de continuation
    while (true) {
      String next = reader.readLine();
      if (next != null && isContinuation(next)) {
        logicalLine += next.substring(1); // on enlève l'espace/tab du début
      } else {
        bufferedLine = next; // on garde la prochaine vraie ligne pour plus tard
        break;
      }
    }
    return logicalLine;
  }

  // détecte si la ligne est une continuation (commence par espace ou tab)
  private boolean isContinuation(String line) {
    return line.startsWith(" ") || line.startsWith("\t");
  }

  // ferme le reader
  // appelle automatiquement à la fin d'un try pour fermer proprement
  @Override
  public void close() throws IOException {
    reader.close();
  }
}
