package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.projection.AuthorView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author,Long> {

    Author findByFirstNameAndLastName(String firstName, String lastName);

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) AS numOfBooks, author_id AS id FROM books_authors WHERE book_id IN (" +
                        "SELECT id FROM book WHERE category_id IN (" +
                            "SELECT descendant_id FROM catpath WHERE ancestor_id = :categoryId" +
                        ")" +
                    ") GROUP BY author_id ORDER BY 1 DESC LIMIT :numTopAuthors"
    )
    List<AuthorView> findTopAuthorViewsByCategoryId(@Param("categoryId") Long categoryId, @Param("numTopAuthors") int numTopAuthors);

}
