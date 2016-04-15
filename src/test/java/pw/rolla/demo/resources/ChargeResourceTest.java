package pw.rolla.demo.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import pw.rolla.demo.domain.Account;
import pw.rolla.demo.domain.ChargeRequest;
import pw.rolla.demo.domain.ErrorResponse;
import pw.rolla.demo.domain.Transaction;
import pw.rolla.demo.persistence.AccountRepository;
import spark.Request;
import spark.Response;

import java.util.Optional;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Matteo Rolla
 */
public class ChargeResourceTest {

  private ChargeResource chargeResource;

  private ObjectMapper objectMapper = new ObjectMapper();
  private AccountRepository accountRepository;

  @Before
  public void setUp() throws Exception {
    accountRepository = mock(AccountRepository.class);
    chargeResource = new ChargeResource(objectMapper, accountRepository);
  }

  @Test
  public void testMalformedPost() throws Exception {
    final Request request = mock(Request.class);
    when(request.body()).thenReturn("Not a valid json.");

    final Response response = mock(Response.class);

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, times(1)).status(400);
    assertThat(object)
        .isInstanceOf(ErrorResponse.class)
        .hasFieldOrPropertyWithValue("message", "Malformed json.");
  }

  @Test
  public void testNotValidPayload() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest();
    final String body = objectMapper.writeValueAsString(chargeRequest);

    final Request request = mock(Request.class);
    when(request.body()).thenReturn(body);

    final Response response = mock(Response.class);

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, times(1)).status(422);
    assertThat(object)
        .isInstanceOf(ErrorResponse.class)
        .hasFieldOrPropertyWithValue("message", "'amount' must be a non-zero positive number.");
  }

  @Test
  public void testNoSourceAccount() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(1, "frank", "john");
    final String body = objectMapper.writeValueAsString(chargeRequest);

    final Request request = mock(Request.class);
    when(request.body()).thenReturn(body);

    final Response response = mock(Response.class);

    when(accountRepository.find("frank")).thenReturn(Optional.empty());

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, times(1)).status(404);
    assertThat(object)
        .isInstanceOf(ErrorResponse.class)
        .hasFieldOrPropertyWithValue("message", "The source account 'frank' does not exist.");
  }

  @Test
  public void testNoDestinationAccount() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(1, "frank", "john");
    final String body = objectMapper.writeValueAsString(chargeRequest);

    final Request request = mock(Request.class);
    when(request.body()).thenReturn(body);

    final Response response = mock(Response.class);

    when(accountRepository.find("frank")).thenReturn(Optional.ofNullable(mock(Account.class)));
    when(accountRepository.find("john")).thenReturn(Optional.empty());

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, times(1)).status(404);
    assertThat(object)
        .isInstanceOf(ErrorResponse.class)
        .hasFieldOrPropertyWithValue("message", "The destination account 'john' does not exist.");
  }

  @Test
  public void testCurrenciesDoNotMatch() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(1, "frank", "john");
    final String body = objectMapper.writeValueAsString(chargeRequest);

    final Request request = mock(Request.class);
    when(request.body()).thenReturn(body);

    final Response response = mock(Response.class);

    when(accountRepository.find("frank"))
        .thenReturn(Optional.of(new Account("frank", "GBP", 1)));
    when(accountRepository.find("john"))
        .thenReturn(Optional.of(new Account("john", "USD", 1)));

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, times(1)).status(422);
    assertThat(object)
        .isInstanceOf(ErrorResponse.class)
        .hasFieldOrPropertyWithValue("message", "Currencies do not match, cannot exchange money");
  }

  @Test
  public void testNotEnoughMoney() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(12000, "alice", "bob");
    final String body = objectMapper.writeValueAsString(chargeRequest);

    final Request request = mock(Request.class);
    when(request.body()).thenReturn(body);

    final Response response = mock(Response.class);

    when(accountRepository.find("alice"))
        .thenReturn(Optional.of(new Account("alice", "GBP", 10000)));
    when(accountRepository.find("bob"))
        .thenReturn(Optional.of(new Account("bob", "GBP", 10000)));

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, times(1)).status(403);
    assertThat(object)
        .isInstanceOf(ErrorResponse.class)
        .hasFieldOrPropertyWithValue("message", "Sorry, poor 'alice' does not have that much money.");
  }

  @Test
  public void testTransferMoneySuccessfully() throws Exception {
    final ChargeRequest chargeRequest = new ChargeRequest(9000, "alice", "bob");
    final String body = objectMapper.writeValueAsString(chargeRequest);

    final Request request = mock(Request.class);
    when(request.body()).thenReturn(body);

    final Response response = mock(Response.class);

    when(accountRepository.find("alice"))
        .thenReturn(Optional.of(new Account("alice", "GBP", 10000)));
    when(accountRepository.find("bob"))
        .thenReturn(Optional.of(new Account("bob", "GBP", 10000)));

    final Object object = chargeResource.handleCharges(request, response);

    verify(response, never()).status(anyInt());
    assertThat(object)
        .isInstanceOf(Transaction.class)
        .hasFieldOrPropertyWithValue("transactionId", 1);
  }

}
