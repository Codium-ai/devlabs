package ai.codium.spock;

import ai.codium.spock.validation.Validator;

public class OddEvenCampImpl implements OddEvenCamp {

    private final Validator<Integer> validator;

    // Constructor for dependency injection
    public OddEvenCampImpl(Validator<Integer> validator) {
        this.validator = validator;
    }

    @Override
    public int check(Number number) {
        // Validate the number
        validator.validate(number.intValue());

        // Check if the number is even or odd
        return (number.intValue() % 2 == 0) ? 1 : 0;
    }
}