package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ImageRepository;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Image persist(Image image) throws IllegalArgumentException {
        if (image.getId() != null){ // image is already persisted
            return this.findById(image.getId());
        }
        else if (image.getData() != null) { // check for data field duplicated
            Image duplicatedImage = imageRepository.findByData(image.getData());
            return (duplicatedImage != null) ? duplicatedImage : imageRepository.save(image);
        }
        else {
            throw new IllegalArgumentException("Trying to persist an image without id or data");
        }
    }

    public Image findById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id " + id));
    }

    public List<Image> getAllMetaInfo() {
        return imageRepository.getAllMetaInfo();
    }

    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

    public Image getDefaultImage() {
        return imageRepository.findByIsDefaultIsTrue();
    }

    public boolean isDefaultImage(Long imageId) throws IllegalArgumentException {
        return this.findById(imageId).isDefault();
    }

    public Page<Image> getPageOfMetaInfo(Pageable pageable) {
        return imageRepository.getPageOfMetaInfo(pageable);
    }

    @Transactional
    public void setDefaultImage(Long imageId) throws IllegalArgumentException {
        Image oldDefaultImage = this.getDefaultImage();
        Image newDefaultImage = this.findById(imageId);
        oldDefaultImage.setDefault(false);
        newDefaultImage.setDefault(true);
        imageRepository.save(oldDefaultImage);
        imageRepository.save(newDefaultImage);
    }
}