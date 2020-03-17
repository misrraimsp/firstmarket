package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author,Long> {


    Author findByFirstNameAndLastName(String firstName, String lastName);


    @Query(
            nativeQuery = true,
            value = "SELECT aux2.author_id FROM (" +
                        "SELECT aux1.author_id, COUNT(*) FROM (" +
                            "SELECT * FROM books_authors ba WHERE ba.book_id IN :bookIds" +
                        ") AS aux1 GROUP BY author_id ORDER BY 2 DESC LIMIT :numTopAuthors" +
                    ") AS aux2"
    )
    List<Long> findTopIdsByBookIds(@Param("bookIds") List<Long> bookIds, @Param("numTopAuthors") int numTopAuthors);


    @Query("SELECT au FROM Author au WHERE au.id IN :authorIds")
    List<Author> findAuthorsByIds(@Param("authorIds") List<Long> authorIds);
}
