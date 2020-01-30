package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    private UserServer userServer;
    private BookServer bookServer;

    @Autowired
    public CartController(UserServer userServer, BookServer bookServer) {
        this.userServer = userServer;
        this.bookServer = bookServer;
    }

    @PostMapping("/user/addItem")
    String processAddItem(@AuthenticationPrincipal User authUser, @RequestParam Long bookId){
        userServer.addCartItem(authUser.getId(), bookServer.findById(bookId));
        return "redirect:/home";
    }
}
