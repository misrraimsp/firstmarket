package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.core.model.Book;
import misrraimsp.uned.pfg.firstmarket.core.model.projection.LanguageView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Book b SET b.category.id = :new_category_id WHERE b.category.id = :category_id")
    void updateCategoryIdByCategoryId(@Param("category_id") Long category_id, @Param("new_category_id") Long new_category_id);

    Iterable<? extends Book> findByImageId(Long id);

    Book findByIsbn(String isbn);

    @Query(
            nativeQuery = true,
            value = "SELECT language, COUNT(*) AS count FROM book b WHERE b.id IN :bookIds GROUP BY language ORDER BY 2 DESC LIMIT :numTopLanguages"
    )
    List<LanguageView> findTopLanguageViewsByBookIds(@Param("bookIds") Set<Long> bookIds, @Param("numTopLanguages") int numTopLanguages);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM book LIMIT :numOfBooks"
    )
    List<Book> findRandom(int numOfBooks);

    @Override
    Set<Book> findAll();

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM book ORDER BY created_date DESC LIMIT :maxNumOfNewBooks"
    )
    Set<Book> findTopNewBooks(int maxNumOfNewBooks);

    @Query("SELECT b FROM Book b WHERE b.id IN :ids")
    Page<Book> findByIds(@Param("ids") Set<Long> ids, Pageable pageable);

    @Query("SELECT b.id FROM Book b WHERE b.category.id IN (SELECT cp.descendant.id FROM Catpath cp WHERE cp.ancestor.id = :id)")
    Set<Long> findIdByAncestorCategoryId(@Param("id") Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT book_id FROM books_authors WHERE author_id IN :authorIds"
    )
    Set<Long> findIdByAuthorIds(@Param("authorIds") Set<Long> authorIds);

    @Query("SELECT b.id FROM Book b WHERE b.publisher.id IN :publisherIds")
    Set<Long> findIdByPublisherIds(@Param("publisherIds") Set<Long> publisherIds);

    @Query("SELECT b.id FROM Book b WHERE b.language IN :languageIds")
    Set<Long> findIdByLanguageIds(@Param("languageIds") Set<Language> languageIds);

    @Query("SELECT b.id FROM Book b WHERE b.price >= :lowLimit AND b.price < :highLimit")
    Set<Long> findIdByPrice(@Param("lowLimit") BigDecimal lowLimit, @Param("highLimit") BigDecimal highLimit);

    @Query("SELECT b.id FROM Book b WHERE b.title LIKE :q")
    Set<Long> findIdByTitleLike(@Param("q") String q);

    @Query("SELECT b.id FROM Book b WHERE b.isbn LIKE :q")
    Set<Long> findIdByIsbnLike(@Param("q") String s);

    @Query("SELECT b.id FROM Book b WHERE b.publisher.id IN (SELECT p.id FROM Publisher p WHERE p.name LIKE :q)")
    Set<Long> findIdByPublisherNameLike(@Param("q") String s);

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT book_id FROM books_authors WHERE author_id IN (" +
                        "SELECT id FROM author WHERE first_name LIKE :q" +
                    ")"
    )
    Set<Long> findIdByAuthorFirstNameLike(@Param("q") String s);

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT book_id FROM books_authors WHERE author_id IN (" +
                        "SELECT id FROM author WHERE last_name LIKE :q" +
                    ")"
    )
    Set<Long> findIdByAuthorLastNameLike(@Param("q") String s);
}