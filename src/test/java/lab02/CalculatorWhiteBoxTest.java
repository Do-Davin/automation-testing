package lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorWhiteBoxTest {

    @Test
    void shouldCoverMultiplyZeroShortCircuitBranch() {
        Calculator calculator = new Calculator(true);

        double result = calculator.multiply(0, 99);

        assertEquals(0.0, result, 0.0001);
        assertTrue(calculator.getDebugLog().stream().anyMatch(log -> log.contains("short-circuit")));
    }

    @Test
    void shouldCoverDivideByZeroExceptionPathAndLog() {
        Calculator calculator = new Calculator(true);

        assertThrows(ArithmeticException.class, () -> calculator.divide(8, 0));
        assertTrue(calculator.getDebugLog().stream().anyMatch(log -> log.contains("divide by zero")));
    }

    @Test
    void shouldNotRecordDebugLogsWhenDebugModeIsOff() {
        Calculator calculator = new Calculator(false);

        calculator.add(1, 2);
        calculator.multiply(3, 4);

        assertTrue(calculator.getDebugLog().isEmpty());
    }

    @Test
    void shouldClearDebugLogState() {
        Calculator calculator = new Calculator(true);
        calculator.add(1, 1);
        assertFalse(calculator.getDebugLog().isEmpty());

        calculator.clearDebugLog();

        assertTrue(calculator.getDebugLog().isEmpty());
    }
}
