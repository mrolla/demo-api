package pw.rolla.demo.domain;

import java.util.Optional;

/**
 * Charge request payload sent by the client.
 *
 * @author Matteo Rolla
 */
public class ChargeRequest implements Validable {

  /**
   * The amount of money to move from the source account to the destination account.
   */
  private int amount;

  /**
   * The id of the destination account.
   */
  private String destination;

  /**
   * Default constructor. Needed for Jackson deserialization.
   */
  public ChargeRequest() {
    // Jackson deserialization.
  }

  /**
   * Full constructor.
   *
   * @param amount      the amount of the charge in a zero-decimal format (i.e. 1.00 EUR becomes 100).
   * @param destination the account id to deposit money to.
   */
  public ChargeRequest(final int amount, final String destination) {
    this.amount = amount;
    this.destination = destination;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getDestination() {
    return destination;
  }

  @Override
  public Optional<String> isValid() {
    if (amount <= 0) {
      return Optional.of("'amount' must be a non-zero positive number.");
    }

    if (destination == null || destination.isEmpty()) {
      return Optional.of("'destination' cannot be null.");
    }

    return Optional.empty();
  }

}
