package misrraimsp.uned.pfg.firstmarket.validation;


import misrraimsp.uned.pfg.firstmarket.config.Patterns;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String>, Patterns {

    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(Isbn constraintAnnotation) { }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return (this.validateFormat(isbn)) && (this.validateChecksum(isbn));
    }

    private boolean validateFormat(String isbn) {
        pattern = Pattern.compile(ISBN_PATTERN);
        matcher = pattern.matcher(isbn);
        return matcher.matches();
    }

    private boolean validateChecksum(String isbn) {
        List<Integer> numbers = this.getNumbers(isbn);
        int size = numbers.size();
        if (size == 10){
            return checksum10(numbers);
        }
        else if (size == 13){
            return checksum13(numbers);
        }
        else return false;
    }

    private List<Integer> getNumbers(String isbn) {
        String isbnDigits = isbn.replaceAll("X", "10").replaceAll("[^\\d]", "");
        int size = isbnDigits.length();
        if (size != 10 && size != 11 && size != 13){//cut numbers on isbn head
            isbnDigits = isbnDigits.substring(2);
            size -= 2;
        }
        List<Integer> numbers = new ArrayList<>();
        if (size == 11){ // X has been substituted by 10
            for (int i = 0; i < size - 2; i++) {
                numbers.add(Integer.parseInt(isbnDigits.substring(i, i + 1)));
            }
            numbers.add(10);
        }
        else {
            for (int i = 0; i < size; i++) {
                numbers.add(Integer.parseInt(isbnDigits.substring(i, i + 1)));
            }
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