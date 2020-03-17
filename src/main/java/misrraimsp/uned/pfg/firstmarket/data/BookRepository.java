package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Book b SET b.category.id = :new_category_id WHERE b.category.id = :category_id")
    void updateCategoryIdByCategoryId(@Param("category_id") Long category_id, @Param("new_category_id") Long new_category_id);

    Iterable<? extends Book> findByImageId(Long id);

    Book findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE b.category.id IN (" +
            "SELECT c.id FROM Category c WHERE c.id IN (" +
            "SELECT cp.descendant.id FROM Catpath cp WHERE cp.ancestor.id = :id))")
    List<Book> findByAncestorCategory(@Param("id") Long id);
}