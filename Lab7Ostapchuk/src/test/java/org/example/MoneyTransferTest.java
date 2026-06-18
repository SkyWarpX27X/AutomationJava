package org.example;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoneyTransferTest {
    @Mock
    AccountsSave accountsSave;
    @Mock
    CommissionService commissionService;
    @Mock
    AuditLog auditLog;
    @InjectMocks
    MoneyTransfer moneyTransfer;

    @Tag("survived")
    @Test
    void successfulDataValidationMutantSurvived() {
        Account from = new Account(1, 200, true);
        Account to = new Account(2, 100, true);
        Mockito.when(accountsSave.getAccount(1)).thenReturn(from);
        Mockito.when(accountsSave.getAccount(2)).thenReturn(to);
        Mockito.when(commissionService.getCommission(100)).thenReturn(5);
        moneyTransfer.transfer(1, 2, 100);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(from.getBalance()).isEqualTo(95);
        softAssertions.assertThat(to.getBalance()).isEqualTo(200);
        softAssertions.assertAll();
        verify(accountsSave, times(2)).save(any(Account.class));
        verify(auditLog).writeLog(anyInt(), anyInt(), anyInt(), eq(true));
    }

    @Tag("killed")
    @Test
    void successfulDataValidationMutantKilled() {
        Account from = new Account(1, 105, true);
        Account to = new Account(2, 100, true);
        Mockito.when(accountsSave.getAccount(1)).thenReturn(from);
        Mockito.when(accountsSave.getAccount(2)).thenReturn(to);
        Mockito.when(commissionService.getCommission(100)).thenReturn(5);
        moneyTransfer.transfer(1, 2, 100);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(from.getBalance()).isEqualTo(0);
        softAssertions.assertThat(to.getBalance()).isEqualTo(200);
        softAssertions.assertAll();
        verify(accountsSave, times(2)).save(any(Account.class));
        verify(auditLog).writeLog(anyInt(), anyInt(), anyInt(), eq(true));
    }

    @Test
    void failedAccountValidation() {
        Account from = new Account(1, 200, false);
        Account to = new Account(2, 100, true);
        Mockito.when(accountsSave.getAccount(1)).thenReturn(from);
        Mockito.when(accountsSave.getAccount(2)).thenReturn(to);
        assertThatThrownBy(() -> moneyTransfer.transfer(1, 2, 100)).hasMessageContaining("must be active");
        verify(auditLog).writeLog(anyInt(), anyInt(), anyInt(), eq(false));
    }

    @Test
    void failedBalanceValidation() {
        Account from = new Account(1, 100, true);
        Account to = new Account(2, 100, true);
        Mockito.when(accountsSave.getAccount(1)).thenReturn(from);
        Mockito.when(accountsSave.getAccount(2)).thenReturn(to);
        Mockito.when(commissionService.getCommission(100)).thenReturn(5);
        assertThatThrownBy(() -> moneyTransfer.transfer(1, 2, 100)).hasMessageContaining("balance");
        verify(auditLog).writeLog(anyInt(), anyInt(), anyInt(), eq(false));
    }
    @Test
    void failedAmountValidation() {
        assertThatThrownBy(() -> moneyTransfer.transfer(1, 2, 0)).hasMessageContaining("amount");
        verify(auditLog, never()).writeLog(anyInt(), anyInt(), anyInt(), anyBoolean());
    }
    @Test
    void getActiveAccounts() {
        Account acc1 =  new Account(1, 200, true);
        Account acc2 = new Account(2, 100, true);
        Account acc3 = new Account(3, 100, false);
        Account acc4 = new Account(4, 100, false);
        Account acc5 = new Account(5, 100, true);
        Mockito.when(accountsSave.getAccounts()).thenReturn(List.of(acc1, acc2, acc3, acc4, acc5));
        List<Account> activeAccounts = moneyTransfer.getActiveAccounts();
        assertThat(activeAccounts)
                .hasSize(3)
                .allMatch(Account::isActive)
                .doesNotContain(acc3, acc4);
    }
}
