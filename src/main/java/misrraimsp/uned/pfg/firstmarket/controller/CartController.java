package misrraimsp.uned.pfg.firstmarket.controller;

import com.stripe.exception.StripeException;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@RestController
public class CartController extends BasicController {

    @Autowired
    public CartController(UserServer userServer,
                          BookServer bookServer,
                          CatServer catServer,
                          ImageServer imageServer,
                          MessageSource messageSource,
                          OrderServer orderServer) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
    }

    @GetMapping("/ajaxCart/addBook/{id}")
    public ResponseEntity<?> processAddBook(@PathVariable("id") Long bookId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException, StripeException {

        Principal userPrincipal = request.getUserPrincipal();
        String ajaxCartRequested =  request.getHeader("isAjaxCartRequested");
        if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
            LOGGER.warn("Missing 'isAjaxCartRequested' header on adding book(id={}) into cart. Redirected to /home", bookId);
            response.sendRedirect(request.getContextPath() + "/home");
            return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
        }
        else if (userPrincipal == null) {
            LOGGER.debug("Anonymous user trying to add book(id={}) into cart. Login requested", bookId);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            String userEmail = userPrincipal.getName();
            User authUser = userServer.getUserByEmail(userEmail);
            if (authUser.getCart().isCommitted()) {
                orderServer.unCommitCart(authUser);
            }
            userServer.addBookToCart(authUser.getId(), bookId);
            LOGGER.debug("Book(id={}) added into user(id={}) cart", bookId, authUser.getId());
            return new ResponseEntity<>(authUser.getCart().getSize(), HttpStatus.OK);
        }
    }

    @GetMapping("/ajaxCart/incrementItem/{id}")
    public ResponseEntity<?> processIncrementItem(@PathVariable("id") Long itemId,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws IOException, StripeException {

        Principal userPrincipal = request.getUserPrincipal();
        String ajaxCartRequested =  request.getHeader("isAjaxCartRequested");
        if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
            LOGGER.warn("Missing 'isAjaxCartRequested' header on incrementing cart item(id={}). Redirected to /home", itemId);
            response.sendRedirect(request.getContextPath() + "/home");
            return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
        }
        else if (userPrincipal == null) {
            LOGGER.debug("Anonymous user trying to increment cart item(id={}). Login requested", itemId);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            String userEmail = userPrincipal.getName();
            User authUser = userServer.getUserByEmail(userEmail);
            if (authUser.getCart().isCommitted()) {
                orderServer.unCommitCart(authUser);
            }
            userServer.incrementItemFromCart(authUser.getId(), itemId);
            LOGGER.debug("Item(id={}) incremented inside user(id={}) cart", itemId, authUser.getId());
            return new ResponseEntity<>(authUser.getCart().getSize(), HttpStatus.OK);
        }
    }

    @GetMapping("/ajaxCart/decrementItem/{id}")
    public ResponseEntity<?> processDecrementItem(@PathVariable("id") Long itemId,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) throws IOException, StripeException {

        Principal userPrincipal = request.getUserPrincipal();
        String ajaxCartRequested =  request.getHeader("isAjaxCartRequested");
        if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
            LOGGER.warn("Missing 'isAjaxCartRequested' proper header on decrementing cart item(id={}). Redirected to /home", itemId);
            response.sendRedirect(request.getContextPath() + "/home");
            return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
        }
        else if (userPrincipal == null) {
            LOGGER.debug("Anonymous user trying to decrement cart item(id={}). Login requested", itemId);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            String userEmail = userPrincipal.getName();
            User authUser = userServer.getUserByEmail(userEmail);
            if (authUser.getCart().isCommitted()) {
                orderServer.unCommitCart(authUser);
            }
            userServer.decrementItemFromCart(authUser.getId(), itemId);
            LOGGER.debug("Item(id={}) decremented inside user(id={}) cart", itemId, authUser.getId());
            return new ResponseEntity<>(authUser.getCart().getSize(), HttpStatus.OK);
        }
    }

    @GetMapping("/ajaxCart/removeItems")
    public ResponseEntity<?> processRemoveItem(@RequestParam(required = false) Set<Long> ids,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws IOException, StripeException {

        Principal userPrincipal = request.getUserPrincipal();
        String ajaxCartRequested =  request.getHeader("isAjaxCartRequested");
        if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
            LOGGER.warn("Missing 'isAjaxCartRequested' proper header on removing cart items(ids={}). Redirected to /home", ids);
            response.sendRedirect(request.getContextPath() + "/home");
            return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
        }
        else if (userPrincipal == null) {
            LOGGER.debug("Anonymous user trying to remove cart items(ids={}). Login requested", ids);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else if (ids.isEmpty()) {
            LOGGER.warn("Trying to remove items without providing its ids. Redirected to /home");
            response.sendRedirect(request.getContextPath() + "/home");
            return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
        }
        else {
            String userEmail = userPrincipal.getName();
            User authUser = userServer.getUserByEmail(userEmail);
            if (authUser.getCart().isCommitted()) {
                orderServer.unCommitCart(authUser);
            }
            ids.forEach(id -> {
                userServer.removeItemFromCart(authUser.getId(), id);
                LOGGER.debug("Item(id={}) removed from user(id={}) cart", id, authUser.getId());
            });
            return new ResponseEntity<>(authUser.getCart().getSize(), HttpStatus.OK);
        }
    }
}
