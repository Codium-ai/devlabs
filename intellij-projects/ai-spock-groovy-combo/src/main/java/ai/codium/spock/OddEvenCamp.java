package ai.codium.spock;

public interface OddEvenCamp {
    /**
     * Create a method that checks if the integer value is an even or odd number, return 1 for even and 0 for odd.
     * If number is 0 or negative number throw a runtime exception
     *
     * @param number the number to be checked
     * @return 1 for even 0 for odd
     * @throws NegativeNumberException
     */
    int check(int number);
}
