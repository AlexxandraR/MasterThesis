package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DFA {
    private Set<String> states;
    private Set<String> alphabet;
    private String initialState;
    private Set<String> acceptStates;
    private Map<Map<String, String>, String> transitionFunction;

    /**
     * Prints the components of the DFA to the standard output.
     * This includes the set of states, input alphabet, initial state,
     * accepting (final) states, and the transition table in a tabular format.
     */
    public void printDFA() {
        System.out.println("States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Initial State: " + initialState);
        System.out.println("Accepting States: " + acceptStates);
        System.out.println("Transition Table:");

        System.out.print("State");
        for (String symbol : alphabet) {
            System.out.print("\t" + symbol);
        }
        System.out.println();

        for (String state : states) {
            System.out.print(state);
            for (String symbol : alphabet) {
                Map<String, String> transitionKey = new HashMap<>();
                transitionKey.put(state, symbol);
                String nextState = transitionFunction.get(transitionKey);
                System.out.print("\t" + (nextState != null ? nextState : "-"));
            }
            System.out.println();
        }
    }

    /**
     *Prints the DFA to the standard output using the same format as the input automaton.
     */
    public void printOutputDFA() {
        System.out.println(initialState);

        for (String state : states) {
            for (String symbol : alphabet) {
                Map<String, String> transitionKey = new HashMap<>();
                transitionKey.put(state, symbol);
                String nextState = transitionFunction.get(transitionKey);
                if(nextState != null){
                    System.out.println(symbol + "," + state + "->" + nextState);
                }
            }
        }
        acceptStates.forEach(System.out::println);
    }

    /**
     * Retrieves a list of rejecting states in the DFA.
     *
     * @return A list of rejecting states.
     */
    private List<String> getRejectingStates(){
        return this.states.stream()
                .filter(state -> !this.acceptStates.contains(state))
                .distinct()
                .toList();
    }

//---------------------------------------------Minimal automaton -------------------------------------------------------

    /**
     * Computes the set of reachable states in the finite automaton from the initial state.
     * A state is considered reachable if it can be accessed through any combination of
     * transition functions starting from the initial state.
     *
     * @return A set of reachable states. If the initial state is not defined, returns null.
     */
    public Set<String> reachableStates() {
        Set<String> S = new HashSet<>();
        if (initialState == null) {
            return null;
        }
        S.add(initialState);
        Set<String> oldS;

        do {
            oldS = new HashSet<>(S);
            for (String state : oldS) {
                for (String symbol : alphabet) {
                    String nextState = transitionFunction.get(Map.of(state, symbol));
                    if (nextState != null) {
                        S.add(nextState);
                    }
                }
            }
        } while (!S.equals(oldS));

        return S;
    }

    /**
     * Removes all unreachable states from the automaton. After calling this method,
     * only the states that are reachable from the initial state will be retained.
     * This operation also removes unreachable transitions and adjusts the set of
     * accepting states accordingly.
     */
    public void removeUnreachableStates() {
        Set<String> reachableStates = reachableStates();

        states.retainAll(reachableStates);

        transitionFunction.entrySet().removeIf(entry ->
                !reachableStates.contains(entry.getKey().keySet().iterator().next()) ||
                        !reachableStates.contains(entry.getValue())
        );

        acceptStates.retainAll(reachableStates);
    }

    /**
     * Partitions the states of the automaton into equivalence classes based on
     * their indistinguishability. The first partition contains accepting states,
     * and the second contains rejecting states. The partitioning continues
     * until no further distinctions between states can be made.
     *
     * @return A list of sets, where each set represents a partition of indistinguishable states.
     */
    private List<Set<String>> partitionStates() {
        List<Set<String>> partitions = new ArrayList<>();
        Set<String> rejectingStates = new HashSet<>(states);
        rejectingStates.removeAll(acceptStates);

        partitions.add(acceptStates);
        partitions.add(rejectingStates);

        boolean changed;

        do {
            changed = false;
            List<Set<String>> newPartitions = new ArrayList<>();

            for (Set<String> group : partitions) {
                Map<String, Set<String>> splitGroups = new HashMap<>();

                for (String state : group) {
                    String signature = "";
                    for (String symbol : alphabet) {
                        String nextState = transitionFunction.get(Map.of(state, symbol));
                        if (nextState != null) {
                            for (int i = 0; i < partitions.size(); i++) {
                                if (partitions.get(i).contains(nextState)) {
                                    signature += i;
                                    break;
                                }
                            }
                        }
                    }

                    splitGroups.computeIfAbsent(signature, k -> new HashSet<>()).add(state);
                }

                newPartitions.addAll(splitGroups.values());

                if (splitGroups.size() > 1) {
                    changed = true;
                }
            }

            partitions = newPartitions;
        } while (changed);

        return partitions;
    }

    /**
     * Minimizes the deterministic finite automaton (DFA) by:
     * 1. Removing unreachable states.
     * 2. Partitioning the states into indistinguishable equivalence classes.
     * 3. Constructing a new minimized DFA based on the partitioned states.
     *
     * @return A new minimized DFA with the same behavior as the original DFA but with fewer states.
     */
    public DFA minimize() {
        if(this.getInitialState() == null){
            return new DFA(
                    Set.of("q0"),
                    alphabet,
                    null,
                    Set.of("q0"),
                    Map.of()
            );
        }
        //1.
        removeUnreachableStates();

        //2.
        List<Set<String>> partitions = partitionStates();

        //3.
        Map<Map<String, String>, String> minimizedTransitions = new HashMap<>();
        Map<String, String> stateMapping = new HashMap<>();
        int index = 0;

        for (Set<String> group : partitions) {
            String newState = "q" + index++;
            for (String oldState : group) {
                stateMapping.put(oldState, newState);
            }
        }

        for (Map.Entry<Map<String, String>, String> entry : transitionFunction.entrySet()) {
            String fromState = entry.getKey().keySet().iterator().next();
            String symbol = entry.getKey().values().iterator().next();
            String toState = entry.getValue();

            String newFromState = stateMapping.get(fromState);
            String newToState = stateMapping.get(toState);

            if (newFromState != null && newToState != null) {
                minimizedTransitions.put(Map.of(newFromState, symbol), newToState);
            }
        }

        Set<String> minimizedAcceptingStates = new HashSet<>();
        for (String acceptingState : acceptStates) {
            minimizedAcceptingStates.add(stateMapping.get(acceptingState));
        }

        String minimizedInitialState = stateMapping.get(initialState);

        return new DFA(
                new HashSet<>(stateMapping.values()),
                alphabet,
                minimizedInitialState,
                minimizedAcceptingStates,
                minimizedTransitions
        );
    }

//----------------------------------------------Complete automaton------------------------------------------------------

    /**
     * Checks if the deterministic finite automaton (DFA) is complete.
     * A DFA is considered complete if for every state and every symbol in the alphabet,
     * there is a defined transition to another state.
     *
     * @return true if the DFA is complete, false otherwise.
     */
    public boolean isComplete() {
        for (String state : states) {
            for (String symbol : alphabet) {
                if (transitionFunction.get(Map.of(state, symbol)) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Helper method to check if a given state has any outgoing transitions.
     * A state is considered to have outgoing transitions if there is at least
     * one transition from the state on any symbol in the alphabet.
     *
     * @param state The state to check for outgoing transitions.
     * @return true if the state has outgoing transitions, false otherwise.
     */
    private boolean hasOutgoingTransitions(String state) {
        for (Map.Entry<Map<String, String>, String> entry : transitionFunction.entrySet()) {
            String fromState = entry.getKey().keySet().iterator().next();
            if (fromState.equals(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Completes the deterministic finite automaton (DFA) by ensuring that for every state
     * and every symbol in the alphabet, there is a defined transition. If a state lacks
     * transitions for certain symbols, a "sink" state is introduced. This "sink" state has
     * self-looping transitions for all symbols and ensures the DFA becomes complete.
     *
     * @return A new DFA that is complete. If the original DFA was already complete, it returns the original DFA.
     */
    public DFA completeDFA() {
        if (isComplete()) {
            return this;
        }

        String sinkState = "sink";
        boolean sinkNeeded = false;

        Set<String> newStates = new HashSet<>(states);
        Map<Map<String, String>, String> newTransitions = new HashMap<>(transitionFunction);

        for (String state : states) {
            for (String symbol : alphabet) {
                String transitionTarget = transitionFunction.get(Map.of(state, symbol));

                if (transitionTarget == null) {
                    if ((acceptStates == null || !acceptStates.contains(state)) && !hasOutgoingTransitions(state)) {
                        newTransitions.put(Map.of(state, symbol), state);
                    } else {
                        newTransitions.put(Map.of(state, symbol), sinkState);
                        sinkNeeded = true;
                    }
                }
            }
        }

        if (sinkNeeded) {
            newStates.add(sinkState);

            for (String symbol : alphabet) {
                newTransitions.put(Map.of(sinkState, symbol), sinkState);
            }
        }

        return new DFA(newStates, alphabet, initialState, acceptStates, newTransitions);
    }

//------------------------------------------Permutation automaton-------------------------------------------------------

    /**
     * Checks if the DFA's transition function represents a permutation for each symbol in the alphabet.
     * A DFA transition function is considered a permutation if, for each symbol, every state maps to a unique next state,
     * meaning there are no duplicate transitions and each state is reached exactly once.
     *
     * @return true if the transition function is a permutation for each symbol, false otherwise.
     */
    public boolean isPermutation() {
        for (String symbol : alphabet) {
            Set<String> S = new HashSet<>();

            for (String state : states) {
                String nextState = transitionFunction.get(Map.of(state, symbol));
                if (nextState != null) {
                    if (S.contains(nextState)) {
                        return false;
                    } else {
                        S.add(nextState);
                    }
                }
            }

            if (S.size() != states.size()) {
                return false;
            }
        }

        return true;
    }

//--------------------------------------------Commutative automaton-----------------------------------------------------

    /**
     * Checks whether the DFA is commutative.
     * Implements an algorithm that verifies the existence of specific transition behaviors
     * that contradict the commutative property, by relying on methods from the Pattern Logic.
     *
     * @return true if the DFA is commutative; false otherwise.
     */
    public boolean isCommutative() {
        for (String q : states) {
            for (String qu : states) {
                for (String qv : states) {
                    for (String quv : states) {
                        for (String qvu : states) {

                            DFA A1 = new DFA(this.states, this.getAlphabet(), this.initialState, this.acceptStates,
                                    this.transitionFunction);
                            A1.setInitialState(q);
                            A1.setAcceptStates(Set.of(qu));

                            DFA A2 = new DFA(this.states, this.getAlphabet(), this.initialState, this.acceptStates,
                                    this.transitionFunction);
                            A2.setInitialState(qv);
                            A2.setAcceptStates(Set.of(qvu));

                            DFA Au = product(A1, A2);

                            DFA A11 = new DFA(this.states, this.getAlphabet(), this.initialState, this.acceptStates,
                                    this.transitionFunction);
                            A11.setInitialState(q);
                            A11.setAcceptStates(Set.of(qv));

                            DFA A22 = new DFA(this.states, this.getAlphabet(), this.initialState, this.acceptStates,
                                    this.transitionFunction);
                            A22.setInitialState(qu);
                            A22.setAcceptStates(Set.of(quv));

                            DFA Av = product(A11, A22);

                            if (Au.isAcceptStateReachable() && Av.isAcceptStateReachable() && !quv.equals(qvu)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

//--------------------------------------------isPrime_general_algorithm-------------------------------------------------

    /**
     * Generates all possible deterministic finite automata (DFAs) with a given number of states.
     * The method creates all possible transition functions and valid accepting state combinations,
     * then checks whether the language of the original DFA is a subset of the language
     * of the generated DFA. Only valid DFAs are returned.
     *
     * @param numStates The number of states to generate for the DFAs.
     * @return A set of valid DFAs.
     */
    public Set<DFA> generateAllDFAs(int numStates) {
        Set<DFA> validDFAs = new HashSet<>();

        Set<String> states = new HashSet<>();
        for (int i = 0; i < numStates; i++) {
            states.add("q" + i);
        }

        for (int acceptMask = 1; acceptMask < (1 << numStates); acceptMask++) {
            Set<String> acceptStates = new HashSet<>();
            int stateIndex = 0;
            for (String state : states) {
                if ((acceptMask & (1 << stateIndex)) != 0) {
                    acceptStates.add(state);
                }
                stateIndex++;
            }

            List<Map<Map<String, String>, String>> transitionList = generateAllTransitionFunctions(states, alphabet);

            for (Map<Map<String, String>, String> transitionFunction : transitionList) {
                DFA newDFA = new DFA(states, alphabet, "q0", acceptStates, transitionFunction);

                newDFA.flipAcceptStates();
                DFA finalDfa = product(this, newDFA);
                newDFA.flipAcceptStates();

                if (!finalDfa.isAcceptStateReachable()) {
                    validDFAs.add(newDFA);
                }
            }
        }
        return validDFAs;
    }

    /**
     * Helper method to generate all possible transition functions for a given set of states and alphabet.
     * The method iterates over all possible combinations of state transitions to create a list of
     * transition functions for DFAs.
     *
     * @param states   The set of DFA states.
     * @param alphabet The alphabet used for transitions.
     * @return A list of all possible transition functions.
     */
    public List<Map<Map<String, String>, String>> generateAllTransitionFunctions(Set<String> states, Set<String> alphabet) {
        List<Map<Map<String, String>, String>> transitionList = new ArrayList<>();
        int numStates = states.size();
        int totalCombinations = (int) Math.pow(numStates, numStates * alphabet.size());

        List<String> stateList = new ArrayList<>(states);

        for (int i = 0; i < totalCombinations; i++) {
            Map<Map<String, String>, String> transitionFunction = new HashMap<>();
            int index = i;

            for (String state : stateList) {
                for (String symbol : alphabet) {
                    Map<String, String> transitionKey = new HashMap<>();
                    transitionKey.put(state, symbol);
                    String nextState = stateList.get(index % numStates);
                    transitionFunction.put(transitionKey, nextState);
                    index /= numStates;
                }
            }

            transitionList.add(transitionFunction);
        }

        return transitionList;
    }

    /**
     * Generates the set alpha(A), which consists of smaller DFAs that accept the language of the original DFA.
     *
     * @return A set of smaller DFAs that accept the language of the original DFA.
     */
    public Set<DFA> getAlphaDFA() {
        Set<DFA> alphaDFA = new HashSet<>();

        for (int numStates = 1; numStates < states.size(); numStates++) {
            Set<DFA> generatedDFAs = generateAllDFAs(numStates);
            generatedDFAs.forEach(dfa -> alphaDFA.add(dfa.minimize().completeDFA()));
            for (DFA dfa : generatedDFAs) {
                System.out.println("\nGenerated DFA:");
                dfa.printDFA();
            }
        }
        return alphaDFA;
    }

    /**
     * Renames states in the provided DFA to avoid conflicts with the states in this DFA.
     * This method checks for any overlapping state names between this DFA and the provided DFA.
     * If there are conflicts, it renames the states in DFA from parameter by appending a
     * specified prefix to each conflicting state name. The method also updates provided DFA´s
     * transition function, initial state and accept states to reflect the new names.
     *
     * @param otherDFA The DFA whose states will be renamed in case of conflicts with this DFA.
     */
    public void renameStatesIfConflict(DFA otherDFA) {
        Set<String> commonStates = new HashSet<>(this.states);
        commonStates.retainAll(otherDFA.states);

        if (!commonStates.isEmpty()) {
            String prefix = "_1";

            Set<String> renamedStates = new HashSet<>();
            Map<Map<String, String>, String> renamedTransitionFunction = new HashMap<>();

            for (String state : otherDFA.states) {
                String newStateName = commonStates.contains(state) ? state + prefix : state;
                renamedStates.add(newStateName);

                for (Map.Entry<Map<String, String>, String> entry : otherDFA.transitionFunction.entrySet()) {
                    Map<String, String> key = entry.getKey();
                    String symbol = key.values().iterator().next();
                    String fromState = key.keySet().iterator().next();

                    Map<String, String> newKey = Map.of(
                            commonStates.contains(fromState) ? fromState + prefix : fromState,
                            symbol
                    );
                    String toState = entry.getValue();
                    renamedTransitionFunction.put(newKey, commonStates.contains(toState) ? toState + prefix : toState);
                }
            }

            otherDFA.states = renamedStates;
            otherDFA.transitionFunction = renamedTransitionFunction;

            if (commonStates.contains(otherDFA.initialState)) {
                otherDFA.initialState = otherDFA.initialState + prefix;
            }

            Set<String> renamedAcceptStates = new HashSet<>();
            for (String acceptState : otherDFA.acceptStates) {
                renamedAcceptStates.add(commonStates.contains(acceptState) ? acceptState + prefix : acceptState);
            }
            otherDFA.acceptStates = renamedAcceptStates;
        }
    }

    /**
     * Computes the product of two DFAs.
     * The product DFA accepts the language that is the intersection of the languages accepted by input DFAs.
     *
     * @param dfa1 The first DFA.
     * @param dfa2 The second DFA.
     * @return The product of the two DFAs as a new DFA.
     */
    public static DFA product(DFA dfa1, DFA dfa2) {
        if (dfa1.getAlphabet() != dfa2.getAlphabet()) {
            throw new IllegalArgumentException("DFAs must have the same alphabet");
        }

        dfa1.renameStatesIfConflict(dfa2);

        Map<Map<String, String>, String> dfaTransitions = new HashMap<>();
        Set<String> q0Closure = new HashSet<>();
        q0Closure.add(dfa2.getInitialState());
        q0Closure.add(dfa1.getInitialState());
        List<Set<String>> qDone = new ArrayList<>();
        List<Set<String>> qList = new ArrayList<>();
        qList.add(q0Closure);

        while (!qList.isEmpty()) {
            Set<String> state = qList.get(0);
            qDone.add(state);
            qList.remove(0);

            for (String symbol : dfa1.getAlphabet()) {
                Set<String> transitions = new HashSet<>();

                for (String i : state) {
                    Map<String, String> key = Map.of(i, symbol);
                    String transition1 = dfa1.getTransitionFunction().get(key);
                    String transition2 = dfa2.getTransitionFunction().get(key);
                    if (transition1 != null) {
                        transitions.add(transition1);
                    }
                    if (transition2 != null) {
                        transitions.add(transition2);
                    }
                }

                if (!transitions.isEmpty()) {
                    dfaTransitions.put(
                            Map.of(sortedStateString(state), symbol),
                            sortedStateString(transitions)
                    );

                    if (!qDone.contains(transitions) && !qList.contains(transitions)) {
                        qList.add(transitions);
                    }
                }
            }
        }

        Set<String> allStates = new HashSet<>();
        for (Map.Entry<Map<String, String>, String> entry : dfaTransitions.entrySet()) {
            allStates.add(entry.getKey().keySet().iterator().next());
            allStates.add(entry.getValue());
        }

        Set<String> acceptingStates = new HashSet<>();
        for (String state : allStates) {
            boolean counter = true;
            Set<String> qStateSet = evalState(state);
            for (String qState : qStateSet) {
                if (!dfa1.getAcceptStates().contains(qState) && !dfa2.getAcceptStates().contains(qState)) {
                    counter = false;
                    break;
                }
            }
            if (counter) {
                acceptingStates.add(state);
            }
        }
        return new DFA(allStates, dfa1.alphabet, sortedStateString(q0Closure), acceptingStates, dfaTransitions);
    }

    /**
     * Sorts a set of state names and returns them as a comma-separated string.
     *
     * @param stateSet the set of states to sort
     * @return a sorted comma-separated string of state names
     */
    private static String sortedStateString(Set<String> stateSet) {
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
    private static Set<String> evalState(String stateString) {
        return new HashSet<>(Arrays.asList(stateString.split(",")));
    }

    /**
     * Computes the product of a set of deterministic finite automata (DFAs).
     * The resulting DFA accepts the intersection of the languages accepted by all DFAs in the set.
     *
     * @param dfas A set of DFAs to product.
     * @return The product of all DFAs in the set as a new DFA.
     * @throws IllegalArgumentException if the set of DFAs is empty.
     */
    public static DFA product(Set<DFA> dfas) {
        Iterator<DFA> iterator = dfas.iterator();

        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("The set of DFAs cannot be empty");
        }

        DFA result = iterator.next();
        iterator.remove();

        while (iterator.hasNext()) {
            DFA nextDFA = iterator.next();
            result = product(result, nextDFA);
            iterator.remove();
        }

        return result;
    }

    /**
     * Computes the "roof" DFA, which is the product of all DFAs in the alpha(A) set.
     * This DFA represents the product of all DFAs that accept the language of the original DFA.
     *
     * @return The roof DFA representing the product of all smaller DFAs.
     * @throws IllegalArgumentException if the alphaDFA set is empty.
     */
    public DFA getRoofDFA() {
        Set<DFA> alphaDFA = getAlphaDFA();
        if (alphaDFA.isEmpty()) {
            return new DFA();
        }

        return DFA.product(alphaDFA);
    }

    /**
     * Flips the accept states of the DFA.
     * States that were previously accepting become rejecting, and rejecting states become accepting.
     * This is useful for complementing the DFA's language.
     */
    public void flipAcceptStates() {
        Set<String> newAcceptStates = new HashSet<>(states);
        newAcceptStates.removeAll(acceptStates);

        this.acceptStates = newAcceptStates;
    }

    /**
     * Checks if any accept state of the DFA is reachable from the start state.
     * The method performs a breadth-first search (BFS) to determine whether there is a path from the initial state
     * to any of the accepting states.
     *
     * @return true if at least one accept state is reachable, false otherwise.
     */
    public boolean isAcceptStateReachable() {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(initialState);
        visited.add(initialState);

        while (!queue.isEmpty()) {
            String currentState = queue.poll();

            if (acceptStates.contains(currentState)) {
                return true;
            }

            for (String symbol : alphabet) {
                String nextState = transitionFunction.get(Map.of(currentState, symbol));
                if (nextState != null && !visited.contains(nextState)) {
                    visited.add(nextState);
                    queue.add(nextState);
                }
            }
        }

        return false;
    }

    /**
     * Checks whether the DFA is a prime DFA.
     * Implements the algorithm using the roof of DFA for general DFAs.
     *
     * @return true if the DFA is prime, false otherwise.
     */
    public boolean isPrimeDFA() {
        DFA roof = getRoofDFA();
        if (roof.getStates() == null && roof.getTransitionFunction() == null) {
            return true;
        }
        roof.flipAcceptStates();
        DFA finalDfa = product(this, roof);

        return !finalDfa.isAcceptStateReachable();
    }

//--------------------------------------------isComposite - Memory------------------------------------------------------

    /**
     * Determines whether the permutation automaton is composite.
     * This algorithm generates the rejecting states of the orbit-DFA and records the covered rejecting states
     * of the original automaton. It also stores all possible initial states of processed orbit-DFAs
     * to prevent redundant regeneration of the same orbit.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isCompositeMemory() {
        List<String> rejectingStates = this.getRejectingStates();

        if (rejectingStates.size() <= 1) {
            return false;
        }

        Set<String> covered = new HashSet<>();
        Set<Set<String>> processedOrbits = new HashSet<>();
        for (int size = 2; size <= rejectingStates.size(); size++) {
            if (generateCombinationMemory(rejectingStates, size, 0, new HashSet<>(), processedOrbits, covered)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively generates the rejecting states of orbit-DFAs by exploring subsets of states.
     * The method iterates through possible subsets, considering the target size and current state configuration.
     * It ensures that all rejecting states are covered, and avoids redundant calculations
     * by storing previously processed orbits.
     *
     * @param rejectingStates The set of rejecting states.
     * @param size The target size of the subset to be generated.
     * @param index The current index in the subset of states.
     * @param current The current subset of states being considered for the orbit-DFA.
     * @param processedOrbits  A set of initial states from previously processed orbits to avoid redundant generation.
     * @param covered A set of states that are covered during the generation process.
     *
     * @return True if all rejecting states are covered; false otherwise.
     */
    private boolean generateCombinationMemory(List<String> rejectingStates, int size, int index, Set<String> current,
                                              Set<Set<String>> processedOrbits, Set<String> covered) {
        if (current.size() == size) {
            if (!processedOrbits.contains(current)) {
                covered.addAll(coverMemory(new HashSet<>(current), processedOrbits));
                return covered.size() == rejectingStates.size();
            }
            return false;
        }

        for (int i = index; i < rejectingStates.size(); i++) {
            current.add(rejectingStates.get(i));
            if (generateCombinationMemory(rejectingStates, size, i + 1, current, processedOrbits, covered)) {
                return true;
            }
            current.remove(rejectingStates.get(i));
        }
        return false;
    }

    /**
     * Computes the set of states covered by the orbit-DFA defined by the rejecting state
     * and stores all states of the orbit-DFA.
     *
     * @param U The set containing the generated rejecting state used to construct the orbit-DFA.
     * @param processedOrbits A set of already processed orbits to prevent redundant generation.
     * @return The set of covered rejecting states; otherwise, returns an empty set.
     */
    public Set<String> coverMemory(Set<String> U, Set<Set<String>> processedOrbits) {
        Set<Set<String>> CU = new HashSet<>();
        CU.add(U);

        if (expandOrbitsMemory(CU, processedOrbits) && CU.stream().anyMatch(subset -> subset
                .contains(this.getInitialState()))) {
            return CU.stream()
                    .filter(subset -> subset.stream().noneMatch(this.acceptStates::contains))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * Determines whether the orbit generated from the given state covers any rejecting states
     * of the original automaton.
     * The method constructs the complete orbit-DFA by exhaustively applying all transitions from the initial state.
     * If the generated orbit is smaller than the original automaton, it may cover some rejecting states.
     * Conversely, if the orbit is larger, it does not cover any states.
     *
     * @param CU A set containing the rejecting state used to generate the orbit-DFA.
     * @param processedOrbits A set of previously processed orbits to prevent redundant computations.
     *
     * @return True if the generated orbit-DFA covers any states of the original automaton; false otherwise.
     */
    private boolean expandOrbitsMemory(Set<Set<String>> CU, Set<Set<String>> processedOrbits) {
        Set<Set<String>> addedSets = new HashSet<>(CU);

        while (true) {
            addedSets = addedSets.stream()
                    .flatMap(S -> this.getAlphabet().stream()
                            .map(sigma -> S.stream()
                                    .map(state -> this.getTransitionFunction().getOrDefault(Map.of(state, sigma),
                                            null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet())))
                    .filter(set -> !set.isEmpty())
                    .collect(Collectors.toSet());

            processedOrbits.addAll(addedSets);
            if (!CU.addAll(addedSets)) break;

            if (CU.size() >= this.getStates().size()) {
                return false;
            }
        }
        return true;
    }

//--------------------------------------------isComposite - Time--------------------------------------------------------

    /**
     * Determines whether the permutation automaton is composite.
     * This algorithm generates the rejecting states of the orbit-DFA and records the covered rejecting states
     * of the original automaton.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isCompositeTime() {
        List<String> rejectingStates = this.getRejectingStates();

        if (rejectingStates.size() <= 1) {
            return false;
        }

        Set<String> covered = new HashSet<>();
        for (int size = 2; size <= rejectingStates.size(); size++) {
            if (generateCombinationTime(rejectingStates, size, 0, new HashSet<>(), covered)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively generates the rejecting states of orbit-DFAs by exploring subsets of rejecting states.
     * The method iterates through possible subsets, considering the target size and current state configuration.
     * It ensures that all rejecting states of original automaton are covered.
     *
     * @param size The target size of the subset to be generated.
     * @param index The current index in the subset of states.
     * @param current The current subset of states being considered for the orbit-DFA.
     * @param covered A set of states that are covered during the generation process.
     * @param rejectingStates The set of rejecting states.
     *
     * @return True if all rejecting states are covered; false otherwise.
     */
    private boolean generateCombinationTime(List<String> rejectingStates, int size, int index, Set<String> current,
                                            Set<String> covered) {
        if (current.size() == size) {
            covered.addAll(coverTime(new HashSet<>(current)));
            return covered.size() == rejectingStates.size();
        }

        for (int i = index; i < rejectingStates.size(); i++) {
            current.add(rejectingStates.get(i));
            if (generateCombinationTime(rejectingStates, size, i + 1, current, covered)) {
                return true;
            }
            current.remove(rejectingStates.get(i));
        }
        return false;
    }

    /**
     * Computes the set of covered rejecting states.
     *
     * @param U The set containing state used to generate the orbit-DFA.
     * @return The set of covered states.
     */
    public Set<String> coverTime(Set<String> U) {
        Set<Set<String>> CU = new HashSet<>();
        CU.add(U);

        if (expandOrbitsTime(CU) && CU.stream().anyMatch(subset -> subset.contains(this.getInitialState()))) {
            return CU.stream()
                    .filter(subset -> subset.stream().noneMatch(this.acceptStates::contains))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * Determines whether the orbit generated from the given state covers any rejecting states
     * of the original automaton.
     * The method constructs the complete orbit-DFA by exhaustively applying all transitions from the initial state.
     * If the generated orbit is smaller than the original automaton, it may cover some rejecting states.
     * Conversely, if the orbit is larger, it does not cover any states.
     *
     * @param CU A set containing the state used to generate the orbit-DFA.
     *
     * @return True if the generated orbit-DFA covers any states of the original automaton; false otherwise.
     */
    private boolean expandOrbitsTime(Set<Set<String>> CU) {
        Set<Set<String>> addedSets = new HashSet<>(CU);

        while (true) {
            addedSets = addedSets.stream()
                    .flatMap(S -> this.getAlphabet().stream()
                            .map(sigma -> S.stream()
                                    .map(state -> this.getTransitionFunction().getOrDefault(Map.of(state, sigma),
                                            null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet())))
                    .filter(set -> !set.isEmpty())
                    .collect(Collectors.toSet());

            if (!CU.addAll(addedSets)) break;

            if (CU.size() >= this.getStates().size()) {
                return false;
            }
        }
        return true;
    }

//--------------------------------------------isComposite - Original----------------------------------------------------

    /**
     * Determines whether the permutation automaton is composite.
     * It iterates over rejecting states, checking if they can be covered.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isComposite() {
        List<String> rejectingStates = this.getRejectingStates();

        if (rejectingStates.size() <= 1) {
            return false;
        }

        for (String p : rejectingStates) {
            int flag = 0;
            List<String> filteredStates = new ArrayList<>(rejectingStates);
            filteredStates.remove(p);
            for (int size = 2; size <= rejectingStates.size(); size++) {
                if (generateCombination(filteredStates, size, 0, new HashSet<>(Set.of(p))) == 1) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recursively generates the states of orbit-DFAs by exploring subsets of states.
     * The method iterates through possible subsets containing chosen rejecting state,
     * considering the target size and the current state configuration.
     * It ensures that the rejecting state is covered.
     *
     * @param size The target size of the subset to be generated.
     * @param index The current index in the subset of states.
     * @param current The current subset of states being considered for the orbit-DFA.
     * @param rejectingStates The list of rejecting states.
     *
     * @return True if the rejecting state is covered; false otherwise.
     */
    private int generateCombination(List<String> rejectingStates, int size, int index, Set<String> current) {
        if (current.size() == size) {
            if (cover(current)) {
                return 1;
            }
            return 0;
        }
        for (int i = index; i < rejectingStates.size(); i++) {
            current.add(rejectingStates.get(i));
            if (generateCombination(rejectingStates, size, i + 1, current) == 1) {
                return 1;
            }
            current.remove(rejectingStates.get(i));
        }
        return 0;
    }

    /**
     * Checks whether the orbit-DFA generated from the given subset of states covers the states.
     *
     * @param U The set containing the state used to generate the orbit-DFA.
     * @return True if the orbit-DFA generated from the subset covers the rejecting state; false otherwise.
     */
    public boolean cover(Set<String> U) {
        Set<Set<String>> CU = new HashSet<>();
        CU.add(U);

        return expandOrbits(CU) && CU.stream().anyMatch(subset -> subset.contains(this.getInitialState()));
    }

    /**
     * Determines whether the orbit generated from the given state covers the rejecting state
     * of the original automaton.
     * The method constructs the complete orbit-DFA by exhaustively applying all transitions from the state.
     * If the generated orbit is smaller than the original automaton, it covers the rejecting state.
     * Conversely, if the orbit is larger, it does not cover the state.
     *
     * @param CU A set containing the state used to generate the orbit-DFA.
     *
     * @return True if the generated orbit-DFA covers the chosen state of the original automaton; false otherwise.
     */
    private boolean expandOrbits(Set<Set<String>> CU) {
        Set<Set<String>> addedSets = new HashSet<>(CU);

        while (true) {
            addedSets = addedSets.stream()
                    .flatMap(S -> this.getAlphabet().stream()
                            .map(sigma -> S.stream()
                                    .map(state -> this.getTransitionFunction().getOrDefault(Map.of(state, sigma),
                                            null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet())))
                    .filter(set -> !set.isEmpty())
                    .collect(Collectors.toSet());

            if (!CU.addAll(addedSets)) break;

            if (CU.size() >= this.getStates().size()) {
                return false;
            }
        }
        return true;
    }

//--------------------------------------------isComposite - Initial-----------------------------------------------------

    /**
     * Determines whether the permutation automaton is composite.
     * This algorithm generates the initial states of the orbit-DFA and records the covered rejecting states
     * of the original automaton.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isCompositeInitial() {
        List<String> statesWithoutInitial = new ArrayList<>(this.states.stream().toList());
        statesWithoutInitial.remove(this.initialState);

        int rejectingStatesSize = this.getRejectingStates().size();

        if (rejectingStatesSize <= 1) {
            return false;
        }

        Set<String> covered = new HashSet<>();
        for (int size = 2; size <= rejectingStatesSize; size++) {
            if (generateCombinationInitial(statesWithoutInitial, size, 0,
                    new HashSet<>(Set.of(this.initialState)), covered, rejectingStatesSize)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively generates the initial states of orbit-DFAs by exploring subsets of states.
     * The method iterates through possible subsets containing initial state of the original automaton,
     * considering the target size and the current state configuration.
     * It ensures that all rejecting states are covered.
     *
     * @param statesWithoutInitial A list of states excluding the initial state.
     * @param size The target size of the subset to be generated.
     * @param index The current index in the subset of states.
     * @param current The current subset of states being considered for the orbit-DFA.
     * @param covered A set of states that are covered during the generation process.
     * @param rejectingStatesSize The number of rejecting states.
     *
     * @return True if all rejecting states are covered; false otherwise.
     */
    private boolean generateCombinationInitial(List<String> statesWithoutInitial, int size, int index,
                                               Set<String> current, Set<String> covered, int rejectingStatesSize) {
        if (current.size() == size) {
            covered.addAll(coverInitial(new HashSet<>(current)));
            return covered.size() == rejectingStatesSize;
        }

        for (int i = index; i < statesWithoutInitial.size(); i++) {
            current.add(statesWithoutInitial.get(i));
            if (generateCombinationInitial(statesWithoutInitial, size, i + 1, current, covered,
                    rejectingStatesSize)) {
                return true;
            }
            current.remove(statesWithoutInitial.get(i));
        }
        return false;
    }

    /**
     * Computes the set of states covered by the orbit-DFA defined by its initial state.
     *
     * @param U The set containing the generated initial state used to construct the orbit-DFA.
     * @return The set of covered rejecting states; otherwise, returns an empty set.
     */
    public Set<String> coverInitial(Set<String> U) {
        Set<Set<String>> CU = new HashSet<>();
        CU.add(U);

        if (expandOrbitsInitial(CU)) {
            return CU.stream()
                    .filter(subset -> subset.stream().noneMatch(this.acceptStates::contains))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * Determines whether the orbit generated from the given initial state covers any rejecting states
     * of the original automaton.
     * The method constructs the complete orbit-DFA by exhaustively applying all transitions from the initial state.
     * If the generated orbit is smaller than the original automaton, it may cover some rejecting states.
     * Conversely, if the orbit is larger, it does not cover any states.
     *
     * @param CU A set containing the initial state used to generate the orbit-DFA.
     *
     * @return True if the generated orbit-DFA covers any states of the original automaton; false otherwise.
     */
    private boolean expandOrbitsInitial(Set<Set<String>> CU) {
        Set<Set<String>> addedSets = new HashSet<>(CU);

        while (true) {
            addedSets = addedSets.stream()
                    .flatMap(S -> this.getAlphabet().stream()
                            .map(sigma -> S.stream()
                                    .map(state -> this.getTransitionFunction().getOrDefault(Map.of(state, sigma),
                                            null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet())))
                    .filter(set -> !set.isEmpty())
                    .collect(Collectors.toSet());

            if (!CU.addAll(addedSets)) break;

            if (CU.size() >= this.getStates().size()) {
                return false;
            }
        }
        return true;
    }

//--------------------------------------------isComposite - InitialMemory-----------------------------------------------

    /**
     * Determines whether the permutation automaton is composite.
     * This algorithm generates the initial states of the orbit-DFA and records the covered rejecting states
     * of the original automaton. It also stores all possible initial states of processed orbit-DFAs
     * to prevent redundant regeneration of the same orbit.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isCompositeInitialMemory() {
        List<String> statesWithoutInitial = new ArrayList<>(this.states.stream().toList());
        statesWithoutInitial.remove(this.initialState);

        int rejectingStatesSize = this.getRejectingStates().size();

        if (rejectingStatesSize <= 1) {
            return false;
        }

        Set<String> covered = new HashSet<>();
        for (int size = 2; size <= rejectingStatesSize; size++) {
            if (generateCombinationInitialMemory(statesWithoutInitial, size, 0,
                    new HashSet<>(Set.of(this.initialState)), new HashSet<>(), covered, rejectingStatesSize)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively generates the initial states of orbit-DFAs by exploring subsets of states.
     * The method iterates through possible subsets containing initial state of the original automaton,
     * considering the target size and current state configuration.
     * It ensures that all rejecting states are covered, and avoids redundant calculations
     * by storing previously processed orbits.
     *
     * @param statesWithoutInitial A list of states excluding the initial state.
     * @param size The target size of the subset to be generated.
     * @param index The current index in the subset of states.
     * @param current The current subset of states being considered for the orbit-DFA.
     * @param processedOrbits  A set of initial states from previously processed orbits to avoid redundant generation.
     * @param covered A set of states that are covered during the generation process.
     *
     * @return True if all rejecting states are covered; false otherwise.
     */
    private boolean generateCombinationInitialMemory(List<String> statesWithoutInitial, int size, int index,
                                                     Set<String> current, Set<Set<String>> processedOrbits,
                                                     Set<String> covered, int rejectingStatesSize) {
        if (current.size() == size) {
            if (!processedOrbits.contains(current)) {
                covered.addAll(coverInitialMemory(new HashSet<>(current), processedOrbits));
                return covered.size() == rejectingStatesSize;
            }
            return false;
        }

        for (int i = index; i < statesWithoutInitial.size(); i++) {
            current.add(statesWithoutInitial.get(i));
            if (generateCombinationInitialMemory(statesWithoutInitial, size, i + 1, current, processedOrbits,
                    covered, rejectingStatesSize)) {
                return true;
            }
            current.remove(statesWithoutInitial.get(i));
        }
        return false;
    }

    /**
     * Computes the set of states covered by the orbit-DFA defined by its initial state
     * and stores all possible initial states of the orbit-DFA.
     *
     * @param U The set containing the generated initial state used to construct the orbit-DFA.
     * @param processedOrbits A set of already processed orbits to prevent redundant generation.
     * @return The set of covered rejecting states; otherwise, returns an empty set.
     */
    public Set<String> coverInitialMemory(Set<String> U, Set<Set<String>> processedOrbits) {
        Set<Set<String>> CU = new HashSet<>();
        CU.add(U);

        if (expandOrbitsInitialMemory(CU, processedOrbits)) {
            return CU.stream()
                    .filter(subset -> subset.stream().noneMatch(this.acceptStates::contains))
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }

    /**
     * Determines whether the orbit generated from the given initial state covers any rejecting states
     * of the original automaton.
     * The method constructs the complete orbit-DFA by exhaustively applying all transitions from the initial state.
     * If the generated orbit is smaller than the original automaton, it may cover some rejecting states.
     * Conversely, if the orbit is larger, it does not cover any states.
     *
     * @param CU A set containing the initial state used to generate the orbit-DFA.
     * @param processedOrbits A set of previously processed orbits to prevent redundant computations.
     *
     * @return True if the generated orbit-DFA covers any states of the original automaton; false otherwise.
     */
    private boolean expandOrbitsInitialMemory(Set<Set<String>> CU, Set<Set<String>> processedOrbits) {
        Set<Set<String>> addedSets = new HashSet<>(CU);

        while (true) {
            addedSets = addedSets.stream()
                    .flatMap(S -> this.getAlphabet().stream()
                            .map(sigma -> S.stream()
                                    .map(state -> this.getTransitionFunction().getOrDefault(Map.of(state, sigma),
                                            null))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet())))
                    .filter(set -> !set.isEmpty())
                    .collect(Collectors.toSet());

            processedOrbits.addAll(addedSets.stream().filter(state -> state.contains(this.initialState))
                    .collect(Collectors.toSet()));
            if (!CU.addAll(addedSets)) break;

            if (CU.size() >= this.getStates().size()) {
                return false;
            }
        }
        return true;
    }

//---------------------------------isComposite - Commutative - Deterministic--------------------------------------------
    /**
     * Determines whether the commutative permutation automaton is composite.
     * The algorithm attempts to find a word that covers all rejecting states of the DFA.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isCompositeCommutative(){
        List<String> rejectingStates = this.getRejectingStates();
        for(String p : rejectingStates){
            boolean cover_found = false;
            for(String q : rejectingStates){
                if(!Objects.equals(p, q) && coverCommutative(p, q)){
                    cover_found = true;
                    break;
                }
            }
            if(!cover_found){
                return false;
            }
        }
        return !rejectingStates.isEmpty();
    }

    /**
     * Determines whether the state p is covered, i.e., whether there exists a word
     * that leads from state p to the target state q, such that no state encountered
     * during any number of repeated applications of this word is an accepting state.
     *
     * @param p The state whose coverage is being analyzed.
     * @param q The target state that should be reached after processing the input from state p.
     *
     * @return True if the state p is covered, false otherwise.
     */
    private boolean coverCommutative(String p, String q){
        String s = q;
        List<Integer> powers = new ArrayList<>(java.util.Collections.nCopies(this.getAlphabet().size(), 0));
        while(!Objects.equals(s, p)){
            s = mimic(powers, 0, 0, p, q, s);
            if(s == null || this.getAcceptStates().contains(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * Constructs a word based on a list of exponents, where each exponent represents
     * the number of repetitions of the corresponding alphabet letter.
     *
     * @param exponents A list of integers indicating how many times each letter should appear in the word.
     *
     * @return A list of letters forming the constructed word.
     */
    private List<String> createWord(List<Integer> exponents){
        List<String> word = new ArrayList<>();
        int index = 0;
        for(String letter : this.getAlphabet()){
            for(int i = 0; i < exponents.get(index); i++){
                word.add(letter);
            }
            index++;
        }
        return word;
    }

    /**
     * Generates all possible exponent combinations to construct candidate words under the assumption of commutativity.
     *
     * @param powers A list of exponents representing how many times each letter from the alphabet is used.
     * @param index The index of the current letter in the alphabet.
     * @param sum The number of letters used so far.
     * @param p The rejecting state being checked for coverage.
     * @param q The target state the automaton should reach after processing the generated word from state p.
     * @param s An auxiliary state used during the computation.
     *
     * @return True if the rejecting state p is covered; false otherwise.
     */
    public String mimic(List<Integer> powers, int index, int sum, String p, String q, String s) {
        if(index == this.getAlphabet().size()){
            if(sum == 0){
                return null;
            }
            List<String> word = createWord(powers);
            if(Objects.equals(traverse(p, word), q)){
                return traverse(s, word);
            }
            else{
                return null;
            }
        }
        for(int x = 0; x <= this.getStates().size() - sum; x++){
            powers.set(index, x);
            String state = mimic(powers, index + 1, sum + powers.get(index), p, q, s);
            powers.set(index, 0);
            if(state != null){
                return state;
            }
        }
        return null;
    }

    /**
     * Executes the transition function of the automaton by processing the given word
     * starting from the specified state.
     *
     * @param state The initial state from which the word is processed.
     * @param word The word to be processed by the automaton.
     *
     * @return The resulting state after the entire word has been processed.
     */
    private String traverse(String state, List<String> word){
        for(String letter : word){
            state = this.getTransitionFunction().get(Map.of(state, letter));
        }
        return state;
    }

//---------------------------------isComposite - Commutative - Saved Word-----------------------------------------------

    /**
     * Determines whether the commutative permutation automaton is composite.
     * The algorithm attempts to find a word that covers all rejecting states of the DFA.
     * Once a covering word is found, it is stored to prevent redundant computation.
     *
     * @return True if the automaton is composite; false otherwise.
     */
    public boolean isCompositeCommutativeSavedWord(){
        List<String> rejectingStates = this.getRejectingStates();
        for(String p : rejectingStates){
            boolean cover_found = false;
            for(String q : rejectingStates){
                if(!Objects.equals(p, q) && coverCommutativeSavedWord(p, q)){
                    cover_found = true;
                    break;
                }
            }
            if(!cover_found){
                return false;
            }
        }
        return !rejectingStates.isEmpty();
    }

    /**
     * Determines whether the state p is covered, i.e., whether there exists a word
     * that leads from state p to the target state q, such that no state encountered
     * during any number of repeated applications of this word is an accepting state.
     * The covering word is stored to avoid redundant recomputation during repeated
     * applications of the word.
     *
     * @param p The state whose coverage is being analyzed.
     * @param q The target state that should be reached after processing the input from state p.
     *
     * @return True if the state p is covered, false otherwise.
     */
    private boolean coverCommutativeSavedWord(String p, String q){
        String s = q;
        List<Integer> powers = new ArrayList<>(java.util.Collections.nCopies(this.getAlphabet().size(), 0));
        List<String> word = new ArrayList<>();
        while(!Objects.equals(s, p)){
            if(word.isEmpty()){
                Pair<String, List<String>> pair = mimicSavedWord(powers, 0, 0, p, q, s);
                if(pair == null){
                    return false;
                }
                else{
                    s = pair.getKey();
                    word = pair.getValue();
                }
            }
            else{
                s = traverse(s, word);
            }
            if(this.getAcceptStates().contains(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * Generates all possible exponent combinations to construct candidate words under the assumption of commutativity.
     *
     * @param powers A list of exponents representing how many times each letter from the alphabet is used.
     * @param index The index of the current letter in the alphabet.
     * @param sum The number of letters used so far.
     * @param p The rejecting state being checked for coverage.
     * @param q The target state the automaton should reach after processing the generated word from state p.
     * @param s An auxiliary state used during the computation.
     *
     * @return True if the rejecting state p is covered; false otherwise.
     */
    public Pair<String, List<String>> mimicSavedWord(List<Integer> powers, int index, int sum, String p, String q, String s) {
        if(index == this.getAlphabet().size()){
            if(sum == 0){
                return null;
            }
            List<String> word = createWord(powers);
            if(Objects.equals(traverse(p, word), q)){
                return Pair.of(traverse(s, word), word);
            }
            else{
                return null;
            }
        }
        for(int x = 0; x <= this.getStates().size() - sum; x++){
            powers.set(index, x);
            Pair<String, List<String>> pair = mimicSavedWord(powers, index + 1, sum + powers.get(index), p, q, s);
            powers.set(index, 0);
            if(pair != null){
                return pair;
            }
        }
        return null;
    }

//---------------------------------isComposite - Commutative - Big Alphabet--------------------------------------------

    /**
     * Determines whether the commutative permutation automaton is composite.
     * The algorithm attempts to find a word that covers all rejecting states of the DFA
     * with respect to a large input alphabet.
     *
     * @return True if the automaton is composite; true otherwise.
     */
    public boolean isCompositeCommutativeBigAlphabet(){
        List<String> rejectingStates = this.getRejectingStates();
        for(String p : rejectingStates){
            boolean cover_found = false;
            for(String q : rejectingStates){
                if(!Objects.equals(p, q) && coverCommutativeBigAlphabet(p, q)){
                    cover_found = true;
                    break;
                }
            }
            if(!cover_found){
                return false;
            }
        }
        return !rejectingStates.isEmpty();
    }

    /**
     * Determines whether the state p is covered, i.e., whether there exists a word
     * that leads from state p to the target state q, such that no state encountered
     * during any number of repeated applications of this word is an accepting state.
     *
     * @param p The state whose coverage is being analyzed.
     * @param q The target state that should be reached after processing the input from state p.
     *
     * @return True if the state p is covered, false otherwise.
     */
    private boolean coverCommutativeBigAlphabet(String p, String q){
        String s = q;
        while(!Objects.equals(s, p)){
            s = mimicBigAlphabet(0, p, q, s);
            if(s == null || this.getAcceptStates().contains(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * Iteratively explores all possible input words of length up to the number of states in the automaton,
     * simulating transitions character by character without storing the actual words.
     *
     * @param l Counter used to track the current length of the input word.
     * @param p The state whose coverage is being analyzed.
     * @param q The target state that should be reached after processing the input from state p.
     * @param s Auxiliary state used during the computation.
     *
     * @return The state reached after simulating the input word from state q.
     */
    public String mimicBigAlphabet(int l, String p, String q, String s) {
        if(Objects.equals(p, q)){
            return s;
        }
        else if(l >= this.getStates().size()){
            return null;
        }
        for(String letter : this.getAlphabet()){
            String r = mimicBigAlphabet(
                    l + 1,
                    this.getTransitionFunction().get(Map.of(p, letter)),
                    q,
                    this.getTransitionFunction().get(Map.of(s, letter))
            );
            if(r != null){
                return r;
            }
        }
        return null;
    }
}