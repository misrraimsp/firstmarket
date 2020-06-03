package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.UserForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityRandomPasswordProperties;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityTokenProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.converter.ConversionManager;
import misrraimsp.uned.pfg.firstmarket.data.SecurityTokenRepository;
import misrraimsp.uned.pfg.firstmarket.data.UserDeletionRepository;
import misrraimsp.uned.pfg.firstmarket.data.UserRepository;
import misrraimsp.uned.pfg.firstmarket.exception.EmailNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.UserNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.security.LockManager;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServer implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final SecurityTokenRepository securityTokenRepository;
    private final UserDeletionRepository userDeletionRepository;

    private final ProfileServer profileServer;
    private final RoleServer roleServer;
    private final CartServer cartServer;

    private final SecurityTokenProperties securityTokenProperties;
    private final SecurityRandomPasswordProperties securityRandomPasswordProperties;

    private final LockManager lockManager;

    private final ConversionManager conversionManager;

    @Autowired
    public UserServer(UserRepository userRepository,
                      SecurityTokenRepository securityTokenRepository,
                      UserDeletionRepository userDeletionRepository,
                      ProfileServer profileServer,
                      RoleServer roleServer,
                      CartServer cartServer,
                      SecurityTokenProperties securityTokenProperties,
                      SecurityRandomPasswordProperties securityRandomPasswordProperties,
                      LockManager lockManager,
                      ConversionManager conversionManager) {

        this.userRepository = userRepository;
        this.securityTokenRepository = securityTokenRepository;
        this.userDeletionRepository = userDeletionRepository;

        this.profileServer = profileServer;
        this.roleServer = roleServer;
        this.cartServer = cartServer;

        this.securityTokenProperties = securityTokenProperties;
        this.securityRandomPasswordProperties = securityRandomPasswordProperties;

        this.lockManager = lockManager;

        this.conversionManager = conversionManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, LockedException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else if (lockManager.isLocked(email)) {
            throw new LockedException("locked");
        } else {
            return user;
        }
    }

    public User getUserByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailNotFoundException(email);
        }
        return user;
    }

    // role ROLE_USER assigned by default if not specified
    // new cart created if not specified
    public User persist(UserForm userForm, PasswordEncoder passwordEncoder, Set<Role> roles, Cart cart) {
        if (roles == null){
            roles = Collections.singleton(roleServer.findByName("ROLE_USER"));
        }
        if (cart == null){
            cart = new Cart();
        }
        Profile profile = new Profile();
        profile.setFirstName("");
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
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public void editProfile(Profile editedProfile) {
        profileServer.edit(editedProfile);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User editEmail(Long userId, String editedEmail)  throws UserNotFoundException {
        User user = this.findById(userId);
        user.setEmail(editedEmail);
        return userRepository.save(user);
    }

    public User setCompletedState(Long userId, boolean b) throws UserNotFoundException {
        User user = this.findById(userId);
        user.setCompleted(b);
        return userRepository.save(user);
    }

    public User setSuspendedState(Long userId, boolean b) throws UserNotFoundException {
        User user = this.findById(userId);
        user.setSuspended(b);
        return userRepository.save(user);
    }

    public User editPassword(Long userId, PasswordEncoder passwordEncoder, String password)  throws UserNotFoundException {
        User user = this.findById(userId);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean checkPassword(Long userId, PasswordEncoder passwordEncoder, String candidatePassword)  throws UserNotFoundException {
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
    @Scheduled(cron = "${fm.schedule.garbage-collection.cron}")
    public void garbageCollection() {
        Date present = Calendar.getInstance().getTime();
        Set<SecurityToken> securityTokens = securityTokenRepository.findByExpiryDateBefore(present);
        securityTokens.forEach(securityToken -> {
            if (securityToken.getSecurityEvent().equals(SecurityEvent.NEW_USER)){
                userRepository.deleteById(securityToken.getUser().getId());
            }
            securityTokenRepository.deleteById(securityToken.getId());
        });
        LOGGER.info("Garbage collection: number of tokens deleted - {}", securityTokens.size());
    }

    public UserDeletion createUserDeletion(Long userId, DeletionReason deletionReason, String comment) throws UserNotFoundException {
        User user = this.findById(userId);
        UserDeletion userDeletion = new UserDeletion();
        userDeletion.setUser(user);
        userDeletion.setDeletionReason((deletionReason == null) ? DeletionReason.OTHER : deletionReason);
        userDeletion.setComment(comment);
        userDeletion.setDate(Calendar.getInstance().getTime());
        return userDeletionRepository.save(userDeletion);
    }

    public boolean isEmailConfirmationAlreadyNeededFor(Long userId, SecurityEvent securityEvent) throws UserNotFoundException {
        return securityTokenRepository.findByUserAndSecurityEventAndExpiryDateAfter(
                this.findById(userId),
                securityEvent,
                Calendar.getInstance().getTime()
        ).size() != 0;
    }

    public ProfileForm getProfileForm(Long userId) throws UserNotFoundException {
        return conversionManager.convertProfileToProfileForm(this.findById(userId).getProfile());
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        return conversionManager.convertProfileFormToProfile(profileForm);
    }

    public boolean hasRole(User authUser, String roleName) {
        for (Role role : authUser.getRoles()) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }
}