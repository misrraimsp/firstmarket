package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImplementation implements AuditorAware<String> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                LOGGER.debug("user principal({}) of type User", ((User) authentication.getPrincipal()).getId());
                return Optional.of(((User) authentication.getPrincipal()).getId().toString());
            }
            else if (authentication.getPrincipal() instanceof String) {
                LOGGER.debug("user principal({}) of type String", authentication.getPrincipal());
                return Optional.of((String) authentication.getPrincipal());
            }
            else if (authentication.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
                LOGGER.debug("user principal({}) of type UsernamePasswordAuthenticationToken", authentication.getPrincipal());
                return Optional.of(((UsernamePasswordAuthenticationToken) authentication.getPrincipal()).getName());
            }
            else {
                LOGGER.debug("user principal({}) of unexpected type {}", authentication.getPrincipal(), authentication.getPrincipal().getClass());
                return Optional.of("unknown");
            }
        }
        else {
            if (authentication == null) {
                LOGGER.debug("authentication is null");
            }
            else {
                LOGGER.debug("authentication not null but authentication.isAuthenticated is {}", authentication.isAuthenticated());
            }
            return Optional.of("unknown");
        }
    }
}
