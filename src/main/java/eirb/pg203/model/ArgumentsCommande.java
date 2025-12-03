package eirb.pg203.model;

public class ArgumentsCommande {
    public enum ExtractionType { events, todos }
    private String inputFile;
    private String outputFile; // peut etre null/vide
    private ExtractionType type;
    private Output outputGenerator;

    public ArgumentsCommande(String[] args) throws IllegalArgumentException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: clical file.ics events|todos [-ics|-txt] [-o output_file]");
        }

        this.inputFile = args[0];
        this.type = parseType(args[1]);
        
        // par défaut
        this.outputGenerator = new OutputTxt();
        this.outputFile = "";

        parseOptions(args);
    }

    private ExtractionType parseType(String arg) {
        try {
            return ExtractionType.valueOf(arg.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Second argument must be 'events' or 'todos'");
        }
    }

    private void parseOptions(String[] args) {
        for (int i = 2; i < args.length; i++) {
            switch (args[i]) {
                case "-ics": this.outputGenerator = new OutputIcs(); break;
                case "-txt": this.outputGenerator = new OutputTxt(); break;
                case "-html": this.outputGenerator = new OutputHtml(); break;
                case "-o":
                    if (i + 1 < args.length) { 
                        this.outputFile = args[i + 1]; // argument après le -o
                        i++; // du coup il est déjà traité, donc ignorer pour la suite 
                    } else {
                        throw new IllegalArgumentException("Error: -o requires a filename");
                    }
                    break;
            }
        }
    }

    public String getInputFile() { return inputFile; }
    public String getOutputFile() { return outputFile; }
    public ExtractionType getType() { return type; }
    public Output getOutputGenerator() { return outputGenerator; }
}