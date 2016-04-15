package pw.rolla.demo.domain;

import java.util.Optional;

/**
 * Simple interface for validating objects.
 *
 * @author Matteo Rolla
 */
public interface Validable {

  /**
   * Implement all the logic for validating an object.
   *
   * @return an {@link Optional} containing the first error message, if any.
   */
  Optional<String> isValid();

}
