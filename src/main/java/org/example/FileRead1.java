package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileRead1 {

    /**
     * parses the file into an automaton
     *
     * @param fileName  the file from which the grammar is loaded
     * @param automaton variable into which the automaton from the file is saved
     * @return null if everything was loaded correctly and an error message if the loading was not successful
     */
    public String readText(String fileName, Automaton automaton){
        java.io.FileReader fReader;
        try {
            fReader = new java.io.FileReader(fileName);
        } catch (FileNotFoundException e) {
            return "Failed to load file.";
        }
        BufferedReader buffReader = new BufferedReader(fReader);

        try {
            int counter = 0;
            int old_counter;
            String line;
            String state1;
            String state2;
            String symbol;

            while (buffReader.ready()) {
                old_counter = counter;
                line = buffReader.readLine().replaceAll("\\s+", "");

                if(counter == 0 && !line.contains("->")){
                    automaton.setInitialState(line);
                    counter++;
                }
                else if(line.contains("->")) {
                    symbol = line.subSequence(0, line.indexOf(",")).toString();
                    state1 = line.subSequence(line.indexOf(",") + 1, line.indexOf("->")).toString();
                    state2 = line.subSequence(line.indexOf("->") + 2, line.length()).toString();
                    if(!symbol.equals("")){
                        automaton.getAlphabet().add(symbol);
                    }
                    automaton.getStates().add(state1);
                    automaton.getStates().add(state2);
                    counter++;
                    if (automaton.getTransitionFunction().containsKey(Map.of(state1, symbol))) {
                        automaton.getTransitionFunction().get(Map.of(state1, symbol)).add(state2);
                    } else {
                        Set<String> value = new HashSet<>();
                        value.add(state2);
                        automaton.getTransitionFunction().put(Map.of(state1, symbol), value);
                    }
                }
                else if(!line.contains(",") && !line.contains("-") && !line.contains(">")) {
                    automaton.getStates().add(line);
                    automaton.getAcceptStates().add(line);
                    counter++;
                }
                if(old_counter == counter){
                    throw new IllegalArgumentException();
                }
            }
            return null;
        }
        catch(Exception e){
            return "Incorrect format of file.";
        }
    }
}