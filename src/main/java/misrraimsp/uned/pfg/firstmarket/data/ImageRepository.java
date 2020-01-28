package misrraimsp.uned.pfg.firstmarket.data;

import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Image findByData(byte[] data);

    @Query(value = "SELECT id,null AS data,is_default,mime_type,name FROM image ", nativeQuery = true)
    List<Image> getAllMetaInfo();

    Image findByIsDefaultIsTrue();

    @Query(value = "SELECT id FROM image WHERE is_default = true", nativeQuery = true)
    Long findIdByIsDefaultIsTrue();
}
