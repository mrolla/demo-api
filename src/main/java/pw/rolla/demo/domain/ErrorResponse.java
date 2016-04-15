package pw.rolla.demo.domain;

/**
 * Simple immutable error response.
 *
 * @author Matteo Rolla
 */
public class ErrorResponse {

  /**
   * The error message.
   */
  private final String message;

  /**
   * Full constructor.
   *
   * @param message the error message.
   */
  public ErrorResponse(final String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
