package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Purchase;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    Set<Purchase> findByUser(User user);
}
