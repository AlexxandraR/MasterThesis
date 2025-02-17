package NFA;

import org.example.Automaton;
import org.example.FileReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileReaderTests {

    @Test
    public void testWrongFile(){
        String fileName = "automatoon.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertEquals("Failed to load file.", msg);
    }

    @Test
    public void testIncorrectFileArrow(){
        String fileName = "automaton4.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertEquals("Incorrect format of file: This line: bd,q1-/>q0 is incorrect.", msg);
    }

    @Test
    public void testIncorrectFileComma(){
        String fileName = "automaton10.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertEquals("Incorrect format of file: This line: bdq1->q0 is incorrect.", msg);
    }

    @Test
    public void testCorrectFileWithTwoAcceptingStatesAndEmptyLines(){
        String fileName = "automaton.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertNull(msg);
    }

    @Test
    public void testCorrectFileWithBiggerSymbolsAndSpaces(){
        String fileName = "automaton5.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertNull(msg);
    }

    @Test
    public void testCorrectFileWithTwoEpsilonTransitions(){
        String fileName = "automaton1.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertNull(msg);
    }

    @Test
    public void testCorrectFileWithNoInitialState(){
        String fileName = "automaton2.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertNull(msg);
    }

    @Test
    public void testCorrectFileWithNoAcceptingState(){
        String fileName = "automaton3.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertNull(msg);
    }

    @Test
    public void testCorrectFileWithArrowSymbol(){
        String fileName = "automaton8.txt";
        Automaton automaton = new Automaton();
        FileReader f = new FileReader();
        String msg = f.readText(fileName, automaton);
        assertNull(msg);
    }
}
