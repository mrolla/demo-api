package pw.rolla.demo.domain;

/**
 * Simple immutable class representing a successful transaction.
 *
 * @author Matteo Rolla
 */
public class Transaction {

  /**
   * Successful transaction id.
   */
  private final int transactionId;

  /**
   * Full constructor.
   *
   * @param transactionId the successful transaction id.
   */
  public Transaction(final int transactionId) {
    this.transactionId = transactionId;
  }

  public int getTransactionId() {
    return transactionId;
  }

}
