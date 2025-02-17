package NFA;

import org.example.Automaton;
import org.example.DFA;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeterminizationTests {

    @Test
    public void testOne() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3", "q4");
        Set<String> alphabet = new HashSet<>(Set.of("a", "b"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2", "q4");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        Set<String> value = new HashSet<>();
        value.add("q1");
        transitionFunction.put(Map.of("q0", ""), value);
        transitionFunction.get(Map.of("q0", "")).add("q3");
        transitionFunction.put(Map.of("q1", "a"), Set.of("q2"));
        transitionFunction.put(Map.of("q3", "b"), Set.of("q4"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q0,q1,q3", "q2", "q4"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a", "b"));
        assertEquals("q0,q1,q3", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q2", "q4"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0,q1,q3", "a"), "q2");
        newTransitionFunction.put(Map.of("q0,q1,q3", "b"), "q4");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testTwo() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3");
        Set<String> alphabet = new HashSet<>(Set.of("a", "b"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q3");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q1", ""), Set.of("q2"));
        transitionFunction.put(Map.of("q2", "b"), Set.of("q3"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q1,q2", "q0", "q3"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a", "b"));
        assertEquals("q0", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q3"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0", "a"), "q1,q2");
        newTransitionFunction.put(Map.of("q1,q2", "b"), "q3");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testThree() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = new HashSet<>(Set.of("a"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q0", "q2");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", ""), Set.of("q1"));
        transitionFunction.put(Map.of("q1", "a"), Set.of("q2"));
        transitionFunction.put(Map.of("q2", ""), Set.of("q1"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q1,q2", "q0,q1"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a"));
        assertEquals("q0,q1", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q1,q2", "q0,q1"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0,q1", "a"), "q1,q2");
        newTransitionFunction.put(Map.of("q1,q2", "a"), "q1,q2");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testFour() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3");
        Set<String> alphabet = new HashSet<>(Set.of("a"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2", "q3");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        Set<String> value = new HashSet<>();
        value.add("q1");
        transitionFunction.put(Map.of("q0", ""), value);
        transitionFunction.get(Map.of("q0", "")).add("q3");
        transitionFunction.put(Map.of("q1", "a"), Set.of("q2"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q2", "q0,q1,q3"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a"));
        assertEquals("q0,q1,q3", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q2", "q0,q1,q3"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0,q1,q3", "a"), "q2");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testFive() {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<String> alphabet = new HashSet<>(Set.of("a"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q2");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), Set.of("q1"));
        transitionFunction.put(Map.of("q1", ""), Set.of("q2"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q0", "q1,q2"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a"));
        assertEquals("q0", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q1,q2"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0", "a"), "q1,q2");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testSix() {
        Set<String> states = Set.of("q0", "q1");
        Set<String> alphabet = new HashSet<>();
        String startState = "q0";
        Set<String> acceptStates = Set.of("q0", "q1");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", ""), Set.of("q1"));
        transitionFunction.put(Map.of("q1", ""), Set.of("q1"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q0,q1"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of());
        assertEquals("q0,q1", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q0,q1"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testSeven() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q12");
        Set<String> alphabet = new HashSet<>(Set.of("a", "c"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q1", "q8", "q10", "q12");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        Set<String> value = new HashSet<>();
        value.add("q1");
        transitionFunction.put(Map.of("q0", ""), value);
        transitionFunction.get(Map.of("q0", "")).add("q9");
        transitionFunction.put(Map.of("q2", "c"), Set.of("q3"));
        transitionFunction.put(Map.of("q3", ""), Set.of("q4"));
        transitionFunction.put(Map.of("q4", ""), Set.of("q5"));
        transitionFunction.put(Map.of("q5", "c"), Set.of("q6"));
        transitionFunction.put(Map.of("q6", ""), Set.of("q7"));
        transitionFunction.put(Map.of("q7", "a"), Set.of("q8"));
        transitionFunction.put(Map.of("q8", ""), Set.of("q2"));
        transitionFunction.put(Map.of("q1", ""), Set.of("q2"));
        Set<String> value1 = new HashSet<>();
        value1.add("q10");
        transitionFunction.put(Map.of("q9", ""), value1);
        transitionFunction.get(Map.of("q9", "")).add("q11");
        transitionFunction.put(Map.of("q11", "a"), Set.of("q12"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q2,q8", "q0,q1,q10,q11,q2,q9", "q6,q7", "q12", "q3,q4,q5"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a", "c"));
        assertEquals("q0,q1,q10,q11,q2,q9", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q2,q8", "q12", "q0,q1,q10,q11,q2,q9"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0,q1,q10,q11,q2,q9", "a"), "q12");
        newTransitionFunction.put(Map.of("q0,q1,q10,q11,q2,q9", "c"), "q3,q4,q5");
        newTransitionFunction.put(Map.of("q3,q4,q5", "c"), "q6,q7");
        newTransitionFunction.put(Map.of("q6,q7", "a"), "q2,q8");
        newTransitionFunction.put(Map.of("q2,q8", "c"), "q3,q4,q5");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testEight() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q12", "q13", "q14");
        Set<String> alphabet = new HashSet<>(Set.of("a", "b", "c"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q14");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q1", "b"), Set.of("q2"));
        transitionFunction.put(Map.of("q2", ""), Set.of("q3"));
        Set<String> value = new HashSet<>();
        value.add("q1");
        transitionFunction.put(Map.of("q3", ""), value);
        transitionFunction.get(Map.of("q3", "")).add("q4");
        transitionFunction.put(Map.of("q0", ""), value);
        transitionFunction.put(Map.of("q5", "c"), Set.of("q6"));
        transitionFunction.put(Map.of("q6", ""), Set.of("q7"));
        transitionFunction.put(Map.of("q7", "a"), Set.of("q8"));
        Set<String> value1 = new HashSet<>();
        value1.add("q5");
        transitionFunction.put(Map.of("q8", ""), value1);
        transitionFunction.get(Map.of("q8", "")).add("q9");
        transitionFunction.put(Map.of("q4", ""), value1);
        transitionFunction.put(Map.of("q9", "c"), Set.of("q10"));
        transitionFunction.put(Map.of("q10", ""), Set.of("q11"));
        transitionFunction.put(Map.of("q12", "b"), Set.of("q13"));
        Set<String> value2 = new HashSet<>();
        value2.add("q12");
        transitionFunction.put(Map.of("q13", ""), value2);
        transitionFunction.get(Map.of("q13", "")).add("q14");
        transitionFunction.put(Map.of("q11", ""), value2);

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q1,q2,q3,q4,q5,q9", "q10,q11,q12,q14,q6,q7", "q5,q8,q9", "q12,q13,q14", "q0,q1,q4,q5,q9"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a", "b", "c"));
        assertEquals("q0,q1,q4,q5,q9", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q12,q13,q14", "q10,q11,q12,q14,q6,q7"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0,q1,q4,q5,q9", "c"), "q10,q11,q12,q14,q6,q7");
        newTransitionFunction.put(Map.of("q0,q1,q4,q5,q9", "b"), "q1,q2,q3,q4,q5,q9");
        newTransitionFunction.put(Map.of("q10,q11,q12,q14,q6,q7", "a"), "q5,q8,q9");
        newTransitionFunction.put(Map.of("q10,q11,q12,q14,q6,q7", "b"), "q12,q13,q14");
        newTransitionFunction.put(Map.of("q1,q2,q3,q4,q5,q9", "c"), "q10,q11,q12,q14,q6,q7");
        newTransitionFunction.put(Map.of("q1,q2,q3,q4,q5,q9", "b"), "q1,q2,q3,q4,q5,q9");
        newTransitionFunction.put(Map.of("q5,q8,q9", "c"), "q10,q11,q12,q14,q6,q7");
        newTransitionFunction.put(Map.of("q12,q13,q14", "b"), "q12,q13,q14");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

    @Test
    public void testTen() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q12", "q13", "q14");
        Set<String> alphabet = new HashSet<>(Set.of("a", "b", "c"));
        String startState = "q0";
        Set<String> acceptStates = Set.of("q5", "q12", "q7", "q14");

        Map<Map<String, String>, Set<String>> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q2", "a"), Set.of("q3"));
        Set<String> value = new HashSet<>();
        value.add("q2");
        transitionFunction.put(Map.of("q3", ""), value);
        transitionFunction.get(Map.of("q3", "")).add("q4");
        transitionFunction.put(Map.of("q1", ""), value);
        Set<String> value3 = new HashSet<>();
        value3.add("q1");
        transitionFunction.put(Map.of("q0", ""), value3);
        transitionFunction.get(Map.of("q0", "")).add("q6");
        transitionFunction.put(Map.of("q4", "c"), Set.of("q5"));
        Set<String> value4 = new HashSet<>();
        value4.add("q7");
        transitionFunction.put(Map.of("q6", ""), value4);
        transitionFunction.get(Map.of("q6", "")).add("q13");
        transitionFunction.put(Map.of("q8", "c"), Set.of("q9"));
        transitionFunction.put(Map.of("q9", ""), Set.of("q10"));
        transitionFunction.put(Map.of("q10", ""), Set.of("q11"));
        transitionFunction.put(Map.of("q11", "a"), Set.of("q12"));
        transitionFunction.put(Map.of("q12", ""), Set.of("q8"));
        transitionFunction.put(Map.of("q7", ""), Set.of("q8"));
        transitionFunction.put(Map.of("q13", "b"), Set.of("q14"));

        Automaton automaton = new Automaton(states, alphabet, startState, acceptStates, transitionFunction);

        DFA convertedAutomaton = automaton.convertToDeterministic();

        assertEquals(convertedAutomaton.getStates(), Set.of("q2,q3,q4", "q5", "q12,q8", "q10,q11,q5,q9", "q14", "q10,q11,q9", "q0,q1,q13,q2,q4,q6,q7,q8"));
        assertEquals(convertedAutomaton.getAlphabet(), Set.of("a", "b", "c"));
        assertEquals("q0,q1,q13,q2,q4,q6,q7,q8", convertedAutomaton.getInitialState());
        assertEquals(convertedAutomaton.getAcceptStates(), Set.of("q5", "q12,q8", "q10,q11,q5,q9", "q14", "q0,q1,q13,q2,q4,q6,q7,q8"));
        Map<Map<String, String>, String> newTransitionFunction = new HashMap<>();
        newTransitionFunction.put(Map.of("q0,q1,q13,q2,q4,q6,q7,q8", "a"), "q2,q3,q4");
        newTransitionFunction.put(Map.of("q0,q1,q13,q2,q4,q6,q7,q8", "c"), "q10,q11,q5,q9");
        newTransitionFunction.put(Map.of("q0,q1,q13,q2,q4,q6,q7,q8", "b"), "q14");
        newTransitionFunction.put(Map.of("q2,q3,q4", "a"), "q2,q3,q4");
        newTransitionFunction.put(Map.of("q2,q3,q4", "c"), "q5");
        newTransitionFunction.put(Map.of("q10,q11,q5,q9", "a"), "q12,q8");
        newTransitionFunction.put(Map.of("q12,q8", "c"), "q10,q11,q9");
        newTransitionFunction.put(Map.of("q10,q11,q9", "a"), "q12,q8");
        assertEquals(convertedAutomaton.getTransitionFunction(), newTransitionFunction);
    }

}
