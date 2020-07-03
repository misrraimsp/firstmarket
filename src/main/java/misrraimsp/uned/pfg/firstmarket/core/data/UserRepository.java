package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.Role;
import misrraimsp.uned.pfg.firstmarket.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findByRolesContains(Role role, Pageable pageable);
}
