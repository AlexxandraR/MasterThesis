package DFA;
import org.example.DFA;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRemoveOfUnreachableStates {

    private DFA dfa;

    @BeforeEach
    void setUp() {
        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");
        states.add("q2");
        states.add("q3");

        Set<String> alphabet = new HashSet<>();
        alphabet.add("a");
        alphabet.add("b");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q2");
        transitionFunction.put(Map.of("q2", "b"), "q3");

        String initialState = "q0";
        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q3");

        dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);
    }

    @Test
    void testRemoveUnreachabilityWithAllReachableStates() {
        dfa.removeUnreachableStates();

        Set<String> expectedStates = Set.of("q0", "q1", "q2", "q3");
        assertEquals(expectedStates, dfa.getStates(), "All states should be reachable");

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "a"), "q1");
        expectedTransitions.put(Map.of("q1", "a"), "q2");
        expectedTransitions.put(Map.of("q2", "b"), "q3");

        assertEquals(expectedTransitions, dfa.getTransitionFunction(), "All transitions should remain");

        Set<String> expectedAcceptStates = Set.of("q3");
        assertEquals(expectedAcceptStates, dfa.getAcceptStates(), "q3 should remain as the accepting state");
    }

    @Test
    void testReachabilityWithUnreachableState() {
        dfa.getStates().add("q4");
        dfa.getTransitionFunction().put(Map.of("q4", "a"), "q0");

        dfa.removeUnreachableStates();

        Set<String> expectedStates = Set.of("q0", "q1", "q2", "q3");
        assertEquals(expectedStates, dfa.getStates(), "Unreachable state q4 should be removed");

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "a"), "q1");
        expectedTransitions.put(Map.of("q1", "a"), "q2");
        expectedTransitions.put(Map.of("q2", "b"), "q3");

        assertEquals(expectedTransitions, dfa.getTransitionFunction(), "Only reachable state transitions should remain");

        Set<String> expectedAcceptStates = Set.of("q3");
        assertEquals(expectedAcceptStates, dfa.getAcceptStates(), "Only reachable accepting states should remain");
    }

    @Test
    void testReachabilityWithNoTransitions() {
        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");

        Set<String> alphabet = new HashSet<>();
        alphabet.add("a");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();

        String initialState = "q0";
        Set<String> acceptStates = new HashSet<>();

        DFA dfaWithoutTransitions = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);
        dfaWithoutTransitions.removeUnreachableStates();

        Set<String> expectedStates = Set.of("q0");
        assertEquals(expectedStates, dfaWithoutTransitions.getStates(), "Only the initial state should remain");

        assertTrue(dfaWithoutTransitions.getTransitionFunction().isEmpty(), "There should be no transitions left");

        assertTrue(dfaWithoutTransitions.getAcceptStates().isEmpty(), "No accepting states should remain");
    }

    @Test
    void testReachabilityWithCyclicTransitions() {
        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q0");

        dfa = new DFA(dfa.getStates(), dfa.getAlphabet(), dfa.getInitialState(), dfa.getAcceptStates(), transitionFunction);

        dfa.removeUnreachableStates();

        Set<String> expectedStates = Set.of("q0", "q1");
        assertEquals(expectedStates, dfa.getStates(), "Only q0 and q1 should remain due to the cycle between them");

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "a"), "q1");
        expectedTransitions.put(Map.of("q1", "a"), "q0");

        assertEquals(expectedTransitions, dfa.getTransitionFunction(), "Only the cyclic transitions should remain");

        assertTrue(dfa.getAcceptStates().isEmpty(), "There should be no accepting states in this case");
    }
}
