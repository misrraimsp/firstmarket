package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationInfo {

    @NotBlank(message = "username cannot be empty")
    private String username;

    @NotBlank(message = "password cannot be empty")
    private String password;
    // TODO private String confirmPassword;

    @NotBlank(message = "fullName cannot be empty")
    private String fullName;

    @NotBlank(message = "street cannot be empty")
    private String street;

    @NotBlank(message = "city cannot be empty")
    private String city;

    @NotBlank(message = "zip cannot be empty")
    private String zip;

    @NotBlank(message = "phoneNumber cannot be empty")
    private String phoneNumber;

    public User toUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setStreet(street);
        user.setCity(city);
        user.setZip(zip);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}