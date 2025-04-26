package DFA;

import org.example.DFA;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
public class IsCommutativeTests {
    @Test
    public void testIsCommutative_ForNonCommutativeDFA() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String initialState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction = Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q1", "b"), "q2",
                Map.of("q0", "b"), "q2"
        );

        DFA dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);

        assertFalse(dfa.completeDFA().isCommutative());
    }

    @Test
    public void testIsCommutative_ForCommutativeDFA() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3");
        Set<String> alphabet = Set.of("a", "b");
        String initialState = "q0";
        Set<String> acceptStates = Set.of("q3");

        Map<Map<String, String>, String> transitionFunction = Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q1", "b"), "q3",
                Map.of("q0", "b"), "q2",
                Map.of("q2", "a"), "q3"
        );

        DFA dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);

        assertTrue(dfa.completeDFA().isCommutative());
    }

    @Test
    public void testIsCommutative_SingleStateDFA() {
        Set<String> states = Set.of("q0");
        Set<String> alphabet = Set.of("a", "b");
        String initialState = "q0";
        Set<String> acceptStates = Set.of("q0");

        Map<Map<String, String>, String> transitionFunction = Map.of(
                Map.of("q0", "a"), "q0",
                Map.of("q0", "b"), "q0"
        );

        DFA dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);

        assertTrue(dfa.isCommutative());
    }

    @Test
    public void testIsCommutative_NoAcceptStates() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a");
        String initialState = "q0";
        Set<String> acceptStates = Set.of();

        Map<Map<String, String>, String> transitionFunction = Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q1", "a"), "q0"
        );

        DFA dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);

        assertTrue(dfa.isCommutative());
    }

    @Test
    public void testIsCommutative_NoTransitions() {
        Set<String> states = Set.of("q0");
        Set<String> alphabet = Set.of();
        String initialState = "q0";
        Set<String> acceptStates = Set.of("q0");

        Map<Map<String, String>, String> transitionFunction = Map.of();

        DFA dfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);

        assertTrue(dfa.isCommutative(), "DFA with no transitions should be considered commutative.");
    }
}
