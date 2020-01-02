package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CatPathRepository extends CrudRepository<CatPath, Long> {


    @Query(
            value = "SELECT * FROM cat_path WHERE path_length=1",
            nativeQuery = true)
    Set<CatPath> getDirectPaths();
}
