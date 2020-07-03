package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    //self-parenthood root category property
    @Query(value = "SELECT * FROM category WHERE id = parent_id", nativeQuery = true)
    List<Category> getRootCategory();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c SET c.name = :name WHERE c.id = :id")
    void updateName(@Param("id") Long id, @Param("name") String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c SET c.parent.id = :new_parent_id WHERE c.id = :id")
    void updateParentById(@Param("id") Long id, @Param("new_parent_id") Long new_parent_id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c SET c.parent.id = :new_parent_id WHERE c.parent.id = :parent_id")
    void updateParentByParentId(@Param("parent_id") Long parent_id, @Param("new_parent_id") Long new_parent_id);

}