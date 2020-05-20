package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showHome(@RequestParam(defaultValue = "${pagination.default-index}") String pageNo,
                           @RequestParam(defaultValue = "${pagination.default-size.book-home}") String pageSize,
                           Model model,
                           @AuthenticationPrincipal User authUser) {

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                Integer.parseInt(pageSize),
                Sort.by("price").descending().and(Sort.by("id").ascending()));

        populateModel(model, authUser);
        model.addAttribute("pageOfEntities", bookServer.findAll(pageable));
        return "home";
    }

    @GetMapping("/login")
    public String showLogin(Model model,
                            @AuthenticationPrincipal User authUser) {

        if (authUser != null) {
            LOGGER.warn("Authenticated user trying to GET /login (id={})", authUser.getId());
            return "redirect:/home";
        }
        populateModel(model, null);
        return "login";
    }

}