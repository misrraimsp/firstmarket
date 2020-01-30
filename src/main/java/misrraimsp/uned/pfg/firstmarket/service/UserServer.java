package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServer implements UserDetailsService {

    private UserRepository userRepository;
    private RoleServer roleServer;
    private CartServer cartServer;
    private ItemServer itemServer;

    @Autowired
    public UserServer(UserRepository userRepository,
                      RoleServer roleServer,
                      CartServer cartServer,
                      ItemServer itemServer) {

        this.userRepository = userRepository;
        this.roleServer = roleServer;
        this.cartServer = cartServer;
        this.itemServer = itemServer;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + email + "' not found");
        }
        return user;
    }

    //if role is not specified it is by default assigned to ROLE_USER
    //if cart is not specified it is created a new one
    public User persist(User user, PasswordEncoder passwordEncoder){
        Cart cart = new Cart();
        cart.setLastModified(LocalDateTime.now());
        return this.persist(user, passwordEncoder, Arrays.asList(roleServer.findByName("ROLE_USER")), cartServer.persist(cart));
    }

    public User persist(User user, PasswordEncoder passwordEncoder, List<Role> roles, Cart cart){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        user.setCart(cart);
        return userRepository.save(user);
    }

    public void edit(User editedUser, User authUser) {
        editedUser.setId(authUser.getId());
        editedUser.setPassword(authUser.getPassword());
        editedUser.setCart(authUser.getCart());
        editedUser.setPurchases(authUser.getPurchases());
        userRepository.save(editedUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
    }

    public void addCartItem(Long userId, Book book) {
        //get user, cart and item entities from db
        User user = this.getUserById(userId);
        Cart cart = user.getCart();
        List<Item> items = cart.getItems();
        //check if any item already exist on user cart
        boolean alreadyExists = false;
        for (Item i : items) {
            if (i.getBook().getId().equals(book.getId())) {
                //update and persist item
                i.setQuantity(1 + i.getQuantity());
                itemServer.persist(i);
                alreadyExists = true;
                break;
            }
        }
        if(!alreadyExists){
            //create and persist item
            Item newItem = itemServer.create(book);
            items.add(newItem);
        }
        //update and persist cart
        cart.setItems(items);
        cart.setLastModified(LocalDateTime.now());
        cartServer.persist(cart);
        //update and persist user
        user.setCart(cart);
        userRepository.save(user);
    }
}