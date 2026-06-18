package org.example;

public class Account {
    private int id;
    private int balance;
    private boolean isActive;
    public Account(int id, int balance, boolean isActive) {
        this.id = id;
        this.balance = balance;
        this.isActive = isActive;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
