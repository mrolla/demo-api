package pw.rolla.demo.persistence;

import pw.rolla.demo.domain.Account;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple {@link AccountRepository} implementation using a {@link ConcurrentHashMap} as backing storage.
 *
 * @author Matteo Rolla
 */
public class InMemoryAccountRepository implements AccountRepository {

  /**
   * Backing storage for accounts.
   */
  private Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public Optional<Account> find(final String accountId) {
    if (null == accountId) {
      return Optional.empty();
    }
    return Optional.ofNullable(accounts.get(accountId));
  }

  @Override
  public void save(final Account account) {
    accounts.put(account.getAccountId(), account);
  }

}
