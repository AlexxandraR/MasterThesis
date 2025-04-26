package org.example;

public class Main {
    public static void main(String[] args) {

        Application app = new Application();


        //EXPERIMENTS
        //random composite
        //List<String> subory = List.of("automaton74.txt", "automaton75.txt", "automaton82.txt", "automaton76.txt", "automaton77.txt", "automaton78.txt", "automaton79.txt", "automaton80.txt", "automaton81.txt");
        //random prime
        //List<String> subory = List.of("automaton59.txt", "automaton60.txt", "automaton61.txt", "automaton62.txt", "automaton63.txt", "automaton64.txt", "automaton65.txt", "automaton66.txt", "automaton67.txt");
        //article
        //List<String> subory = List.of("automaton69.txt", "automaton70.txt", "automaton52.txt", "automaton71.txt", "automaton72.txt", "automaton73.txt");
//        List<String> subory = List.of("automaton83.txt", "automaton84.txt");
//        FileReader f = new FileReader();
//
//        try (FileWriter writer = new FileWriter("vystup1.txt")) {
//            for (String subor : subory) {
//                //writer.write(String.format("%s\n", subor));
//                Automaton automaton = new Automaton();
//                f.readText(subor, automaton);
//                DFA dfa = automaton.toDFA();
//
//                double sumaCasov = 0.0;
//                for (int i = 0; i < 55; i++) {
//                    long startTime = System.nanoTime();
//                    dfa.isCompositeCommutativeNondeterministic();
//                    long endTime = System.nanoTime();
//                    double executionTime = (endTime - startTime) / 1e6;
//
//                    if (i >= 5) {
//                        sumaCasov += executionTime;
//                    }
//                }
//                double priemer = sumaCasov / 50.0;
//                writer.write(String.format("%.6f%n", priemer));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}