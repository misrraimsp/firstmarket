package misrraimsp.uned.pfg.firstmarket.exception;

public class ImageNotFoundException extends IdNotFoundException {

    public ImageNotFoundException(Long imageId) {
        super("There is no Image with id=" + imageId);
    }
}
