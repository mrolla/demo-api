package pw.rolla.demo.persistence;

import pw.rolla.demo.domain.Account;

import java.util.Optional;

/**
 * Simple interface for handling account persistence.
 *
 * @author Matteo Rolla
 */
public interface AccountRepository {

  /**
   * Retrieve an {@link Account} from the repository if available.
   *
   * @param accountId the id of the account to retrieve.
   * @return an {@link Optional} containing the {@link Account} if any.
   */
  Optional<Account> find(String accountId);

  /**
   * Save an account to the repository.
   *
   * @param account the account to save.
   */
  void save(Account account);

}
