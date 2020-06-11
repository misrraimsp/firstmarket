package misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort;

import org.springframework.data.domain.Sort;

public enum ImageSortCriteria {

    SIZE_ASC ("Size asc", "size", Sort.Direction.ASC),
    SIZE_DESC ("Size desc", "size", Sort.Direction.DESC),
    DEFAULT ("Default image", "is_default", Sort.Direction.DESC),
    NAME_ASC ("Name asc", "name", Sort.Direction.ASC),
    NAME_DESC ("Name desc", "name", Sort.Direction.DESC),
    MIME_ASC ("MIME Type asc", "mime_type", Sort.Direction.ASC),
    MIME_DESC ("MIME Type desc", "mime_type", Sort.Direction.DESC),
    MODIFIED_DATE_ASC("Last modified asc", "last_modified_date", Sort.Direction.ASC),
    MODIFIED_DATE_DESC("Last modified desc", "last_modified_date", Sort.Direction.DESC);

    /*
    CREATED_DATE_ASC("Uploading date asc", "created_date", Sort.Direction.ASC),
    CREATED_DATE_DESC("Uploading date desc", "created_date", Sort.Direction.DESC),
    CREATED_BY_ASC("Creator ascending", "createdBy", Sort.Direction.ASC),
    CREATED_BY_DESC("Creator descending", "createdBy", Sort.Direction.DESC),
    MODIFIED_BY_ASC("Last updater ascending", "createdBy", Sort.Direction.ASC),
    MODIFIED_BY_DESC("Last updater descending", "createdBy", Sort.Direction.DESC);*/

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
