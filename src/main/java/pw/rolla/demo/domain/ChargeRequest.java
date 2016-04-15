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
   * The id of the source account.
   */
  private String source;

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
   * @param source      the account id to withdraw money from.
   * @param destination the account id to deposit money to.
   */
  public ChargeRequest(final int amount, final String source, final String destination) {
    this.amount = amount;
    this.source = source;
    this.destination = destination;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getSource() {
    return source;
  }

  public String getDestination() {
    return destination;
  }

  @Override
  public Optional<String> isValid() {
    if (amount <= 0) {
      return Optional.of("'amount' must be a non-zero positive number.");
    }

    if (source == null || source.isEmpty()) {
      return Optional.of("'source' cannot be null.");
    }

    if (destination == null || destination.isEmpty()) {
      return Optional.of("'destination' cannot be null.");
    }

    return Optional.empty();
  }

}
