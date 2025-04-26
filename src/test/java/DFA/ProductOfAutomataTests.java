package DFA;

import org.example.DFA;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;

public class ProductOfAutomataTests {
    @Test
    public void testProduct_CommonAcceptStates() {
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

        DFA productDFA = DFA.product(dfa1.completeDFA(), dfa2.completeDFA());

        assertEquals(Set.of("sink,sink_1", "p1,q1", "p2,q2", "p0,q0"), productDFA.getStates());
        assertEquals(Set.of("a", "b"), productDFA.getAlphabet());
        assertEquals("p0,q0", productDFA.getInitialState());
        assertEquals(Set.of("p2,q2"), productDFA.getAcceptStates());
        assertEquals(Map.of(
                Map.of("sink,sink_1", "b"), "sink,sink_1",
                Map.of("sink,sink_1", "a"), "sink,sink_1",
                Map.of("p1,q1", "b"), "p2,q2",
                Map.of("p2,q2", "b"), "sink,sink_1",
                Map.of("p0,q0", "b"), "sink,sink_1",
                Map.of("p0,q0", "a"), "p1,q1",
                Map.of("p1,q1", "a"), "sink,sink_1",
                Map.of("p2,q2", "a"), "sink,sink_1"
        ), productDFA.getTransitionFunction());
    }

    @Test
    public void testProduct_DifferentAlphabets() {
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

        assertThrows(IllegalArgumentException.class, () -> DFA.product(dfa1, dfa2),
                "DFAs with different alphabets should throw IllegalArgumentException.");
    }

    @Test
    public void testProduct_NoCommonAcceptStates() {
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

        DFA productDFA = DFA.product(dfa1.completeDFA(), dfa2.completeDFA());

        assertEquals(Set.of("p1,q1", "p2,q2", "p0,q0", "p2,sink"), productDFA.getStates());
        assertEquals(Set.of("a"), productDFA.getAlphabet());
        assertEquals("p0,q0", productDFA.getInitialState());
        assertEquals(Set.of(), productDFA.getAcceptStates());
        assertEquals(Map.of(
                Map.of("p2,sink", "a"), "p2,sink",
                Map.of("p0,q0", "a"), "p1,q1",
                Map.of("p1,q1", "a"), "p2,q2",
                Map.of("p2,q2", "a"), "p2,sink"
        ), productDFA.getTransitionFunction());
    }

    @Test
    public void testProduct_SingleStateDFAs() {
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

        DFA productDFA = DFA.product(dfa1.completeDFA(), dfa2.completeDFA());

        assertEquals(Set.of("p0,q0", "sink,sink_1"), productDFA.getStates());
        assertEquals(Set.of("a"), productDFA.getAlphabet());
        assertEquals("p0,q0", productDFA.getInitialState());
        assertEquals(Set.of("p0,q0"), productDFA.getAcceptStates());
        assertEquals(Map.of(
                Map.of("sink,sink_1", "a"), "sink,sink_1",
                Map.of("p0,q0", "a"), "sink,sink_1"
        ), productDFA.getTransitionFunction());
    }
}
