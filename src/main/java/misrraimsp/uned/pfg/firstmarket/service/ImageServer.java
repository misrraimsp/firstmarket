package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ImageRepository;
import misrraimsp.uned.pfg.firstmarket.exception.StorageFileNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServer {

    private ImageRepository imageRepository;


    @Autowired
    public ImageServer(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Este método está pensado para asegurar que en la base de datos
     * no exista dos imágenes iguales, en el sentido de la igualdad
     * de su byte[]
     *
     * @param image
     * @return image or repositoryImage
     */
    public Image persist(Image image) {
        if (image.getId() != null){
            return this.findById(image.getId());
        }
        else if (image.getData() != null) {
            Image storedImage = imageRepository.findByData(image.getData());
            return (storedImage != null) ? storedImage : imageRepository.save(image);
        }
        else {
            return null;
        }
    }

    public Image findById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new StorageFileNotFoundException("File not found with id " + id));
    }

    public List<Image> getAllMetaInfo() {
        return imageRepository.getAllMetaInfo();
    }

    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

    public Image findByIsDefaultIsTrue() {
        return imageRepository.findByIsDefaultIsTrue();
    }

    /*
    public Long getDefaultImageId() {
        return imageRepository.findIdByIsDefaultIsTrue();
    }
     */
}