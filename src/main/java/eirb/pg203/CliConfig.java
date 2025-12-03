package eirb.pg203;

import eirb.pg203.model.*;

public class CliConfig {
  public final String inputFile;
  public final String componentType;
  public final Output outputGenerator;
  public final String outputFile;
  public final EventFilter eventFilter;
  public final TodoFilter todoFilter;

  public CliConfig(
      String inputFile,
      String componentType,
      Output outputGenerator,
      String outputFile,
      EventFilter eventFilter,
      TodoFilter todoFilter) {
    this.inputFile = inputFile;
    this.componentType = componentType;
    this.outputGenerator = outputGenerator;
    this.outputFile = outputFile;
    this.eventFilter = eventFilter;
    this.todoFilter = todoFilter;
  }
}
