package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.UserForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityRandomPasswordProperties;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityTokenProperties;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.TimeFormatProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Gender;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final TimeFormatProperties timeFormatProperties;

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
                      TimeFormatProperties timeFormatProperties,
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
        this.timeFormatProperties = timeFormatProperties;

        this.lockManager = lockManager;

        this.conversionManager = conversionManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, LockedException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        else if (lockManager.isLocked(email)) {
            throw new LockedException("Temporarily locked");
        }
        else {
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
        profile.setLastName("");
        profile.setPhone("");
        profile.setGender(Gender.UNDEFINED);
        User user = new User();
        user.setCompleted(false);
        user.setSuspended(false);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setProfile(profileServer.persist(profile));
        user.setRoles(roles);
        user.setCart(cartServer.persist(cart));
        User savedUser = userRepository.save(user);
        profile.setFirstName("user" + user.getId());
        profileServer.persist(profile);
        return savedUser;
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

    public User setAccountLockedState(Long userId, boolean b) throws UserNotFoundException {
        User user = this.findById(userId);
        user.setAccountLocked(b);
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
        securityToken.setTargetUser(user);
        securityToken.setToken(UUID.randomUUID().toString());
        securityToken.setTargetEmail(editedEmail);
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
        LOGGER.debug("Garbage collection: started");
        securityTokenRepository.findByCreatedDateBefore(LocalDateTime.now().minusMinutes(securityTokenProperties.getExpirationInMinutes()))
                .forEach(st -> {
                    if (st.getSecurityEvent().equals(SecurityEvent.NEW_USER)) {
                        Long userId = st.getTargetUser().getId();
                        userRepository.deleteById(userId);
                        LOGGER.debug("Garbage collection: not completed user(id={}) removed", userId);
                    }
                    securityTokenRepository.deleteById(st.getId());
                    LOGGER.debug("Garbage collection: token(id={}) of type {} removed", st.getId(), st.getSecurityEvent().name());
                });
        LOGGER.debug("Garbage collection: finished");
    }

    public UserDeletion createUserDeletion(Long userId, DeletionReason deletionReason, String comment) throws UserNotFoundException {
        UserDeletion userDeletion = new UserDeletion();
        userDeletion.setUser(this.findById(userId));
        userDeletion.setDeletionReason((deletionReason == null) ? DeletionReason.OTHER : deletionReason);
        userDeletion.setComment(comment);
        return userDeletionRepository.save(userDeletion);
    }

    public boolean isEmailConfirmationAlreadyNeededFor(Long userId, SecurityEvent securityEvent) throws UserNotFoundException {
        return !securityTokenRepository.findByTargetUserAndSecurityEventAndCreatedDateAfter(
                this.findById(userId),
                securityEvent,
                LocalDateTime.now().minusMinutes(securityTokenProperties.getExpirationInMinutes())
        ).isEmpty();
    }

    public ProfileForm getProfileForm(Long userId) throws UserNotFoundException {
        return conversionManager.convertProfileToProfileForm(this.findById(userId).getProfile());
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        return conversionManager.convertProfileFormToProfile(profileForm);
    }

    public boolean hasRole(User authUser, String roleName) {
        if (authUser == null) return false;
        return authUser.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
    }

    public List<Long> getAllCartBookIds() {
         return cartServer.findAll()
                 .stream()
                 .map(Cart::getItems)
                 .flatMap(Set::stream)
                 .map(Item::getBook)
                 .map(Book::getId)
                 .collect(Collectors.toList());
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findByRolesContains(roleServer.findByName("ROLE_USER"), pageable);
    }

    public Set<String> getLockedMails() {
        return lockManager.getLocked();
    }
}