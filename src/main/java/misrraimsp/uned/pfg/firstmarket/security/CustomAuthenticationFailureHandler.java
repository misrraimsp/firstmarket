package misrraimsp.uned.pfg.firstmarket.security;

import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private MessageSource messageSource;

    public CustomAuthenticationFailureHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException, ServletException {

        setDefaultFailureUrl("/login?error=true");

        super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);

        String errorMessage;
        if (e.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = messageSource.getMessage("auth.disabled", null, null);
        }
        else if (e.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = messageSource.getMessage("auth.expired", null, null);
        }
        else if (e.getMessage().equalsIgnoreCase("locked")) {
            errorMessage = messageSource.getMessage("auth.locked", null, null);
        }
        else {
            errorMessage = messageSource.getMessage("auth.badCredentials", null, null);
        }

        httpServletRequest.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }
}
