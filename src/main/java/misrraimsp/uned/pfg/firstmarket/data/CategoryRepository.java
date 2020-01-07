package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

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


    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM CatPath cp WHERE " +
            "cp.descendant.id IN (SELECT cp1.descendant.id FROM CatPath cp1 WHERE cp1.ancestor.id = :id) AND " +
            "cp.ancestor.id IN (SELECT cp2.ancestor.id FROM CatPath cp2 WHERE cp2.descendant.id = :id AND " +
            "cp.ancestor.id <> cp.descendant.id)")
    void deletePaths(@Param("id") Long id);


    @Query(value = "SELECT supertree.path_length+subtree.path_length+1, supertree.ancestor, subtree.descendant " +
            "FROM CatPath supertree, CatPath subtree " +
            "WHERE supertree.descendant.id = :parent_id AND subtree.ancestor.id = :id")
    List<CatPath> generatePaths(@Param("parent_id") Long parent_id, @Param("id") Long id);
}
