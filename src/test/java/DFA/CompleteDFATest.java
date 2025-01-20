package DFA;

import org.junit.jupiter.api.BeforeEach;

import org.example.DFA;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CompleteDFATest {
    private DFA dfa;

    @BeforeEach
    void setUp() {
        dfa = new DFA();
    }

    @Test
    void testAlreadyCompleteDFA() {
        dfa.setStates(Set.of("q0", "q1"));
        dfa.setInitialState("q0");
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q0", "b"), "q0",
                Map.of("q1", "a"), "q0",
                Map.of("q1", "b"), "q1"
        ));

        DFA completedDFA = dfa.completeDFA();

        assertEquals(dfa, completedDFA, "If the DFA is already complete, completeDFA should return the same DFA.");
    }

    @Test
    void testIncompleteDFAWithSinkState() {
        dfa.setStates(Set.of("q0", "q1"));
        dfa.setInitialState("q0");
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q0", "b"), "q0",
                Map.of("q1", "a"), "q0"
        ));

        DFA completedDFA = dfa.completeDFA();

        assertTrue(completedDFA.isComplete(), "The completed DFA should be complete.");
        assertTrue(completedDFA.getStates().contains("sink"), "The completed DFA should contain a sink state.");

        for (String symbol : dfa.getAlphabet()) {
            assertEquals("sink", completedDFA.getTransitionFunction().get(Map.of("sink", symbol)),
                    "The sink state should transition to itself on all symbols.");
        }
    }

    @Test
    void testIncompleteDFAWithoutNeedForSinkState() {
        dfa.setStates(Set.of("q0"));
        dfa.setInitialState("q0");
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(new HashMap<>());

        DFA completedDFA = dfa.completeDFA();

        assertTrue(completedDFA.isComplete(), "The completed DFA should be complete.");
        assertFalse(completedDFA.getStates().contains("sink"), "The completed DFA should not contain a sink state.");

        for (String symbol : dfa.getAlphabet()) {
            assertEquals("q0", completedDFA.getTransitionFunction().get(Map.of("q0", symbol)),
                    "Missing transitions should loop to the same state when no sink is needed.");
        }
    }

    @Test
    void testEmptyDFA() {
        dfa.setStates(new HashSet<>());
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(new HashMap<>());

        DFA completedDFA = dfa.completeDFA();

        assertTrue(completedDFA.getStates().isEmpty(), "The completed DFA should have no states if the original DFA is empty.");
    }

    @Test
    void testSingleStateNoTransitionsDFA() {
        dfa.setStates(Set.of("q0"));
        dfa.setInitialState("q0");
        dfa.setAlphabet(Set.of("a", "b"));
        dfa.setTransitionFunction(new HashMap<>());

        DFA completedDFA = dfa.completeDFA();

        assertTrue(completedDFA.isComplete(), "The completed DFA should be complete.");
        assertFalse(completedDFA.getStates().contains("sink"), "The completed DFA should not contain a sink state if not needed.");

        for (String symbol : dfa.getAlphabet()) {
            assertEquals("q0", completedDFA.getTransitionFunction().get(Map.of("q0", symbol)),
                    "Missing transitions should loop to the same state when no sink is needed.");
        }
    }
}
