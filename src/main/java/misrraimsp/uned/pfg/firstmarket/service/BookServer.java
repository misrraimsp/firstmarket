package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Service
public class BookServer implements Constants {

    private BookRepository bookRepository;
    private ImageServer imageServer;

    @Autowired
    public BookServer(BookRepository bookRepository, ImageServer imageServer) {
        this.bookRepository = bookRepository;
        this.imageServer = imageServer;
    }

    @Transactional
    public Book persist(FormBook formBook) throws IsbnAlreadyExistsException {
        //System.out.println("persisting FormBook: " + formBook);
        // check for isbn uniqueness
        if (this.isbnExists(formBook.getIsbn().replaceAll(ISBN_FILTER, ""))){
            throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  formBook.getIsbn());
        }
        //convert (and save if necessary)
        Book book = this.convertFormBook(formBook);
        // save
        return bookRepository.save(book);
    }

    private Book convertFormBook(FormBook formBook) {
        Book book = new Book();
        // set ISBN
        book.setIsbn(this.convertFormBookIsbn(formBook.getIsbn()));
        // set title
        book.setTitle(formBook.getTitle());
        //set category
        book.setCategory(formBook.getCategory());
        // set image
        book.setImage(this.convertFormBookImage(formBook.getStoredImageId(), formBook.getImage()));
        //set authors
        book.setAuthors(this.convertFormBookAuthors(formBook.getAuthors()));
        //set publisher
        book.setPublisher(this.convertFormBookPublisher(formBook.getPublisherName()));
        //set description
        book.setDescription(formBook.getDescription());
        //set numPages
        book.setNumPages(formBook.getNumPages());
        //set language
        book.setLanguage(formBook.getLanguage());
        //set price
        book.setPrice(this.convertFormBookPrice(formBook.getPrice()));
        //set stock
        book.setStock(formBook.getStock());
        //set year
        book.setYear(convertFormBookYear(formBook.getYear()));
        return book;
    }

    private String convertFormBookIsbn(String isbn) {
        return isbn.replaceAll(ISBN_FILTER, "");
    }

    private Image convertFormBookImage(Long storedImageId, Image image) {
        return (storedImageId == null) ? imageServer.persist(image) : imageServer.findById(storedImageId);
    }
    //TODO
    private List<Author> convertFormBookAuthors(String authors) {
        return null;
    }
    //TODO
    private Publisher convertFormBookPublisher(String publisherName) {
        return null;
    }
    //TODO
    private BigDecimal convertFormBookPrice(String price) {
        return BigDecimal.ZERO;
    }
    //TODO
    private int convertFormBookYear(Year year) {
        return 0;
    }

    public Book persist(Book book) throws IsbnAlreadyExistsException {
        book.setIsbn(book.getIsbn().replaceAll(ISBN_FILTER, ""));
        if (this.isbnExists(book.getIsbn())){
            throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  book.getIsbn());
        }
        return bookRepository.save(book);
    }

    public Book edit(Book book) throws IsbnAlreadyExistsException {
        book.setIsbn(book.getIsbn().replaceAll(ISBN_FILTER, ""));
        if (!this.findById(book.getId()).getIsbn().equals(book.getIsbn())){
            if (this.isbnExists(book.getIsbn())){
                throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  book.getIsbn());
            }
        }
        return bookRepository.save(book);
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
