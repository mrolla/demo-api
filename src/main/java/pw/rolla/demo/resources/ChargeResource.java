package pw.rolla.demo.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import pw.rolla.demo.domain.Account;
import pw.rolla.demo.domain.ChargeRequest;
import pw.rolla.demo.domain.ErrorResponse;
import pw.rolla.demo.domain.Transaction;
import pw.rolla.demo.persistence.AccountRepository;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Handle all the calls to the <code>/api/charges</code> endpoint.
 *
 * @author Matteo Rolla
 */
public class ChargeResource {

  /**
   * Jackson object mapper. Needed for de-serializing the request body.
   */
  private final ObjectMapper objectMapper;

  /**
   * Instance of the {@link AccountRepository} for retrieving accounts to operate on.
   */
  private final AccountRepository accountRepository;

  /**
   * Mimic a database auto increment index.
   */
  private final AtomicInteger autoIncrement = new AtomicInteger();

  /**
   * Full constructor.
   *
   * @param objectMapper      an instance of the {@link ObjectMapper}.
   * @param accountRepository the account repository.
   */
  public ChargeResource(final ObjectMapper objectMapper,
                        final AccountRepository accountRepository) {
    this.objectMapper = objectMapper;
    this.accountRepository = accountRepository;
  }

  /**
   * Handler method for the <code>/api/charges</code> endpoint.
   *
   * @param request  the http request.
   * @param response the http response.
   * @return the returned model (either a {@link Transaction} or an {@link ErrorResponse}).
   */
  public Object handleCharges(final Request request, final Response response) {
    final ChargeRequest chargeRequest;
    try {
      chargeRequest = objectMapper.readValue(request.body(), ChargeRequest.class);
    } catch (final IOException exception) {
      response.status(400);
      return new ErrorResponse("Malformed json.");
    }

    // Probably some kind of validation should be also done on the authorization header value.
    final String authorization = request.headers("Authorization");

    final Optional<Account> maybeAccount = accountRepository.find(authorization);

    if (!maybeAccount.isPresent()) {
      response.status(401);
      return new ErrorResponse("Authorization error.");
    }

    final Optional<String> validated = chargeRequest.isValid();

    if (validated.isPresent()) {
      response.status(422);
      return new ErrorResponse(validated.get());
    }

    final String destinationId = chargeRequest.getDestination();

    final Optional<Account> maybeAccountDestination = accountRepository.find(destinationId);

    if (!maybeAccountDestination.isPresent()) {
      response.status(404);
      return new ErrorResponse("The destination account '" + destinationId + "' does not exist.");
    }

    final Account source = maybeAccount.get();
    final Account destination = maybeAccountDestination.get();

    final int amount = chargeRequest.getAmount();

    if (!source.getCurrency().equals(destination.getCurrency())) {
      response.status(422);
      return new ErrorResponse("Currencies do not match, cannot exchange money");
    }

    if (source.withdraw(amount)) {
      destination.deposit(amount);
    } else {
      response.status(403);
      return new ErrorResponse("Sorry, you don't have that much money.");
    }

    return new Transaction(autoIncrement.incrementAndGet());
  }

}
