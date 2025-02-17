package org.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        FileReader f = new FileReader();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n Select an operation:");
            System.out.println("1 - Is the automaton deterministic?");
            System.out.println("2 - Convert the automaton to a deterministic one.");
            System.out.println("3 - Minimize the automaton");
            System.out.println("4 - Is the automaton complete?");
            System.out.println("5 - Make the automaton complete");
            System.out.println("6 - Is the automaton permutation?");
            System.out.println("7 - Is the automaton commutative?");
            System.out.println("8 - Is the automaton prime? - General algorithm");
            System.out.println("9 - Is the automaton composite? - Basic algorithm for permutation DFAs");
            System.out.println("10 - Is the automaton composite? - Algorithm for permutation DFAs " +
                    "with saved processed states");
            System.out.println("11 - Is the automaton composite? - Algorithm for permutation DFAs " +
                    "with saved processed orbit-DFAs states");
            System.out.println("100 - Exit application");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 100) {
                System.out.println("\nExiting application.");
                break;
            }

            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine();
            System.out.println();

            Automaton automaton = new Automaton();;
            String msg = f.readText(filePath, automaton);
            if(msg == null){
                DFA dfa;
                switch (choice) {
                    case 1:
                        if(automaton.isDeterministic())
                            System.out.println("The automaton is deterministic.");
                        else
                            System.out.println("The automaton is NOT deterministic.");
                        break;
                    case 2:
                        automaton.toDFA().printDFA();
                        break;
                    case 3:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        automaton.toDFA().minimize().printDFA();
                        break;
                    case 4:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        else if(automaton.toDFA().isComplete())
                            System.out.println("The automaton is complete.");
                        else
                            System.out.println("The automaton is NOT complete.");
                        break;
                    case 5:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        else automaton.toDFA().completeDFA().printDFA();
                        break;
                    case 6:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        else if(automaton.toDFA().isPermutation())
                            System.out.println("The automaton is permutation.");
                        else
                            System.out.println("The automaton is NOT permutation.");
                        break;
                    case 7:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        else if(automaton.toDFA().isCommutative())
                            System.out.println("The automaton is commutative.");
                        else
                            System.out.println("The automaton is NOT commutative.");
                        break;
                    case 8:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        else if(automaton.toDFA().minimize().completeDFA().isPrimeDFA())
                            System.out.println("The automaton is prime.");
                        else
                            System.out.println("The automaton is NOT prime. The automaton is COMPOSITE.");
                        break;
                    case 9:
                        if(!automaton.isDeterministic()) {
                            System.out.println("The given automaton is not deterministic.");
                            break;
                        }
                        dfa = automaton.toDFA();
                        if(!dfa.isPermutation())
                            System.out.println("The given automaton is not permutation.");
                        else if(dfa.isComposite())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 10:
                        if(!automaton.isDeterministic()) {
                            System.out.println("The given automaton is not deterministic.");
                            break;
                        }
                        dfa = automaton.toDFA();
                        if(!dfa.isPermutation())
                            System.out.println("The given automaton is not permutation.");
                        else if(dfa.isCompositeTime())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 11:
                        if(!automaton.isDeterministic()) {
                            System.out.println("The given automaton is not deterministic.");
                            break;
                        }
                        dfa = automaton.toDFA();
                        if(!dfa.isPermutation())
                            System.out.println("The given automaton is not permutation.");
                        else if(dfa.isCompositeMemory())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            }
            else{
                System.out.println(msg);
            }
        }
        scanner.close();

//        String fileName = "automaton37.txt";
//        Automaton automaton = new Automaton();
//        FileReader f = new FileReader();
//        String msg = f.readText(fileName, automaton);
//        if(msg == null){
//            System.out.println("\n NFA");
//            automaton.printNFA();
//            System.out.println("\n DFA");
//            DFA dfa = automaton.toDFA();
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

//            for(int i = 0; i < 3; i++) {
//
//                long startTime = System.nanoTime();
//
//                dfa.isCompositeMemory();
//
//                long endTime = System.nanoTime();
//                double executionTime = (endTime - startTime) / 1e6;
//
//                System.out.printf("%.6f%n", executionTime);
//
//            }

//        }
//        else{
//            System.out.println(msg);
//        }

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

}