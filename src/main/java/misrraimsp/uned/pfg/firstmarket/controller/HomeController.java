package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private BookServer bookServer;
    private CatServer catServer;
    private UserServer userServer;

    private ValidationRegexProperties validationRegexProperties;

    @Autowired
    public HomeController(BookServer bookServer,
                          CatServer catServer,
                          UserServer userServer,
                          ValidationRegexProperties validationRegexProperties) {

        this.bookServer = bookServer;
        this.catServer = catServer;
        this.userServer = userServer;

        this.validationRegexProperties = validationRegexProperties;
    }

    @GetMapping("/")
    public String redirectHome(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHome(@RequestParam(defaultValue = "${pagination.default.number}") String pageNo,
                           @RequestParam(defaultValue = "${pagination.default.size}") String pageSize,
                           Model model,
                           @AuthenticationPrincipal User authUser){

        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                Integer.parseInt(pageSize),
                Sort.by("price").descending().and(Sort.by("id").ascending()));

        model.addAttribute("pageOfBooks", bookServer.findAll(pageable));
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("patterns", validationRegexProperties);
        return "home";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("patterns", validationRegexProperties);
        return "login";
    }

}