package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTokenRepository extends CrudRepository<SecurityToken, Long> {

    SecurityToken findByToken(String token);

}
