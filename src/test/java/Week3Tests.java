import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class Week3Tests {
    @ParameterizedTest
    @ValueSource(strings = {"Short string", "Random very long string"})
    public void testStringLength(String testString) {
        assertTrue(testString.length() > 15);
    }

}
