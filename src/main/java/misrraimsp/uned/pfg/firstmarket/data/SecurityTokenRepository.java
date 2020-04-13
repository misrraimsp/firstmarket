package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.event.security.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface SecurityTokenRepository extends CrudRepository<SecurityToken, Long> {

    SecurityToken findByToken(String token);

    //int deleteByExpiryDateBefore(Date date);

    Set<SecurityToken> findByExpiryDateBefore(Date date);

    Set<SecurityToken> findByUserAndSecurityEventAndExpiryDateAfter(User user, SecurityEvent securityEvent, Date date);
}
