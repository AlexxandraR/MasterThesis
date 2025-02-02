package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DFA {
    private Set<String> states;
    private Set<String> alphabet;
    private String initialState;
    private Set<String> acceptStates;
    private Map<Map<String, String>, String> transitionFunction;

    // Method to print all attributes and transition table of the DFA
    public void printDFA() {
        System.out.println("States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Start State: " + initialState);
        System.out.println("Accept States: " + acceptStates);
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

    private List<String> getNonAcceptingStates(){
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
     * and the second contains non-accepting states. The partitioning continues
     * until no further distinctions between states can be made.
     *
     * @return A list of sets, where each set represents a partition of indistinguishable states.
     */
    private List<Set<String>> partitionStates() {
        List<Set<String>> partitions = new ArrayList<>();
        Set<String> nonAcceptingStates = new HashSet<>(states);
        nonAcceptingStates.removeAll(acceptStates);

        partitions.add(acceptStates);
        partitions.add(nonAcceptingStates);

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
     * Checks if the DFA is commutative.
     * This method verifies whether the DFA is commutative by testing all possible combinations of
     * states as initial and accept states for two newly constructed DFAs, `A1` and `A2`.
     * The method constructs the intersection of these DFAs in two different ways (`Au` and `Av`)
     * based on different permutations of initial and accept states. If both intersections reach
     * their accept states, the DFA is not commutative and the method returns `false`.
     *
     * @return `true` if the DFA is commutative; `false` otherwise.
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

                            DFA Au = intersection(A1, A2);

                            DFA A11 = new DFA(this.states, this.getAlphabet(), this.initialState, this.acceptStates,
                                    this.transitionFunction);
                            A11.setInitialState(q);
                            A11.setAcceptStates(Set.of(qv));

                            DFA A22 = new DFA(this.states, this.getAlphabet(), this.initialState, this.acceptStates,
                                    this.transitionFunction);
                            A22.setInitialState(qu);
                            A22.setAcceptStates(Set.of(quv));

                            DFA Av = intersection(A11, A22);

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

//--------------------------------------------isPrime_algorithm1--------------------------------------------------------

    /**
     * Generates all possible deterministic finite automata (DFAs) with a given number of states.
     * The method creates all possible transition functions and valid accept state combinations,
     * then checks whether the DFA's accept states are reachable through intersection with the original DFA.
     * Only valid DFAs are returned.
     *
     * @param numStates The number of states to generate for the DFAs.
     * @return A set of valid DFAs that satisfy the condition of unreachable accept states after intersection.
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
                DFA finalDfa = intersection(this, newDFA);
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
     * Generates the set alpha(A), which consists of smaller DFAs that accept the same language as the original DFA.
     *
     * @return A set of DFAs that accept the same language as the original DFA.
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
     * This method checks for any overlapping state names between this DFA and the given `otherDFA`.
     * If there are conflicts, it renames the states in `otherDFA` by appending a specified prefix to each
     * conflicting state name. The method also updates `otherDFA`'s transition function, initial state,
     * and accept states to reflect the new names.
     *
     * @param otherDFA The DFA whose states will be renamed in case of conflicts with this DFA.
     */
    public void renameStatesIfConflict(DFA otherDFA) {
        Set<String> commonStates = new HashSet<>(this.states);
        commonStates.retainAll(otherDFA.states);

        if (!commonStates.isEmpty()) {
            String prefix = "1";

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
     * Computes the intersection of two deterministic finite automata (DFAs).
     * The intersection DFA accepts the language that is the intersection of the languages accepted by both input DFAs.
     *
     * @param dfa1 The first DFA.
     * @param dfa2 The second DFA.
     * @return The intersection of the two DFAs as a new DFA.
     */
    public static DFA intersection(DFA dfa1, DFA dfa2) {
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
     * Computes the intersection of a set of deterministic finite automata (DFAs).
     * The resulting DFA accepts the intersection of the languages accepted by all DFAs in the set.
     *
     * @param dfas A set of DFAs to intersect.
     * @return The intersection of all DFAs in the set as a new DFA.
     * @throws IllegalArgumentException if the set of DFAs is empty.
     */
    public static DFA intersection(Set<DFA> dfas) {
        Iterator<DFA> iterator = dfas.iterator();

        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("The set of DFAs cannot be empty");
        }

        DFA result = iterator.next();
        iterator.remove();

        while (iterator.hasNext()) {
            DFA nextDFA = iterator.next();
            result = intersection(result, nextDFA);
            iterator.remove();
        }

        return result;
    }

    /**
     * Computes the "roof" DFA, which is the intersection of all DFAs in the alpha(A) set.
     * This DFA represents the intersection of all DFAs that accept the same language as the original DFA.
     *
     * @return The roof DFA representing the intersection of all smaller DFAs.
     * @throws IllegalArgumentException if the alphaDFA set is empty.
     */
    public DFA getRoofDFA() {
        Set<DFA> alphaDFA = getAlphaDFA();
        if (alphaDFA.isEmpty()) {
            return new DFA();
        }

        return DFA.intersection(alphaDFA);
    }

    /**
     * Flips the accept states of the DFA. States that were previously accepting become non-accepting,
     * and non-accepting states become accepting. This is useful for complementing the DFA's language.
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
     * Checks whether the DFA is a prime DFA. A DFA is considered prime if its language cannot
     * be represented as the intersection of smaller DFAs. This method computes the roof DFA, complements
     * its accept states, and checks whether the intersection of the original DFA with the roof DFA has reachable
     * accept states.
     *
     * @return true if the DFA is prime, false otherwise.
     */
    public boolean isPrimeDFA() {
        DFA roof = getRoofDFA();
        if (roof.getStates() == null && roof.getTransitionFunction() == null) {
            return true;
        }
        roof.flipAcceptStates();
        DFA finalDfa = intersection(this, roof);

        return !finalDfa.isAcceptStateReachable();
    }

//--------------------------------------------isComposite - Memory------------------------------------------------------

    /**
     * Determines whether the automaton is composite based on memory constraints.
     * It iterates over subsets of non-accept states, checking if they can be covered.
     *
     * @return true if the automaton is composite, false otherwise.
     */
    public boolean isCompositeMemory() {
        List<String> nonAcceptStates = this.getNonAcceptingStates();

        if (nonAcceptStates.size() <= 1) {
            return false;
        }

        Set<String> covered = new HashSet<>();
        Set<Set<String>> processedOrbits = new HashSet<>();
        for (int size = 2; size <= nonAcceptStates.size(); size++) {
            if (generateCombinationMemory(nonAcceptStates, size, 0, new HashSet<>(), processedOrbits, covered)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively generates subsets of non-accept states and checks if they are covered.
     *
     * @param nonAcceptStates List of non-accept states.
     * @param size Target size of the subset.
     * @param index Current index in the list.
     * @param current Current subset of states being considered.
     * @param processedOrbits Set of already processed orbits.
     * @param covered Set of covered states.
     * @return true if all non-accept states are covered, false otherwise.
     */
    private boolean generateCombinationMemory(List<String> nonAcceptStates, int size, int index, Set<String> current,
                                              Set<Set<String>> processedOrbits, Set<String> covered) {
        if (current.size() == size) {
            if (!processedOrbits.contains(current)) {
                covered.addAll(coverMemory(new HashSet<>(current), processedOrbits));
                return covered.size() == nonAcceptStates.size();
            }
            return false;
        }

        for (int i = index; i < nonAcceptStates.size(); i++) {
            current.add(nonAcceptStates.get(i));
            if (generateCombinationMemory(nonAcceptStates, size, i + 1, current, processedOrbits, covered)) {
                return true;
            }
            current.remove(nonAcceptStates.get(i));
        }
        return false;
    }

    /**
     * Computes the cover set of a given subset of states, taking processed orbits into account.
     *
     * @param U The subset of states to cover.
     * @param processedOrbits Set of already processed orbits.
     * @return The set of covered states if they do not include accept states, otherwise an empty set.
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
     * Expands the orbits of state subsets by applying transition functions.
     *
     * @param CU Set of state subsets to expand.
     * @param processedOrbits Set of already processed orbits.
     * @return true if expansion completes successfully, false otherwise.
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
     * Determines whether the automaton is composite based on time-based coverage criteria.
     * The method checks if non-accepting states can be covered in subsets of increasing sizes.
     *
     * @return true if the automaton is composite, false otherwise.
     */
    public boolean isCompositeTime() {
        List<String> nonAcceptStates = this.getNonAcceptingStates();

        if (nonAcceptStates.size() <= 1) {
            return false;
        }

        Set<String> covered = new HashSet<>();
        for (int size = 2; size <= nonAcceptStates.size(); size++) {
            if (generateCombinationTime(nonAcceptStates, size, 0, new HashSet<>(), covered)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Recursively generates combinations of non-accepting states and checks if all states can be covered.
     *
     * @param nonAcceptStates List of non-accepting states.
     * @param size The size of the combination being generated.
     * @param index The current index in the list of states.
     * @param current The current combination of states being considered.
     * @param covered The set of covered states.
     * @return true if all non-accepting states are covered, false otherwise.
     */
    private boolean generateCombinationTime(List<String> nonAcceptStates, int size, int index, Set<String> current,
                                            Set<String> covered) {
        if (current.size() == size) {
            covered.addAll(coverTime(new HashSet<>(current)));
            return covered.size() == nonAcceptStates.size();
        }

        for (int i = index; i < nonAcceptStates.size(); i++) {
            current.add(nonAcceptStates.get(i));
            if (generateCombinationTime(nonAcceptStates, size, i + 1, current, covered)) {
                return true;
            }
            current.remove(nonAcceptStates.get(i));
        }
        return false;
    }

    /**
     * Computes the set of states covered by a given set U based on time-based expansion.
     *
     * @param U The initial set of states to expand.
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
     * Expands the given set of state subsets based on transitions, ensuring that all possible reachable states
     * are considered. Stops expansion if the number of covered states reaches the total number of states in the
     * automaton.
     *
     * @param CU The set of subsets to be expanded.
     * @return true if expansion is successful, false if it reaches the full set of states.
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
     * Determines whether the automaton is composite.
     * A composite automaton is one where every non-accepting state belongs to some covering subset
     * that satisfies the transition properties defined by the `cover` method.
     *
     * @return true if the automaton is composite, false otherwise.
     */
    public boolean isComposite() {
        List<String> nonAcceptStates = this.getNonAcceptingStates();

        if (nonAcceptStates.size() <= 1) {
            return false;
        }

        for (String p : nonAcceptStates) {
            int flag = 0;
            List<String> filteredStates = new ArrayList<>(nonAcceptStates);
            filteredStates.remove(p);
            for (int size = 2; size <= nonAcceptStates.size(); size++) {
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
     * Recursively generates combinations of non-accepting states and checks if any subset satisfies the cover
     * condition.
     *
     * @param nonAcceptStates List of non-accepting states.
     * @param size The target size of the subset to generate.
     * @param index The starting index for combination selection.
     * @param current The current subset being constructed.
     * @return 1 if a valid covering subset is found, otherwise 0.
     */
    private int generateCombination(List<String> nonAcceptStates, int size, int index, Set<String> current) {
        if (current.size() == size) {
            if (cover(current)) {
                return 1;
            }
            return 0;
        }
        for (int i = index; i < nonAcceptStates.size(); i++) {
            current.add(nonAcceptStates.get(i));
            if (generateCombination(nonAcceptStates, size, i + 1, current) == 1) {
                return 1;
            }
            current.remove(nonAcceptStates.get(i));
        }
        return 0;
    }

    /**
     * Checks whether a given subset of states covers the initial state through transitions.
     *
     * @param U The subset of states to test for coverage.
     * @return true if the subset covers the initial state, otherwise false.
     */
    public boolean cover(Set<String> U) {
        Set<Set<String>> CU = new HashSet<>();
        CU.add(U);

        return expandOrbits(CU) && CU.stream().anyMatch(subset -> subset.contains(this.getInitialState()));
    }

    /**
     * Expands orbits by applying state transitions iteratively.
     * The process continues until no new subsets are generated or all states are covered.
     *
     * @param CU A set of state subsets representing the current coverage.
     * @return true if the expansion remains within bounds, false if all states are covered.
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

}