package eirb.pg203.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class IcsReader implements AutoCloseable {
  private final BufferedReader reader;
  private String bufferedLine = null;

  public IcsReader(Reader reader) {
    this.reader = new BufferedReader(reader);
  }

  public String readLogicalLine() throws IOException {
    String logicalLine = (bufferedLine != null) ? bufferedLine : reader.readLine();
    bufferedLine = null;

    if (logicalLine == null) return null;

    while (true) {
      String next = reader.readLine();
      if (next != null && isContinuation(next)) {
        logicalLine += next.substring(1);
      } else {
        bufferedLine = next;
        break;
      }
    }
    return logicalLine;
  }

  private boolean isContinuation(String line) {
    return line.startsWith(" ") || line.startsWith("\t");
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
