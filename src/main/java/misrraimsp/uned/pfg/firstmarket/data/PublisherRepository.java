package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublisherRepository extends CrudRepository<Publisher,Long> {


    Publisher findByName(String publisherName);


    @Query(
            nativeQuery = true,
            value = "SELECT aux.publisher_id FROM (" +
                    "SELECT publisher_id, COUNT(*) FROM book b WHERE b.id IN :bookIds GROUP BY publisher_id ORDER BY 2 DESC LIMIT :numTopPublishers" +
                    ") AS aux"
    )
    List<Long> findTopIdsByBookIds(@Param("bookIds") List<Long> bookIds, @Param("numTopPublishers") int numTopPublishers);



    @Query("SELECT p FROM Publisher p WHERE p.id IN :publisherIds")
    List<Publisher> findPublishersByIds(@Param("publisherIds") List<Long> publisherIds);

}
