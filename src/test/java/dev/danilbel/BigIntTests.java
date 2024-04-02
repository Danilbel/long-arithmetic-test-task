package dev.danilbel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BigIntTests {

    @ParameterizedTest
    @MethodSource("provideBigIntFromValidString")
    void testCreateBigIntFromValidString(String number, String expected) {
        BigInt bigInt = new BigInt(number);
        assertEquals(expected, bigInt.toString());
    }

    private static Stream<Arguments> provideBigIntFromValidString() {
        return Stream.of(
                Arguments.of("123", "123"),
                Arguments.of("-123", "-123"),
                Arguments.of("0", "0"),
                Arguments.of("123456789012345678901234567890", "123456789012345678901234567890"),
                Arguments.of("-123456789012345678901234567890", "-123456789012345678901234567890")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntFromInvalidString")
    void testCreateBigIntFromInvalidString(String number) {
        assertThrows(IllegalArgumentException.class, () -> new BigInt(number));
    }

    private static Stream<Arguments> provideBigIntFromInvalidString() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("abc"),
                Arguments.of("123.45"),
                Arguments.of("123 456"),
                Arguments.of("123456789012345678901234567890.0"),
                Arguments.of("123456789012345678901234567890,0")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntToString")
    void testBigIntToString(BigInt bigInt, String expected) {
        assertEquals(expected, bigInt.toString());
    }

    private static Stream<Arguments> provideBigIntToString() {
        return Stream.of(
                Arguments.of(new BigInt("123"), "123"),
                Arguments.of(new BigInt("-123"), "-123"),
                Arguments.of(new BigInt("0"), "0"),
                Arguments.of(new BigInt("123456789012345678901234567890"), "123456789012345678901234567890"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), "-123456789012345678901234567890")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntFromInt")
    void testCreateBigIntFromInt(int number, String expected) {
        BigInt bigInt = new BigInt(number);
        assertEquals(expected, bigInt.toString());
    }

    private static Stream<Arguments> provideBigIntFromInt() {
        return Stream.of(
                Arguments.of(123, "123"),
                Arguments.of(-123, "-123"),
                Arguments.of(0, "0"),
                Arguments.of(1234567890, "1234567890"),
                Arguments.of(-1234567890, "-1234567890")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntToInt")
    void testBigIntToInt(BigInt bigInt, int expected) {
        assertEquals(expected, bigInt.toInt());
    }

    private static Stream<Arguments> provideBigIntToInt() {
        return Stream.of(
                Arguments.of(new BigInt("123"), 123),
                Arguments.of(new BigInt("-123"), -123),
                Arguments.of(new BigInt("0"), 0),
                Arguments.of(new BigInt("1234567890"), 1234567890),
                Arguments.of(new BigInt("-1234567890"), -1234567890),
                Arguments.of(new BigInt("123456789012345678901234567890"), Integer.MAX_VALUE),
                Arguments.of(new BigInt("-123456789012345678901234567890"), Integer.MIN_VALUE)
        );
    }


    @ParameterizedTest
    @MethodSource("provideBigIntComparison")
    void testBigIntComparison(BigInt first, BigInt second,
                              boolean lessThan, boolean lessThanOrEquals,
                              boolean greaterThan, boolean greaterThanOrEquals,
                              boolean equalsTo) {
        assertEquals(lessThan, first.isLessThan(second));
        assertEquals(lessThanOrEquals, first.isLessThanOrEquals(second));
        assertEquals(greaterThan, first.isGreaterThan(second));
        assertEquals(greaterThanOrEquals, first.isGreaterThanOrEquals(second));
        assertEquals(equalsTo, first.isEqualsTo(second));
    }

    private static Stream<Arguments> provideBigIntComparison() {
        return Stream.of(
                // comparison with 0
                Arguments.of(new BigInt("0"), new BigInt("0"), false, true, false, true, true),
                Arguments.of(new BigInt("0"), new BigInt("1"), true, true, false, false, false),
                Arguments.of(new BigInt("1"), new BigInt("0"), false, false, true, true, false),
                Arguments.of(new BigInt("-1"), new BigInt("0"), true, true, false, false, false),
                Arguments.of(new BigInt("0"), new BigInt("-1"), false, false, true, true, false),
                // comparing numbers of the same length
                Arguments.of(new BigInt("100"), new BigInt("200"), true, true, false, false, false),
                Arguments.of(new BigInt("200"), new BigInt("100"), false, false, true, true, false),
                Arguments.of(new BigInt("-100"), new BigInt("-200"), false, false, true, true, false),
                Arguments.of(new BigInt("-200"), new BigInt("-100"), true, true, false, false, false),
                Arguments.of(new BigInt("100"), new BigInt("-200"), false, false, true, true, false),
                Arguments.of(new BigInt("-100"), new BigInt("200"), true, true, false, false, false),
                Arguments.of(new BigInt("200"), new BigInt("-100"), false, false, true, true, false),
                Arguments.of(new BigInt("-200"), new BigInt("100"), true, true, false, false, false),
                // comparing similar numbers
                Arguments.of(new BigInt("300"), new BigInt("300"), false, true, false, true, true),
                Arguments.of(new BigInt("-300"), new BigInt("-300"), false, true, false, true, true),
                // comparing numbers of different length
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("123456789012345678901234567890"), true, true, false, false, false),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("12345678901234567890"), false, false, true, true, false),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("-123456789012345678901234567890"), false, false, true, true, false),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("-12345678901234567890"), true, true, false, false, false),
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("-123456789012345678901234567890"), false, false, true, true, false),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("123456789012345678901234567890"), true, true, false, false, false),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("-12345678901234567890"), false, false, true, true, false),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("12345678901234567890"), true, true, false, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntNegation")
    void testBigIntNegation(BigInt bigInt, String expected) {
        BigInt result = bigInt.negate();
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntNegation() {
        return Stream.of(
                Arguments.of(new BigInt("123"), "-123"),
                Arguments.of(new BigInt("-123"), "123"),
                Arguments.of(new BigInt("0"), "0"),
                Arguments.of(new BigInt("123456789012345678901234567890"), "-123456789012345678901234567890"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), "123456789012345678901234567890")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntAbs")
    void testBigIntAbs(BigInt bigInt, String expected) {
        BigInt result = bigInt.abs();
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntAbs() {
        return Stream.of(
                Arguments.of(new BigInt("123"), "123"),
                Arguments.of(new BigInt("-123"), "123"),
                Arguments.of(new BigInt("0"), "0"),
                Arguments.of(new BigInt("123456789012345678901234567890"), "123456789012345678901234567890"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), "123456789012345678901234567890")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntAddition")
    void testBigIntAddition(BigInt first, BigInt second, String expected) {
        BigInt result = first.add(second);
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntAddition() {
        return Stream.of(
                // one of the numbers zero
                Arguments.of(new BigInt("0"), new BigInt("0"), "0"),
                Arguments.of(new BigInt("0"), new BigInt("5"), "5"),
                Arguments.of(new BigInt("5"), new BigInt("0"), "5"),
                Arguments.of(new BigInt("-5"), new BigInt("0"), "-5"),
                Arguments.of(new BigInt("0"), new BigInt("-5"), "-5"),
                // equal numbers modulo different signs
                Arguments.of(new BigInt("5"), new BigInt("-5"), "0"),
                Arguments.of(new BigInt("-5"), new BigInt("5"), "0"),
                // numbers of the same length
                Arguments.of(new BigInt("5"), new BigInt("3"), "8"),
                Arguments.of(new BigInt("5"), new BigInt("-3"), "2"),
                Arguments.of(new BigInt("-5"), new BigInt("3"), "-2"),
                Arguments.of(new BigInt("-5"), new BigInt("-3"), "-8"),
                // numbers of different length
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("123456789012345678901234567890"), "123456789024691357802469135780"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("12345678901234567890"), "123456789024691357802469135780"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("-123456789012345678901234567890"), "-123456789024691357802469135780"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("-12345678901234567890"), "-123456789024691357802469135780"),
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("-123456789012345678901234567890"), "-123456789000000000000000000000"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("123456789012345678901234567890"), "123456789000000000000000000000"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("-12345678901234567890"), "123456789000000000000000000000"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("12345678901234567890"), "-123456789000000000000000000000")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntSubtraction")
    void testBigIntSubtraction(BigInt first, BigInt second, String expected) {
        BigInt result = first.subtract(second);
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntSubtraction() {
        return Stream.of(
                // one of the numbers zero
                Arguments.of(new BigInt("0"), new BigInt("0"), "0"),
                Arguments.of(new BigInt("0"), new BigInt("5"), "-5"),
                Arguments.of(new BigInt("5"), new BigInt("0"), "5"),
                Arguments.of(new BigInt("-5"), new BigInt("0"), "-5"),
                Arguments.of(new BigInt("0"), new BigInt("-5"), "5"),
                // equal numbers modulo different signs
                Arguments.of(new BigInt("5"), new BigInt("-5"), "10"),
                Arguments.of(new BigInt("-5"), new BigInt("5"), "-10"),
                // numbers of the same length
                Arguments.of(new BigInt("5"), new BigInt("3"), "2"),
                Arguments.of(new BigInt("5"), new BigInt("-3"), "8"),
                Arguments.of(new BigInt("-5"), new BigInt("3"), "-8"),
                Arguments.of(new BigInt("-5"), new BigInt("-3"), "-2"),
                // numbers of different length
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("123456789012345678901234567890"), "-123456789000000000000000000000"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("12345678901234567890"), "123456789000000000000000000000"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("-123456789012345678901234567890"), "123456789000000000000000000000"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("-12345678901234567890"), "-123456789000000000000000000000"),
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("-123456789012345678901234567890"), "123456789024691357802469135780"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("123456789012345678901234567890"), "-123456789024691357802469135780"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("-12345678901234567890"), "123456789024691357802469135780"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("12345678901234567890"), "-123456789024691357802469135780")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntMultiplication")
    void testBigIntMultiplication(BigInt first, BigInt second, String expected) {
        BigInt result = first.multiply(second);
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntMultiplication() {
        return Stream.of(
                // multiplication of zero
                Arguments.of(new BigInt("0"), new BigInt("0"), "0"),
                Arguments.of(new BigInt("0"), new BigInt("5"), "0"),
                Arguments.of(new BigInt("0"), new BigInt("-5"), "0"),
                // multiplication by zero
                Arguments.of(new BigInt("5"), new BigInt("0"), "0"),
                Arguments.of(new BigInt("-5"), new BigInt("0"), "0"),
                //  multiplication of one
                Arguments.of(new BigInt("1"), new BigInt("5"), "5"),
                Arguments.of(new BigInt("1"), new BigInt("-5"), "-5"),
                Arguments.of(new BigInt("-1"), new BigInt("5"), "-5"),
                Arguments.of(new BigInt("-1"), new BigInt("-5"), "5"),
                // multiplication by one
                Arguments.of(new BigInt("5"), new BigInt("1"), "5"),
                Arguments.of(new BigInt("5"), new BigInt("-1"), "-5"),
                Arguments.of(new BigInt("-5"), new BigInt("1"), "-5"),
                Arguments.of(new BigInt("-5"), new BigInt("-1"), "5"),
                // numbers of the same length
                Arguments.of(new BigInt("5"), new BigInt("3"), "15"),
                Arguments.of(new BigInt("5"), new BigInt("-3"), "-15"),
                Arguments.of(new BigInt("-5"), new BigInt("3"), "-15"),
                Arguments.of(new BigInt("-5"), new BigInt("-3"), "15"),
                // numbers of different length
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("123456789012345678901234567890"), "1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("12345678901234567890"), "1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("-123456789012345678901234567890"), "1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("-12345678901234567890"), "1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("-123456789012345678901234567890"), "-1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("123456789012345678901234567890"), "-1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("-12345678901234567890"), "-1524157875323883675034293577501905199875019052100"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("12345678901234567890"), "-1524157875323883675034293577501905199875019052100")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigIntDivision")
    void testBigIntDivision(BigInt first, BigInt second, String expected) {
        BigInt result = first.divide(second);
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntDivision() {
        return Stream.of(
                // division of zero
                Arguments.of(new BigInt("0"), new BigInt("5"), "0"),
                Arguments.of(new BigInt("0"), new BigInt("-5"), "0"),
                // division of one
                Arguments.of(new BigInt("5"), new BigInt("1"), "5"),
                Arguments.of(new BigInt("5"), new BigInt("-1"), "-5"),
                Arguments.of(new BigInt("-5"), new BigInt("1"), "-5"),
                Arguments.of(new BigInt("-5"), new BigInt("-1"), "5"),
                // division without remainder of numbers of the same length
                Arguments.of(new BigInt("9"), new BigInt("3"), "3"),
                Arguments.of(new BigInt("9"), new BigInt("-3"), "-3"),
                Arguments.of(new BigInt("-9"), new BigInt("3"), "-3"),
                Arguments.of(new BigInt("-9"), new BigInt("-3"), "3"),
                // division without remainder of numbers of different length
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("12345678901234567890"), "10000000000"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("-12345678901234567890"), "-10000000000"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("12345678901234567890"), "-10000000000"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("-12345678901234567890"), "10000000000"),
                // division with remainder of numbers of the same length
                Arguments.of(new BigInt("8"), new BigInt("3"), "2"),
                Arguments.of(new BigInt("8"), new BigInt("-3"), "-2"),
                Arguments.of(new BigInt("-8"), new BigInt("3"), "-2"),
                Arguments.of(new BigInt("-8"), new BigInt("-3"), "2"),
                // division with remainder of numbers of different length
                Arguments.of(new BigInt("123456789012345678901234567891"), new BigInt("12345678901234567890"), "10000000000"),
                Arguments.of(new BigInt("123456789012345678901234567891"), new BigInt("-12345678901234567890"), "-10000000000"),
                Arguments.of(new BigInt("-123456789012345678901234567891"), new BigInt("12345678901234567890"), "-10000000000"),
                Arguments.of(new BigInt("-123456789012345678901234567891"), new BigInt("-12345678901234567890"), "10000000000"),
                // division of a smaller number by a larger number of the same length
                Arguments.of(new BigInt("3"), new BigInt("8"), "0"),
                Arguments.of(new BigInt("3"), new BigInt("-8"), "0"),
                Arguments.of(new BigInt("-3"), new BigInt("8"), "0"),
                Arguments.of(new BigInt("-3"), new BigInt("-8"), "0"),
                // division of a smaller number by a larger number of different length
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("123456789012345678901234567890"), "0"),
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("-123456789012345678901234567890"), "0"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("123456789012345678901234567890"), "0"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("-123456789012345678901234567890"), "0")
        );
    }

    @Test
    void testBigIntDivisionByZero() {
        BigInt first = new BigInt("5");
        BigInt second = new BigInt("0");
        assertThrows(ArithmeticException.class, () -> first.divide(second));
    }

    @ParameterizedTest
    @MethodSource("provideBigIntRemainder")
    void testBigIntModulo(BigInt first, BigInt second, String expected) {
        BigInt result = first.remainder(second);
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntRemainder() {
        return Stream.of(
                // division of zero
                Arguments.of(new BigInt("0"), new BigInt("5"), "0"),
                Arguments.of(new BigInt("0"), new BigInt("-5"), "0"),
                // division of one
                Arguments.of(new BigInt("5"), new BigInt("1"), "0"),
                Arguments.of(new BigInt("5"), new BigInt("-1"), "0"),
                Arguments.of(new BigInt("-5"), new BigInt("1"), "0"),
                Arguments.of(new BigInt("-5"), new BigInt("-1"), "0"),
                // division without remainder of numbers of the same length
                Arguments.of(new BigInt("9"), new BigInt("3"), "0"),
                Arguments.of(new BigInt("9"), new BigInt("-3"), "0"),
                Arguments.of(new BigInt("-9"), new BigInt("3"), "0"),
                Arguments.of(new BigInt("-9"), new BigInt("-3"), "0"),
                // division without remainder of numbers of different length
                Arguments.of(new BigInt("123456789012345678900000000000"), new BigInt("12345678901234567890"), "0"),
                Arguments.of(new BigInt("123456789012345678900000000000"), new BigInt("-12345678901234567890"), "0"),
                Arguments.of(new BigInt("-123456789012345678900000000000"), new BigInt("12345678901234567890"), "0"),
                Arguments.of(new BigInt("-123456789012345678900000000000"), new BigInt("-12345678901234567890"), "0"),
                // division with remainder of numbers of the same length
                Arguments.of(new BigInt("8"), new BigInt("3"), "2"),
                Arguments.of(new BigInt("8"), new BigInt("-3"), "2"),
                Arguments.of(new BigInt("-8"), new BigInt("3"), "-2"),
                Arguments.of(new BigInt("-8"), new BigInt("-3"), "-2"),
                // division with remainder of numbers of different length
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("12345678901234567890"), "1234567890"),
                Arguments.of(new BigInt("123456789012345678901234567890"), new BigInt("-12345678901234567890"), "1234567890"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("12345678901234567890"), "-1234567890"),
                Arguments.of(new BigInt("-123456789012345678901234567890"), new BigInt("-12345678901234567890"), "-1234567890"),
                // division of a smaller number by a larger number of the same length
                Arguments.of(new BigInt("3"), new BigInt("8"), "3"),
                Arguments.of(new BigInt("3"), new BigInt("-8"), "3"),
                Arguments.of(new BigInt("-3"), new BigInt("8"), "-3"),
                Arguments.of(new BigInt("-3"), new BigInt("-8"), "-3"),
                // division of a smaller number by a larger number of different length
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("123456789012345678901234567890"), "12345678901234567890"),
                Arguments.of(new BigInt("12345678901234567890"), new BigInt("-123456789012345678901234567890"), "12345678901234567890"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("123456789012345678901234567890"), "-12345678901234567890"),
                Arguments.of(new BigInt("-12345678901234567890"), new BigInt("-123456789012345678901234567890"), "-12345678901234567890")
        );
    }

    @Test
    void testBigIntRemainderByZero() {
        BigInt first = new BigInt("5");
        BigInt second = new BigInt("0");
        assertThrows(ArithmeticException.class, () -> first.remainder(second));
    }

    @ParameterizedTest
    @MethodSource("provideBigIntPow")
    void testBigIntPow(BigInt base, int exponent, String expected) {
        BigInt result = base.pow(exponent);
        assertEquals(expected, result.toString());
    }

    private static Stream<Arguments> provideBigIntPow() {
        return Stream.of(
                Arguments.of(new BigInt("1234567890"), 0, "1"),
                Arguments.of(new BigInt("1234567890"), 1, "1234567890"),
                Arguments.of(new BigInt("1234567890"), 2, "1524157875019052100"),
                Arguments.of(new BigInt("1234567890"), 3, "1881676371789154860897069000"),
                Arguments.of(new BigInt("1234567890"), 4, "2323057227982592441500937982514410000"),
                Arguments.of(new BigInt("1234567890"), 5, "2867971860299718107233761438093672048294900000")
        );
    }

    @Test
    void testBigIntPowNegativeExponent() {
        BigInt base = new BigInt("5");
        int exponent = -1;
        assertThrows(ArithmeticException.class, () -> base.pow(exponent));
    }
}