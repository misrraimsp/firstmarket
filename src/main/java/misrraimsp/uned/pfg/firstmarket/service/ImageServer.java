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
    private BookServer bookServer;
    private Long defaultImageId;

    @Autowired
    public ImageServer(ImageRepository imageRepository, BookServer bookServer) {
        this.imageRepository = imageRepository;
        this.bookServer = bookServer;
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
        Image repositoryImage = imageRepository.findByData(image.getData());
        if (repositoryImage == null){
            return imageRepository.save(image);
        }
        return repositoryImage;
    }

    public Image findById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new StorageFileNotFoundException("File not found with id " + id));
    }

    public List<Image> getAllMetaInfo() {
        return imageRepository.getAllMetaInfo();
    }

    public void deleteById(Long id) {
        bookServer.updateImageByImageId(id, imageRepository.findByIsDefaultIsTrue());
        imageRepository.deleteById(id);
    }

    public void loadDefaultImageId(){
        defaultImageId = imageRepository.findIdByIsDefaultIsTrue();
    }

    public Long getDefaultImageId() {
        return defaultImageId;
    }
}