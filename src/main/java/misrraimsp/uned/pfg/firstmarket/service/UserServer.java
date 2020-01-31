package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.model.Cart;
import misrraimsp.uned.pfg.firstmarket.model.Role;
import misrraimsp.uned.pfg.firstmarket.model.User;
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

    @Autowired
    public UserServer(UserRepository userRepository,
                      RoleServer roleServer,
                      CartServer cartServer) {

        this.userRepository = userRepository;
        this.roleServer = roleServer;
        this.cartServer = cartServer;
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

    public User findById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
    }

    public void addBookToCart(Long userId, Long bookId) {
        cartServer.addBook(this.findById(userId).getCart(), bookId);
    }

    public void removeBookFromCart(Long userId, Long bookId) {
        cartServer.removeBook(this.findById(userId).getCart(), bookId);
    }

    public void removeItemFromCart(Long userId, Long itemId) {
        cartServer.removeItem(this.findById(userId).getCart(), itemId);
    }
}