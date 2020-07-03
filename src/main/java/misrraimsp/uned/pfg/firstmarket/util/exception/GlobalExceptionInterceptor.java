package misrraimsp.uned.pfg.firstmarket.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionInterceptor {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    public void interceptException(Exception e, HttpServletRequest request) throws Exception {
        LOGGER.warn(
                "{} has raised. Request details:(Request_URL={}, HTTP_Session_Id={}, User_Principal={}). Exception details:",
                e.getClass().getSimpleName(),
                request.getRequestURL(),
                request.getSession().getId(),
                request.getUserPrincipal(),
                e
        );
        throw e;
    }
}
