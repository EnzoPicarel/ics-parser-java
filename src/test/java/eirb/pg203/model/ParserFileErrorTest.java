package eirb.pg203.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParserFileErrorTest {

  @Test
  @DisplayName("chooseParser selects URL parser and returns non-null calendar")
  void urlParserNonNull() {
    Calendar cal = AbstractParser.chooseParser("http://example.invalid/some.ics", "events");
    assertNotNull(cal);
    // No guarantee of network -> expect either empty or something; just ensure no
    // exception
    assertTrue(cal.getAllComponents().isEmpty());
  }

  @Test
  @DisplayName("chooseParser with https also works")
  void httpsParserNonNull() {
    Calendar cal = AbstractParser.chooseParser("https://example.invalid/some.ics", "todos");
    assertNotNull(cal);
  }
}
