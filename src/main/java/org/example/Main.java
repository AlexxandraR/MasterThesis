package org.example;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

//        int numStates = 6;
//        Set<String> alphabet = new HashSet<>(Arrays.asList("a", "b", "c"));
//        List<String> states = new ArrayList<>();
//
//        // Generujeme stavy q0, q1, ..., q5
//        for (int i = 0; i < numStates; i++) {
//            states.add("q" + i);
//        }
//
//        // Generujeme všetky permutácie stavov
//        List<List<String>> statePermutations = generatePermutations(states);
//
//        // Iterujeme cez permutácie stavov pre každé písmeno
//        for (List<String> aPerm : statePermutations) {
//            for (List<String> bPerm : statePermutations) {
//                for (List<String> cPerm : statePermutations) {
//                    //for (List<String> dPerm : statePermutations) {
//                        // Vytvorenie prechodovej funkcie
//                        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
//                        addTransitions(transitionFunction, states, "a", aPerm);
//                        addTransitions(transitionFunction, states, "b", bPerm);
//                        addTransitions(transitionFunction, states, "c", cPerm);
////                        addTransitions(transitionFunction, states, "d", dPerm);
//
//                        // Nastavenie počiatočného stavu a akceptačných stavov
//                        String initialState = "q0";
//                        Set<String> acceptStates = new HashSet<>();
//                        acceptStates.add("q" + (numStates - 1)); // Posledný stav ako akceptačný
//
//                        // Vytvorenie DFA
//                        DFA dfa = new DFA(new HashSet<>(states), alphabet, initialState, acceptStates, transitionFunction);
//
//                        // Overenie, či je composite
//                        dfa = dfa.minimize();
//                        dfa = dfa.completeDFA();
//                        if(dfa.isPermutation()) {
//                            boolean composite = dfa.isComposite();
//                            // Výstup výsledkov
//                            if(composite){
//                                System.out.println("Transition Function: " + transitionFunction);
//                                System.out.println("Initial State: " + initialState);
//                                System.out.println("Accept States: " + acceptStates);
//                                System.out.println("Is Composite: " + composite);
//                                System.out.println("===================================");
//                            }
//                        }
//                        // Ďalší automat sa generuje iteratívne
//                   //}
//                }
//            }
//        }

        String fileName = "automaton18.txt";
        Automaton automaton = new Automaton();
        FileRead f = new FileRead();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
//            System.out.println("\n NFA");
//            automaton.printNFA();
//            System.out.println("\n DFA");
            DFA dfa = automaton.toDFA();
//            dfa.printDFA();
//            dfa = dfa.minimize().completeDFA();
//            System.out.println("\n Minimaze");
//            dfa.printDFA();
//            System.out.println(dfa.isComplete());
//            dfa.completeDFA().printDFA();

//            System.out.println(dfa.isCommutative());
//            System.out.println("\n Algorithm");
//            System.out.println(dfa.isPrimeDFA());

//            System.out.println(dfa.isCompositeTime());

//            long startTime = System.nanoTime();
//
//            System.out.println(dfa.isCompositeTime());
//
//            long endTime = System.nanoTime();
//            double executionTime = (endTime - startTime) / 1e6;
//
//            System.out.printf("%.6f%n", executionTime);

            for (int i = 0; i < 55; i++) {

                long startTime = System.nanoTime();

                dfa.isCompositeMemory();

                long endTime = System.nanoTime();
                double executionTime = (endTime - startTime) / 1e6;

                System.out.printf("%.6f%n", executionTime);

            }

        }
        else{
            System.out.println(msg);
        }

//        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
//
//        //L1
//        Set<String> states = new HashSet<>(Arrays.asList("q0", "q1", "q2"));
//        Set<String> alphabet = new HashSet<>(List.of("a"));
//        String startState = "q0";
//        Set<String> acceptStates = new HashSet<>(List.of("q1"));
//
//        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
//        transitionFunction.put(Map.of("q1", "a"), Set.of("q2"));
//        transitionFunction.put(Map.of("q2", "a"), Set.of("q2"));

//        states = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
//        alphabet = new HashSet<>(List.of("a", "b"));
//        startState = "q0";
//        acceptStates = new HashSet<>(List.of("q0", "q1", "q2"));
//
//        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
//        transitionFunction.put(Map.of("q0", "b"), Set.of("q2"));
//        transitionFunction.put(Map.of("q1", "a"), Set.of("q1"));
//        transitionFunction.put(Map.of("q1", "b"), Set.of("q3"));
//        transitionFunction.put(Map.of("q2", "a"), Set.of("q3"));
//        transitionFunction.put(Map.of("q2", "b"), Set.of("q2"));
//        transitionFunction.put(Map.of("q3", "a"), Set.of("q3"));
//        transitionFunction.put(Map.of("q3", "b"), Set.of("q3"));

//        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);
//
//        DFA dfa = new Automaton(states, alphabet, startState, acceptStates, transitionFunction).toDFA();
//
//        System.out.println("\n Is prime:" + dfa.isPrimeDFA());


//        Set<String> states = new HashSet<>(Arrays.asList("q1", "q2"));
//        Set<String> alphabet = new HashSet<>(List.of("a", "b"));
//        String startState = "q1";
//        Set<String> acceptStates = new HashSet<>(List.of("q2"));
//
//        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
//
//        transitionFunction.put(Map.of("q1", "a"), Set.of("q2"));
//        transitionFunction.put(Map.of("q1", "b"), Set.of("q1"));
//        transitionFunction.put(Map.of("q2", "a"), Set.of("q2"));
//        transitionFunction.put(Map.of("q2", "b"), Set.of("q2"));
//
//        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);
//
//        DFA dfa = automaton.toDFA();
//
//        states = new HashSet<>(Arrays.asList("q1", "q2"));
//        alphabet = new HashSet<>(List.of("a", "b"));
//        startState = "q1";
//        acceptStates = new HashSet<>(List.of("q2"));
//
//        transitionFunction = new HashMap<>();
//
//        transitionFunction.put(Map.of("q1", "a"), Set.of("q2"));
//        transitionFunction.put(Map.of("q1", "b"), Set.of("q2"));
//        transitionFunction.put(Map.of("q2", "a"), Set.of("q2"));
//        transitionFunction.put(Map.of("q2", "b"), Set.of("q2"));
//
//        automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);
//
//        DFA dfa1 = automaton.toDFA();

//        boolean isDeterministic = automaton.isDeterministic();
//        if(isDeterministic){
//            System.out.println("\nAutomaton is deterministic.");
//        }
//        else{
//            System.out.println("\nAutomaton is not deterministic.");
//        }
//        DFA dfa = automaton.toDFA();
//        dfa.printDFA();
//        System.out.println("\nPermutation DFA: " + dfa.isPermutation());
//        System.out.println("\nCommutative DFA: " + dfa.isCommutative(10));

//        Set<DFA> dfas = new HashSet<>(Arrays.asList(dfa, dfa1));
//        dfa = dfa.intersection(dfas);
//        dfa.printDFA();
//        dfa.flipAcceptStates();
//        dfa.printDFA();
//        System.out.println(dfa.isAcceptStateReachable());
    }

    public static List<List<String>> generatePermutations(List<String> states) {
        List<List<String>> permutations = new ArrayList<>();
        permute(states, 0, permutations);
        return permutations;
    }

    public static void permute(List<String> states, int index, List<List<String>> result) {
        if (index == states.size()) {
            result.add(new ArrayList<>(states));
        } else {
            for (int i = index; i < states.size(); i++) {
                Collections.swap(states, i, index);
                permute(states, index + 1, result);
                Collections.swap(states, i, index);
            }
        }
    }

    public static void addTransitions(Map<Map<String, String>, String> transitionFunction, List<String> states, String symbol, List<String> permutation) {
        for (int i = 0; i < states.size(); i++) {
            Map<String, String> key = new HashMap<>();
            key.put(states.get(i), symbol);
            transitionFunction.put(key, permutation.get(i));
        }
    }

}