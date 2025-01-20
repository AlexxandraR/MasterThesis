package DFA;
import org.example.DFA;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StatesReachabilityTest {
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
        Set<String> acceptingStates = new HashSet<>();
        acceptingStates.add("q3");

        dfa = new DFA(states, alphabet, initialState, acceptingStates, transitionFunction);
    }

    @Test
    public void testReachabilityWithAllReachableStates() {
        Set<String> expectedReachableStates = new HashSet<>();
        expectedReachableStates.add("q0");
        expectedReachableStates.add("q1");
        expectedReachableStates.add("q2");
        expectedReachableStates.add("q3");

        Set<String> reachableStates = dfa.reachableStates();
        assertEquals(expectedReachableStates, reachableStates, "All states should be reachable from q0");
    }

    @Test
    public void testReachabilityWithUnreachableState() {
        dfa.getStates().add("q4");

        Set<String> expectedReachableStates = new HashSet<>();
        expectedReachableStates.add("q0");
        expectedReachableStates.add("q1");
        expectedReachableStates.add("q2");
        expectedReachableStates.add("q3");

        Set<String> reachableStates = dfa.reachableStates();
        assertEquals(expectedReachableStates, reachableStates, "Unreachable state q4 should not be in reachable states");
    }

    @Test
    public void testReachabilityWithNoTransitions() {
        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");

        Set<String> alphabet = new HashSet<>();
        alphabet.add("a");
        alphabet.add("b");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();

        String initialState = "q0";
        Set<String> acceptingStates = new HashSet<>();

        DFA dfaWithoutTransitions = new DFA(states, alphabet, initialState, acceptingStates, transitionFunction);

        Set<String> expectedReachableStates = new HashSet<>();
        expectedReachableStates.add("q0");

        Set<String> reachableStates = dfaWithoutTransitions.reachableStates();
        assertEquals(expectedReachableStates, reachableStates, "Only the initial state should be reachable when no transitions exist");
    }

    @Test
    public void testReachabilityWithCyclicTransitions() {
        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q0");

        dfa = new DFA(dfa.getStates(), dfa.getAlphabet(), dfa.getInitialState(), dfa.getAcceptStates(), transitionFunction);

        Set<String> expectedReachableStates = new HashSet<>();
        expectedReachableStates.add("q0");
        expectedReachableStates.add("q1");

        Set<String> reachableStates = dfa.reachableStates();
        assertEquals(expectedReachableStates, reachableStates, "Only q0 and q1 should be reachable due to the cycle between them");
    }

    @Test
    public void testReachabilityWithNoInitialState() {
        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q0");

        dfa = new DFA(dfa.getStates(), dfa.getAlphabet(), null, dfa.getAcceptStates(), transitionFunction);

        Set<String> reachableStates = dfa.reachableStates();
        assertNull(reachableStates, "Only q0 and q1 should be reachable due to the cycle between them");
    }
}
