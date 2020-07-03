package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    Set<Cart> findAll();
}
