package DFA;

import org.example.DFA;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PermutationAutomatonTests {
    @Test
    public void testIsPermutationWithValidPermutationAutomaton() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q0", "b"), "q2");
        transitionFunction.put(Map.of("q1", "a"), "q2");
        transitionFunction.put(Map.of("q1", "b"), "q0");
        transitionFunction.put(Map.of("q2", "a"), "q0");
        transitionFunction.put(Map.of("q2", "b"), "q1");

        DFA automaton = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(automaton.isPermutation());
    }

    @Test
    public void testIsPermutationWithInvalidPermutationAutomaton() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q0", "b"), "q2");
        transitionFunction.put(Map.of("q1", "a"), "q2");
        transitionFunction.put(Map.of("q1", "b"), "q2");
        transitionFunction.put(Map.of("q2", "a"), "q0");
        transitionFunction.put(Map.of("q2", "b"), "q1");

        DFA automaton = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(automaton.isPermutation());
    }

    @Test
    public void testIsPermutationWithIncompleteTransition() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q0", "b"), "q2");
        transitionFunction.put(Map.of("q1", "a"), "q2");
        transitionFunction.put(Map.of("q2", "a"), "q0");
        transitionFunction.put(Map.of("q2", "b"), "q1");

        DFA automaton = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(automaton.isPermutation());
    }

    @Test
    public void testIsPermutationWithOneState() {
        Set<String> states = Set.of("q0");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q0");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q0");
        transitionFunction.put(Map.of("q0", "b"), "q0");

        DFA automaton = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(automaton.isPermutation());
    }

    @Test
    public void testIsPermutationWithNoTransitions() {
        Set<String> states = Set.of("q0");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q0");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();

        DFA automaton = new DFA(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(automaton.isPermutation());
    }
}
