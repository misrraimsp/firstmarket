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
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServer implements Constants {

    private BookRepository bookRepository;
    private CatServer catServer;
    private ImageServer imageServer;
    private PublisherServer publisherServer;
    private AuthorServer authorServer;

    @Autowired
    public BookServer(BookRepository bookRepository,
                      CatServer catServer,
                      ImageServer imageServer,
                      PublisherServer publisherServer,
                      AuthorServer authorServer) {

        this.bookRepository = bookRepository;
        this.catServer = catServer;
        this.imageServer = imageServer;
        this.publisherServer = publisherServer;
        this.authorServer = authorServer;
    }

    @Transactional
    public Book persist(FormBook formBook) throws IsbnAlreadyExistsException {
        // check for isbn uniqueness
        if (this.isbnExists(formBook.getIsbn().replaceAll(ISBN_FILTER, ""))){
            throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  formBook.getIsbn());
        }
        //convert (and persist if necessary)
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
        book.setCategory(this.convertFormBookCategoryId(formBook.getCategoryId()));
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

    private Category convertFormBookCategoryId(Long categoryId) {
        return catServer.findCategoryById(categoryId);
    }

    private String convertFormBookIsbn(String isbn) {
        return isbn.replaceAll(ISBN_FILTER, "");
    }

    private Image convertFormBookImage(Long storedImageId, Image image) {
        return (storedImageId == null) ? imageServer.persist(image) : imageServer.findById(storedImageId);
    }

    private List<Author> convertFormBookAuthors(String formBookAuthors) {
        //System.out.println("formBookAuthors: " + formBookAuthors);
        List<Author> authors = new ArrayList<>();
        if (formBookAuthors.isBlank()) {
            return authors;
        }
        for (String formBookAuthor : formBookAuthors.split(";")){
            //System.out.println("formBookAuthor: " + formBookAuthor);
            String [] authorParts = formBookAuthor.split(",");
            //System.out.println("authorParts[0]: " + authorParts[0]);
            //System.out.println("authorParts[1]: " + authorParts[1]);
            Author author = authorServer.findByFirstNameAndLastName(authorParts[0], authorParts[1]);
            if (author == null){
                author = new Author();
                author.setFirstName(authorParts[0]);
                author.setLastName(authorParts[1]);
                author = authorServer.persist(author);
            }
            if (!authors.contains(author)) authors.add(author); // avoiding books with author repeated
        }
        return authors;
    }

    private Publisher convertFormBookPublisher(String publisherName) {
        if (publisherName.isBlank()){
            return null;
        }
        Publisher publisher = publisherServer.findByName(publisherName);
        if (publisher == null) {
            publisher = new Publisher();
            publisher.setName(publisherName);
            return publisherServer.persist(publisher); // new publisher
        }
        return publisher; // publisher already exist
    }

    private BigDecimal convertFormBookPrice(String price) {
        return (price.isBlank()) ? BigDecimal.ZERO : new BigDecimal(price);
    }

    private int convertFormBookYear(Year year) {
        return (year == null) ? 0 : year.getValue();
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
