import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This program reads a structured Backus-Naur Form (BNF) grammar from an input file,
 * stores it using Java Collections, and prints out all possible strings that can be
 * generated from the grammar recursively.
 */

public class BNF {

    /**
     * The main method of the program.
     * @param args Command line arguments. The first argument should be the path to the input file,
     *             and the second argument should be the path to the output file.
     */

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("ERROR: This program works exactly with two command line arguments.");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        // Check if input file exists and readable
        File input = new File(inputFileName);
        if (!input.exists() || !input.isFile() || !input.canRead()) {
            System.out.println("ERROR: This program cannot read from the \"" + inputFileName + "\", either this program does not have read permission to read that file or file does not exist. Program is going to terminate!");
            return;
        }

        try {
        FileWriter writer = new FileWriter(outputFileName);
        String[] inputContent = FileInput.readFile(inputFileName, false, false);
        FileOutput.writeToFile(outputFileName, "", false, false);

        Operations(inputContent, writer);
        writer.close();

    }
        catch (IOException e) {
            e.printStackTrace();
        }

       }

    /**
     * Reads the input BNF grammar, stores it using Java Collections, and prints out
     * all possible strings recursively.
     * @param inputContent The content of the input file containing the BNF grammar.
     * @param writer The FileWriter object for writing output to a file.
     * @throws IOException If an I/O error occurs.
     */
    public static void Operations(String[] inputContent, FileWriter writer) throws IOException {
        Map<String, List<String>> productions = new HashMap<>(); // Create a map to store production rules

        // Parse each line of the input BNF grammar and store it in the map
        for (int i = 0; i < inputContent.length; i++) {
            String[] line = inputContent[i].split("->");
            String[] valueOfMap = line[1].split("\\|");

            productions.put(line[0], Arrays.asList(valueOfMap));
        }

        // Start the process of retrieving terminal symbols recursively
        gettingTerminals("S", productions, writer);

    }

    /**
     * Recursively retrieves terminal symbols from the BNF grammar and writes them to the output file.
     * @param nonterminal The non-terminal symbol to expand.
     * @param productions The map containing production rules.
     * @param writer The FileWriter object for writing output to a file.
     * @throws IOException If an I/O error occurs.
     */
    public static void gettingTerminals(String nonterminal, Map<String, List<String>> productions, FileWriter writer) throws IOException {
        // Iterate over the production rules to find the nonterminal symbol
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String key = entry.getKey();
            if (Objects.equals(nonterminal, key)) {
                List<String> values = entry.getValue();
                writer.write("(");
                int counter = 0;
                for (String value : values) {
                    counter++;
                    for (int j = 0; j < value.length(); j++) {
                        char c = value.charAt(j);
                        // If the character is a lowercase letter, write it to the output file
                        if (Character.isLowerCase(c)) {
                            writer.write(c);
                            // If the character is an uppercase letter, recursively expand it
                            // by calling the gettingTerminals method
                        } else if (Character.isUpperCase(c)) {
                            gettingTerminals(String.valueOf(c), productions, writer);
                        }
                    }
                    // Add a '|' separator between production rules
                    if (counter != values.size()) {
                        writer.write("|");
                    }
                }
                writer.write(")");
            } else {
                continue;
            }
        }
    }
}