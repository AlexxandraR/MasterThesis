package DFA;

import org.example.Automaton;
import org.example.DFA;
import org.example.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class IsCompositeTest {
    private DFA automaton;

    @BeforeEach
    void setUp() {
        Set<String> states = Set.of("q0", "q1", "q2", "q3");
        Set<String> alphabet = Set.of("a", "b");
        String initialState = "q0";
        Set<String> acceptStates = Set.of("q3");

        Map<Map<String, String>, String> transitionFunction = new HashMap<>();
        transitionFunction.put(Map.of("q0", "a"), "q1");
        transitionFunction.put(Map.of("q1", "a"), "q2");
        transitionFunction.put(Map.of("q2", "a"), "q3");
        transitionFunction.put(Map.of("q3", "a"), "q0");
        transitionFunction.put(Map.of("q0", "b"), "q2");
        transitionFunction.put(Map.of("q1", "b"), "q3");
        transitionFunction.put(Map.of("q2", "b"), "q0");
        transitionFunction.put(Map.of("q3", "b"), "q1");

        automaton = new DFA(states, alphabet, initialState, acceptStates, transitionFunction);
    }

    @Test
    void testIsCompositeMemory1() {
        assertFalse(automaton.isCompositeMemory());
    }

    @Test
    void testCoverMemory1() {
        Set<String> subset = Set.of("q0", "q1");
        Set<Set<String>> processedOrbits = new HashSet<>();

        Set<String> covered = automaton.coverMemory(subset, processedOrbits);

        assertEquals(Set.of(), covered);
    }

    @Test
    void testIsCompositeMemory2() {
        String fileName = "automaton15.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
            DFA dfa = automaton.toDFA();
            assertFalse(dfa.isCompositeMemory());
            assertFalse(dfa.isCompositeTime());
            assertFalse(dfa.isComposite());
        }
    }

    @Test
    void testIsCompositeMemory3() {
        String fileName = "automaton17.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
            DFA dfa = automaton.toDFA();
            assertFalse(dfa.isCompositeMemory());
            assertFalse(dfa.isCompositeTime());
            assertFalse(dfa.isComposite());
        }
    }

    @Test
    void testCoverMemory3() {
        String fileName = "automaton17.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
            DFA dfa = automaton.toDFA();
            Set<String> subset = Set.of("q0", "q1");
            Set<Set<String>> processedOrbits = new HashSet<>();
            Set<String> covered = dfa.coverMemory(subset, processedOrbits);
            assertEquals(Set.of("q0", "q1"), covered);
        }
    }

    @Test
    void testIsCompositeMemory4() {
        String fileName = "automaton20.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
            DFA dfa = automaton.toDFA();
            assertFalse(dfa.isCompositeMemory());
            assertFalse(dfa.isCompositeTime());
            assertFalse(dfa.isComposite());
        }
    }

    @Test
    void testIsCompositeMemory5() {
        String fileName = "automaton18.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
            DFA dfa = automaton.toDFA();
            assertTrue(dfa.isCompositeMemory());
            assertTrue(dfa.isCompositeTime());
            assertTrue(dfa.isComposite());
        }
    }

    @Test
    void testCoverMemory4() {
        String fileName = "automaton18.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        if(msg == null){
            DFA dfa = automaton.toDFA();
            Set<String> subset = Set.of("q1", "q2", "q3");
            Set<Set<String>> processedOrbits = new HashSet<>();
            Set<String> covered = dfa.coverMemory(subset, processedOrbits);
            assertEquals(Set.of("q1", "q2", "q3", "q4", "q6"), covered);
        }
    }
}
