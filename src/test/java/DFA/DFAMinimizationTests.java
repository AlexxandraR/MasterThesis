package DFA;
import org.example.DFA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DFAMinimizationTests {

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
        transitionFunction.put(Map.of("q0", "b"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q0");
        transitionFunction.put(Map.of("q1", "b"), "q0");
        transitionFunction.put(Map.of("q2", "a"), "q3"); // q2 and q3 are unreachable
        transitionFunction.put(Map.of("q3", "b"), "q2");

        String initialState = "q0";
        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q0");
        acceptStates.add("q1");

        dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);
    }

    @Test
    void testMinimize_BasicDFA() {
        DFA minimizedDfa = dfa.minimize();

        Set<String> expectedStates = Set.of("q0");
        assertEquals(expectedStates, minimizedDfa.getStates(), "Only one state should remain after minimization");

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "a"), "q0");
        expectedTransitions.put(Map.of("q0", "b"), "q0");

        assertEquals(expectedTransitions, minimizedDfa.getTransitionFunction(), "The minimized DFA should have self-looping transitions");

        String expectedInitialState = "q0";
        assertEquals(expectedInitialState, minimizedDfa.getInitialState(), "The initial state should remain q0");

        Set<String> expectedAcceptStates = Set.of("q0");
        assertEquals(expectedAcceptStates, minimizedDfa.getAcceptStates(), "The only remaining state should be accepting");
    }

    @Test
    void testMinimize_NoAcceptingStates() {
        dfa.getAcceptStates().clear();

        DFA minimizedDfa = dfa.minimize();

        Set<String> expectedStates = Set.of("q0");
        assertEquals(expectedStates, minimizedDfa.getStates(), "The DFA should be minimized to a single state even without accepting states");

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "a"), "q0");
        expectedTransitions.put(Map.of("q0", "b"), "q0");

        assertEquals(expectedTransitions, minimizedDfa.getTransitionFunction(), "The minimized DFA should have self-looping transitions");

        String expectedInitialState = "q0";
        assertEquals(expectedInitialState, minimizedDfa.getInitialState(), "The initial state should remain q0");

        assertTrue(minimizedDfa.getAcceptStates().isEmpty(), "There should be no accepting states in the minimized DFA");
    }

    @Test
    void testMinimize_NoTransitionBetweenTwoStates() {
        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q0");
        transitionFunction.put(Map.of("q1", "a"), "q1");

        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");

        Set<String> accept = new HashSet<>();
        accept.add("q1");

        dfa = new DFA(states, Set.of("a"), "q0", accept, transitionFunction);

        DFA minimizedDfa = dfa.minimize();

        Set<String> expectedStates = Set.of("q0");
        assertEquals(expectedStates, minimizedDfa.getStates(), "Only one state should remain after minimization");

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "a"), "q0");

        assertEquals(expectedTransitions, minimizedDfa.getTransitionFunction(), "\"Only one transition should remain after minimization\"");

        String expectedInitialState = "q0";
        assertEquals(expectedInitialState, minimizedDfa.getInitialState(), "The initial state should remain q0");

        Set<String> expectedAcceptStates = Set.of();
        assertEquals(expectedAcceptStates, minimizedDfa.getAcceptStates(), "No accepting state should remain after minimization");
    }

    @Test
    void testMinimize_FromBook() {
        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "0"), "q1");
        transitionFunction.put(Map.of("q0", "1"), "q2");
        transitionFunction.put(Map.of("q1", "0"), "q2");
        transitionFunction.put(Map.of("q1", "1"), "q3");
        transitionFunction.put(Map.of("q2", "0"), "q1");
        transitionFunction.put(Map.of("q2", "1"), "q4");
        transitionFunction.put(Map.of("q3", "0"), "q7");
        transitionFunction.put(Map.of("q3", "1"), "q4");
        transitionFunction.put(Map.of("q4", "0"), "q7");
        transitionFunction.put(Map.of("q4", "1"), "q3");
        transitionFunction.put(Map.of("q5", "0"), "q7");
        transitionFunction.put(Map.of("q5", "1"), "q6");
        transitionFunction.put(Map.of("q6", "0"), "q5");
        transitionFunction.put(Map.of("q6", "1"), "q7");
        transitionFunction.put(Map.of("q7", "0"), "q7");
        transitionFunction.put(Map.of("q7", "1"), "q0");

        Set<String> states = new HashSet<>(Set.of("q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7"));

        Set<String> accept = new HashSet<>();
        accept.add("q7");

        dfa = new DFA(states, Set.of("0", "1"), "q0", accept, transitionFunction);

        DFA minimizedDfa = dfa.minimize();

        Set<String> expectedStates = Set.of("q0", "q1", "q2", "q3");
        assertEquals(expectedStates, minimizedDfa.getStates());

        String expectedInitialState = "q1";
        assertEquals(expectedInitialState, minimizedDfa.getInitialState());

        Map<Map<String, String>, String> expectedTransitions = new HashMap<>();
        expectedTransitions.put(Map.of("q0", "0"), "q0");
        expectedTransitions.put(Map.of("q0", "1"), "q1");
        expectedTransitions.put(Map.of("q1", "0"), "q2");
        expectedTransitions.put(Map.of("q1", "1"), "q2");
        expectedTransitions.put(Map.of("q2", "0"), "q2");
        expectedTransitions.put(Map.of("q2", "1"), "q3");
        expectedTransitions.put(Map.of("q3", "0"), "q0");
        expectedTransitions.put(Map.of("q3", "1"), "q3");

        assertEquals(expectedTransitions, minimizedDfa.getTransitionFunction());

        Set<String> expectedAcceptStates = Set.of("q0");
        assertEquals(expectedAcceptStates, minimizedDfa.getAcceptStates(), "One accepting state should remain after minimization");
    }
}

