package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.naming.InsufficientResourcesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {
	
	@Autowired
	NotificationService notificationService;

	@Getter
	private final AccountsRepository accountsRepository;
	
	public Lock accLock;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
		accLock = new ReentrantLock();
	}
	
	

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	// TODO:Replace Hard coded string with custom exception for Insufficient fund
	// scenario and use @Autowired for  NotificationService object instead of new.
	/**
	 * This method is used to transfer money between the accounts and notify both
	 * the account holders in case of successful transfer.
	 * 
	 * @param accountTo
	 * @param accountFrom
	 * @param amount
	 * @return the appropriate transfer message.
	 */
	public String transferMoney(String accountTo, String accountFrom, BigDecimal amount) {
		accLock.lock();
		try {
		this.accountsRepository.addMoney(accountTo, amount);
		String withdrawalMsg = this.accountsRepository.withdrawMoney(accountFrom, amount);
		
		Account accountToTransfer = this.accountsRepository.getAccount(accountTo);
		Account accountFromTransfer = this.accountsRepository.getAccount(accountFrom);
		if (withdrawalMsg.equalsIgnoreCase("Insufficient Funds"))
			return "Error in transferring funds";

		else {
			notificationService.notifyAboutTransfer(accountToTransfer, "{amount} money credited");
			notificationService.notifyAboutTransfer(accountFromTransfer, "{amount} money withdrawal");
			return "Transfer Successful";
		}
		}
		finally {
			accLock.unlock();
		}
	}
}
