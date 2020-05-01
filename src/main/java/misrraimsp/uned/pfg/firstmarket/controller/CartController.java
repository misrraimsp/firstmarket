package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.exception.BookNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.EmailNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.ItemNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.UserNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
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
                          MessageSource messageSource) {

        super(userServer, bookServer, catServer, imageServer, messageSource);
    }

    @GetMapping("/ajaxCart/addBook/{id}")
    public ResponseEntity<?> processAddBook(@PathVariable("id") Long bookId,
                                            HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse) {

        String userEmail = null;
        User authUser = null;
        try {
            Principal userPrincipal = httpServletRequest.getUserPrincipal();
            String ajaxCartRequested =  httpServletRequest.getHeader("isAjaxCartRequested");
            if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
                LOGGER.warn("Missing 'isAjaxCartRequested' proper header on adding book(id={}) into cart. Redirected to /home", bookId);
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            }
            else if (userPrincipal == null) {
                LOGGER.debug("Anonymous user trying to add book(id={}) into cart. Login requested", bookId);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            else {
                userEmail = userPrincipal.getName();
                authUser = userServer.getUserByEmail(userEmail);
                userServer.addBookToCart(authUser.getId(), bookId);
                LOGGER.debug("Book(id={}) added into user(id={}) cart", bookId, authUser.getId());
                return new ResponseEntity<>(authUser.getCart().getCartSize(), HttpStatus.OK);
            }
        }
        catch (BookNotFoundException e) {
            assert authUser != null;
            LOGGER.warn("Trying to add a non-existent book(id={}) into user(id={}) cart", bookId, authUser.getId(), e);
        }
        catch (ItemNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Trying to increment a non-existent item into user(id={}) cart", authUser.getId(), e);
        }
        catch (EmailNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated userEmail={} does not exist'", userEmail, e);
        }
        catch (UserNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Theoretically unreachable state has been met: 'user(id={}) does not exist'", authUser.getId(), e);
        }
        catch (IOException e) {
            LOGGER.error("sendRedirect IOException has occurred", e);
        }
        catch (Exception e) {
            LOGGER.error("Some unknown exception has occurred", e);
        }
        return null;
    }

    @GetMapping("/ajaxCart/incrementItem/{id}")
    public ResponseEntity<?> processIncrementItem(@PathVariable("id") Long itemId,
                                                  HttpServletRequest httpServletRequest,
                                                  HttpServletResponse httpServletResponse) {

        String userEmail = null;
        User authUser = null;
        try {
            Principal userPrincipal = httpServletRequest.getUserPrincipal();
            String ajaxCartRequested =  httpServletRequest.getHeader("isAjaxCartRequested");
            if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
                LOGGER.warn("Missing 'isAjaxCartRequested' proper header on incrementing cart item(id={}). Redirected to /home", itemId);
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            }
            else if (userPrincipal == null) {
                LOGGER.debug("Anonymous user trying to increment cart item(id={}). Login requested", itemId);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            else {
                userEmail = userPrincipal.getName();
                authUser = userServer.getUserByEmail(userEmail);
                userServer.incrementItemFromCart(authUser.getId(), itemId);
                LOGGER.debug("Item(id={}) incremented inside user(id={}) cart", itemId, authUser.getId());
                return new ResponseEntity<>(authUser.getCart().getCartSize(), HttpStatus.OK);
            }
        }
        catch (ItemNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Trying to increment a non-existent item(id={}) into user(id={}) cart", itemId, authUser.getId(), e);
        }
        catch (EmailNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated userEmail={} does not exist'", userEmail, e);
        }
        catch (UserNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Theoretically unreachable state has been met: 'user(id={}) does not exist'", authUser.getId(), e);
        }
        catch (IOException e) {
            LOGGER.error("sendRedirect IOException has occurred", e);
        }
        catch (Exception e) {
            LOGGER.error("Some unknown exception has occurred", e);
        }
        return null;
    }

    @GetMapping("/ajaxCart/decrementItem/{id}")
    public ResponseEntity<?> processDecrementItem(@PathVariable("id") Long itemId,
                                                  HttpServletRequest httpServletRequest,
                                                  HttpServletResponse httpServletResponse) {

        String userEmail = null;
        User authUser = null;
        try {
            Principal userPrincipal = httpServletRequest.getUserPrincipal();
            String ajaxCartRequested =  httpServletRequest.getHeader("isAjaxCartRequested");
            if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
                LOGGER.warn("Missing 'isAjaxCartRequested' proper header on decrementing cart item(id={}). Redirected to /home", itemId);
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            }
            else if (userPrincipal == null) {
                LOGGER.debug("Anonymous user trying to decrement cart item(id={}). Login requested", itemId);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            else {
                userEmail = userPrincipal.getName();
                authUser = userServer.getUserByEmail(userEmail);
                userServer.decrementItemFromCart(authUser.getId(), itemId);
                LOGGER.debug("Item(id={}) decremented inside user(id={}) cart", itemId, authUser.getId());
                return new ResponseEntity<>(authUser.getCart().getCartSize(), HttpStatus.OK);
            }
        }
        catch (ItemNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Trying to decrement a non-existent item(id={}) into user(id={}) cart", itemId, authUser.getId(), e);
        }
        catch (EmailNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated userEmail={} does not exist'", userEmail, e);
        }
        catch (UserNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Theoretically unreachable state has been met: 'user(id={}) does not exist'", authUser.getId(), e);
        }
        catch (IOException e) {
            LOGGER.error("sendRedirect IOException has occurred", e);
        }
        catch (Exception e) {
            LOGGER.error("Some unknown exception has occurred", e);
        }
        return null;
    }

    @GetMapping("/ajaxCart/removeItems")
    public ResponseEntity<?> processRemoveItem(@RequestParam(required = false) Set<Long> ids,
                                               HttpServletRequest httpServletRequest,
                                               HttpServletResponse httpServletResponse) {

        String userEmail = null;
        User authUser = null;
        try {
            Principal userPrincipal = httpServletRequest.getUserPrincipal();
            String ajaxCartRequested =  httpServletRequest.getHeader("isAjaxCartRequested");
            if (ajaxCartRequested == null || !ajaxCartRequested.equals("1")) {
                LOGGER.warn("Missing 'isAjaxCartRequested' proper header on removing cart items(ids={}). Redirected to /home", ids);
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            }
            else if (userPrincipal == null) {
                LOGGER.debug("Anonymous user trying to remove cart items(ids={}). Login requested", ids);
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            else if (ids.isEmpty()) {
                LOGGER.warn("Trying to remove items without providing its ids. Redirected to /home");
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/home");
            }
            else {
                userEmail = userPrincipal.getName();
                authUser = userServer.getUserByEmail(userEmail);
                User finalAuthUser = authUser;
                ids.forEach(id -> {
                    userServer.removeItemFromCart(finalAuthUser.getId(), id);
                    LOGGER.debug("Item(id={}) removed from user(id={}) cart", id, finalAuthUser.getId());
                });
                return new ResponseEntity<>(authUser.getCart().getCartSize(), HttpStatus.OK);
            }
        }
        catch (ItemNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Trying to remove a non-existent item(id={}) from user(id={}) cart", ids, authUser.getId(), e);
        }
        catch (EmailNotFoundException e) {
            LOGGER.error("Theoretically unreachable state has been met: 'authenticated userEmail={} does not exist'", userEmail, e);
        }
        catch (UserNotFoundException e) {
            assert authUser != null;
            LOGGER.error("Theoretically unreachable state has been met: 'user(id={}) does not exist'", authUser.getId(), e);
        }
        catch (IOException e) {
            LOGGER.error("sendRedirect IOException has occurred", e);
        }
        catch (Exception e) {
            LOGGER.error("Some unknown exception has occurred", e);
        }
        return null;
    }
}
