package lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountBlackBoxTest {

    @Test
    void shouldDepositAndWithdrawWithFeeForPersonalAccount() {
        BankAccount account = new BankAccount("Alice", 1000, "personal", true);

        account.deposit(200);
        account.withdraw(100);

        assertEquals(1099.0, account.getBalance(), 0.0001);
    }

    @Test
    void shouldBlockLargeWithdrawalForUnverifiedAccount() {
        BankAccount account = new BankAccount("Bob", 500, "personal", false);

        assertThrows(SecurityException.class, () -> account.withdraw(250));
    }

    @Test
    void shouldBlockWithdrawalBeyondDailyLimit() {
        BankAccount account = new BankAccount("Carol", 5000, "personal", true);

        account.withdraw(900);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(200));
    }

    @Test
    void shouldTransferMoneyBetweenAccounts() {
        BankAccount from = new BankAccount("Dara", 1000, "business", true);
        BankAccount to = new BankAccount("Ean", 100, "personal", true);

        from.transferTo(to, 300);

        // business fee 0.5 when withdrawing
        assertEquals(699.5, from.getBalance(), 0.0001);
        assertEquals(400.0, to.getBalance(), 0.0001);
    }

    @Test
    void shouldRejectInvalidMonetaryInput() {
        BankAccount account = new BankAccount("Fina", 100, "personal", true);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-10));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(Double.NaN));
    }
}
