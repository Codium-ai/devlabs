package ai.codium.spock.validation;

import ai.codium.spock.NegativeNumberException;

public class NegativeChecker implements Validator<Integer> {
    /**
     * Checks if the value is 0 or less will throw a runtime exception
     * @param value the number to validate
     */
    @Override
    public void validate(Integer value) {
        if(value == null || value <= 0) {
            throw new NegativeNumberException("Value is a 0 or negative value");
        }
    }
}
