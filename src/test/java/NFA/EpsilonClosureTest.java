package NFA;

import org.example.Automaton;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class EpsilonClosureTest {
    private Automaton createAutomaton() {
        Automaton automaton = new Automaton();
        automaton.setStates(new HashSet<>(Set.of("q0", "q1", "q2", "q3")));
        automaton.setAlphabet(new HashSet<>(Set.of("a", "b")));
        automaton.setInitialState("q0");
        automaton.setAcceptStates(new HashSet<>(Set.of("q2")));
        Map<String, String> key = Map.of("q0", "a");
        automaton.getTransitionFunction().computeIfAbsent(key, k -> new HashSet<>()).add("q1");
        key = Map.of("q0", "b");
        automaton.getTransitionFunction().computeIfAbsent(key, k -> new HashSet<>()).add("q2");
        key = Map.of("q1", "a");
        automaton.getTransitionFunction().computeIfAbsent(key, k -> new HashSet<>()).add("q3");
        key = Map.of("q3", "a");
        automaton.getTransitionFunction().computeIfAbsent(key, k -> new HashSet<>()).add("q2");
        return automaton;
    }

    private void addTransition(Automaton automaton, String fromState, String toState) {
        Map<String, String> key = Map.of(fromState, "");
        automaton.getTransitionFunction().computeIfAbsent(key, k -> new HashSet<>()).add(toState);
    }

    @Test
    public void testClosureWithNoEpsilonTransitions() {
        Automaton automaton = createAutomaton();
        Set<String> initialStates = new HashSet<>(Set.of("q0"));
        Set<String> result = automaton.closureEpsilon(initialStates);

        assertEquals(Set.of("q0"), result, "Should only contain the initial state when there are no epsilon transitions.");
    }

    @Test
    public void testClosureWithSingleEpsilonTransition() {
        Automaton automaton = createAutomaton();
        // q0 --ε--> q1
        addTransition(automaton, "q0", "q1");

        Set<String> initialStates = new HashSet<>(Set.of("q0"));
        Set<String> result = automaton.closureEpsilon(initialStates);

        assertEquals(Set.of("q0", "q1"), result, "Should contain q0 and q1 after epsilon transition.");
    }

    @Test
    public void testClosureWithSingleEpsilonTransitionFromDifferentState() {
        Automaton automaton = createAutomaton();
        // q0 --ε--> q1
        addTransition(automaton, "q0", "q1");

        Set<String> initialStates = new HashSet<>(Set.of("q1"));
        Set<String> result = automaton.closureEpsilon(initialStates);

        assertEquals(Set.of("q1"), result, "Should contain q0 and q1 after epsilon transition.");
    }

    @Test
    public void testClosureWithMultipleEpsilonTransitions() {
        Automaton automaton = createAutomaton();
        // q0 --ε--> q1, q1 --ε--> q2
        addTransition(automaton, "q0", "q1");
        addTransition(automaton, "q1", "q2");

        Set<String> initialStates = new HashSet<>(Set.of("q0"));
        Set<String> result = automaton.closureEpsilon(initialStates);

        assertEquals(Set.of("q0", "q1", "q2"), result, "Should contain q0, q1, and q2 after multiple epsilon transitions.");
    }

    @Test
    public void testClosureWithCycleInEpsilonTransitions() {
        Automaton automaton = createAutomaton();
        // q0 --ε--> q1, q1 --ε--> q0 (cycle)
        addTransition(automaton, "q0", "q1");
        addTransition(automaton, "q1", "q0");

        Set<String> initialStates = new HashSet<>(Set.of("q0"));
        Set<String> result = automaton.closureEpsilon(initialStates);

        assertEquals(Set.of("q0", "q1"), result, "Should contain q0 and q1, even with a cycle in epsilon transitions.");
    }

    @Test
    public void testClosureWithMultipleInitialStates() {
        Automaton automaton = createAutomaton();
        // q0 --ε--> q1, q2 --ε--> q3
        addTransition(automaton, "q0", "q1");
        addTransition(automaton, "q2", "q3");

        Set<String> initialStates = new HashSet<>(Set.of("q0", "q2"));
        Set<String> result = automaton.closureEpsilon(initialStates);

        assertEquals(Set.of("q0", "q1", "q2", "q3"), result, "Should contain q0, q1, q2, and q3 with multiple initial states and epsilon transitions.");
    }
}


