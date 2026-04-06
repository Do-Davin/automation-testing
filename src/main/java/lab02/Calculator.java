package lab02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator {
    private final boolean debugMode;
    private final List<String> debugLog = new ArrayList<>();

    public Calculator() {
        this(false);
    }

    public Calculator(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private double toValidDouble(Number value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }

        double number = value.doubleValue();
        if (Double.isNaN(number) || Double.isInfinite(number)) {
            throw new IllegalArgumentException(name + " must be a finite number");
        }
        return number;
    }

    private void log(String message) {
        if (debugMode) {
            debugLog.add(message);
        }
    }

    public List<String> getDebugLog() {
        return Collections.unmodifiableList(debugLog);
    }

    public void clearDebugLog() {
        debugLog.clear();
    }

    public double add(Number a, Number b) {
        double x = toValidDouble(a, "a");
        double y = toValidDouble(b, "b");
        double result = x + y;
        log("add(" + x + ", " + y + ") = " + result);
        return result;
    }

    public double subtract(Number a, Number b) {
        double x = toValidDouble(a, "a");
        double y = toValidDouble(b, "b");
        double result = x - y;
        log("subtract(" + x + ", " + y + ") = " + result);
        return result;
    }

    public double multiply(Number a, Number b) {
        double x = toValidDouble(a, "a");
        double y = toValidDouble(b, "b");

        if (x == 0 || y == 0) {
            log("multiply short-circuit: one operand is zero");
            return 0;
        }

        double result = x * y;
        log("multiply(" + x + ", " + y + ") = " + result);
        return result;
    }

    public double divide(Number a, Number b) {
        double x = toValidDouble(a, "a");
        double y = toValidDouble(b, "b");

        if (y == 0) {
            log("divide error: divide by zero");
            throw new ArithmeticException("Cannot divide by zero");
        }

        double result = x / y;
        log("divide(" + x + ", " + y + ") = " + result);
        return result;
    }
}
