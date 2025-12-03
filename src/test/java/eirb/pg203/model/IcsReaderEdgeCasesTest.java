package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IcsReaderEdgeCasesTest {

  @Test
  @DisplayName("Unfolding multiple continuation lines")
  void unfoldingMultipleLines() throws IOException {
    String raw = "DESCRIPTION:Line1\r\n continuation1\r\n continuation2\r\nNEXT:Value\r\n";
    try (IcsReader r = new IcsReader(new StringReader(raw))) {
      String first = r.readLogicalLine();
      assertEquals("DESCRIPTION:Line1continuation1continuation2", first);
      String second = r.readLogicalLine();
      assertEquals("NEXT:Value", second);
    }
  }

  @Test
  @DisplayName("Empty reader returns null immediately")
  void emptyReader() throws IOException {
    try (IcsReader r = new IcsReader(new StringReader(""))) {
      assertNull(r.readLogicalLine());
    }
  }

  @Test
  @DisplayName("Does not merge non-indented lines")
  void noMerge() throws IOException {
    // Aucune indentation avant UID -> ne doit pas être fusionné
    String raw = "SUMMARY:Test\r\nDESCRIPTION:Abc\r\nUID:1\r\n";
    try (IcsReader r = new IcsReader(new StringReader(raw))) {
      assertEquals("SUMMARY:Test", r.readLogicalLine());
      assertEquals("DESCRIPTION:Abc", r.readLogicalLine());
      assertEquals("UID:1", r.readLogicalLine());
      assertNull(r.readLogicalLine());
    }
  }
}
