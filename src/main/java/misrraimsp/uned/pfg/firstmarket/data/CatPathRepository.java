package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CatPathRepository extends CrudRepository<CatPath, Long> {

    @Query(value = "SELECT * FROM cat_path WHERE path_length=1", nativeQuery = true)
    List<CatPath> getDirectPaths();

    List<CatPath> getCatPathsByDescendant(Category descendant);

    List<CatPath> getCatPathsByAncestor(Category ancestor);

    /**
     * Elimina los caminos que parten desde cada ancestro de la categoría a editar (INcluída ella misma)
     * hasta cada uno de sus descendientes (INcluída ella misma), salvo su propio autocamino
     *
     * @param id

     @Modifying(clearAutomatically = true)
     @Query(value = "DELETE FROM CatPath cp WHERE " +
     "cp.ancestor.id IN (SELECT cp1.ancestor.id FROM CatPath cp1 WHERE cp1.descendant.id = :id) AND " +
     "cp.descendant.id IN (SELECT cp2.descendant.id FROM CatPath cp2 WHERE cp2.ancestor.id = :id) AND " +
     "cp.ancestor.id <> cp.descendant.id")
     void deletePaths(@Param("id") Long id);
     */

    /**
     * Elimina los caminos que parten desde cada ancestro de la categoría a editar (EXcluída ella misma)
     * hasta cada uno de sus descendientes (INcluída ella misma)
     *
     * @param id
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM CatPath cp WHERE " +
            "cp.ancestor.id IN (SELECT cp1.ancestor.id FROM CatPath cp1 WHERE (cp1.descendant.id = :id AND cp1.ancestor.id <> :id)) AND " +
            "cp.descendant.id IN (SELECT cp2.descendant.id FROM CatPath cp2 WHERE cp2.ancestor.id = :id)")
    void deleteCatPathsFromAncestorsToDescendantsOf(@Param("id") Long id);

    /**
     * Elimina los caminos que tienen como ancestro ó descendiente la categoría a eliminar
     *
     * @param id
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM CatPath cp WHERE " +
            "cp.ancestor.id = :id OR cp.descendant.id = :id")
    void deleteCatPathsOf(@Param("id") Long id);

    /**
     * Reduce una unidad los caminos que parten desde cada ancestro de la categoría a eliminar (EXcluída ella misma)
     * hasta cada uno de sus descendientes (INcluída ella misma)
     *
     * @param id
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE CatPath cp SET cp.path_length = cp.path_length - 1 WHERE " +
            "cp.ancestor.id IN (SELECT cp1.ancestor.id FROM CatPath cp1 WHERE (cp1.descendant.id = :id AND cp1.ancestor.id <> :id)) AND " +
            "cp.descendant.id IN (SELECT cp2.descendant.id FROM CatPath cp2 WHERE cp2.ancestor.id = :id)")
    void reduceCatPathsFromAncestorsToDescendantsOf(@Param("id") Long id);

}