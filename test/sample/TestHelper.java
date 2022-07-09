package sample;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.fail;

public class TestHelper {
    private static PrintStream originalOutput;
    private static InputStream originalInput;
    private static ByteArrayOutputStream outputStream;
    static final Random random = new Random();

    static Matcher check(Pattern pattern) {
        var result = outputStream.toString();
        var matcher = pattern.matcher(result);
        if (!matcher.matches()) {
            fail("formatting doesn't match requirements:\n" + result);
        }

        return matcher;
    }

    static void updateSystemIn(String userInput) {
        originalInput = System.in;
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        textio.TextIO.readStream(System.in);
    }

    static void updateSystemOut() {
        originalOutput = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        textio.TextIO.writeStream(System.out);
    }

    static void resetSystemIO() {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }
}
