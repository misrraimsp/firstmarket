package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

import org.springframework.data.domain.Sort;

public enum UserSortCriteria {

    LOCKED_ASC ("Locked account ascending", "accountLocked", Sort.Direction.ASC),
    LOCKED_DESC ("Locked account descending", "accountLocked", Sort.Direction.DESC),
    SUSPENDED_ASC ("Suspended account ascending", "suspended", Sort.Direction.ASC),
    SUSPENDED_DESC ("Suspended account descending", "suspended", Sort.Direction.DESC),
    COMPLETED_ASC ("Email confirmation ascending", "completed", Sort.Direction.ASC),
    COMPLETED_DESC ("Email confirmation descending", "completed", Sort.Direction.DESC),
    EMAIL_ASC ("Email ascending", "email", Sort.Direction.ASC),
    EMAIL_DESC ("Email descending", "email", Sort.Direction.DESC);

    private final String text;
    private final String property;
    private final Sort.Direction direction;

    UserSortCriteria(String text, String property, Sort.Direction direction) {
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
