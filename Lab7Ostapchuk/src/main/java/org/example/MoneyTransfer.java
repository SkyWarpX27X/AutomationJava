package org.example;

import java.util.List;

public class MoneyTransfer {
    private final AccountsSave accountsSave;
    private final CommissionService commissionService;
    private final AuditLog auditLog;

    public MoneyTransfer(AccountsSave accountsSave, CommissionService commissionService, AuditLog auditLog) {
        this.accountsSave = accountsSave;
        this.commissionService = commissionService;
        this.auditLog = auditLog;
    }

    public void transfer(int fromAccountId, int toAccountId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account fromAccount = accountsSave.getAccount(fromAccountId);
        Account toAccount = accountsSave.getAccount(toAccountId);

        if (!fromAccount.isActive() || !toAccount.isActive()) {
            auditLog.writeLog(fromAccountId, toAccountId, amount, false);
            throw new IllegalArgumentException("Both accounts must be active");
        }

        int commission = commissionService.getCommission(amount);
        int totalDebit = amount + commission;

        if (fromAccount.getBalance() < totalDebit) {
            auditLog.writeLog(fromAccountId, toAccountId, amount, false);
            throw new IllegalArgumentException("Not enough balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - totalDebit);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountsSave.save(fromAccount);
        accountsSave.save(toAccount);

        auditLog.writeLog(fromAccountId, toAccountId, amount, true);
    }

    public List<Account> getActiveAccounts() {
        return accountsSave.getAccounts().stream().filter(Account::isActive).toList();
    }
}
