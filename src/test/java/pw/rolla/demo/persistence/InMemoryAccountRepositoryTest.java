package pw.rolla.demo.persistence;

import org.junit.Before;
import org.junit.Test;
import pw.rolla.demo.domain.Account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link InMemoryAccountRepository} class.
 *
 * @author Matteo Rolla
 */
public class InMemoryAccountRepositoryTest {

  private InMemoryAccountRepository repository;

  @Before
  public void setUp() throws Exception {
    repository = new InMemoryAccountRepository();
  }

  @Test
  public void testNull() throws Exception {
    assertThat(repository.find(null)).isEmpty();
  }

  @Test
  public void testAbsent() throws Exception {
    assertThat(repository.find("test")).isEmpty();
  }

  @Test
  public void testPresent() throws Exception {
    final Account account = mock(Account.class);
    when(account.getAccountId()).thenReturn("test");

    repository.save(account);
    verify(account, times(1)).getAccountId();

    assertThat(repository.find("test")).isPresent().contains(account);
  }

}