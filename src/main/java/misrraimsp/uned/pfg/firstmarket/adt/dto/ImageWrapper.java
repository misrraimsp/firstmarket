package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

@Data
public class ImageWrapper {

    @ValidImage(message = "{image.error}")
    private Image image;
}
