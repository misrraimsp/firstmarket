package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface SecurityTokenRepository extends CrudRepository<SecurityToken, Long> {

    SecurityToken findByToken(String token);

    Set<SecurityToken> findByCreatedDateBefore(LocalDateTime dateTime);

    Set<SecurityToken> findByTargetUserAndSecurityEventAndCreatedDateAfter(User targetUser, SecurityEvent securityEvent, LocalDateTime dateTime);
}
