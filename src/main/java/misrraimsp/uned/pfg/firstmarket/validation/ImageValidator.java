package misrraimsp.uned.pfg.firstmarket.validation;

import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.model.Image;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator implements ConstraintValidator<ValidImage, Object>, Patterns {

    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(ValidImage constraintAnnotation) { }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        Image image = (Image) object;
        return (this.validateMimeType(image.getMimeType()) &&
                this.validateSize(image.getData()) &&
                this.validateName(image.getName()));
    }

    private boolean validateName(String name) {
        pattern = Pattern.compile(IMAGE_NAME);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

    private boolean validateSize(byte[] data) {
        return data.length < 128000;
    }

    private boolean validateMimeType(String mimeType) {
        pattern = Pattern.compile(IMAGE_MIME_TYPE);
        matcher = pattern.matcher(mimeType);
        return matcher.matches();
    }

}
