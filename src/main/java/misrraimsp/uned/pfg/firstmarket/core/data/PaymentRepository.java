package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
