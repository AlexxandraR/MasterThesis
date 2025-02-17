package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The FileReader class implements reading an automaton from a file and was inspired
 * by the FORKLIFT project available on GitHub.
 *
 * @see <a href="https://github.com/Mazzocchi/FORKLIFT/blob/master/program/automata/AutomatonParser.java">FORKLIFT</a>
 */

public class FileReader {

    /**
     * Parses the file into an automaton.
     *
     * @param fileName  the file from which the grammar is loaded
     * @param automaton variable into which the automaton from the file is saved
     * @return null if everything was loaded correctly and an error message if the loading was not successful
     */
    public String readText(String fileName, Automaton automaton) {
        java.io.FileReader fReader;
        try {
            fReader = new java.io.FileReader(fileName);
        } catch (FileNotFoundException e) {
            return "Failed to load file.";
        }
        BufferedReader buffReader = new BufferedReader(fReader);

        try {
            String line = readLine(buffReader);
            if (line == null) throw new IllegalArgumentException("Empty file");

            // Handle the first line (initial state or transition)
            readInitial(line, automaton);

            // Handle the rest of the lines (transitions and final states)
            line = readLine(buffReader);
            while (line != null) {
                if (line.matches(".*->.*")) {
                    readEdges(line, automaton);
                }
                else if(!line.contains(",") && !line.contains("->")) {
                    readFinals(line, automaton);
                }
                else{
                    throw new IllegalArgumentException("This line: " + line + " is incorrect.");
                }
                line = readLine(buffReader);
            }

            return null;
        } catch (Exception e) {
            return "Incorrect format of file: " + e.getMessage();
        }
    }

    /**
     * Reads a line from the buffered reader, removes all whitespaces and returns the cleaned line.
     *
     * @param buffReader the BufferedReader from which to read the line
     * @return the cleaned line or null if the end of the file is reached
     * @throws Exception if there is an error while reading the file
     */
    private String readLine(BufferedReader buffReader) throws Exception {
        String line = buffReader.readLine();
        while(Objects.equals(line, "")){
            line = buffReader.readLine();
        }
        return (line != null) ? line.replaceAll("\\s+", "") : null;
    }

    /**
     * Reads and processes the first line of the file as the initial state or as a transition.
     *
     * @param line      the first line from the file
     * @param automaton the automaton into which the initial state or transition will be loaded
     */
    private void readInitial(String line, Automaton automaton){
        if (line.matches(".*->.*")) {
            readEdges(line, automaton);
        } else {
            String initialState = line.strip();
            automaton.setInitialState(initialState);
            automaton.getStates().add(initialState);
        }
    }

    /**
     * Reads and processes a transition line from the file, adding the transition to the automaton.
     *
     * @param line      the transition line from the file
     * @param automaton the automaton into which the transition will be added
     */
    private void readEdges(String line, Automaton automaton){
        try{
            // Transition: symbol, from -> to
            String[] parts1 = line.split(",");
            String[] parts2 = parts1[1].split("->");
            String[] names = new String[]{parts1[0], parts2[0], parts2[1]};

            for (int i = 0; i < names.length; i++) {
                names[i] = names[i].strip();
            }

            // Symbol
            String symbol = names[0];
            if (symbol.isEmpty()){
                symbol = "";
            }
            automaton.getAlphabet().add(symbol);

            // From
            String from = names[1];
            automaton.getStates().add(from);

            // To
            String to = names[2];
            automaton.getStates().add(to);

            // Add transition
            if (automaton.getTransitionFunction().containsKey(Map.of(from, symbol))) {
                automaton.getTransitionFunction().get(Map.of(from, symbol)).add(to);
            } else {
                Set<String> value = new HashSet<>();
                value.add(to);
                automaton.getTransitionFunction().put(Map.of(from, symbol), value);
            }
        }catch(Exception e){
            throw new IllegalArgumentException("This line: " + line + " is incorrect.");
        }
    }

    /**
     * Reads and processes a line representing a final state, adding the state to the automaton.
     *
     * @param line      the line representing the final state
     * @param automaton the automaton into which the final state will be added
     */
    private void readFinals(String line, Automaton automaton) {
        String finalState = line.strip();
        automaton.getStates().add(finalState);
        automaton.getAcceptStates().add(finalState);
    }
}

