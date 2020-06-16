package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import misrraimsp.uned.pfg.firstmarket.model.projection.PublisherView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublisherRepository extends CrudRepository<Publisher,Long> {

    Publisher findByName(String publisherName);

    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) AS numOfBooks, publisher_id AS id FROM book WHERE id IN (" +
                        "SELECT id FROM book WHERE category_id IN (" +
                            "SELECT descendant_id FROM catpath WHERE ancestor_id = :categoryId" +
                        ")" +
                    ") GROUP BY publisher_id ORDER BY 1 DESC LIMIT :numTopPublishers"
    )
    List<PublisherView> findTopPublisherViewsByCategoryId(@Param("categoryId") Long categoryId, @Param("numTopPublishers") int numTopPublishers);

}
