package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BasicController {

    @Autowired
    public HomeController(UserServer userServer,
                          BookServer bookServer,
                          CatServer catServer,
                          ImageServer imageServer,
                          MessageSource messageSource,
                          OrderServer orderServer) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
    }

    @GetMapping("/")
    public String redirectHome(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome(Model model,
                           @AuthenticationPrincipal User authUser) {

        populateModel(model.asMap(), authUser);
        model.addAttribute("trendingBooks", bookServer.findTopTrendingBooks());
        model.addAttribute("newBooks", bookServer.findTopNewBooks());
        return "home";
    }

    @GetMapping("/login")
    public String showLogin(Model model,
                            @AuthenticationPrincipal User authUser) {

        if (authUser != null) {
            LOGGER.warn("Authenticated user trying to GET /login (id={})", authUser.getId());
            return "redirect:/home";
        }
        populateModel(model.asMap(), null);
        return "login";
    }

}