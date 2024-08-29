package ai.codium.spock;

/**
 * The {@code OddEvenCamp} interface defines a method for checking whether a given integer is even or odd.
 *
 * <p>This interface provides a contract for implementing a method that determines the parity (even or odd)
 * of an integer value. Implementations are expected to return {@code 1} for even numbers and {@code 0} for odd numbers.
 * If the provided number is zero or negative, a {@code NegativeNumberException} should be thrown.
 *
 * <p>Usage example:
 * <pre>
 * {@code
 * OddEvenCamp checker = new OddEvenCampImpl();
 * int result = checker.check(4);  // returns 1 for even number
 * int result = checker.check(3);  // returns 0 for odd number
 * }
 * </pre>
 *
 * @see NegativeNumberException
 */
public interface OddEvenCamp {
    /**
     * Create a method that checks if the integer value is an even or odd number, return 1 for even and 0 for odd.
     * If number is 0 or negative number throw a runtime exception.
     *
     * @param number the number to be checked
     * @return 1 for even 0 for odd
     * @throws NegativeNumberException
     */
    int check(Number number);
}
