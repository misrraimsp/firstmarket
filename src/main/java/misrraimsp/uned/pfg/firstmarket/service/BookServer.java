package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.config.appParameters.Languages;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.PriceIntervals;
import misrraimsp.uned.pfg.firstmarket.converter.BookConverter;
import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

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

    public Set<Author> findTopAuthorsByCategoryId(Long categoryId, int numTopAuthors) {
        return authorServer.findTopAuthorsByCategoryId(categoryId, numTopAuthors);
    }

    public Set<Publisher> findTopPublishersByCategoryId(Long categoryId, int numTopPublishers) {
        return publisherServer.findTopPublishersByCategoryId(categoryId, numTopPublishers);
    }

    public Set<Languages> findTopLanguagesByCategoryId(Long categoryId, int numTopLanguages) {
        return bookRepository.findTopLanguagesByBookIds(bookRepository.findIdByAncestorCategoryId(categoryId), numTopLanguages);
    }

    public Page<Book> findSearchResults(Long categoryId, Set<String> priceIds, Set<Long> authorIds, Set<Long> publisherIds, Set<Languages> languageIds, String q, Pageable pageable) {
        Set<Long> idsByCategory = bookRepository.findIdByAncestorCategoryId(categoryId);
        Set<Long> idsByAuthor = (authorIds != null) ? bookRepository.findIdByAuthorIds(authorIds) : null;
        Set<Long> idsByPublisher = (publisherIds != null) ? bookRepository.findIdByPublisherIds(publisherIds) : null;
        Set<Long> idsByLanguage = (languageIds != null) ? bookRepository.findIdByLanguageIds(languageIds) : null;
        Set<Long> idsByPrice  = (priceIds != null) ? this.getIdsByPriceIntervals(priceIds) : null;
        Set<Long> idsByQ  = (q != null) ? this.getIdsByQueryText(q) : null;

        Set<Long> resultIds = intersect(idsByCategory, idsByPrice, idsByAuthor, idsByPublisher, idsByLanguage, idsByQ);
        if (resultIds.size() == 0){
            resultIds.add(0L);
        }
        return bookRepository.findByIds(resultIds, pageable);
    }

    private Set<Long> getIdsByQueryText(String q) {
        Set<Long> idsByQ = new HashSet<>();
        String[] qs = q.split("\\s+");
        for (String query : qs){
            idsByQ.addAll(bookRepository.findIdByTitleLike("%" + query + "%"));
            idsByQ.addAll(bookRepository.findIdByIsbnLike("%" + query + "%"));
            idsByQ.addAll(bookRepository.findIdByPublisherNameLike("%" + query + "%"));
            idsByQ.addAll(bookRepository.findIdByAuthorFirstNameLike("%" + query + "%"));
            idsByQ.addAll(bookRepository.findIdByAuthorLastNameLike("%" + query + "%"));
        }
        return idsByQ;
    }

    private Set<Long> getIdsByPriceIntervals(Set<String> priceIds) {
        Set<Long> idsByPrice = new HashSet<>();
        for (String priceIntervalIndex : priceIds){
            PriceIntervals pi = PriceIntervals.values()[Integer.parseInt(priceIntervalIndex)];
            idsByPrice.addAll(bookRepository.findIdByPrice(pi.getLowLimit(), pi.getHighLimit()));
        }
        return idsByPrice;
    }

    /**
     * This method intersect a variable quantity of Long number sets.
     * Also, any set can be of NULL value
     * @param sets
     * @return intersection set. If no intersection is possible returns NULL
     */
    private Set<Long> intersect(Set<Long> ... sets) {
        int i = 0;
        int size = sets.length;
        while (sets[i] == null && i < size){
            i++;
        }
        if (i == size){
            return null;
        }
        Set<Long> result = new HashSet<>(sets[i]);
        for (int j = i + 1; j < size; j++) {
            Set<Long> s = sets[j];
            if (s != null) {
                result.retainAll(s);
            }
        }
        return result;
    }

}
