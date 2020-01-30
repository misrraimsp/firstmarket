package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class CartController {

    private UserServer userServer;
    private BookServer bookServer;
    private ImageServer imageServer;

    @Autowired
    public CartController(UserServer userServer, BookServer bookServer, ImageServer imageServer) {
        this.userServer = userServer;
        this.bookServer = bookServer;
        this.imageServer = imageServer;
    }

    @PostMapping("/addItem")
    String processAddItem(@AuthenticationPrincipal User authUser, @RequestParam Long bookId){
        userServer.addCartItem(authUser.getId(), bookServer.findById(bookId));
        return "redirect:/home";
    }

    @GetMapping("/cart")
    public String showCart(@AuthenticationPrincipal User authUser, Model model){
        model.addAttribute("title", "Cart");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("items", userServer.getUserById(authUser.getId()).getCart().getItems());
        return "cart";
    }
}
