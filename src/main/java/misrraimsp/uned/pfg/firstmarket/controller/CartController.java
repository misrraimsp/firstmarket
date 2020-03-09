package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    private UserServer userServer;

    @Autowired
    public CartController(UserServer userServer) {
        this.userServer = userServer;
    }

    @GetMapping("/user/addBook/{id}")
    public ResponseEntity<Integer> processAddBook(@AuthenticationPrincipal User authUser, @PathVariable("id") Long bookId){
        userServer.addBookToCart(authUser.getId(), bookId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }

    @GetMapping("/user/removeBook/{id}")
    public ResponseEntity<Integer> processRemoveBook(@AuthenticationPrincipal User authUser, @PathVariable("id") Long bookId){
        userServer.removeBookFromCart(authUser.getId(), bookId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }

    @GetMapping("/user/removeItem/{id}")
    public ResponseEntity<Integer> processRemoveItem(@AuthenticationPrincipal User authUser, @PathVariable("id") Long itemId){
        userServer.removeItemFromCart(authUser.getId(), itemId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }
}
