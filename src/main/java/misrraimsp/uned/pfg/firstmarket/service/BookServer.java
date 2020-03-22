package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.config.appParameters.Languages;
import misrraimsp.uned.pfg.firstmarket.converter.BookConverter;
import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormBook;
import misrraimsp.uned.pfg.firstmarket.model.search.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServer {

    private BookRepository bookRepository;
    private BookConverter bookConverter;
    private ImageServer imageServer;
    private PublisherServer publisherServer;
    private AuthorServer authorServer;

    @Autowired
    public BookServer(BookRepository bookRepository,
                      BookConverter bookConverter,
                      ImageServer imageServer,
                      PublisherServer publisherServer,
                      AuthorServer authorServer) {

        this.bookRepository = bookRepository;
        this.bookConverter = bookConverter;
        this.imageServer = imageServer;
        this.publisherServer = publisherServer;
        this.authorServer = authorServer;
    }

    @Transactional
    public Book persist(Book book) throws IsbnAlreadyExistsException {
        //check for isbn uniqueness
        if (this.isbnExists(book.getIsbn())){
            throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  book.getIsbn());
        }
        //persist dependencies
        book.setImage(imageServer.persist(book.getImage()));
        book.setPublisher(publisherServer.persist(book.getPublisher()));
        book.setAuthors(authorServer.persist(book.getAuthors()));
        //save book
        return bookRepository.save(book);
    }

    @Transactional
    public Book edit(Book book) throws IsbnAlreadyExistsException {
        //check for isbn uniqueness (allowing self-uniqueness)
        if (!this.findById(book.getId()).getIsbn().equals(book.getIsbn())){
            if (this.isbnExists(book.getIsbn())){
                throw new IsbnAlreadyExistsException("There is a book with that isbn: " +  book.getIsbn());
            }
        }
        //persist dependencies
        book.setImage(imageServer.persist(book.getImage()));
        book.setPublisher(publisherServer.persist(book.getPublisher()));
        book.setAuthors(authorServer.persist(book.getAuthors()));
        //save book
        return bookRepository.save(book);
    }

    private boolean isbnExists(String isbn) {
        return bookRepository.findByIsbn(isbn) != null;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
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

    public Book convertFormBookToBook(FormBook formBook) {
        return bookConverter.convertFormBookToBook(formBook);
    }

    public FormBook convertBookToFormBook(Book book) {
        return bookConverter.convertBookToFormBook(book);
    }

    public List<Author> findTopAuthorsByCategoryId(Long categoryId, int numTopAuthors) {
        return authorServer.findTopAuthorsByCategoryId(categoryId, numTopAuthors);
    }

    public List<Publisher> findTopPublishersByCategoryId(Long categoryId, int numTopPublishers) {
        return publisherServer.findTopPublishersByCategoryId(categoryId, numTopPublishers);
    }

    public List<Languages> findTopLanguagesByCategoryId(Long categoryId, int numTopLanguages) {
        List<Long> bookIds = new ArrayList<>();
        bookRepository.findByAncestorCategoryId(categoryId).forEach(book -> bookIds.add(book.getId()));
        return bookRepository.findTopLanguagesByBookIds(bookIds, numTopLanguages);
    }

    //TODO

    public Page<Book> findWithFilter(Filter filter, Pageable pageable) {
        Page<Book> books = bookRepository.findByAncestorCategoryIdInPage(filter.getCategory().getId(), pageable);
        return books;
    }
}
