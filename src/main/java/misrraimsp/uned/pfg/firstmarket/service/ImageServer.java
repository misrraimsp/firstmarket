package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.ImageRepository;
import misrraimsp.uned.pfg.firstmarket.exception.StorageFileNotFoundException;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServer {

    private ImageRepository imageRepository;

    @Autowired
    public ImageServer(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image persistImage(Image image) {
        return imageRepository.save(image);
    }

    public Image findImage(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new StorageFileNotFoundException("File not found with id " + id));
    }

}