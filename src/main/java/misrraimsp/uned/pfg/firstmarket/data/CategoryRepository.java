package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    @Query(
            value = "SELECT id,name,parent_id FROM (SELECT c.id,name,parent_id,COUNT(c.id) AS count FROM category AS c, cat_path AS cp WHERE c.id=cp.descendant_id GROUP BY c.id) AS aux WHERE aux.count=1",
            nativeQuery = true)
    Category getRootCategory();
}
