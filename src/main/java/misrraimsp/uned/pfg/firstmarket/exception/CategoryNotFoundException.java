package misrraimsp.uned.pfg.firstmarket.exception;

public class CategoryNotFoundException extends IdNotFoundException {

    public CategoryNotFoundException(Long categoryId) {
        super("There is no Category with id=" + categoryId);
    }
}
