package lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorBlackBoxTest {

    @Test
    void shouldCalculateBasicArithmeticCorrectly() {
        Calculator calculator = new Calculator();

        assertEquals(8.0, calculator.add(5, 3), 0.0001);
        assertEquals(2.0, calculator.subtract(5, 3), 0.0001);
        assertEquals(15.0, calculator.multiply(5, 3), 0.0001);
        assertEquals(2.5, calculator.divide(5, 2), 0.0001);
    }

    @Test
    void shouldHandleNegativeAndDecimalInputs() {
        Calculator calculator = new Calculator();

        assertEquals(-2.5, calculator.add(-5, 2.5), 0.0001);
        assertEquals(-7.5, calculator.multiply(-3, 2.5), 0.0001);
    }

    @Test
    void shouldRejectInvalidInputAndDivideByZero() {
        Calculator calculator = new Calculator();

        assertThrows(IllegalArgumentException.class, () -> calculator.add(Double.NaN, 2));
        assertThrows(IllegalArgumentException.class, () -> calculator.subtract(null, 2));
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }
}
