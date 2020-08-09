package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;

import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;
  
  @Mock
  private NotificationService notificationService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);
    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  @Test
  public void testTransferMoney() {
	  
	  Account accountTo = new Account("Acc1", new BigDecimal("100"));
	  Account accountFrom = new Account("Acc2", new BigDecimal("500"));
	  this.accountsService.createAccount(accountTo);
	  this.accountsService.createAccount(accountFrom);
	  Mockito.doNothing().when(notificationService).notifyAboutTransfer(Mockito.any(Account.class), Mockito.anyString());
      String msg = this.accountsService.transferMoney("Acc1", "Acc2", new BigDecimal(150));
      assertEquals(msg,"Transfer Successful");
  }
  @Test
  public void testTransferMoneyForInsufficientFunds() {
	  
	  Account accountTo = new Account("Acc3", new BigDecimal("500"));
	  Account accountFrom = new Account("Acc4", new BigDecimal("100"));
	  this.accountsService.createAccount(accountTo);
	  this.accountsService.createAccount(accountFrom);
	  Mockito.doNothing().when(notificationService).notifyAboutTransfer(Mockito.any(Account.class), Mockito.anyString());
      String msg = this.accountsService.transferMoney("Acc3", "Acc4", new BigDecimal(150));
      assertEquals(msg,"Error in transferring funds");
  }
}
