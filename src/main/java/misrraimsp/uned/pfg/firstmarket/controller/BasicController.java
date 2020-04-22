package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.exception.UserNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;


public abstract class BasicController {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected UserServer userServer;
    protected BookServer bookServer;
    protected CatServer catServer;
    protected ImageServer imageServer;

    @Autowired
    public BasicController(UserServer userServer,
                           BookServer bookServer,
                           CatServer catServer,
                           ImageServer imageServer) {

        this.userServer = userServer;
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
        LOGGER.trace("{} created", this.getClass().getName());
    }

    protected void populateModelWithUserInfo(Model model, User authUser) {
        if (authUser == null) return;
        try {
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        catch (UserNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated user(id={}) does not exist'", authUser.getId(), e);
        }
    }

}
