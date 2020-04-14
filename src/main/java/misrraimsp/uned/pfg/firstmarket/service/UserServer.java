package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.dto.UserForm;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityRandomPasswordProperties;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityTokenProperties;
import misrraimsp.uned.pfg.firstmarket.data.SecurityTokenRepository;
import misrraimsp.uned.pfg.firstmarket.data.UserDeletionRepository;
import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.event.security.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServer implements UserDetailsService {

    private UserRepository userRepository;
    private SecurityTokenRepository securityTokenRepository;
    private SecurityTokenProperties securityTokenProperties;
    private UserDeletionRepository userDeletionRepository;

    private ProfileServer profileServer;
    private RoleServer roleServer;
    private CartServer cartServer;
    private PurchaseServer purchaseServer;

    private SecurityRandomPasswordProperties securityRandomPasswordProperties;

    @Autowired
    public UserServer(UserRepository userRepository,
                      SecurityTokenRepository securityTokenRepository,
                      SecurityTokenProperties securityTokenProperties,
                      UserDeletionRepository userDeletionRepository,
                      ProfileServer profileServer,
                      RoleServer roleServer,
                      CartServer cartServer,
                      PurchaseServer purchaseServer,
                      SecurityRandomPasswordProperties securityRandomPasswordProperties) {

        this.userRepository = userRepository;
        this.securityTokenRepository = securityTokenRepository;
        this.securityTokenProperties = securityTokenProperties;
        this.userDeletionRepository = userDeletionRepository;

        this.profileServer = profileServer;
        this.roleServer = roleServer;
        this.cartServer = cartServer;
        this.purchaseServer = purchaseServer;

        this.securityRandomPasswordProperties = securityRandomPasswordProperties;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + email + "' not found");
        }
        return user;
    }

    // role ROLE_USER assigned by default if not specified
    // new cart created if not specified
    public User persist(UserForm userForm, PasswordEncoder passwordEncoder, List<Role> roles, Cart cart) {
        if (roles == null){
            roles = Arrays.asList(roleServer.findByName("ROLE_USER"));
        }
        if (cart == null){
            cart = new Cart();
            cart.setLastModified(LocalDateTime.now());
        }
        Profile profile = new Profile();
        profile.setFirstName(userForm.getFirstName());
        profile.setLastName(userForm.getLastName());
        User user = new User();
        user.setCompleted(false);
        user.setSuspended(false);
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setProfile(profileServer.persist(profile));
        user.setRoles(roles);
        user.setCart(cartServer.persist(cart));
        return userRepository.save(user);
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

    public void editProfile(Long userId, Profile newProfile) {
        profileServer.edit(this.findById(userId).getProfile().getId(), newProfile);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User editEmail(Long userId, String editedEmail) {
        User user = this.findById(userId);
        user.setEmail(editedEmail);
        return userRepository.save(user);
    }

    public User setCompletedState(Long userId, boolean b) {
        User user = this.findById(userId);
        user.setCompleted(b);
        return userRepository.save(user);
    }

    public User setSuspendedState(Long userId, boolean b) {
        User user = this.findById(userId);
        user.setSuspended(b);
        return userRepository.save(user);
    }

    public User editPassword(Long userId, PasswordEncoder passwordEncoder, String password) {
        User user = this.findById(userId);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean checkPassword(Long userId, PasswordEncoder passwordEncoder, String candidatePassword) {
        User user = this.findById(userId);
        return this.checkPassword(passwordEncoder, candidatePassword, user.getPassword());
    }

    private boolean checkPassword(PasswordEncoder passwordEncoder, String candidatePassword, String storedPassword) {
        return passwordEncoder.matches(candidatePassword, storedPassword);
    }

    public String getRandomPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        // lowercase
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(securityRandomPasswordProperties.getNumOfLowerCase());

        // uppercase
        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(securityRandomPasswordProperties.getNumOfUpperCase());

        // digit
        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(securityRandomPasswordProperties.getNumOfDigit());

        return passwordGenerator.generatePassword(securityRandomPasswordProperties.getSize(), lowerCaseRule, upperCaseRule, digitRule);
    }

    public SecurityToken createSecurityToken(SecurityEvent securityEvent, User user, String editedEmail) {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setSecurityEvent(securityEvent);
        securityToken.setUser(user);
        securityToken.setToken(UUID.randomUUID().toString());
        securityToken.setEditedEmail(editedEmail);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, securityTokenProperties.getExpirationInMinutes());
        securityToken.setExpiryDate(new Date(calendar.getTime().getTime()));

        return securityTokenRepository.save(securityToken);
    }

    public SecurityToken getSecurityToken(String token) {
        return securityTokenRepository.findByToken(token);
    }

    public void deleteSecurityToken(Long userTokenId) {
        securityTokenRepository.deleteById(userTokenId);
    }

    @Transactional
    @Scheduled(cron = "${schedule.garbage-collection.cron}")
    public void garbageCollection() {
        Date present = Calendar.getInstance().getTime();
        Set<SecurityToken> securityTokens = securityTokenRepository.findByExpiryDateBefore(present);
        securityTokens.forEach(securityToken -> {
            if (securityToken.getSecurityEvent().equals(SecurityEvent.NEW_USER)){
                userRepository.deleteById(securityToken.getUser().getId());
            }
            securityTokenRepository.deleteById(securityToken.getId());
        });
        //int numDeleted = securityTokenRepository.deleteByExpiryDateBefore(present);
        System.out.println("Deleted at " + present + ": " + securityTokens.size());
    }

    public UserDeletion createUserDeletion(Long userId, String deletionReason, String comment) {
        User user = this.findById(userId);
        UserDeletion userDeletion = new UserDeletion();
        userDeletion.setUser(user);
        userDeletion.setDeletionReason((deletionReason == null) ?
                DeletionReason.OTHER : DeletionReason.values()[Integer.parseInt(deletionReason)]
        );
        userDeletion.setComment(comment);
        userDeletion.setDate(Calendar.getInstance().getTime());
        return userDeletionRepository.save(userDeletion);
    }

    public boolean isEmailConfirmationAlreadyNeededFor(Long userId, SecurityEvent securityEvent) {
        return securityTokenRepository.findByUserAndSecurityEventAndExpiryDateAfter(
                this.findById(userId),
                securityEvent,
                Calendar.getInstance().getTime()
        ).size() != 0;
    }
}