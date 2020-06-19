package misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort;

import org.springframework.data.domain.Sort;

public enum BookSortCriteria {

    STATUS_ASC ("Status a-z", "status", Sort.Direction.ASC),
    STATUS_DESC ("Status z-a", "status", Sort.Direction.DESC),
    PRICE_ASC ("Price increasing", "price", Sort.Direction.ASC),
    PRICE_DESC ("Price decreasing", "price", Sort.Direction.DESC),
    YEAR_ASC ("Less recent", "year", Sort.Direction.ASC),
    YEAR_DESC ("Most recent", "year", Sort.Direction.DESC),
    STOCK_ASC ("Stock increasing", "stock", Sort.Direction.ASC),
    STOCK_DESC ("Stock decreasing", "stock", Sort.Direction.DESC),
    TITLE_ASC ("Title a-z", "title", Sort.Direction.ASC),
    TITLE_DESC ("Title z-a", "title", Sort.Direction.DESC),
    LANGUAGE_ASC ("Language a-z", "language", Sort.Direction.ASC),
    LANGUAGE_DESC ("Language z-a", "language", Sort.Direction.DESC);

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
