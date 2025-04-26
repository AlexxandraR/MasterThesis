package DFA;

import org.example.Automaton;
import org.example.DFA;
import org.example.FileReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PrimalityCheckingTests {
    @Test
    public void testPrimeAutomaton15() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton15.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertTrue(dfa.isPrimeDFA());
        assertFalse(dfa.isComposite());
        assertFalse(dfa.isCompositeTime());
        assertFalse(dfa.isCompositeMemory());
        assertFalse(dfa.isCompositeInitial());
        assertFalse(dfa.isCompositeInitialMemory());
        assertFalse(dfa.isCompositeCommutative());
        assertFalse(dfa.isCompositeCommutativeSavedWord());
        assertFalse(dfa.isCompositeCommutativeBigAlphabet());
    }

    @Test
    public void testPrimeAutomaton20() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton20.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertFalse(dfa.isComposite());
        assertFalse(dfa.isCompositeTime());
        assertFalse(dfa.isCompositeMemory());
        assertFalse(dfa.isCompositeInitial());
        assertFalse(dfa.isCompositeInitialMemory());
        assertFalse(dfa.isCompositeCommutative());
        assertFalse(dfa.isCompositeCommutativeSavedWord());
        assertFalse(dfa.isCompositeCommutativeBigAlphabet());
    }

    @Test
    public void testPrimeAutomaton17() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton17.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertFalse(dfa.isComposite());
        assertFalse(dfa.isCompositeTime());
        assertFalse(dfa.isCompositeMemory());
        assertFalse(dfa.isCompositeInitial());
        assertFalse(dfa.isCompositeInitialMemory());
        assertFalse(dfa.isCompositeCommutative());
        assertFalse(dfa.isCompositeCommutativeSavedWord());
        assertFalse(dfa.isCompositeCommutativeBigAlphabet());
    }

    @Test
    public void testPrimeAutomaton2_1() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton2_1.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertFalse(dfa.isComposite());
        assertFalse(dfa.isCompositeTime());
        assertFalse(dfa.isCompositeMemory());
        assertFalse(dfa.isCompositeInitial());
        assertFalse(dfa.isCompositeInitialMemory());
        assertFalse(dfa.isCompositeCommutative());
        assertFalse(dfa.isCompositeCommutativeSavedWord());
        assertFalse(dfa.isCompositeCommutativeBigAlphabet());
    }

    @Test
    public void testPrimeAutomaton2_2() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton2_2.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertFalse(dfa.isComposite());
        assertFalse(dfa.isCompositeTime());
        assertFalse(dfa.isCompositeMemory());
        assertFalse(dfa.isCompositeInitial());
        assertFalse(dfa.isCompositeInitialMemory());
        assertFalse(dfa.isCompositeCommutative());
        assertFalse(dfa.isCompositeCommutativeSavedWord());
        assertFalse(dfa.isCompositeCommutativeBigAlphabet());
    }

    @Test
    public void testCompositeAutomaton18() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton18.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertTrue(dfa.isComposite());
        assertTrue(dfa.isCompositeTime());
        assertTrue(dfa.isCompositeMemory());
        assertTrue(dfa.isCompositeInitial());
        assertTrue(dfa.isCompositeInitialMemory());
    }

    @Test
    public void testCompositeAutomaton2_9() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton2_9.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertTrue(dfa.isComposite());
        assertTrue(dfa.isCompositeTime());
        assertTrue(dfa.isCompositeMemory());
        assertTrue(dfa.isCompositeInitial());
        assertTrue(dfa.isCompositeInitialMemory());
        assertTrue(dfa.isCompositeCommutative());
        assertTrue(dfa.isCompositeCommutativeSavedWord());
        assertTrue(dfa.isCompositeCommutativeSavedWord());
    }

    @Test
    public void testCompositeAutomaton2_10() {
        FileReader f = new FileReader();
        Automaton automaton = new Automaton();
        f.readText("automaton2_10.txt", automaton);
        DFA dfa = automaton.toDFA();

        assertTrue(dfa.isComposite());
        assertTrue(dfa.isCompositeTime());
        assertTrue(dfa.isCompositeMemory());
        assertTrue(dfa.isCompositeInitial());
        assertTrue(dfa.isCompositeInitialMemory());
        assertTrue(dfa.isCompositeCommutative());
        assertTrue(dfa.isCompositeCommutativeSavedWord());
        assertTrue(dfa.isCompositeCommutativeSavedWord());
    }
}
