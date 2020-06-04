package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

import org.springframework.data.domain.Sort;

public enum BookSortCriteria {

    PRICE_ASC ("Price ascending", "price", Sort.Direction.ASC),
    PRICE_DESC ("Price descending", "price", Sort.Direction.DESC),
    YEAR_ASC ("Year ascending", "year", Sort.Direction.ASC),
    YEAR_DESC ("Year descending", "year", Sort.Direction.DESC),
    TITLE_ASC ("Title ascending", "title", Sort.Direction.ASC),
    TITLE_DESC ("Title descending", "title", Sort.Direction.DESC),
    ISBN_ASC ("ISBN ascending", "isbn", Sort.Direction.ASC),
    ISBN_DESC ("ISBN descending", "isbn", Sort.Direction.DESC),
    PAGES_ASC ("Pages ascending", "pages", Sort.Direction.ASC),
    PAGES_DESC ("Pages descending", "pages", Sort.Direction.DESC),
    LANGUAGE_ASC ("Language ascending", "language", Sort.Direction.ASC),
    LANGUAGE_DESC ("Language descending", "language", Sort.Direction.DESC),
    STOCK_ASC ("Stock ascending", "stock", Sort.Direction.ASC),
    STOCK_DESC ("Stock descending", "stock", Sort.Direction.DESC);

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
