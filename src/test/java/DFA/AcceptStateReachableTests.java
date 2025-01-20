package DFA;
import org.example.DFA;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptStateReachableTests {
    @Test
    public void testIsAcceptStateReachableFromStart() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(dfa.isAcceptStateReachable());
    }

    @Test
    public void testIsAcceptStateReachableThroughTransitions() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "b"), "q2");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(dfa.isAcceptStateReachable());
    }

    @Test
    public void testIsAcceptStateWithComplexCyclicTransitions() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3", "q4", "q5");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q4", "q5");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q0", "b"), "q2");
        transitionFunction.put(Map.of("q1", "a"), "q3");
        transitionFunction.put(Map.of("q1", "b"), "q0");
        transitionFunction.put(Map.of("q2", "a"), "q4");
        transitionFunction.put(Map.of("q2", "b"), "q1");
        transitionFunction.put(Map.of("q3", "a"), "q2");
        transitionFunction.put(Map.of("q3", "b"), "q5");
        transitionFunction.put(Map.of("q4", "a"), "q0");
        transitionFunction.put(Map.of("q4", "b"), "q4");
        transitionFunction.put(Map.of("q5", "a"), "q3");
        transitionFunction.put(Map.of("q5", "b"), "q5");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(dfa.isAcceptStateReachable());
    }

    @Test
    public void testIsAcceptStateNotReachable() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q1");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(dfa.isAcceptStateReachable());
    }

    @Test
    public void testIsAcceptStateWithNoAcceptStates() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        String startState = "q0";
        Set<String> acceptStates = new HashSet<>();

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(dfa.isAcceptStateReachable());
    }

    @Test
    public void testIsAcceptStateInEmptyDFA() {
        Set<String> states = Set.of();
        Set<String> alphabet = Set.of();
        Set<String> acceptStates = new HashSet<>();

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();

        DFA dfa = new DFA(states, alphabet, null, acceptStates, transitionFunction);

        assertFalse(dfa.isAcceptStateReachable());
    }
}
