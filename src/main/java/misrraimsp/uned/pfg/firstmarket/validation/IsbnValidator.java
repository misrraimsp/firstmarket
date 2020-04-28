package misrraimsp.uned.pfg.firstmarket.validation;


import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidIsbn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ValidationRegexProperties validationRegexProperties;

    @Autowired
    public IsbnValidator(ValidationRegexProperties validationRegexProperties){
        this.validationRegexProperties = validationRegexProperties;
    }

    @Override
    public void initialize(ValidIsbn constraintAnnotation) { }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return (this.validateFormat(isbn)) && (this.validateChecksum(isbn));
    }

    private boolean validateFormat(String isbn) {
        Pattern pattern = Pattern.compile(validationRegexProperties.getIsbnCode());
        Matcher matcher = pattern.matcher(isbn);
        return matcher.matches();
    }

    private boolean validateChecksum(String isbn) {
        List<Integer> numbers = this.getNumbers(isbn);
        int size = numbers.size();
        if (size == 10){
            LOGGER.trace("Validating an ISBN code that is 10 digits in length");
            return checksum10(numbers);
        }
        else if (size == 13){
            LOGGER.trace("Validating an ISBN code that is 13 digits in length");
            return checksum13(numbers);
        }
        else {
            LOGGER.trace("Validating an ISBN code that is not 10 or 13 digits in length");
            return false;
        }
    }

    private List<Integer> getNumbers(String isbn) {
        String filtered = isbn.replaceAll(validationRegexProperties.getIsbnFilter(), "");
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < filtered.length() - 1; i++) {
            numbers.add(Integer.parseInt(filtered.substring(i, i + 1)));
        }
        if (filtered.contains("X")){
            numbers.add(10);
        }
        else {
            numbers.add(Integer.parseInt(filtered.substring(filtered.length() - 1)));
        }
        return numbers;
    }

    private boolean checksum13(List<Integer> numbers) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += ((i % 2) == 0) ? numbers.get(i) : 3 * numbers.get(i);
        }
        int checksum = 10 - (sum % 10);
        checksum = (checksum == 10) ? 0 : checksum;
        return checksum == numbers.get(12);
    }

    private boolean checksum10(List<Integer> numbers) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum = sum + (numbers.get(i) * (10 - i));
        }
        int checksum = 11 - (sum % 11);
        checksum = (checksum == 11) ? 0 : checksum;
        return checksum == numbers.get(9);
    }


}