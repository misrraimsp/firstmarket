package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceInterval;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;
import misrraimsp.uned.pfg.firstmarket.core.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Book;
import misrraimsp.uned.pfg.firstmarket.core.model.Image;
import misrraimsp.uned.pfg.firstmarket.core.model.Item;
import misrraimsp.uned.pfg.firstmarket.core.model.projection.AuthorView;
import misrraimsp.uned.pfg.firstmarket.core.model.projection.LanguageView;
import misrraimsp.uned.pfg.firstmarket.core.model.projection.PublisherView;
import misrraimsp.uned.pfg.firstmarket.util.adt.dto.SearchCriteria;
import misrraimsp.uned.pfg.firstmarket.util.exception.BadImageException;
import misrraimsp.uned.pfg.firstmarket.util.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.util.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.util.exception.ItemsAvailabilityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class BookServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final Map<Long,Integer> cartBookRegistry = new HashMap<>();

    private final BookRepository bookRepository;
    private final ImageServer imageServer;
    private final PublisherServer publisherServer;
    private final AuthorServer authorServer;

    @Value("${fm.front-end.home.trending-books-max}")
    private int maxNumOfTrendingBooks;

    @Value("${fm.front-end.home.new-books-max}")
    private int maxNumOfNewBooks;

    @Autowired
    public BookServer(BookRepository bookRepository,
                      ImageServer imageServer,
                      PublisherServer publisherServer,
                      AuthorServer authorServer) {

        this.bookRepository = bookRepository;
        this.imageServer = imageServer;
        this.publisherServer = publisherServer;
        this.authorServer = authorServer;
    }

    @Transactional
    public Book persist(Book book) throws
            IsbnAlreadyExistsException,
            EntityNotFoundByIdException,
            BadImageException {

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
    public Book edit(Book book) throws
            IsbnAlreadyExistsException,
            EntityNotFoundByIdException,
            BadImageException {

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

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundByIdException(bookId, Book.class.getSimpleName())
        );
    }

    public List<Book> findTopTrendingBooks() {
        if (cartBookRegistry.size() < 0.5 * maxNumOfTrendingBooks) {
            LOGGER.warn("Not enough cart-books to show as trending books. Showing random books");
            return this.getRandomBooks(maxNumOfTrendingBooks);
        }
        List<Long> trendingIds = cartBookRegistry.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
        if (trendingIds.size() >= maxNumOfTrendingBooks) {
            trendingIds = trendingIds.subList(trendingIds.size() - maxNumOfTrendingBooks, trendingIds.size());
        }
        List<Book> trendingBooks = new ArrayList<>();
        trendingIds.forEach(id -> trendingBooks.add(0,this.findById(id)));
        return trendingBooks;
    }

    public Set<Book> findTopNewBooks() {
        return bookRepository.findTopNewBooks(maxNumOfNewBooks);
    }

    private List<Book> getRandomBooks(int numOfBooks) {
        return bookRepository.findRandom(numOfBooks);
    }

    @Transactional
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

    public List<AuthorView> findTopAuthorViewsByCategoryId(Long categoryId, int numTopAuthors) {
        return authorServer.findTopAuthorViewsByCategoryId(categoryId, numTopAuthors);
    }

    public List<PublisherView> findTopPublisherViewsByCategoryId(Long categoryId, int numTopPublishers) {
        return publisherServer.findTopPublisherViewsByCategoryId(categoryId, numTopPublishers);
    }

    public List<LanguageView> findTopLanguagesByCategoryId(Long categoryId, int numTopLanguages) {
        Set<Long> bookIds = bookRepository.findIdByAncestorCategoryId(categoryId);
        return (bookIds.isEmpty()) ? new ArrayList<>() : bookRepository.findTopLanguageViewsByBookIds(bookIds, numTopLanguages);
    }

    public Page<Book> findSearchResults(SearchCriteria searchCriteria, Pageable pageable) {

        Set<Long> idsByCategory = bookRepository.findIdByAncestorCategoryId(searchCriteria.getCategoryId());
        Set<Long> idsByAuthor = (searchCriteria.getAuthorId() != null) ? bookRepository.findIdByAuthorIds(searchCriteria.getAuthorId()) : null;
        Set<Long> idsByPublisher = (searchCriteria.getPublisherId() != null) ? bookRepository.findIdByPublisherIds(searchCriteria.getPublisherId()) : null;
        Set<Long> idsByLanguage = (searchCriteria.getLanguageId() != null) ? bookRepository.findIdByLanguageIds(searchCriteria.getLanguageId()) : null;
        Set<Long> idsByPrice  = (searchCriteria.getPriceId() != null) ? this.getIdsByPriceIntervals(searchCriteria.getPriceId()) : null;
        Set<Long> idsByQ  = (searchCriteria.getQ() != null) ? this.getIdsByQueryText(searchCriteria.getQ()) : null;
        Set<Long> idsByStatus  = (searchCriteria.getExcludedStatus() == null) ? null : this.getIdsByExcludedStatus(searchCriteria.getExcludedStatus());

        Set<Long> resultIds = intersect(idsByCategory, idsByPrice, idsByAuthor, idsByPublisher, idsByLanguage, idsByQ, idsByStatus);
        if (resultIds.size() == 0){
            resultIds.add(0L);
        }
        return bookRepository.findByIds(resultIds, pageable);
    }

    private Set<Long> getIdsByExcludedStatus(ProductStatus excludedStatus) {
        return bookRepository
                .findAll()
                .stream()
                .filter(book -> !book.getStatus().equals(excludedStatus))
                .map(Book::getId)
                .collect(Collectors.toSet())
                ;
    }

    private Set<Long> getIdsByQueryText(String q) {
        Set<Long> idsByQ = new HashSet<>(getIdsByQueryTextFromIsbn(q));
        String[] qs = q.split("\\s+");
        for (String query : qs){
            if (!query.isBlank()) {
                idsByQ.addAll(bookRepository.findIdByTitleLike("%" + query + "%"));
                idsByQ.addAll(bookRepository.findIdByPublisherNameLike("%" + query + "%"));
                idsByQ.addAll(bookRepository.findIdByAuthorFirstNameLike("%" + query + "%"));
                idsByQ.addAll(bookRepository.findIdByAuthorLastNameLike("%" + query + "%"));
            }
        }
        return idsByQ;
    }

    private Set<Long> getIdsByQueryTextFromIsbn(String q) {
        Set<Long> ids = new HashSet<>();
        if (q.isBlank()) return ids;
        extractIsbnsFromQ(q).forEach(isbn -> ids.addAll(bookRepository.findIdByIsbnLike("%" + isbn + "%")));
        return ids;
    }

    private Set<String> extractIsbnsFromQ(String q) {
        Set<String> isbns = new HashSet<>();
        Pattern pattern;
        Matcher matcher;

        //isbn13
        pattern = Pattern.compile("(\\d){13}");
        matcher = pattern.matcher(q);
        while (matcher.find()) {
            isbns.add(q.substring(matcher.start(), matcher.end()));
        }
        //isbn10
        pattern = Pattern.compile("(\\d){9}[xX1-9]");
        matcher = pattern.matcher(q);
        while (matcher.find()) {
            isbns.add(q.substring(matcher.start(), matcher.end()));
        }
        return isbns;
    }

    private Set<Long> getIdsByPriceIntervals(Set<PriceInterval> priceIds) {
        Set<Long> idsByPrice = new HashSet<>();
        priceIds.forEach(pi -> idsByPrice.addAll(bookRepository.findIdByPrice(pi.getLowLimit(), pi.getHighLimit())));
        return idsByPrice;
    }

    /**
     * This method intersect a variable quantity of Long number sets.
     * Also, any set can be of NULL value
     * @param sets
     * @return intersection set. If no intersection is possible returns empty set
     */
    private Set<Long> intersect(Set<Long> ... sets) {
        int i = 0;
        int size = sets.length;
        while (sets[i] == null && i < size){
            i++;
        }
        if (i == size){
            return new HashSet<>();
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

    public boolean existsBook(Long id) {
        return bookRepository.existsById(id);
    }

    public void checkAvailabilityFor(Set<Item> items) throws ItemsAvailabilityException {
        Set<Item> itemsOutOfStock = new HashSet<>();
        Set<Item> itemsDisabled = new HashSet<>();
        items.forEach(item -> {
            Book storedBook = this.findById(item.getBook().getId());
            if (storedBook.getStatus().equals(ProductStatus.DISABLED)) {
                itemsDisabled.add(item);
            }
            else {
                int originalStock = storedBook.getStock();
                int editedStock = originalStock - item.getQuantity();
                if (editedStock < 0) {
                    itemsOutOfStock.add(item);
                }
            }
        });
        if (!itemsOutOfStock.isEmpty() || !itemsDisabled.isEmpty()) {
            throw new ItemsAvailabilityException(itemsOutOfStock, itemsDisabled);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeFromStock(Set<Item> items) throws EntityNotFoundByIdException {
        items.forEach(item -> {
            Book storedBook = this.findById(item.getBook().getId());
            int originalStock = storedBook.getStock();
            int editedStock = originalStock - item.getQuantity();
            storedBook.setStock(editedStock);
            if (editedStock == 0) {
                storedBook.setStatus(ProductStatus.OUT_OF_STOCK);
            }
            bookRepository.save(storedBook);
            LOGGER.debug("Book(id={}) stock decrease from {} to {}", storedBook.getId(), originalStock, editedStock);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void restoreStock(Set<Item> items) throws EntityNotFoundByIdException {
        items.forEach(item -> {
            Book storedBook = this.findById(item.getBook().getId());
            int originalStock = storedBook.getStock();
            int editedStock = originalStock + item.getQuantity();
            storedBook.setStock(editedStock);
            if (editedStock > 0) {
                storedBook.setStatus(ProductStatus.OK);
            }
            bookRepository.save(storedBook);
            LOGGER.debug("Book(id={}) stock increase from {} to {}", storedBook.getId(), originalStock, editedStock);
        });
    }

    public void setStatus(Long bookId, ProductStatus productStatus) throws EntityNotFoundByIdException {
        Book book = this.findById(bookId);
        if (productStatus.equals(ProductStatus.OUT_OF_STOCK)) {
            book.setStock(0);
        }
        if (productStatus.equals(ProductStatus.OK) && book.getStock() == 0) {
            book.setStock(10);
        }
        book.setStatus(productStatus);
        bookRepository.save(book);
        LOGGER.debug("Book(id={}) status set as {}", bookId, productStatus);
    }

    public void incrementCartBookRegistry(Long cartBookId) {
        cartBookRegistry.merge(cartBookId, 1, (oldValue, defaultValue) -> ++oldValue);
        LOGGER.debug("CartBook(id={}) incremented on CartBookRegistry", cartBookId);
        LOGGER.trace("CartBookRegistry: {}", this.getCartBookRegistry());
    }

    public void decrementCartBookRegistry(Long cartBookId) {
        cartBookRegistry.computeIfPresent(cartBookId, (key, value) -> (value > 1L) ? --value : null);
        LOGGER.debug("CartBook(id={}) decremented on CartBookRegistry", cartBookId);
        LOGGER.trace("CartBookRegistry: {}", this.getCartBookRegistry());
    }

    public void incrementCartBookRegistry(List<Long> cartBookIds) {
        cartBookIds.forEach(this::incrementCartBookRegistry);
    }

    public Map<Long,Integer> getCartBookRegistry() {
        return cartBookRegistry;
    }
}
