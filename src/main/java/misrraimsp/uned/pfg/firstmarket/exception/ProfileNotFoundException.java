package misrraimsp.uned.pfg.firstmarket.exception;

public class ProfileNotFoundException extends IdNotFoundException {

    public ProfileNotFoundException(Long profileId) {
        super("There is no Profile with id=" + profileId);
    }
}
