package pw.rolla.demo.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Test class for {@link Account} class.
 * <p>
 * Tests are fairly trivial.
 *
 * @author Matteo Rolla
 */
public class AccountTest {

  @Test
  public void testSimple() throws Exception {
    final Account account = new Account("test", "EUR", 0);
    assertThat(account.deposit(10)).isEqualTo(10);
    // Too much
    assertThat(account.withdraw(100)).isFalse();
    // Just enough money.
    assertThat(account.withdraw(10)).isTrue();
    // No more money.
    assertThat(account.withdraw(10)).isFalse();
  }

  @Test
  public void testGetters() throws Exception {
    final Account account = new Account("test", "EUR", 0);
    assertThat(account.getAccountId()).isEqualTo("test");
    assertThat(account.getCurrency()).isEqualTo("EUR");
  }

}
