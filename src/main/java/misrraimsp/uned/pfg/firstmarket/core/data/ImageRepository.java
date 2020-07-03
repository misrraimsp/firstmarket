package misrraimsp.uned.pfg.firstmarket.core.data;

import misrraimsp.uned.pfg.firstmarket.core.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Image findByData(byte[] data);

    @Query(value = "SELECT id,created_by,created_date,last_modified_by,last_modified_date,null AS data,size,is_default,mime_type,name FROM image ", nativeQuery = true)
    List<Image> getAllMetaInfo();

    List<Image> findByIsDefaultIsTrue();

    @Query(value = "SELECT id,created_by,created_date,last_modified_by,last_modified_date,1 AS data,size,is_default,mime_type,name FROM image ", nativeQuery = true)
    Page<Image> getPageOfMetaInfo(Pageable pageable);
}
