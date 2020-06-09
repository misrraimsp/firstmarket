package misrraimsp.uned.pfg.firstmarket.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final MessageSource messageSource;

    public CustomAuthenticationFailureHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request, response, e);
        LOGGER.debug("User({}) authentication failure - {}", request.getParameter("username"), e.getMessage());
        String errorMessage;
        switch (e.getMessage()) {
            case "User is disabled":
                errorMessage = messageSource.getMessage("auth.disabled", null, null);
                break;
            case "Temporarily locked":
                errorMessage = messageSource.getMessage("auth.temporarilyLocked", null, null);
                break;
            case "User account is locked":
                errorMessage = messageSource.getMessage("auth.locked", null, null);
                break;
            default:
                errorMessage = messageSource.getMessage("auth.badCredentials", null, null);
        }
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}
