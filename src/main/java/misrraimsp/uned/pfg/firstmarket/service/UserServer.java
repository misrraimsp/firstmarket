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
    public UserServer(UserRepository userRepository, RoleServer roleServer, CartServer cartServer) {
        this.userRepository = userRepository;
        this.roleServer = roleServer;
        this.cartServer = cartServer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
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

    public void edit(User editedUser, User currentUser) {
        editedUser.setId(currentUser.getId());
        editedUser.setPassword(currentUser.getPassword());
        editedUser.setCart(currentUser.getCart());
        editedUser.setPurchases(currentUser.getPurchases());
        userRepository.save(editedUser);
    }
}