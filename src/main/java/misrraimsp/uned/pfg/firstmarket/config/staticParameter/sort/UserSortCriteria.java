package misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort;

import org.springframework.data.domain.Sort;

public enum UserSortCriteria {

    LOCKED_ASC ("Locked account up", "accountLocked", Sort.Direction.ASC),
    LOCKED_DESC ("Locked account down", "accountLocked", Sort.Direction.DESC),
    SUSPENDED_ASC ("Deleted account up", "suspended", Sort.Direction.ASC),
    SUSPENDED_DESC ("Deleted account down", "suspended", Sort.Direction.DESC),
    //COMPLETED_ASC ("Email confirmation ascending", "completed", Sort.Direction.ASC),
    //COMPLETED_DESC ("Email confirmation descending", "completed", Sort.Direction.DESC),
    EMAIL_ASC ("Email a-z", "email", Sort.Direction.ASC),
    EMAIL_DESC ("Email z-a", "email", Sort.Direction.DESC),
    CREATED_DATE_ASC("Less recent", "createdDate", Sort.Direction.ASC),
    CREATED_DATE_DESC("Most recent", "createdDate", Sort.Direction.DESC);

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
