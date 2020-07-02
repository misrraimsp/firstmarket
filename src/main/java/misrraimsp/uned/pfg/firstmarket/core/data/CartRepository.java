package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Override
    Set<Cart> findAll();
}
