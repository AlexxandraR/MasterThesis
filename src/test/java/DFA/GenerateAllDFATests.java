package DFA;

import org.example.DFA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateAllDFATests {
    private DFA originalDfa;

    @BeforeEach
    void setUp() {
        Set<String> states = new HashSet<>();
        states.add("q0");
        states.add("q1");
        states.add("q2");

        Set<String> alphabet = new HashSet<>();
        alphabet.add("a");

        String initialState = "q0";

        Set<String> acceptStates = new HashSet<>();
        acceptStates.add("q1");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q2");
        transitionFunction.put(Map.of("q2", "a"), "q2");

        originalDfa = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);
    }

    @Test
    void testGenerateAllDFAsWithNoStates() {
        // Test for zero states
        Set<DFA> generatedDFAs = originalDfa.generateAllDFAs(0);

        assertTrue(generatedDFAs.isEmpty(), "Generated DFAs set should be empty when there are no states.");
    }

    @Test
    void testGenerateAllDFAsWithOneState() {
        int numStates = 1;

        Set<DFA> generatedDFAs = originalDfa.generateAllDFAs(numStates);

        assertEquals(1, generatedDFAs.size(), "With one state, there should be one valid DFA generated.");

        assertEquals(Set.of("q0_1"), generatedDFAs.iterator().next().getStates());
        assertEquals(Set.of("a"), generatedDFAs.iterator().next().getAlphabet());
        assertEquals("q0_1", generatedDFAs.iterator().next().getInitialState());
        assertEquals(Set.of("q0_1"), generatedDFAs.iterator().next().getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q0_1"), generatedDFAs.iterator().next().getTransitionFunction());
    }

    @Test
    void testGenerateAllDFAsWithTwoStates() {
        // Test with two states
        int numStates = 2;

        Set<DFA> generatedDFAs = originalDfa.generateAllDFAs(numStates);

        assertEquals(8, generatedDFAs.size(), "With two states, there should be two valid DFAs generated.");

        List<DFA> dfas = generatedDFAs.stream().toList();

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(0).getStates());
        assertEquals(Set.of("a"), dfas.get(0).getAlphabet());
        assertEquals("q0_1", dfas.get(0).getInitialState());
        assertEquals(Set.of("q0_1"), dfas.get(0).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q0_1", Map.of("q1_1", "a"), "q0_1"), dfas.get(0).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(1).getStates());
        assertEquals(Set.of("a"), dfas.get(1).getAlphabet());
        assertEquals("q0_1", dfas.get(1).getInitialState());
        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(1).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q0_1", Map.of("q1_1", "a"), "q1_1"), dfas.get(1).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(2).getStates());
        assertEquals(Set.of("a"), dfas.get(2).getAlphabet());
        assertEquals("q0_1", dfas.get(2).getInitialState());
        assertEquals(Set.of("q1_1", "q0_1"), dfas.get(2).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q1_1", Map.of("q1_1", "a"), "q1_1"), dfas.get(2).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(3).getStates());
        assertEquals(Set.of("a"), dfas.get(3).getAlphabet());
        assertEquals("q0_1", dfas.get(3).getInitialState());
        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(3).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q0_1", Map.of("q1_1", "a"), "q0_1"), dfas.get(3).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(4).getStates());
        assertEquals(Set.of("a"), dfas.get(4).getAlphabet());
        assertEquals("q0_1", dfas.get(4).getInitialState());
        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(4).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q1_1", Map.of("q1_1", "a"), "q0_1"), dfas.get(4).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(5).getStates());
        assertEquals(Set.of("a"), dfas.get(5).getAlphabet());
        assertEquals("q0_1", dfas.get(5).getInitialState());
        assertEquals(Set.of("q1_1"), dfas.get(5).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q1_1", Map.of("q1_1", "a"), "q1_1"), dfas.get(5).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(6).getStates());
        assertEquals(Set.of("a"), dfas.get(6).getAlphabet());
        assertEquals("q0_1", dfas.get(6).getInitialState());
        assertEquals(Set.of("q1_1"), dfas.get(6).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q1_1", Map.of("q1_1", "a"), "q0_1"), dfas.get(6).getTransitionFunction());

        assertEquals(Set.of("q0_1", "q1_1"), dfas.get(7).getStates());
        assertEquals(Set.of("a"), dfas.get(7).getAlphabet());
        assertEquals("q0_1", dfas.get(7).getInitialState());
        assertEquals(Set.of("q0_1"), dfas.get(7).getAcceptStates());
        assertEquals(Map.of(Map.of("q0_1", "a"), "q0_1", Map.of("q1_1", "a"), "q1_1"), dfas.get(7).getTransitionFunction());

    }

    @Test
    void testGenerateAllDFAsNoReachableAcceptStates() {
        // Test to ensure no DFAs are generated when accept states are unreachable
        int numStates = 2;

        originalDfa.setAcceptStates(new HashSet<>());
        originalDfa = originalDfa.minimize().completeDFA();

        Set<DFA> generatedDFAs = originalDfa.generateAllDFAs(numStates);

        assertEquals(12, generatedDFAs.size());
    }
}
