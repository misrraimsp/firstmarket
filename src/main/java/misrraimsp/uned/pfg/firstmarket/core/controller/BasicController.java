package misrraimsp.uned.pfg.firstmarket.core.controller;

import misrraimsp.uned.pfg.firstmarket.core.model.User;
import misrraimsp.uned.pfg.firstmarket.core.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public abstract class BasicController {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected UserServer userServer;
    protected BookServer bookServer;
    protected CatServer catServer;
    protected ImageServer imageServer;
    protected MessageSource messageSource;
    protected OrderServer orderServer;

    @Autowired
    public BasicController(UserServer userServer,
                           BookServer bookServer,
                           CatServer catServer,
                           ImageServer imageServer,
                           MessageSource messageSource,
                           OrderServer orderServer) {

        this.userServer = userServer;
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
        this.messageSource = messageSource;
        this.orderServer = orderServer;
        LOGGER.debug("{} created", this.getClass().getName());
    }

    protected void populateModel(Map<String, Object> properties, User authUser) {
        properties.put("mainCategories", catServer.getMainCategories());
        if (authUser != null && userServer.hasRole(authUser, "ROLE_USER")) {
            properties.put("user", userServer.findById(authUser.getId()));
        }
    }

    protected void populateModelToInfo(Map<String, Object> properties,
                                       String headTitle,
                                       String infoTitle,
                                       List<String> infoMessages,
                                       boolean showContactUs) {

        properties.put("headTitle", headTitle);
        properties.put("infoTitle", infoTitle);
        properties.put("infoMessages", infoMessages);
        properties.put("showContactUs", showContactUs);
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
