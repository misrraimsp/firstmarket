package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Query(value = "SELECT id,name,parent_id FROM (" +
                        "SELECT c.id,name,parent_id,COUNT(c.id) AS count " +
                        "FROM category AS c, cat_path AS cp " +
                        "WHERE c.id=cp.descendant_id " +
                        "GROUP BY c.id" +
                    ") AS aux " +
                    "WHERE aux.count=1",
            nativeQuery = true)
    Category getRootCategory();

    //@Modifying(flushAutomatically = true, clearAutomatically = true)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c SET c.name = :name WHERE c.id = :id")
    void updateName(@Param("id") Long id, @Param("name") String name);
}
