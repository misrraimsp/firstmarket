package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.ItemServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class CartController {

    private UserServer userServer;
    private BookServer bookServer;
    private ImageServer imageServer;
    private ItemServer itemServer;

    @Autowired
    public CartController(UserServer userServer, BookServer bookServer,
                          ImageServer imageServer, ItemServer itemServer) {
        this.userServer = userServer;
        this.bookServer = bookServer;
        this.imageServer = imageServer;
        this.itemServer = itemServer;
    }

    @GetMapping("/cart")
    public String showCart(@AuthenticationPrincipal User authUser, Model model){
        model.addAttribute("title", "Cart");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("items", userServer.getUserById(authUser.getId()).getCart().getItems());
        return "cart";
    }

    @PostMapping("/addItem")
    String processAddItem(@AuthenticationPrincipal User authUser, @RequestParam Long bookId){
        userServer.addCartItem(authUser.getId(), bookServer.findById(bookId));
        return "redirect:/home";
    }

    @GetMapping("/incrementItem/{id}")
    public String processIncrementItem(@PathVariable("id") Long id){
        itemServer.increment(id);
        return "redirect:/user/cart";
    }
}
