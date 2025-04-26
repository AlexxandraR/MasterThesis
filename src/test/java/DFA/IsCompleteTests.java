package DFA;

import org.example.DFA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IsCompleteTests {
    private DFA dfa;

    @BeforeEach
    void setUp() {
        dfa = new DFA();
    }

    @Test
    void testCompleteDFA() {
        // Setup a complete DFA
        dfa.setStates(Set.of("q0", "q1"));
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q0", "b"), "q0",
                Map.of("q1", "a"), "q0",
                Map.of("q1", "b"), "q1"
        ));

        assertTrue(dfa.isComplete(), "The DFA should be complete.");
    }

    @Test
    void testIncompleteDFA() {
        // Setup an incomplete DFA (missing transition for q1 with symbol "b")
        dfa.setStates(Set.of("q0", "q1"));
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q0", "b"), "q0",
                Map.of("q1", "a"), "q0"
        ));

        assertFalse(dfa.isComplete(), "The DFA should be incomplete.");
    }

    @Test
    void testEmptyStates() {
        // DFA with no states (edge case)
        dfa.setStates(Set.of());
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(new HashMap<>()); // Empty transition function

        assertTrue(dfa.isComplete(), "A DFA with no states should be considered complete.");
    }

    @Test
    void testEmptyAlphabet() {
        // DFA with no alphabet symbols (edge case)
        dfa.setStates(Set.of("q0"));
        dfa.setAlphabet(Set.of());
        dfa.setTransitionFunction(new HashMap<>()); // Empty transition function

        assertTrue(dfa.isComplete(), "A DFA with an empty alphabet should be considered complete.");
    }

    @Test
    void testSingleStateSingleSymbolCompleteDFA() {
        // Single state and single symbol, complete DFA
        dfa.setStates(Set.of("q0"));
        dfa.setAlphabet(Set.of("a"));
        dfa.setTransitionFunction(Map.of(
                Map.of("q0", "a"), "q0"
        ));

        assertTrue(dfa.isComplete(), "The single-state single-symbol DFA should be complete.");
    }

    @Test
    void testSingleStateSingleSymbolIncompleteDFA() {
        // Single state and single symbol, incomplete DFA
        dfa.setStates(Set.of("q0"));
        dfa.setAlphabet(Set.of("a"));
        dfa.setTransitionFunction(new HashMap<>()); // No transitions

        assertFalse(dfa.isComplete(), "The single-state single-symbol DFA should be incomplete.");
    }
}
