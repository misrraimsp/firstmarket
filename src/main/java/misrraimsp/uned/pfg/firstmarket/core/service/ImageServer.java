package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.ImageRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Image;
import misrraimsp.uned.pfg.firstmarket.util.exception.BadImageException;
import misrraimsp.uned.pfg.firstmarket.util.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.util.exception.NoDefaultImageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ImageServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ImageRepository imageRepository;

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
    public Image persist(Image image) throws EntityNotFoundByIdException, BadImageException {
        if (image.getId() != null){ // image is already persisted
            try {
                return this.findById(image.getId());
            }
            catch (EntityNotFoundByIdException e) {
                if (e.getClassName().equals(Image.class.getSimpleName())) {
                    LOGGER.error("Trying to persist an image-with-id that is not in the database searching by its id");
                }
                throw e;
            }
        }
        else if (image.getData() != null) { // check for data field duplicated
            Image duplicatedImage = imageRepository.findByData(image.getData());
            return (duplicatedImage != null) ? duplicatedImage : imageRepository.save(image);
        }
        else {
            throw new BadImageException("Trying to persist an image without data or id");
        }
    }

    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() ->
                new EntityNotFoundByIdException(imageId,Image.class.getSimpleName()));
    }

    public List<Image> getAllMetaInfo() {
        return imageRepository.getAllMetaInfo();
    }

    public void deleteById(Long id) {
        imageRepository.deleteById(id);
    }

    public Image getDefaultImage() throws NoDefaultImageException {
        List<Image> images = imageRepository.findByIsDefaultIsTrue();
        if (images.isEmpty()){
            throw new NoDefaultImageException();
        }
        return images.get(0);
    }

    public boolean isDefaultImage(Long imageId) throws EntityNotFoundByIdException {
        return this.findById(imageId).isDefault();
    }

    public Page<Image> getPageOfMetaInfo(Pageable pageable) {
        return imageRepository.getPageOfMetaInfo(pageable);
    }

    @Transactional
    public void setDefaultImage(Long imageId) throws EntityNotFoundByIdException {
        Image newDefaultImage = this.findById(imageId);
        Image oldDefaultImage = null;
        try {
            oldDefaultImage = this.getDefaultImage();
        }
        finally {
            if (oldDefaultImage != null) {
                oldDefaultImage.setDefault(false);
                imageRepository.save(oldDefaultImage);
            }
            newDefaultImage.setDefault(true);
            imageRepository.save(newDefaultImage);
        }
    }

    public void setImageData(Long imageId, Path path) throws EntityNotFoundByIdException, IOException {
        Image image = this.findById(imageId);
        if (image.getData() == null) {
            image.setData(Files.readAllBytes(path));
            imageRepository.save(image);
        }
    }
}