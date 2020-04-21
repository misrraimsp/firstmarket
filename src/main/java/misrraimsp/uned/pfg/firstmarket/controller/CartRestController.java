package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private UserServer userServer;

    @Autowired
    public CartRestController(UserServer userServer) {
        this.userServer = userServer;
        LOGGER.trace("{} created", this.getClass().getName());
    }

    @GetMapping("/user/addBook/{id}")
    public ResponseEntity<Integer> processAddBook(@AuthenticationPrincipal User authUser, @PathVariable("id") Long bookId){
        userServer.addBookToCart(authUser.getId(), bookId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }

    @GetMapping("/user/incrementItem/{id}")
    public ResponseEntity<Integer> processIncrementItem(@AuthenticationPrincipal User authUser, @PathVariable("id") Long itemId){
        userServer.incrementItemFromCart(authUser.getId(), itemId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }

    @GetMapping("/user/decrementItem/{id}")
    public ResponseEntity<Integer> processDecrementItem(@AuthenticationPrincipal User authUser, @PathVariable("id") Long itemId){
        userServer.decrementItemFromCart(authUser.getId(), itemId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }

    @GetMapping("/user/removeItem/{id}")
    public ResponseEntity<Integer> processRemoveItem(@AuthenticationPrincipal User authUser, @PathVariable("id") Long itemId){
        userServer.removeItemFromCart(authUser.getId(), itemId);
        return new ResponseEntity<>(userServer.findById(authUser.getId()).getCart().getCartSize(), HttpStatus.OK);
    }
}
