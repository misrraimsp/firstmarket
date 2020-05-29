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
    public String handleError(HttpServletRequest request,
                              Model model,
                              @AuthenticationPrincipal User authUser) {

        populateModel(model, authUser);
        Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusObj != null) {
            int statusCode = Integer.parseInt(statusObj.toString());
            switch (HttpStatus.valueOf(statusCode)) {
                case NOT_FOUND:
                    model.addAttribute("errorTitle", messageSource.getMessage("http.error.404.title", null, null));
                    model.addAttribute("errorMessage", messageSource.getMessage("http.error.404.message", null, null));
                    break;
                case INTERNAL_SERVER_ERROR:
                    model.addAttribute("errorTitle", messageSource.getMessage("http.error.500.title", null, null));
                    model.addAttribute("errorMessage", messageSource.getMessage("http.error.500.message", null, null));
                    break;
                case BAD_REQUEST:
                    model.addAttribute("errorTitle", messageSource.getMessage("http.error.400.title", null, null));
                    model.addAttribute("errorMessage", messageSource.getMessage("http.error.400.message", null, null));
                    break;
                default:
                    LOGGER.error("Http Error (other than 400, 404 or 500)");
                    model.addAttribute("errorTitle", messageSource.getMessage("http.error.title", null, null));
                    model.addAttribute("errorMessage", messageSource.getMessage("http.error.message", null, null));
            }
            return "error";
        }
        else {
            LOGGER.error("There has been an error and ERROR_STATUS_CODE couldn't be read");
            return "redirect:/home";
        }
    }
}
