package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.exception.EmailAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.exception.InvalidPasswordException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormPassword;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServer implements UserDetailsService {

    private UserRepository userRepository;
    private ProfileServer profileServer;
    private RoleServer roleServer;
    private CartServer cartServer;
    private PurchaseServer purchaseServer;

    @Autowired
    public UserServer(UserRepository userRepository,
                      ProfileServer profileServer,
                      RoleServer roleServer,
                      CartServer cartServer,
                      PurchaseServer purchaseServer) {

        this.userRepository = userRepository;
        this.profileServer = profileServer;
        this.roleServer = roleServer;
        this.cartServer = cartServer;
        this.purchaseServer = purchaseServer;
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
    public User persist(FormUser formUser, PasswordEncoder passwordEncoder, List<Role> roles, Cart cart) throws EmailAlreadyExistsException {
        if (this.emailExists(formUser.getEmail())){
            throw new EmailAlreadyExistsException("There is an account with that email adress: " +  formUser.getEmail());
        }
        else {
            if (roles == null){
                roles = Arrays.asList(roleServer.findByName("ROLE_USER"));
            }
            if (cart == null){
                cart = new Cart();
                cart.setLastModified(LocalDateTime.now());
            }
            Profile profile = new Profile();
            profile.setFirstName(formUser.getFirstName());
            profile.setLastName(formUser.getLastName());
            User user = new User();
            user.setEmail(formUser.getEmail());
            user.setPassword(passwordEncoder.encode(formUser.getPassword()));
            user.setProfile(profileServer.persist(profile));
            user.setRoles(roles);
            user.setCart(cartServer.persist(cart));
            return userRepository.save(user);
        }
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void editProfile(Long userId, Profile newProfile) {
        profileServer.edit(this.findById(userId).getProfile().getId(), newProfile);
    }

    public User findById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
    }

    public void addBookToCart(Long userId, Long bookId) {
        cartServer.addBook(this.findById(userId).getCart(), bookId);
    }

    public void incrementItemFromCart(Long userId, Long itemId) {
        cartServer.incrementItem(this.findById(userId).getCart(), itemId);
    }

    public void decrementItemFromCart(Long userId, Long itemId) {
        cartServer.decrementItem(this.findById(userId).getCart(), itemId);
    }

    public void removeItemFromCart(Long userId, Long itemId) {
        cartServer.removeItem(this.findById(userId).getCart(), itemId);
    }

    @Transactional
    public void addPurchase(Long userId) {
        User user = this.findById(userId);
        Purchase newPurchase = purchaseServer.create(cartServer.emptyCart(user.getCart()));
        if (newPurchase == null){
            return;
        }
        List<Purchase> purchases = user.getPurchases();
        purchases.add(newPurchase);
        user.setPurchases(purchases);
        userRepository.save(user);
    }

    public void editPassword(Long id, PasswordEncoder passwordEncoder, FormPassword formPassword) throws InvalidPasswordException {
        User user = this.findById(id);
        if (!passwordEncoder.matches(formPassword.getCurrentPassword(), user.getPassword())){
            throw new InvalidPasswordException("incorrect password for user: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(formPassword.getPassword()));
        userRepository.save(user);
    }

}