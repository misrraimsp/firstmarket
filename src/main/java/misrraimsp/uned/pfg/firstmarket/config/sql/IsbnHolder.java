package misrraimsp.uned.pfg.firstmarket.config.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsbnHolder extends Random {

    private static final int MIN = 100000000;
    private static final int MAX = 999999999;
    private static final String isbn13Prefix = "978";

    private int index;
    private List<String> isbns = new ArrayList<>();

    public IsbnHolder(int size){
        this.reset(size);
    }

    public String getIsbn() {
        if (index == isbns.size()) {
            this.reset();
        }
        String isbn = isbns.get(index);
        index++;
        return isbn;
    }

    public void reset(int size){
        index = 0;
        isbns = this.build(size);
    }

    public void reset(){
        index = 0;
    }

    private List<String> build(int size) {
        String candidate = "";
        Map<String,String> map = new HashMap<>();
        while (map.size() < size) {
            candidate = String.valueOf(getDiscreteRandomNumber(MIN, MAX));
            if (!map.containsKey(candidate)) {
                map.put(candidate,
                        isbn13Prefix + candidate + this.computeChecksum13(this.getIntegers(isbn13Prefix + candidate)));
            }
        }
        return (List<String>) map.values();
    }

    /**
     *
     * @param numbers
     * @return
     */
    private String computeChecksum13(List<Integer> numbers) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += ((i % 2) == 0) ? numbers.get(i) : 3 * numbers.get(i);
        }
        int checksum = 10 - (sum % 10);
        checksum = (checksum == 10) ? 0 : checksum;
        return String.valueOf(checksum);
    }

    /**
     * Este método asume que la cadena de entrada 'str' está compuesta por únicamente por dígitos
     * @param str
     * @return a list of integers
     */
    private List<Integer> getIntegers(String str) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            numbers.add(Integer.parseInt(str.substring(i, i + 1)));
        }
        return numbers;
    }

}
