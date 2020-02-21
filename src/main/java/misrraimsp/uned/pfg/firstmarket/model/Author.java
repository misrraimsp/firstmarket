package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class Author implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String firstName;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String lastName;


    /**
     * La igualdad entre autores se define únicamente a través
     * de la iguladad de sus Id's
     *
     * Esto es así para evitar que autores con diferencias entre
     * mayúsculas/minúsculas sean diferentes
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Author)) return false;
        final Author other = (Author) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;

        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Author;
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
