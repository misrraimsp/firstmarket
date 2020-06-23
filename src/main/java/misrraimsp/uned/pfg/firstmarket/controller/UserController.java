package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.PasswordForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.UserDeletionForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.UserForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.SecurityTokenProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PageSize;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.UserSortCriteria;
import misrraimsp.uned.pfg.firstmarket.event.*;
import misrraimsp.uned.pfg.firstmarket.exception.EmailNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.UserDeletion;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController extends BasicController {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SecurityTokenProperties securityTokenProperties;

    @Autowired
    public UserController(UserServer userServer,
                          BookServer bookServer,
                          CatServer catServer,
                          ImageServer imageServer,
                          MessageSource messageSource,
                          OrderServer orderServer,
                          PasswordEncoder passwordEncoder,
                          ApplicationEventPublisher applicationEventPublisher,
                          SecurityTokenProperties securityTokenProperties) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.securityTokenProperties = securityTokenProperties;
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model,
                                  @AuthenticationPrincipal User authUser) {

        if (authUser == null) {
            populateModel(model.asMap(), null);
            model.addAttribute("userForm", new UserForm());
            return "newUserForm";
        }
        LOGGER.warn("authenticated users are not allowed to GET request on new user process (userId={})", authUser.getId());
        return "redirect:/home";
    }

    @PostMapping("/newUser")
    public String processNewUser(@Valid UserForm userForm,
                                 Errors errors,
                                 Model model,
                                 @AuthenticationPrincipal User authUser) {

        if (authUser != null) {
            LOGGER.warn("authenticated users are not allowed to POST request on new user process (userId={})", authUser.getId());
            return "redirect:/home";
        }
        // error checks
        boolean isRestarting = false;
        boolean hasError = false;
        if (errors.hasGlobalErrors()) {
            hasError = true;
            handleGlobalErrors(errors);
        }
        // manage email-already-exists situations
        else if (userServer.emailExists(userForm.getEmail())) {
            User user = userServer.getUserByEmail(userForm.getEmail());
            // user is suspended (the user has deleted his account)
            if (user.isSuspended()){
                // there is a valid restart_user token sent waiting for email confirmation
                if (userServer.isEmailConfirmationAlreadyNeededFor(user.getId(),SecurityEvent.RESTART_USER)){
                    hasError = true;
                    errors.rejectValue("email", "email.checkEmail");
                    LOGGER.debug("Trying to create a new user account with a valid {} token waiting for confirmation at: {}",
                            SecurityEvent.RESTART_USER.name(),
                            user.getEmail());
                }
                // normal restoring workflow
                else {
                    isRestarting = true;
                }
            }
            // user is not completed -and not suspended- (email address verification has never occurred)
            else if (!user.isCompleted()) {
                // there is a valid new_user token sent waiting for email confirmation
                if (userServer.isEmailConfirmationAlreadyNeededFor(user.getId(),SecurityEvent.NEW_USER)){
                    hasError = true;
                    errors.rejectValue("email", "email.checkEmail");
                    LOGGER.info("Trying to create a new user account with a valid {} token waiting for confirmation at: {}",
                            SecurityEvent.NEW_USER.name(),
                            user.getEmail());
                }
                // user is not complete but the new_user token has expired, so the user and the token are deleted and
                // normal new user workflow goes on
                else {
                    userServer.garbageCollection();
                    LOGGER.info("A non-completed user, with expired {} token and email={}, is trying to create a new account. " +
                            "Token and non-completed user database info has been deleted",
                            SecurityEvent.NEW_USER.name(),
                            user.getEmail());
                }
            }
            // user is completed and not suspended, so that email can not be used again
            else {
                hasError = true;
                errors.rejectValue("email", "email.notUnique");
                LOGGER.info("Trying to create a new user account with an already used email={}", user.getEmail());
            }
        }
        if (hasError) {
            populateModel(model.asMap(), null);
            return "newUserForm";
        }
        // complete process
        SecurityEvent securityEvent;
        Long userId;
        if (isRestarting) {
            securityEvent = SecurityEvent.RESTART_USER;
            userId = userServer.getUserByEmail(userForm.getEmail()).getId();
        }
        else {
            securityEvent = SecurityEvent.NEW_USER;
            userId = userServer.persist(userForm, passwordEncoder, null, null).getId();
            LOGGER.debug("User created (still not completed) (id={})", userId);
        }
        // trigger email confirmation
        applicationEventPublisher.publishEvent(
                new OnEmailConfirmationNeededEvent(securityEvent, userId, null)
        );
        return "redirect:/emailConfirmationRequest";

    }

    @GetMapping("/user/editEmail")
    public String showEditEmailForm(Model model, @AuthenticationPrincipal User authUser) {
        populateModel(model.asMap(), authUser);
        return "editEmailForm";
    }

    @PostMapping("/user/editEmail")
    public String processEditEmail(@RequestParam(required = false) String password,
                                   @RequestParam(required = false) String editedEmail,
                                   Model model,
                                   @AuthenticationPrincipal User authUser) {

        // error checks
        boolean hasError = false;
        String errorMessage = null;
        if (password == null) {
            hasError = true;
            errorMessage = messageSource.getMessage("password.empty", null, null);
            LOGGER.debug("password empty on trying to change email (userId={})", authUser.getId());
        }
        else if (editedEmail == null) {
            hasError = true;
            errorMessage = messageSource.getMessage("email.empty", null, null);
            LOGGER.debug("email empty on trying to change email (userId={})", authUser.getId());
        }
        else if (!userServer.checkPassword(authUser.getId(), passwordEncoder, password)) { // check password
            hasError = true;
            errorMessage = messageSource.getMessage("password.invalid", null, null);
            LOGGER.debug("password error on trying to change email (userId={})", authUser.getId());
        }
        else if (userServer.emailExists(editedEmail)) { // check email uniqueness
            hasError = true;
            errorMessage = messageSource.getMessage("email.notUnique", null, null);
            LOGGER.info("Trying email change with an already used one={} (userId={})", authUser.getEmail(), authUser.getId());
        }
        if (hasError) {
            populateModel(model.asMap(), authUser);
            model.addAttribute("message", errorMessage);
            return "editEmailForm";
        }
        // trigger email confirmation
        applicationEventPublisher.publishEvent(
                new OnEmailConfirmationNeededEvent(SecurityEvent.EMAIL_CHANGE, authUser.getId(), editedEmail)
        );
        return "redirect:/emailConfirmationRequest";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(Model model,
                                        @AuthenticationPrincipal User authUser) {

        if (authUser == null) {
            populateModel(model.asMap(), null);
            return "resetPasswordForm";
        }
        LOGGER.warn("authenticated users are not allowed to GET request on reset password process (userId={})", authUser.getId());
        return "redirect:/home";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam(required = false) String email,
                                       Model model,
                                       @AuthenticationPrincipal User authUser) {

        if (authUser != null) {
            LOGGER.warn("authenticated users are not allowed to POST request on reset password process (userId={})", authUser.getId());
            return "redirect:/home";
        }
        if (email == null) {
            model.addAttribute("message", messageSource.getMessage("email.empty", null, null));
            populateModel(model.asMap(), null);
            LOGGER.debug("email empty on trying to reset password");
            return "resetPasswordForm";
        }
        try {
            User user = userServer.getUserByEmail(email);
            if (user.isSuspended()) {
                LOGGER.debug("suspended user(id={}) trying to reset password", user.getId());
                return "redirect:/emailConfirmationRequest";
            }
            applicationEventPublisher.publishEvent(
                    new OnEmailConfirmationNeededEvent(SecurityEvent.RESET_PASSWORD, user.getId(), null)
            );
            return "redirect:/emailConfirmationRequest";
        }
        catch (EmailNotFoundException e) { // catch email not found
            LOGGER.debug("Trying to reset a password providing a non-existent email={}", email);
            return "redirect:/emailConfirmationRequest";
        }
    }

    @GetMapping("/emailConfirmationRequest")
    public String showEmailConfirmationRequest(Model model,
                                               @AuthenticationPrincipal User authUser) {

        populateModel(model.asMap(), authUser);
        populateModelToInfo(
                model.asMap(),
                "Email Confirmation",
                messageSource.getMessage("confirmEmail.title",null, null),
                List.of(
                        messageSource.getMessage("confirmEmail.message.1",null, null),
                        messageSource.getMessage("confirmEmail.message.2",null, null)
                ),
                true);
        return "info";
    }

    @GetMapping("/emailConfirmation")
    public String processEmailConfirmation(@RequestParam(required = false) String token,
                                           Model model,
                                           @AuthenticationPrincipal User authUser) {

        if (token == null) {
            LOGGER.error("token not found at /emailConfirmation");
            return "redirect:/home";
        }
        SecurityToken securityToken = userServer.getSecurityToken(token);
        // error checks
        boolean hasError = false;
        String errorMessage = null;
        if (securityToken == null) { // check for invalid token
            hasError = true;
            errorMessage = messageSource.getMessage("auth.invalidToken", null, null);
            LOGGER.debug("Trying to confirm email with a non-existent token={}", token);
        }
        else if (securityToken.getCreatedDate().plusMinutes(securityTokenProperties.getExpirationInMinutes()).isBefore(LocalDateTime.now())) { // check for expired token
            hasError = true;
            errorMessage = messageSource.getMessage("auth.expiredToken", null, null);
            LOGGER.debug("Trying to confirm email with an expired token={}", token);
        }
        if (hasError) {
            populateModel(model.asMap(), authUser);
            populateModelToInfo(
                    model.asMap(),
                    "Email Confirmation Error",
                    messageSource.getMessage("confirmEmail.error.title",null, null),
                    List.of(errorMessage),
                    true);
            return "info";
        }
        // complete confirmation
        switch (securityToken.getSecurityEvent()){
            case NEW_USER:
                userServer.setCompletedState(securityToken.getTargetUser().getId(),true);
                LOGGER.debug("User(id={}) creation completed", securityToken.getTargetUser().getId());
                userServer.deleteSecurityToken(securityToken.getId());
                applicationEventPublisher.publishEvent(
                        new OnNewUserEvent(securityToken.getTargetUser().getId())
                );
                return "redirect:/login";
            case RESTART_USER:
                userServer.setSuspendedState(securityToken.getTargetUser().getId(),false);
                LOGGER.debug("User(id={}) restored", securityToken.getTargetUser().getId());
                userServer.deleteSecurityToken(securityToken.getId());
                applicationEventPublisher.publishEvent(
                        new OnRestartUserEvent(securityToken.getTargetUser().getId())
                );
                return "redirect:/login";
            case EMAIL_CHANGE:
                userServer.editEmail(securityToken.getTargetUser().getId(), securityToken.getTargetEmail());
                LOGGER.debug("User(id={}) email changed", securityToken.getTargetUser().getId());
                userServer.deleteSecurityToken(securityToken.getId());
                applicationEventPublisher.publishEvent(
                        new OnEmailEditionEvent(securityToken.getTargetUser().getId())
                );
                return "redirect:/home";
            case RESET_PASSWORD:
                String randomPassword = userServer.getRandomPassword();
                applicationEventPublisher.publishEvent(
                        new OnResetPasswordEvent(securityToken.getTargetUser().getId(), randomPassword)
                );
                userServer.editPassword(securityToken.getTargetUser().getId(), passwordEncoder, randomPassword);
                LOGGER.debug("User(id={}) password reset", securityToken.getTargetUser().getId());
                userServer.deleteSecurityToken(securityToken.getId());
                populateModel(model.asMap(),authUser);
                populateModelToInfo(
                        model.asMap(),
                        "Password Reset",
                        messageSource.getMessage("password.reset.title",null, null),
                        List.of(messageSource.getMessage("password.reset.message",null, null)),
                        true);
                return "info";
            default:
                return "redirect:/home";
        }
    }

    @GetMapping("/editPassword") //open for user and admin
    public String showEditPasswordForm(Model model,
                                       @AuthenticationPrincipal User authUser) {

        if (authUser == null) {
            LOGGER.warn("Not authenticated user trying to GET request edit password process");
            return "redirect:/home";
        }
        model.addAttribute("passwordForm", new PasswordForm());
        populateModel(model.asMap(), authUser);
        return "editPasswordForm";
    }

    @PostMapping("/editPassword")
    public String processEditPassword(@Valid PasswordForm passwordForm,
                                      Errors errors,
                                      Model model,
                                      @AuthenticationPrincipal User authUser) {

        if (authUser != null){
            // error checks
            boolean hasError = false;
            if (errors.hasGlobalErrors()) {
                hasError = true;
                handleGlobalErrors(errors);
            }
            else if (!userServer.checkPassword(authUser.getId(),passwordEncoder, passwordForm.getCurrentPassword())){
                hasError = true;
                errors.rejectValue("currentPassword", "password.invalid");
                LOGGER.debug("password error on trying to change password (userId={})", authUser.getId());
            }
            if (hasError) {
                populateModel(model.asMap(), authUser);
                return "editPasswordForm";
            }
            // edit password
            userServer.editPassword(authUser.getId(), passwordEncoder, passwordForm.getPassword());
            LOGGER.debug("Password changed (userId={})", authUser.getId());
        }
        else {
            LOGGER.warn("Not authenticated user trying to POST request on edit password process");
        }
        return "redirect:/home";
    }

    @GetMapping("/user/profileForm")
    public String showProfileForm(Model model,
                                  @AuthenticationPrincipal User authUser) {

        model.addAttribute("profileForm", userServer.getProfileForm(authUser.getId()));
        populateModel(model.asMap(), authUser);
        return "profileForm";
    }

    @PostMapping("/user/profileForm")
    public String processProfileForm(@Valid ProfileForm profileForm,
                                     Errors errors,
                                     Model model,
                                     @AuthenticationPrincipal User authUser) {

        if (errors.hasErrors()) {
            if (errors.hasGlobalErrors()) {
                handleGlobalErrors(errors);
            }
            populateModel(model.asMap(), authUser);
            return "profileForm";
        }
        Profile editedProfile = userServer.convertProfileFormToProfile(profileForm);
        userServer.editProfile(editedProfile);
        LOGGER.debug("User(id={}) has edited its Profile(id={}) successfully", authUser.getId(), profileForm.getProfileId());
        return "redirect:/home";
    }

    @GetMapping("/user/cart")
    public String showCart(Model model,
                           @AuthenticationPrincipal User authUser) {

        model.addAttribute("itemsOutOfStock", null);
        model.addAttribute("cartBookRegistry", bookServer.getCartBookRegistry());
        populateModel(model.asMap(), authUser);
        return "cart";
    }

    @GetMapping("/user/deleteUser")
    public String showDeleteUserForm(Model model,
                                     @AuthenticationPrincipal User authUser) {

        populateModel(model.asMap(), authUser);
        model.addAttribute("deletionReasons", DeletionReason.values());
        model.addAttribute("userDeletionForm", new UserDeletionForm());
        return "deleteUserForm";
    }

    @PostMapping("/user/deleteUser")
    public String processDeleteUser(@Valid UserDeletionForm userDeletionForm,
                                    Errors errors,
                                    Model model,
                                    @AuthenticationPrincipal User authUser,
                                    HttpServletRequest request) {

        // error checks
        boolean hasError = false;
        if (errors.hasErrors()) {
            hasError = true;
            LOGGER.debug("validation error on trying to delete account (userId={})", authUser.getId());
        }
        if (!userServer.checkPassword(authUser.getId(), passwordEncoder, userDeletionForm.getPassword())) {
            hasError = true;
            errors.rejectValue("password", "password.invalid");
            LOGGER.debug("password error on trying to delete account (userId={})", authUser.getId());
        }
        if (hasError) {
            populateModel(model.asMap(),authUser);
            model.addAttribute("deletionReasons", DeletionReason.values());
            return "deleteUserForm";
        }
        // complete deletion
        userServer.setSuspendedState(authUser.getId(),true);
        LOGGER.debug("User(id={}) suspended", authUser.getId());
        UserDeletion userDeletion = userServer.createUserDeletion(
                authUser.getId(),
                userDeletionForm.getDeletionReason(),
                userDeletionForm.getComment()
        );
        // logout
        new SecurityContextLogoutHandler().logout(request, null, null);
        // trigger user removal event
        applicationEventPublisher.publishEvent(new OnDeleteUserEvent(userDeletion));
        return "redirect:/home";
    }

    @GetMapping("/admin/users")
    public String showUsers(@RequestParam(defaultValue = "${fm.pagination.default-index}") int pageNo,
                            @RequestParam(defaultValue = "${fm.pagination.default-size-index.user}") PageSize pageSize,
                            @RequestParam(defaultValue = "${fm.pagination.default-sort-index.user}") UserSortCriteria sort,
                            Model model) {

        populateModel(model.asMap(), null);
        populateModelToUser(model, pageNo, pageSize, sort);
        return "users";
    }

    private void populateModelToUser(Model model,
                                     int pageNo,
                                     PageSize pageSize,
                                     UserSortCriteria sort) {

        Pageable pageable = PageRequest.of(pageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
        Page<User> userPage = userServer.findAllUsers(pageable);
        int lastPageNo = userPage.getTotalPages() - 1;
        if (lastPageNo > 0 && lastPageNo < pageNo) {
            pageable = PageRequest.of(lastPageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
            userPage = userServer.findAllUsers(pageable);
        }
        model.addAttribute("pageOfEntities", userPage);
        model.addAttribute("lockedMails", userServer.getLockedMails());
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
    }

    @PostMapping("/admin/lock")
    public ModelAndView processSetLock(ModelAndView modelAndView,
                                       @RequestParam Long userId,
                                       @RequestParam boolean setLock,
                                       @RequestParam(name = "pageNo") Optional<String> optPageNo,
                                       @RequestParam(name = "pageSize") Optional<String> optPageSize,
                                       @RequestParam(name = "sort") Optional<String> optSort) {

        userServer.setAccountLockedState(userId, setLock);
        LOGGER.debug("User(id={}) account locked state has been set {}", userId, setLock);
        modelAndView.setViewName("redirect:/admin/users");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        optPageSize.ifPresent(pageSize -> modelAndView.addObject("pageSize", pageSize));
        optSort.ifPresent(sort -> modelAndView.addObject("sort", sort));
        return modelAndView;
    }

}