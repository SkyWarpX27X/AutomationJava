package org.example;

import java.util.List;

public interface AccountsSave {
    void save(Account account);
    Account getAccount(int id);
    List<Account> getAccounts();
}
