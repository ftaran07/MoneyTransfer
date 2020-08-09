package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);
  /**
   * Method to credit amount in case of money transfer.
   * @param accountId
   * @param amount
   * @return
   */
  String addMoney(String accountId,BigDecimal amount);
  
  /**
   * Method to withdraw money in case of money transfer.
   * @param accountId
   * @param amount
   * @return
   */
  
  String withdrawMoney(String accountId,BigDecimal amount);
  

  void clearAccounts();
}
