package misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort;

import org.springframework.data.domain.Sort;

public enum BookSortCriteria {

    STATUS_ASC ("Status ascending", "status", Sort.Direction.ASC),
    STATUS_DESC ("Status descending", "status", Sort.Direction.DESC),
    PRICE_ASC ("Price ascending", "price", Sort.Direction.ASC),
    PRICE_DESC ("Price descending", "price", Sort.Direction.DESC),
    YEAR_ASC ("Year ascending", "year", Sort.Direction.ASC),
    YEAR_DESC ("Year descending", "year", Sort.Direction.DESC),
    STOCK_ASC ("Stock ascending", "stock", Sort.Direction.ASC),
    STOCK_DESC ("Stock descending", "stock", Sort.Direction.DESC),
    TITLE_ASC ("Title ascending", "title", Sort.Direction.ASC),
    TITLE_DESC ("Title descending", "title", Sort.Direction.DESC),
    LANGUAGE_ASC ("Language ascending", "language", Sort.Direction.ASC),
    LANGUAGE_DESC ("Language descending", "language", Sort.Direction.DESC);

    private final String text;
    private final String property;
    private final Sort.Direction direction;

    BookSortCriteria(String text, String property, Sort.Direction direction) {
        this.text = text;
        this.property = property;
        this.direction = direction;
    }

    public String getText() {
        return text;
    }

    public String getProperty() {
        return property;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

}
