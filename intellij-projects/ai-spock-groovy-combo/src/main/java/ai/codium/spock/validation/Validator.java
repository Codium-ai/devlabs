package ai.codium.spock.validation;

public interface Validator<T> {

    void validate(T value);

}
