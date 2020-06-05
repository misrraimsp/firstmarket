package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

import org.springframework.data.domain.Sort;

public enum ImageSortCriteria {

    SIZE_ASC ("Size ascending", "size", Sort.Direction.ASC),
    SIZE_DESC ("Size descending", "size", Sort.Direction.DESC),
    DEFAULT ("Default image", "is_default", Sort.Direction.DESC),
    NAME_ASC ("Name ascending", "name", Sort.Direction.ASC),
    NAME_DESC ("Name descending", "name", Sort.Direction.DESC),
    MIME_ASC ("MIME Type ascending", "mime_type", Sort.Direction.ASC),
    MIME_DESC ("MIME Type descending", "mime_type", Sort.Direction.DESC);

    private final String text;
    private final String property;
    private final Sort.Direction direction;

    ImageSortCriteria(String text, String property, Sort.Direction direction) {
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
