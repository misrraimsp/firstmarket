package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PublisherRepository extends CrudRepository<Publisher,Long> {


    Publisher findByName(String publisherName);


    @Query(
            nativeQuery = true,
            value = "SELECT aux.publisher_id FROM (" +
                        "SELECT publisher_id, COUNT(*) FROM book WHERE id IN (" +
                            "SELECT id FROM book WHERE category_id IN (" +
                                "SELECT descendant_id FROM catpath WHERE ancestor_id = :categoryId" +
                            ")" +
                        ") GROUP BY publisher_id ORDER BY 2 DESC LIMIT :numTopPublishers" +
                    ") AS aux"
    )
    Set<Long> findTopIdsByCategoryId(@Param("categoryId") Long categoryId, @Param("numTopPublishers") int numTopPublishers);



    @Query("SELECT p FROM Publisher p WHERE p.id IN :publisherIds")
    Set<Publisher> findPublishersByIds(@Param("publisherIds") Set<Long> publisherIds);

}
