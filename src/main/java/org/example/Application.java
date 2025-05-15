package org.example;

import java.util.Scanner;

public class Application {
    private Automaton automaton;
    private DFA dfa;

    public Application() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            this.printMenu();
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
                continue;
            }

            if (choice == 100) {
                System.out.println("\nExiting application...");
                break;
            }
            else if (choice > 16) {
                System.out.println("\nInvalid choice. Please select a valid option.");
                continue;
            }

            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine();
            System.out.println();

            String msg = this.readAutomatonFromFile(filePath);

            if(msg == null){
                switch (choice) {
                    case 1:
                        if(automaton.isDeterministic())
                            System.out.println("The automaton is deterministic.");
                        else
                            System.out.println("The automaton is NOT deterministic.");
                        break;
                    case 2:
                        System.out.println("Here is converted automaton:");
                        automaton.toDFA().printOutputDFA();
                        break;
                    case 3:
                        if(!automaton.isDeterministic()){
                            System.out.println("The given automaton is not deterministic. " +
                                    "The automaton is first converted to DFA as follow:");
                            dfa.printOutputDFA();
                        }
                        System.out.println("Here is minimized automaton:");
                        dfa.minimize().printOutputDFA();
                        break;
                    case 4:
                        if(!automaton.isDeterministic())
                            printWrongAutomatonTypeMsg("deterministic");
                        else if(dfa.isComplete())
                            System.out.println("The automaton is complete.");
                        else
                            System.out.println("The automaton is NOT complete.");
                        break;
                    case 5:
                        if(!automaton.isDeterministic())
                            System.out.println("The given automaton is not deterministic.");
                        else {
                            System.out.println("Here is complete automaton:");
                            dfa.completeDFA().printOutputDFA();
                        }
                        break;
                    case 6:
                        if(!automaton.isDeterministic())
                            printWrongAutomatonTypeMsg("deterministic");
                        else if(dfa.isPermutation())
                            System.out.println("The automaton is permutation.");
                        else
                            System.out.println("The automaton is NOT permutation.");
                        break;
                    case 7:
                        if(!automaton.isDeterministic())
                            printWrongAutomatonTypeMsg("deterministic");
                        else if(dfa.isCommutative())
                            System.out.println("The automaton is commutative.");
                        else
                            System.out.println("The automaton is NOT commutative.");
                        break;
                    case 8:
                        if(!automaton.isDeterministic())
                            printWrongAutomatonTypeMsg("deterministic");
                        else if(dfa.minimize().completeDFA().isPrimeDFA())
                            System.out.println("The automaton is prime.");
                        else
                            System.out.println("The automaton is NOT prime. The automaton is COMPOSITE.");
                        break;
                    case 9:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isPermutation())
                            printWrongAutomatonTypeMsg("permutation");
                        else if(dfa.isComposite())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 10:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isPermutation())
                            printWrongAutomatonTypeMsg("permutation");
                        else if(dfa.isCompositeTime())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 11:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isPermutation())
                            printWrongAutomatonTypeMsg("permutation");
                        else if(dfa.isCompositeMemory())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 12:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isPermutation())
                            printWrongAutomatonTypeMsg("permutation");
                        else if(dfa.isCompositeInitial())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 13:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isPermutation())
                            printWrongAutomatonTypeMsg("permutation");
                        else if(dfa.isCompositeInitialMemory())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 14:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isCommutative())
                            printWrongAutomatonTypeMsg("commutative");
                        else if(dfa.isCompositeCommutative())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 15:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isCommutative())
                            printWrongAutomatonTypeMsg("commutative");
                        else if(dfa.isCompositeCommutativeSavedWord())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                    case 16:
                        if(!automaton.isDeterministic()) {
                            printWrongAutomatonTypeMsg("deterministic");
                            break;
                        }
                        if(!dfa.isCommutative())
                            printWrongAutomatonTypeMsg("commutative");
                        else if(dfa.isCompositeCommutativeBigAlphabet())
                            System.out.println("The automaton is composite.");
                        else
                            System.out.println("The automaton is NOT composite. The automaton is PRIME.");
                        break;
                }
            }
            else{
                System.out.println(msg);
            }
        }
        scanner.close();
    }

    private void printMenu(){
        System.out.println("\n Select an operation:");
        System.out.println("1 - Is the automaton deterministic?");
        System.out.println("2 - Convert the automaton to a deterministic one");
        System.out.println("3 - Minimize the DFA");
        System.out.println("4 - Is the DFA complete?");
        System.out.println("5 - Make the DFA complete");
        System.out.println("6 - Is the DFA permutation?");
        System.out.println("7 - Is the DFA commutative?");
        System.out.println("8 - Is the DFA prime? - General algorithm using the roof of DFA");
        System.out.println("9 - Is the DFA composite? - Original algorithm for permutation DFAs");
        System.out.println("10 - Is the DFA composite? - Algorithm for permutation DFAs " +
                "with saved processed states");
        System.out.println("11 - Is the DFA composite? - Algorithm for permutation DFAs " +
                "with saved processed orbit-DFAs states");
        System.out.println("12 - Is the DFA composite? - Algorithm with saved processed states generating " +
                "the orbit-DFAs from the initial states for permutation DFAs");
        System.out.println("13 - Is the DFA composite? - Algorithm with saved processed orbit-DFAs initial " +
                "states for permutation DFAs");
        System.out.println("14 - Is the DFA composite? - Original algorithm for permutation commutative DFAs");
        System.out.println("15 - Is the DFA composite? - Algorithm for permutation commutative DFAs " +
                "with saved word");
        System.out.println("16 - Is the DFA composite? - Algorithm for permutation commutative DFAs with big alphabet");
        System.out.println("100 - Exit application");
        System.out.print("Enter your choice: ");
    }

    private String readAutomatonFromFile(String filePath){
        FileReader f = new FileReader();
        automaton = new Automaton();
        String msg = f.readText(filePath, automaton);
        if(msg == null)
            dfa = automaton.toDFA();
        return msg;
    }

    private void printWrongAutomatonTypeMsg(String type){
        System.out.println("The given automaton is not " + type + " and the chosen operation cannot be performed.");
    }
}
