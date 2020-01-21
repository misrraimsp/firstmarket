package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.exception.StorageException;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

public class MultipartFileToImageConverter implements Converter<MultipartFile, Image> {

    @Override
    public Image convert(MultipartFile multipartFile) {
        String imageName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));// Normalize image name
        try {
            if(imageName.contains("..")) {// Check if the image's name contains invalid characters
                throw new StorageException("Sorry! Filename contains invalid path sequence " + imageName);
            }
            Image image = new Image();
            image.setName(imageName);
            image.setType(multipartFile.getContentType());
            image.setData(multipartFile.getBytes());
            return image;
        }
        catch (IOException e) {
            throw new StorageException("Could not store image " + imageName + ". Please try again!", e);
        }
    }
}
