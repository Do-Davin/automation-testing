package lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountWhiteBoxTest {

    @Test
    void shouldCoverFrozenStateBranch() {
        BankAccount account = new BankAccount("Gina", 500, "personal", true, true);
        account.freeze();

        assertThrows(IllegalStateException.class, () -> account.withdraw(50));
        assertTrue(account.getDebugLog().stream().anyMatch(log -> log.contains("frozen")));
    }

    @Test
    void shouldCoverOverdraftPathForBusinessAccount() {
        BankAccount account = new BankAccount("Heng", 100, "business", true, true);

        account.withdraw(500);
        assertEquals(-400.5, account.getBalance(), 0.0001);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(100));
        assertTrue(account.getDebugLog().stream().anyMatch(log -> log.contains("overdraft")));
    }

    @Test
    void shouldCoverDailyResetStatePath() {
        BankAccount account = new BankAccount("Ivy", 3000, "personal", true, true);

        account.withdraw(600);
        assertEquals(600.0, account.getDailyWithdrawn(), 0.0001);

        account.resetDailyWithdrawal();
        assertEquals(0.0, account.getDailyWithdrawn(), 0.0001);
        assertTrue(account.getDebugLog().stream().anyMatch(log -> log.contains("reset")));
    }

    @Test
    void shouldCoverUnfreezeStateAndContinueWithdrawal() {
        BankAccount account = new BankAccount("Jara", 500, "personal", true, true);

        account.freeze();
        account.unfreeze();
        account.withdraw(100);

        assertFalse(account.isFrozen());
        assertEquals(399.0, account.getBalance(), 0.0001);
    }

    @Test
    void shouldNotCreateLogsWhenDebugModeIsDisabled() {
        BankAccount account = new BankAccount("Kosal", 500, "personal", true, false);
        account.deposit(100);

        assertTrue(account.getDebugLog().isEmpty());
    }
}
