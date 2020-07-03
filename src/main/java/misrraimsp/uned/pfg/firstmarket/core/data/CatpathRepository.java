package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.Category;
import misrraimsp.uned.pfg.firstmarket.core.model.Catpath;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CatpathRepository extends CrudRepository<Catpath, Long> {

    @Query(value = "SELECT * FROM catpath WHERE size=1", nativeQuery = true)
    List<Catpath> getDirectPaths();

    List<Catpath> getCatpathsByDescendant(Category descendant);

    List<Catpath> getCatpathsByAncestor(Category ancestor);

    /**
     * Elimina los caminos que parten desde cada ancestro de la categoría a editar (EXcluída ella misma)
     * hasta cada uno de sus descendientes (INcluída ella misma)
     *
     * @param id
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Catpath cp WHERE " +
            "cp.ancestor.id IN (SELECT cp1.ancestor.id FROM Catpath cp1 WHERE (cp1.descendant.id = :id AND cp1.ancestor.id <> :id)) AND " +
            "cp.descendant.id IN (SELECT cp2.descendant.id FROM Catpath cp2 WHERE cp2.ancestor.id = :id)")
    void deleteCatpathsFromAncestorsToDescendantsOf(@Param("id") Long id);

    /**
     * Elimina los caminos que tienen como ancestro ó descendiente la categoría a eliminar
     *
     * @param id
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM Catpath cp WHERE " +
            "cp.ancestor.id = :id OR cp.descendant.id = :id")
    void deleteCatpathsOf(@Param("id") Long id);

    /**
     * Reduce una unidad los caminos que parten desde cada ancestro de la categoría a eliminar (EXcluída ella misma)
     * hasta cada uno de sus descendientes (INcluída ella misma)
     *
     * @param id
     */
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Catpath cp SET cp.size = cp.size - 1 WHERE " +
            "cp.ancestor.id IN (SELECT cp1.ancestor.id FROM Catpath cp1 WHERE (cp1.descendant.id = :id AND cp1.ancestor.id <> :id)) AND " +
            "cp.descendant.id IN (SELECT cp2.descendant.id FROM Catpath cp2 WHERE cp2.ancestor.id = :id)")
    void reduceCatpathsFromAncestorsToDescendantsOf(@Param("id") Long id);

    List<Catpath> findByDescendantIdAndSizeIsGreaterThanOrderBySizeDesc(Long descendantId, int minSize);
}