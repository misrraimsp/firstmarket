package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ImageRepository;
import misrraimsp.uned.pfg.firstmarket.exception.BadImageException;
import misrraimsp.uned.pfg.firstmarket.exception.ImageNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.NoDefaultImageException;
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
    public Image persist(Image image) throws ImageNotFoundException, BadImageException {
        if (image.getId() != null){ // image is already persisted
            return this.findById(image.getId());
        }
        else if (image.getData() != null) { // check for data field duplicated
            Image duplicatedImage = imageRepository.findByData(image.getData());
            return (duplicatedImage != null) ? duplicatedImage : imageRepository.save(image);
        }
        else {
            throw new BadImageException();
        }
    }

    public Image findById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(id));
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

    public boolean isDefaultImage(Long imageId) throws ImageNotFoundException {
        return this.findById(imageId).isDefault();
    }

    public Page<Image> getPageOfMetaInfo(Pageable pageable) {
        return imageRepository.getPageOfMetaInfo(pageable);
    }

    @Transactional
    public void setDefaultImage(Long imageId) throws ImageNotFoundException {
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

    //dev-postgresql
    public void setDefaultData(byte[] bytes) throws NoDefaultImageException {
        Image defaultImage = this.getDefaultImage();
        if (defaultImage.getData().length == 0) {
            defaultImage.setData(bytes);
            imageRepository.save(defaultImage);
        }
    }
}