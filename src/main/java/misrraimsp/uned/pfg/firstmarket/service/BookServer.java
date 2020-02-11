package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServer {

    private BookRepository bookRepository;

    @Autowired
    public BookServer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book persist(Book book) throws IsbnAlreadyExistsException {
        book.setIsbn(this.trimIsbn(book.getIsbn()));
        if (this.isbnExists(book.getIsbn())){
            throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  book.getIsbn());
        }
        return bookRepository.save(book);
    }

    public Book edit(Book book) throws IsbnAlreadyExistsException {
        book.setIsbn(this.trimIsbn(book.getIsbn()));
        if (!this.findById(book.getId()).getIsbn().equals(book.getIsbn())){
            if (this.isbnExists(book.getIsbn())){
                throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  book.getIsbn());
            }
        }
        return bookRepository.save(book);
    }

    private String trimIsbn(String isbn) {
        String isbnNumbers = isbn.replaceAll("X", "10").replaceAll("[^\\d]", "");
        int size = isbnNumbers.length();
        if (size != 10 && size != 11 && size != 13){//cut numbers on isbn head
            isbnNumbers = isbnNumbers.substring(2);
            size -= 2;
        }
        if (size == 11){//substitute ...10 by ...X
            isbnNumbers = isbnNumbers.substring(0,9);
            isbnNumbers += "X";
        }
        return isbnNumbers;
    }

    private boolean isbnExists(String isbn) {
        return bookRepository.findByIsbn(isbn) != null;
    }

    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid book Id: " + id));
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public void updateCategoryIdByCategoryId(Long category_id, Long new_category_id) {
        bookRepository.updateCategoryIdByCategoryId(category_id, new_category_id);
    }

    public void updateImageByImageId(Long old_image_id, Image new_image) {
        bookRepository.findByImageId(old_image_id).forEach(
                book -> {
                    book.setImage(new_image);
                    bookRepository.save(book);
                });
    }
}
