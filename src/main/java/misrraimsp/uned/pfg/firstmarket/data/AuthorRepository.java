package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface AuthorRepository extends CrudRepository<Author,Long> {


    Author findByFirstNameAndLastName(String firstName, String lastName);


    @Query(
            nativeQuery = true,
            value = "SELECT aux.author_id FROM (" +
                        "SELECT author_id, COUNT(*) FROM books_authors WHERE book_id IN (" +
                            "SELECT id FROM book WHERE category_id IN (" +
                                "SELECT descendant_id FROM catpath WHERE ancestor_id = :categoryId" +
                            ")" +
                        ") GROUP BY author_id ORDER BY 2 DESC LIMIT :numTopAuthors" +
                    ") AS aux"
    )
    Set<Long> findTopIdsByCategoryId(@Param("categoryId") Long categoryId, @Param("numTopAuthors") int numTopAuthors);


    @Query("SELECT au FROM Author au WHERE au.id IN :authorIds")
    Set<Author> findAuthorsByIds(@Param("authorIds") Set<Long> authorIds);
}
