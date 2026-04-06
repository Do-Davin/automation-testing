package lab02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {
    private final String owner;
    private final String accountType;
    private final boolean verified;
    private final boolean debugMode;
    private final List<String> debugLog = new ArrayList<>();

    private double balance;
    private double dailyWithdrawn;
    private boolean frozen;

    public BankAccount(String owner, double initialBalance, String accountType, boolean verified, boolean debugMode) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (!"personal".equals(accountType) && !"business".equals(accountType)) {
            throw new IllegalArgumentException("accountType must be personal or business");
        }

        this.owner = owner;
        this.balance = round2(initialBalance);
        this.accountType = accountType;
        this.verified = verified;
        this.debugMode = debugMode;
        this.dailyWithdrawn = 0;
        this.frozen = false;
    }

    public BankAccount(String owner, double initialBalance, String accountType, boolean verified) {
        this(owner, initialBalance, accountType, verified, false);
    }

    private double toValidAmount(Number amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        double value = amount.doubleValue();
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Amount must be finite");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        return round2(value);
    }

    private void log(String message) {
        if (debugMode) {
            debugLog.add(message);
        }
    }

    private double dailyLimit() {
        return "business".equals(accountType) ? 5000.0 : 1000.0;
    }

    private double overdraftLimit() {
        return "business".equals(accountType) ? 500.0 : 0.0;
    }

    private double withdrawFee() {
        return "business".equals(accountType) ? 0.5 : 1.0;
    }

    private double round2(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public double getDailyWithdrawn() {
        return dailyWithdrawn;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public List<String> getDebugLog() {
        return Collections.unmodifiableList(debugLog);
    }

    public void clearDebugLog() {
        debugLog.clear();
    }

    public double deposit(Number amount) {
        double value = toValidAmount(amount);
        balance = round2(balance + value);
        log("deposit: +" + value + ", balance=" + balance);
        return balance;
    }

    public double withdraw(Number amount) {
        double value = toValidAmount(amount);

        if (frozen) {
            log("withdraw blocked: account is frozen");
            throw new IllegalStateException("Account is frozen");
        }

        if (!verified && value > 200) {
            log("withdraw blocked: unverified account over 200");
            throw new SecurityException("Unverified account cannot withdraw more than 200");
        }

        if (dailyWithdrawn + value > dailyLimit()) {
            log("withdraw blocked: daily limit exceeded");
            throw new IllegalArgumentException("Daily withdrawal limit exceeded");
        }

        double fee = withdrawFee();
        double totalDeduction = value + fee;
        double minBalance = -overdraftLimit();

        if (balance - totalDeduction < minBalance) {
            log("withdraw blocked: insufficient funds with overdraft rule");
            throw new IllegalArgumentException("Insufficient funds");
        }

        balance = round2(balance - totalDeduction);
        dailyWithdrawn = round2(dailyWithdrawn + value);
        log("withdraw: -" + value + " fee=" + fee + ", balance=" + balance + ", dailyWithdrawn=" + dailyWithdrawn);
        return value;
    }

    public void transferTo(BankAccount other, Number amount) {
        if (other == null) {
            throw new IllegalArgumentException("other account is required");
        }

        double value = toValidAmount(amount);
        withdraw(value);
        other.deposit(value);
        log("transfer out: " + value + " to " + other.owner);
    }

    public void freeze() {
        frozen = true;
        log("account frozen");
    }

    public void unfreeze() {
        frozen = false;
        log("account unfrozen");
    }

    public void resetDailyWithdrawal() {
        dailyWithdrawn = 0;
        log("daily withdrawal reset");
    }
}
