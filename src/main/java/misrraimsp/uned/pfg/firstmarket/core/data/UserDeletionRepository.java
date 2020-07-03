package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.UserDeletion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDeletionRepository extends CrudRepository<UserDeletion, Long> {
}
