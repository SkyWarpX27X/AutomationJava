package org.example;

public interface AuditLog {
    void writeLog(int fromId, int toId, int amount, boolean success);
}
