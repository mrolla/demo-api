package pw.rolla.demo.domain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Really simple account model.
 *
 * @author Matteo Rolla
 */
public class Account {

  /**
   * Account balance ({@link AtomicInteger} for concurrency).
   */
  private final AtomicInteger balance;

  /**
   * Account id.
   */
  private final String accountId;

  /**
   * Account currency (e.g., EUR, USD, etc.)
   */
  private final String currency;

  /**
   * Full constructor.
   *
   * @param accountId the account id.
   * @param currency  the account currency.
   * @param balance   the account balance in a zero-decimal format (i.e. 1.00 EUR becomes 100).
   */
  public Account(final String accountId, final String currency, final int balance) {
    this.accountId = accountId;
    this.currency = currency;
    this.balance = new AtomicInteger(balance);
  }

  public String getAccountId() {
    return accountId;
  }

  public String getCurrency() {
    return currency;
  }

  /**
   * Withdraw money from the account balance.
   *
   * @param amount the amount to withdraw.
   * @return true if the withdrawal was successful, false otherwise (not enough money).
   */
  public boolean withdraw(final int amount) {
    return balance.getAndUpdate(operand -> operand >= amount ? operand - amount : operand) >= amount;
  }

  /**
   * Deposit money to the account balance.
   *
   * @param amount the amount to deposit.
   * @return the updated balance.
   */
  public int deposit(final int amount) {
    return balance.updateAndGet(operand -> operand + amount);
  }

}
