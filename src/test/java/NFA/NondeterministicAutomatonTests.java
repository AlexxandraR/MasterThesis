package NFA;

import org.example.Automaton;
import org.example.DFA;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NondeterministicAutomatonTests {
    @Test
    public void testDeterministicAutomatonNotComplete() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q0", "b"), Set.of("q0"));
        transitionFunction.put(Map.of("q1", "b"), Set.of("q0"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(automaton.isDeterministic());
    }

    @Test
    public void testNonDeterministicAutomaton() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), Set.of("q1", "q0"));
        transitionFunction.put(Map.of("q0", "b"), Set.of("q0"));
        transitionFunction.put(Map.of("q1", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q1", "b"), Set.of("q0"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(automaton.isDeterministic());
    }

    @Test
    public void testAutomatonWithEpsilonTransition() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", ""), Set.of("q1"));
        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q0", "b"), Set.of("q0"));
        transitionFunction.put(Map.of("q1", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q1", "b"), Set.of("q0"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(automaton.isDeterministic());
    }

    @Test
    public void testAutomatonWithEmptyAlphabetAndEpsilonTransition() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of();
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", ""), Set.of("q1"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        assertFalse(automaton.isDeterministic());
    }

    @Test
    public void testAutomatonWithEmptyAlphabetAndNoTransitions() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of();
        String startState = "q0";
        Set<String> acceptStates = Set.of("q0", "q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        assertTrue(automaton.isDeterministic());
    }

    @Test
    public void testToDFAWithDeterministicAutomaton() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = Set.of("a", "b");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q0", "b"), Set.of("q0"));
        transitionFunction.put(Map.of("q1", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q1", "b"), Set.of("q0"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA dfa = automaton.toDFA();

        assertEquals(states, dfa.getStates());
        assertEquals(alphabet, dfa.getAlphabet());
        assertEquals(startState, dfa.getInitialState());
        assertEquals(acceptStates, dfa.getAcceptStates());

        Map<Map<String, String>, String> expectedDFATransitions = new HashMap<>();
        expectedDFATransitions.put(Map.of("q0", "a"), "q1");
        expectedDFATransitions.put(Map.of("q0", "b"), "q0");
        expectedDFATransitions.put(Map.of("q1", "a"), "q1");
        expectedDFATransitions.put(Map.of("q1", "b"), "q0");

        assertEquals(expectedDFATransitions, dfa.getTransitionFunction());
    }

    @Test
    public void testDeterministicNotCompleteAutomaton() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3");
        Set<String> alphabet = Set.of("0", "1");
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "1"), Set.of("q1"));
        transitionFunction.put(Map.of("q0", "0"), Set.of("q3"));
        transitionFunction.put(Map.of("q1", "1"), Set.of("q1"));
        transitionFunction.put(Map.of("q1", "0"), Set.of("q2"));
        transitionFunction.put(Map.of("q2", "1"), Set.of("q1"));
        transitionFunction.put(Map.of("q2", "0"), Set.of("q2"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.toDFA();

        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0", "1"), "q1");
        newTransitionFunction.put(Map.of("q0", "0"), "q3");
        newTransitionFunction.put(Map.of("q1", "1"), "q1");
        newTransitionFunction.put(Map.of("q1", "0"), "q2");
        newTransitionFunction.put(Map.of("q2", "1"), "q1");
        newTransitionFunction.put(Map.of("q2", "0"), "q2");

        assertEquals(convertedAutomaton.getStates(), states);
        assertEquals(convertedAutomaton.getAlphabet(), alphabet);
        assertEquals(startState, convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), acceptStates);
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }
}
