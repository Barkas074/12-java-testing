import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerTest {
    @Test
    @DisplayName("Empty parameter")
    void emptyParameter() {
        Throwable throwable = assertThrows(NumberFormatException.class, () -> Integer.decode(""));
        assertEquals("Zero length string", throwable.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Test with Argument = {0}")
    @ValueSource(strings = {"+5", "-5", "-0", "-2997", "54878"})
    @DisplayName("Correct decoding signs")
    void correctDecodingOfTheMinus(String str) {
        assertEquals(Integer.valueOf(str), Integer.decode(str));
    }

    @ParameterizedTest(name = "#{index} - Test with Argument = {0}")
    @ValueSource(strings = {"++5", "--5", "-+0", "+-2997"})
    @DisplayName("Decoding multiple signs")
    void decodingMultipleSigns(String str) {
        Throwable throwable = assertThrows(NumberFormatException.class, () -> Integer.decode(str));
        assertEquals("Sign character in wrong position", throwable.getMessage());
    }

    @Test
    @DisplayName("Correct decoding hex digits")
    void correctDecodingHexDigits() {
        assertAll("HexDigits",
                () -> assertEquals(Integer.valueOf("124", 16), Integer.decode("0x124")),
                () -> assertEquals(Integer.valueOf("D4", 16), Integer.decode("0XD4")),
                () -> assertEquals(Integer.valueOf("11F", 16), Integer.decode("#11F")),
                () -> assertEquals(Integer.valueOf("0", 16), Integer.decode("0x0")));
    }

    @Test
    @DisplayName("Correct decoding octal digits")
    void correctDecodingOctalDigits() {
        assertAll("OctalDigits",
                () -> assertEquals(Integer.valueOf("124", 8), Integer.decode("0124")),
                () -> assertEquals(Integer.valueOf("74", 8), Integer.decode("074")),
                () -> assertEquals(Integer.valueOf("115", 8), Integer.decode("0115")),
                () -> assertEquals(Integer.valueOf("10", 8), Integer.decode("0010")));
    }

    @ParameterizedTest(name = "#{index} - Test with Argument = {0}")
    @ValueSource(strings = {"-2147483649", "2147483648"})
    @DisplayName("Decoding less MIN_VALUE and more MAX_VALUE")
    void decodingLessMinAndMoreMaxValue(String str) {
        Throwable throwable = assertThrows(NumberFormatException.class, () -> Integer.decode(str));
        assertEquals(String.format("For input string: \"%s\"", str), throwable.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Test with Argument = {0}")
    @ValueSource(strings = {"-2147483648", "2147483647"})
    @DisplayName("Decoding MIN_VALUE and MAX_VALUE")
    void decodingMinAndMaxValue(String str) {
        assertEquals(Integer.valueOf(str), Integer.decode(str));
    }

    @ParameterizedTest(name = "#{index} - Test with Argument = {0}")
    @ValueSource(strings = {"test", "454s5414", "rona1"})
    @DisplayName("Decoding string")
    void decodingString(String str) {
        Throwable throwable = assertThrows(NumberFormatException.class, () -> Integer.decode(str));
        assertEquals(String.format("For input string: \"%s\"", str), throwable.getMessage());
    }
}
