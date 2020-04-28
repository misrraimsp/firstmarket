package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationNumericProperties;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator implements ConstraintValidator<ValidImage, Object> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ValidationRegexProperties validationRegexProperties;
    private ValidationNumericProperties validationNumericProperties;

    @Autowired
    public ImageValidator(ValidationRegexProperties validationRegexProperties,
                          ValidationNumericProperties validationNumericProperties){

        this.validationRegexProperties = validationRegexProperties;
        this.validationNumericProperties = validationNumericProperties;
    }

    @Override
    public void initialize(ValidImage constraintAnnotation) { }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        if (object == null) {
            return true; //needed in case of using a stored image on newBook and editBook workflow
        }
        else if (object instanceof Collection<?>) {
            return this.validateImageCollection((Collection<Image>) object);
        }
        else if (object instanceof Image){
            return this.validateImage((Image) object);
        }
        else {
            LOGGER.warn("Trying to use ImageValidator with an argument that is not of type Image, Collection<Image> or NULL");
            return false;
        }
    }

    private boolean validateImageCollection(Collection<Image> images) {
        boolean valid = true;
        for (Image image : images){
            valid = valid && this.validateImage(image);
        }
        return valid;
    }

    private boolean validateImage(Image image) {
        return (this.validateMimeType(image.getMimeType()) &&
                this.validateSize(image.getData()) &&
                this.validateName(image.getName()));
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile(validationRegexProperties.getImageName());
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean validateSize(byte[] data) {
        return data.length <= validationNumericProperties.getImageMaxSize();
    }

    private boolean validateMimeType(String mimeType) {
        Pattern pattern = Pattern.compile(validationRegexProperties.getImageMimeType());
        Matcher matcher = pattern.matcher(mimeType);
        return matcher.matches();
    }

}
