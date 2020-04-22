package misrraimsp.uned.pfg.firstmarket.exception;

public class UserNotFoundException extends IdNotFoundException {

    public UserNotFoundException(Long userId) {
        super("There is no User with id=" + userId);
    }
}
