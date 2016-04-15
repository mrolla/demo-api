package pw.rolla.demo.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for {@link ChargeRequest} class.
 *
 * @author Matteo Rolla
 */
public class ChargeRequestTest {

  @Test
  public void testInvalidAmount() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest();

    chargeRequest.setAmount(0);
    assertThat(chargeRequest.isValid())
        .isPresent()
        .contains("'amount' must be a non-zero positive number.");

    chargeRequest.setAmount(-1);
    assertThat(chargeRequest.isValid())
        .isPresent()
        .contains("'amount' must be a non-zero positive number.");
  }

  @Test
  public void testInvalidSource() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(1, null, "bob");
    assertThat(chargeRequest.isValid())
        .isPresent()
        .contains("'source' cannot be null.");
  }

  @Test
  public void testInvalidDestination() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(1, "alice", null);
    assertThat(chargeRequest.isValid())
        .isPresent()
        .contains("'destination' cannot be null.");
  }

  @Test
  public void testValidatedSuccessfully() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(1, "alice", "bob");
    assertThat(chargeRequest.isValid())
        .isEmpty();
  }

}