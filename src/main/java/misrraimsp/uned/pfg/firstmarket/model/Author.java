package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Objects;

@Data
@Entity
public class Author extends BasicEntity {

    private String firstName;

    private String lastName;

    @Transient
    private int numOfBooks;

    // case insensitive
    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Author)) return false;

        final Author other = (Author) o;

        final String this$firstName = this.getFirstName();
        final String other$firstName = other.getFirstName();
        if (this$firstName == null ? other$firstName != null : !this$firstName.equalsIgnoreCase(other$firstName)) return false;

        final String this$lastName = this.getLastName();
        final String other$lastName = other.getLastName();
        if (this$lastName == null ? other$lastName != null : !this$lastName.equalsIgnoreCase(other$lastName)) return false;

        final Long this$id = this.getId();
        final Long other$id = other.getId();
        return Objects.equals(this$id, other$id);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $firstName = this.getFirstName();
        result = result * PRIME + ($firstName == null ? 43 : $firstName.hashCode());
        final Object $lastName = this.getLastName();
        result = result * PRIME + ($lastName == null ? 43 : $lastName.hashCode());
        return result;
    }
}
