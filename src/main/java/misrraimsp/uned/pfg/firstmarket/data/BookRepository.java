package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Book b SET b.category.id = :new_category_id WHERE b.category.id = :category_id")
    void updateCategoryIdByCategoryId(@Param("category_id") Long category_id, @Param("new_category_id") Long new_category_id);

    Iterable<? extends Book> findByImageId(Long id);
}