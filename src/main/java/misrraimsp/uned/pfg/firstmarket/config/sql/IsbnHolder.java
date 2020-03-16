package misrraimsp.uned.pfg.firstmarket.config.sql;

import java.util.ArrayList;
import java.util.List;

public class IsbnHolder {

    private static final int MIN = 100000000;
    private static final int MAX = 999999999;
    private static final String isbn13Prefix = "978";

    private int index;
    private List<String> isbns = new ArrayList<>();

    public IsbnHolder(int size){
        this.reset(size);
    }

    public String getIsbn() {
        String isbn = isbns.get(index);
        index++;
        return isbn;
    }

    public void reset(int size){
        index = 0;
        isbns = build(size);
    }

    private List<String> build(int size) {

    }
}
