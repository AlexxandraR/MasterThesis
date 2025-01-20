package DFA;
import org.example.DFA;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlipAcceptStatesTests {
    @Test
    public void testFlipAcceptStatesWithEmptyAcceptStates() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = new HashSet<>();

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "b"), "q2");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        dfa.flipAcceptStates();

        assertEquals(new HashSet<>(states), dfa.getAcceptStates());
    }

    @Test
    public void testFlipAcceptStatesWithSomeAcceptStates() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "b"), "q2");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        dfa.flipAcceptStates();

        Set<String> expectedAcceptStates = new HashSet<>(states);
        expectedAcceptStates.remove("q1");
        assertEquals(expectedAcceptStates, dfa.getAcceptStates());
    }

    @Test
    public void testFlipAcceptStatesWithAllStatesAccepting() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = new HashSet<>(states);

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "b"), "q2");

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        dfa.flipAcceptStates();

        assertTrue(dfa.getAcceptStates().isEmpty());
    }
}
