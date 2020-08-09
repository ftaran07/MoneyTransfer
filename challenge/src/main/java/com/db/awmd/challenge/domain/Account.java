package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;
  
  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }
  /**
   * Method to add money to the existing balance.
   * @param amount
   * @return
   */
  public String addMoney(BigDecimal amount) {
	  this.balance.add(amount);
	  return "Money deposit successful";
  }
  /**
   * Method to subtract money from existing balance.
   * @param amount
   * @return
   */
  public String withdrawMoney(BigDecimal amount) {
	  if(this.balance.compareTo(amount)>=0) {
	  this.balance.subtract(amount);
	  return "Withdrawal successful";
	  }
	  else
		  return "Insufficient Funds";
	 
		  
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }
}
