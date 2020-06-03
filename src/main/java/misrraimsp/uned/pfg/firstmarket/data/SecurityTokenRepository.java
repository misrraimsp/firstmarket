package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SecurityTokenRepository extends CrudRepository<SecurityToken, Long> {

    SecurityToken findByToken(String token);

    @Override
    Set<SecurityToken> findAll();

    Set<SecurityToken> findByUserAndSecurityEvent(User user, SecurityEvent securityEvent);
}
