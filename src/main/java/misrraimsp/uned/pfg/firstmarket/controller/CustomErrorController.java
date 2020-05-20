package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController extends BasicController implements ErrorController {

    @Autowired
    public CustomErrorController(UserServer userServer,
                                 BookServer bookServer,
                                 CatServer catServer,
                                 ImageServer imageServer,
                                 MessageSource messageSource,
                                 OrderServer orderServer) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest httpServletRequest,
                              Model model,
                              @AuthenticationPrincipal User authUser) {

        try {
            populateModel(model, authUser);
            Object statusObj = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            if (statusObj != null) {
                int statusCode = Integer.parseInt(statusObj.toString());
                switch (HttpStatus.valueOf(statusCode)) {
                    case NOT_FOUND:
                        LOGGER.debug("404 Not Found Error");
                        model.addAttribute("errorTitle", messageSource.getMessage("http.error.404.title", null, null));
                        model.addAttribute("errorMessage", messageSource.getMessage("http.error.404.message", null, null));
                        break;
                    case INTERNAL_SERVER_ERROR:
                        LOGGER.error("500 Internal Server Error");
                        model.addAttribute("errorTitle", messageSource.getMessage("http.error.500.title", null, null));
                        model.addAttribute("errorMessage", messageSource.getMessage("http.error.500.message", null, null));
                        break;
                    default:
                        LOGGER.error("Http Error");
                        model.addAttribute("errorTitle", messageSource.getMessage("http.error.title", null, null));
                        model.addAttribute("errorMessage", messageSource.getMessage("http.error.message", null, null));
                }
                return "httpError";
            }
            else {
                LOGGER.error("There has been a http-error and ERROR_STATUS_CODE couldn't be read");
                return "redirect:/home";
            }
        }
        catch (Exception e) {
            LOGGER.error("There has been some error trying to handle a http-error", e);
            return "redirect:/home";
        }
    }
}
