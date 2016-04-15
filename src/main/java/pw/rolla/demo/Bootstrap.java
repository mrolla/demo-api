package pw.rolla.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import pw.rolla.demo.domain.Account;
import pw.rolla.demo.persistence.AccountRepository;
import pw.rolla.demo.persistence.InMemoryAccountRepository;
import pw.rolla.demo.resources.ChargeResource;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.post;

/**
 * Application bootstrapping class.
 *
 * @author Matteo Rolla
 */
public class Bootstrap {

  /**
   * Main application entry point.
   *
   * @param args command line arguments.
   */
  public static void main(final String[] args) {
    // Instantiate the services.
    final ObjectMapper objectMapper = new ObjectMapper();
    final AccountRepository accountRepository = new InMemoryAccountRepository();

    // Add few demo users.
    accountRepository.save(new Account("alice", "GBP", 10000));
    accountRepository.save(new Account("bob", "GBP", 20000));
    accountRepository.save(new Account("eve", "EUR", 10000));

    // Set some common headers.
    before((request, response) -> {
      response.type("application/json");
      response.header("Server", "payments");
      response.header("X-Api-Version", "v1");
    });

    // Instantiate resources.
    final ChargeResource chargeResource = new ChargeResource(objectMapper, accountRepository);

    // Set routes.
    post("/api/charges", chargeResource::handleCharges, objectMapper::writeValueAsString);
  }

}
