package org.example;

public class CommissionService {
    private final AccountsSave accountsSave;

    public CommissionService(AccountsSave accountsSave) {
        this.accountsSave = accountsSave;
    }

    public boolean transaction(int sum, Account from, Account to) {
        if (from.balance < sum) return false;
        else {
            from.balance -= sum;
            to.balance += sum;
            accountsSave.saveAccount(from);
            accountsSave.saveAccount(to);
            return true;
        }
    }
}
