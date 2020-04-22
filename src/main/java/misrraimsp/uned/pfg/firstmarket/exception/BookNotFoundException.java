package misrraimsp.uned.pfg.firstmarket.exception;

public class BookNotFoundException extends IdNotFoundException {

    public BookNotFoundException(Long bookId) {
        super("There is no Book with id=" + bookId);
    }
}
