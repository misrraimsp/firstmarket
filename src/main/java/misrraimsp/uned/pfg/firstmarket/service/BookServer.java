package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.SearchCriteria;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceInterval;
import misrraimsp.uned.pfg.firstmarket.converter.BookConverter;
import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.exception.*;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Book persist(Book book) throws
            IsbnAlreadyExistsException,
            ImageNotFoundException,
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
            BookNotFoundException,
            ImageNotFoundException,
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

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
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

    public Book convertBookFormToBook(BookForm bookForm) throws BookFormAuthorsConversionException {
        return bookConverter.convertBookFormToBook(bookForm);
    }

    public BookForm convertBookToBookForm(Book book) {
        return bookConverter.convertBookToBookForm(book);
    }

    public Set<Author> findTopAuthorsByCategoryId(Long categoryId, int numTopAuthors) {
        return authorServer.findTopAuthorsByCategoryId(categoryId, numTopAuthors);
    }

    public Set<Publisher> findTopPublishersByCategoryId(Long categoryId, int numTopPublishers) {
        return publisherServer.findTopPublishersByCategoryId(categoryId, numTopPublishers);
    }

    public Set<Language> findTopLanguagesByCategoryId(Long categoryId, int numTopLanguages) {
        Set<Long> bookIds = bookRepository.findIdByAncestorCategoryId(categoryId);
        return (bookIds.isEmpty()) ? new HashSet<>() : bookRepository.findTopLanguagesByBookIds(bookIds, numTopLanguages);
    }

    public Page<Book> findSearchResults(SearchCriteria searchCriteria, Pageable pageable) {

        Set<Long> idsByCategory = bookRepository.findIdByAncestorCategoryId(searchCriteria.getCategoryId());
        Set<Long> idsByAuthor = (searchCriteria.getAuthorId() != null) ? bookRepository.findIdByAuthorIds(searchCriteria.getAuthorId()) : null;
        Set<Long> idsByPublisher = (searchCriteria.getPublisherId() != null) ? bookRepository.findIdByPublisherIds(searchCriteria.getPublisherId()) : null;
        Set<Long> idsByLanguage = (searchCriteria.getLanguageId() != null) ? bookRepository.findIdByLanguageIds(searchCriteria.getLanguageId()) : null;
        Set<Long> idsByPrice  = (searchCriteria.getPriceId() != null) ? this.getIdsByPriceIntervals(searchCriteria.getPriceId()) : null;
        Set<Long> idsByQ  = (searchCriteria.getQ() != null) ? this.getIdsByQueryText(searchCriteria.getQ()) : null;

        Set<Long> resultIds = intersect(idsByCategory, idsByPrice, idsByAuthor, idsByPublisher, idsByLanguage, idsByQ);
        assert resultIds != null;
        if (resultIds.size() == 0){
            resultIds.add(0L);
        }
        return bookRepository.findByIds(resultIds, pageable);
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

    public boolean existsBook(Long id) {
        return bookRepository.existsById(id);
    }
}
