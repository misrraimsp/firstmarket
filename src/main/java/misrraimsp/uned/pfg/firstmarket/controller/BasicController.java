package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.exception.UserNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.Objects;


public abstract class BasicController {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected UserServer userServer;
    protected BookServer bookServer;
    protected CatServer catServer;
    protected ImageServer imageServer;
    protected MessageSource messageSource;
    protected PurchaseServer purchaseServer;

    @Autowired
    public BasicController(UserServer userServer,
                           BookServer bookServer,
                           CatServer catServer,
                           ImageServer imageServer,
                           MessageSource messageSource,
                           PurchaseServer purchaseServer) {

        this.userServer = userServer;
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
        this.messageSource = messageSource;
        this.purchaseServer = purchaseServer;
        LOGGER.debug("{} created", this.getClass().getName());
    }

    protected void populateModel(Model model, User authUser) {
        if (authUser != null && userServer.hasRole(authUser, "ROLE_USER")) {
            try {
                User user = userServer.findById(authUser.getId());
                model.addAttribute("firstName", user.getProfile().getFirstName());
                model.addAttribute("cartSize", user.getCart().getCartSize());
            }
            catch (UserNotFoundException e) {
                LOGGER.error("Theoretically unreachable state has been met: 'authenticated user(id={}) does not exist'", authUser.getId(), e);
            }
        }
        model.addAttribute("mainCategories", catServer.getMainCategories());
    }

    protected void handleGlobalErrors(Errors errors) {
        errors.getGlobalErrors().forEach(objectError -> {
            switch (Objects.requireNonNull(objectError.getCode())) {
                case "PasswordMatches":
                    errors.rejectValue("matchingPassword", "password.notMatching", Objects.requireNonNull(objectError.getDefaultMessage()));
                    LOGGER.debug("Passwords does not match: {}", objectError.toString());
                    break;
                case "ValidDate":
                    errors.rejectValue("year", "date.invalid", Objects.requireNonNull(objectError.getDefaultMessage()));
                    LOGGER.debug("Date is not valid: {}", objectError.toString());
                    break;
                default:
                    LOGGER.warn("There has been an unexpected global error: {}", objectError.toString());
            }
        });
    }

}
