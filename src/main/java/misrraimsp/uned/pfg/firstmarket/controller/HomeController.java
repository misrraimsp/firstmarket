package misrraimsp.uned.pfg.firstmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToAdmin(){
        return  "redirect:/admin";
    }

}
