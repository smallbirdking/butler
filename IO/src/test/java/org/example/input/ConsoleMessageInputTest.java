package org.example.input;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleMessageInputTest {

    @Test
    void consoleInputReturnsEnteredMessage() {
        String input = "Hello, World!";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String result = ConsoleMessageInput.consoleInput();
        assertEquals("Hello, World!", result);
    }

    @Test
    void consoleInputHandlesEmptyInput() {
        String input = "";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String result = ConsoleMessageInput.consoleInput();
        assertEquals("", result);
    }

    @Test
    void consoleInputHandlesWhitespaceInput() {
        String input = "   ";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String result = ConsoleMessageInput.consoleInput();
        assertEquals("", result);
    }

    @Test
    void consoleInputHandlesSpecialCharacters() {
        String input = "!@#$%^&*()";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String result = ConsoleMessageInput.consoleInput();
        assertEquals("!@#$%^&*()", result);
    }
}
