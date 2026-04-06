# Lab02 - Blackbox and Whitebox Testing (Java)

This lab contains two testing scenarios implemented in Java with JUnit 5.

## Scenario 1: Calculator Testing
Focus: basic arithmetic operations with edge cases.

- Blackbox tests: input validation, mathematical correctness, divide-by-zero handling.
- Whitebox tests: branch coverage (zero short-circuit), exception paths, debug-mode behavior.

Files:
- `src/main/java/lab02/Calculator.java`
- `src/test/java/lab02/CalculatorBlackBoxTest.java`
- `src/test/java/lab02/CalculatorWhiteBoxTest.java`

## Scenario 2: BankAccount Testing
Focus: financial business rules.

- Blackbox tests: rule validation (verified/unverified), daily limit, transfer behavior.
- Whitebox tests: conditional branches (frozen state, overdraft path), state management, debug path.

Files:
- `src/main/java/lab02/BankAccount.java`
- `src/test/java/lab02/BankAccountBlackBoxTest.java`
- `src/test/java/lab02/BankAccountWhiteBoxTest.java`

## Run tests

```bash
mvn test
```

Expected result:
- Total tests: `17`
- Failures: `0`
- Errors: `0`
