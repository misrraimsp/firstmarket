package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Order;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Page<Order> findByUser(User user, Pageable pageable);

    Page<Order> findAll(Pageable pageable);
}
