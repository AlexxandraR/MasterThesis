package org.example;

public class Main {
    public static void main(String[] args) {

        Application app = new Application();


//        EXPERIMENTS
//        prime
//        List<String> subory = List.of("automaton1_1.txt", "automaton1_2.txt", "automaton1_3.txt", "automaton1_4.txt",
//         "automaton1_5.txt", "automaton1_6.txt", "automaton1_7.txt", "automaton1_8.txt", "automaton1_9.txt");
//        composite
//        List<String> subory = List.of("automaton1_10.txt", "automaton1_11.txt", "automaton1_12.txt",
//         "automaton1_13.txt", "automaton1_14.txt", "automaton1_15.txt", "automaton1_16.txt");

        /*
        FileReader f = new FileReader();
        try (FileWriter writer = new FileWriter("vystup1.txt")) {
            for (String subor : subory) {
                //writer.write(String.format("%s\n", subor));
                Automaton automaton = new Automaton();
                f.readText(subor, automaton);
                DFA dfa = automaton.toDFA();

                double sumaCasov = 0.0;
                for (int i = 0; i < 55; i++) {
                    long startTime = System.nanoTime();
                    dfa.isCompositeCommutativeNondeterministic();
                    long endTime = System.nanoTime();
                    double executionTime = (endTime - startTime) / 1e6;

                    if (i >= 5) {
                        sumaCasov += executionTime;
                    }
                }
                double priemer = sumaCasov / 50.0;
                writer.write(String.format("%.6f%n", priemer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

}