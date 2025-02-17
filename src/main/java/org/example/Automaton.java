package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
public class Automaton {
    private Set<String> states;
    private Set<String> alphabet;
    private String initialState;
    private Set<String> acceptStates;
    private Map<Map<String, String>, Set<String>> transitionFunction;

    /**
     * Constructs an empty Automaton with an empty set of states, alphabet, and transitions.
     */
    public Automaton() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.acceptStates = new HashSet<>();
        this.initialState = null;
        this.transitionFunction = new HashMap<>();
    }

    /**
     * Prints the details of the Automaton.
     */
    public void printNFA() {
        System.out.println("States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Start State: " + initialState);
        System.out.println("Accept States: " + acceptStates);
        System.out.println("Transition Table:");

        Set<String> alpha = new HashSet<>();
        alpha.add("");
        alpha.addAll(alphabet);

        System.out.print("State");
        for (String symbol : alpha) {
            if(Objects.equals(symbol, "")){
                System.out.print("\te");
            }
            else{
                System.out.print("\t\t" + symbol);
            }
        }
        System.out.println();

        for (String state : states) {
            System.out.print(state + "\t");
            for (String symbol : alpha) {
                Map<String, String> transitionKey = new HashMap<>();
                transitionKey.put(state, symbol);
                Set<String> nextState = transitionFunction.get(transitionKey);
                System.out.print("\t" + (nextState != null ? nextState : "-"));
            }
            System.out.println();
        }
    }

    /**
     * Checks if the automaton is deterministic. A deterministic automaton must not have any epsilon transitions
     * and each state must have at most one transition for each symbol in the alphabet.
     *
     * @return true if the automaton is deterministic, otherwise false
     */
    public boolean isDeterministic() {
        for (String state : states) {
            Set<String> epsilonTransition = transitionFunction.get(Map.of(state, ""));
            if (epsilonTransition != null) {
                return false;
            }
            for (String symbol : alphabet) {
                Set<String> nextStates = transitionFunction.get(Map.of(state, symbol));
                if (nextStates != null && nextStates.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Converts the Automaton to a DFA. If the Automaton is not deterministic, it is first converted into a DFA.
     * Otherwise, it creates a DFA directly.
     *
     * @return the corresponding DFA
     */
    public DFA toDFA() {
        if (!isDeterministic()) {
            return this.renameStatesDFA(this.convertToDeterministic());
        }
        else{
            Map<Map<String, String>, String> dfaTransitionFunction = new HashMap<>();

            for (String state : this.states) {
                for (String symbol : this.alphabet) {
                    if(transitionFunction.get(Map.of(state, symbol)) != null){
                        String nextState = transitionFunction.get(Map.of(state, symbol)).iterator().next();
                        dfaTransitionFunction.put(Map.of(state, symbol), nextState);
                    }
                }
            }


            return new DFA(this.states, this.alphabet, this.initialState, this.acceptStates, dfaTransitionFunction);
        }
    }

    /**
     * Computes the epsilon closure for a set of states in the Automaton.
     * The epsilon closure is the set of states reachable from the given set using epsilon transitions.
     *
     * @param s the set of states
     * @return the epsilon closure of the given states
     */
    public Set<String> closureEpsilon(Set<String> s) {
        Set<String> oldS = new HashSet<>(s);
        for (String state : oldS) {
            Map<String, String> key = Map.of(state, "");
            Set<String> transitions = transitionFunction.get(key);
            if (transitions != null) {
                s.addAll(transitions);
            }
        }
        if (s.equals(oldS)) {
            return s;
        } else {
            return closureEpsilon(s);
        }
    }

    /**
     * Converts the NFA into a DFA by removing epsilon transitions and ensuring each state has at most one transition
     * for every symbol in the alphabet.
     *
     * @return the corresponding DFA
     */
    public DFA convertToDeterministic() {
        Map<Map<String, String>, String> dfaTransitions = new HashMap<>();
        Set<String> q0Closure = closureEpsilon(new HashSet<>(Collections.singleton(initialState)));
        List<Set<String>> qDone = new ArrayList<>();
        List<Set<String>> qList = new ArrayList<>();
        qList.add(q0Closure);

        while (!qList.isEmpty()) {
            Set<String> state = qList.get(0);
            qDone.add(state);
            qList.remove(0);
            alphabet.remove("");

            for (String symbol : alphabet) {
                List<String> transitions = new ArrayList<>();

                for (String i : state) {
                    Map<String, String> key = Map.of(i, symbol);
                    Set<String> transition = transitionFunction.get(key);
                    if (transition != null) {
                        transitions.addAll(transition);
                    }
                }

                if (!transitions.isEmpty()) {
                    Set<String> newState = new HashSet<>(closureEpsilon(new HashSet<>(transitions)));
                    dfaTransitions.put(
                            Map.of(sortedStateString(state), symbol),
                            sortedStateString(newState)
                    );

                    if (!qDone.contains(newState) && !qList.contains(newState)) {
                        qList.add(newState);
                    }
                }
            }
        }

        // Determine accepting states
        Set<String> acceptingStates = new HashSet<>();
        for (Map.Entry<Map<String, String>, String> entry : dfaTransitions.entrySet()) {
            String state = entry.getKey().keySet().iterator().next();
            Set<String> qStateSet = evalState(state);
            for (String qState : qStateSet) {
                if (this.acceptStates.contains(qState)) {
                    acceptingStates.add(state);
                    break;
                }
            }
        }

        for (Map.Entry<Map<String, String>, String> entry : dfaTransitions.entrySet()) {
            String nextState = entry.getValue();
            Set<String> qStateSet = evalState(nextState);
            for (String qState : qStateSet) {
                if (this.acceptStates.contains(qState)) {
                    acceptingStates.add(nextState);
                    break;
                }
            }
        }

        // Update of states
        Set<String> allStates = new HashSet<>();
        for (Map.Entry<Map<String, String>, String> entry : dfaTransitions.entrySet()) {
            allStates.add(entry.getKey().keySet().iterator().next());
            allStates.add(entry.getValue());
        }

        if(this.alphabet.isEmpty()){
            allStates.add(sortedStateString(q0Closure));
            for(String state : this.acceptStates){
                if(q0Closure.contains(state)){
                    acceptingStates.add(sortedStateString(q0Closure));
                    break;
                }
            }
        }
        return new DFA(allStates, alphabet, sortedStateString(q0Closure), acceptingStates, dfaTransitions);
    }

    /**
     * Sorts a set of state names and returns them as a comma-separated string.
     *
     * @param stateSet the set of states to sort
     * @return a sorted comma-separated string of state names
     */
    private String sortedStateString(Set<String> stateSet) {
        List<String> sortedState = new ArrayList<>(stateSet);
        sortedState.sort(Comparator.naturalOrder());
        return String.join(",", sortedState);
    }

    /**
     * Evaluates a state string by splitting it into individual state names.
     *
     * @param stateString the comma-separated state string
     * @return the set of individual states
     */
    private Set<String> evalState(String stateString) {
        return new HashSet<>(Arrays.asList(stateString.split(",")));
    }

    /**
     * Renames the states of a DFA, giving them new names in the form q0, q1, q2, etc.
     *
     * @param dfa the DFA to rename
     * @return a new DFA with renamed states
     */
    public DFA renameStatesDFA(DFA dfa) {
        Map<String, String> stateMapping = new HashMap<>();
        int index = 0;

        for (String state : dfa.getStates()) {
            stateMapping.put(state, "q" + index);
            index++;
        }

        Map<Map<String, String>, String> renamedTransitions = new HashMap<>();
        for (Map.Entry<Map<String, String>, String> entry : dfa.getTransitionFunction().entrySet()) {
            Map<String, String> originalKey = entry.getKey();
            String originalValue = entry.getValue();

            String newFromState = stateMapping.get(originalKey.keySet().iterator().next());
            String symbol = originalKey.values().iterator().next();
            String newToState = stateMapping.get(originalValue);

            renamedTransitions.put(Map.of(newFromState, symbol), newToState);
        }

        Set<String> renamedAcceptingStates = new HashSet<>();
        for (String acceptingState : dfa.getAcceptStates()) {
            renamedAcceptingStates.add(stateMapping.get(acceptingState));
        }

        String renamedInitialState = stateMapping.get(dfa.getInitialState());

        return new DFA(
                new HashSet<>(stateMapping.values()),
                dfa.getAlphabet(),
                renamedInitialState,
                renamedAcceptingStates,
                renamedTransitions);
    }

}
