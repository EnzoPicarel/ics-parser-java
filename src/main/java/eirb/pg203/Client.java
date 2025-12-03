package eirb.pg203;

import eirb.pg203.model.*;

public class Client {
  public static void main(String[] args) {
    try {
      // on traite la ligne de commande via une autre classe
      ArgumentsCommande config = new ArgumentsCommande(args);

      // on récupère les paramètres de config
      Calendar C = AbstractParser.chooseParser(config.getInputFile(), config.getType().toString());

      config.getOutputGenerator().displayCalendar(C, config.getOutputFile());

    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
