package DFA;

import org.example.DFA;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;

public class IntersectionTest {
    @Test
    public void testIntersection_CommonAcceptStates() {
        Set<String> states1 = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a", "b");
        String initialState1 = "q0";
        Set<String> acceptStates1 = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction1 = Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q1", "b"), "q2"
        );

        DFA dfa1 = new DFA(states1, alphabet, initialState1, acceptStates1, transitionFunction1);

        Set<String> states2 = Set.of("p0", "p1", "p2");
        String initialState2 = "p0";
        Set<String> acceptStates2 = Set.of("p2");

        Map<Map<String, String>, String> transitionFunction2 = Map.of(
                Map.of("p0", "a"), "p1",
                Map.of("p1", "b"), "p2"
        );

        DFA dfa2 = new DFA(states2, alphabet, initialState2, acceptStates2, transitionFunction2);

        DFA intersectionDFA = DFA.intersection(dfa1.completeDFA(), dfa2.completeDFA());

        assertEquals(Set.of("sink,sink1", "p1,q1", "p2,q2", "p0,q0"), intersectionDFA.getStates());
        assertEquals(Set.of("a", "b"), intersectionDFA.getAlphabet());
        assertEquals("p0,q0", intersectionDFA.getInitialState());
        assertEquals(Set.of("p2,q2"), intersectionDFA.getAcceptStates());
        assertEquals(Map.of(
                Map.of("sink,sink1", "b"), "sink,sink1",
                Map.of("sink,sink1", "a"), "sink,sink1",
                Map.of("p1,q1", "b"), "p2,q2",
                Map.of("p2,q2", "b"), "sink,sink1",
                Map.of("p0,q0", "b"), "sink,sink1",
                Map.of("p0,q0", "a"), "p1,q1",
                Map.of("p1,q1", "a"), "sink,sink1",
                Map.of("p2,q2", "a"), "sink,sink1"
        ), intersectionDFA.getTransitionFunction());
    }

    @Test
    public void testIntersection_DifferentAlphabets() {
        Set<String> states1 = Set.of("q0", "q1");
        Set<String> alphabet1 = Set.of("a");
        String initialState1 = "q0";
        Set<String> acceptStates1 = Set.of("q1");

        Map<Map<String, String>, String> transitionFunction1 = Map.of(
                Map.of("q0", "a"), "q1"
        );

        DFA dfa1 = new DFA(states1, alphabet1, initialState1, acceptStates1, transitionFunction1);

        Set<String> states2 = Set.of("p0", "p1");
        Set<String> alphabet2 = Set.of("b");
        String initialState2 = "p0";
        Set<String> acceptStates2 = Set.of("p1");

        Map<Map<String, String>, String> transitionFunction2 = Map.of(
                Map.of("p0", "b"), "p1"
        );

        DFA dfa2 = new DFA(states2, alphabet2, initialState2, acceptStates2, transitionFunction2);

        assertThrows(IllegalArgumentException.class, () -> DFA.intersection(dfa1, dfa2),
                "DFAs with different alphabets should throw IllegalArgumentException.");
    }

    @Test
    public void testIntersection_NoCommonAcceptStates() {
        Set<String> states1 = Set.of("q0", "q1", "q2");
        Set<String> alphabet = Set.of("a");
        String initialState1 = "q0";
        Set<String> acceptStates1 = Set.of("q2");

        Map<Map<String, String>, String> transitionFunction1 = Map.of(
                Map.of("q0", "a"), "q1",
                Map.of("q1", "a"), "q2"
        );

        DFA dfa1 = new DFA(states1, alphabet, initialState1, acceptStates1, transitionFunction1);

        Set<String> states2 = Set.of("p0", "p1", "p2");
        String initialState2 = "p0";
        Set<String> acceptStates2 = Set.of("p1");

        Map<Map<String, String>, String> transitionFunction2 = Map.of(
                Map.of("p0", "a"), "p1",
                Map.of("p1", "a"), "p2"
        );

        DFA dfa2 = new DFA(states2, alphabet, initialState2, acceptStates2, transitionFunction2);

        DFA intersectionDFA = DFA.intersection(dfa1.completeDFA(), dfa2.completeDFA());

        assertEquals(Set.of("p1,q1", "p2,q2", "p0,q0", "p2,sink"), intersectionDFA.getStates());
        assertEquals(Set.of("a"), intersectionDFA.getAlphabet());
        assertEquals("p0,q0", intersectionDFA.getInitialState());
        assertEquals(Set.of(), intersectionDFA.getAcceptStates());
        assertEquals(Map.of(
                Map.of("p2,sink", "a"), "p2,sink",
                Map.of("p0,q0", "a"), "p1,q1",
                Map.of("p1,q1", "a"), "p2,q2",
                Map.of("p2,q2", "a"), "p2,sink"
        ), intersectionDFA.getTransitionFunction());
    }

    @Test
    public void testIntersection_SingleStateDFAs() {
        Set<String> states1 = Set.of("q0");
        Set<String> alphabet = Set.of("a");
        String initialState1 = "q0";
        Set<String> acceptStates1 = Set.of("q0");

        Map<Map<String, String>, String> transitionFunction1 = Map.of();

        DFA dfa1 = new DFA(states1, alphabet, initialState1, acceptStates1, transitionFunction1);

        Set<String> states2 = Set.of("p0");
        String initialState2 = "p0";
        Set<String> acceptStates2 = Set.of("p0");

        Map<Map<String, String>, String> transitionFunction2 = Map.of();

        DFA dfa2 = new DFA(states2, alphabet, initialState2, acceptStates2, transitionFunction2);

        DFA intersectionDFA = DFA.intersection(dfa1.completeDFA(), dfa2.completeDFA());

        assertEquals(Set.of("p0,q0", "sink,sink1"), intersectionDFA.getStates());
        assertEquals(Set.of("a"), intersectionDFA.getAlphabet());
        assertEquals("p0,q0", intersectionDFA.getInitialState());
        assertEquals(Set.of("p0,q0"), intersectionDFA.getAcceptStates());
        assertEquals(Map.of(
                Map.of("sink,sink1", "a"), "sink,sink1",
                Map.of("p0,q0", "a"), "sink,sink1"
        ), intersectionDFA.getTransitionFunction());
    }
}
