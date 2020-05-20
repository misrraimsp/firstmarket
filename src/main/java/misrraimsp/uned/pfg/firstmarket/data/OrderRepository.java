package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Order;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Set<Order> findByUser(User user);
}
