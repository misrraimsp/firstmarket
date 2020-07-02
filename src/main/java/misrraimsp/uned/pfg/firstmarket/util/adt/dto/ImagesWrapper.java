package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.core.model.Image;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import java.util.HashSet;
import java.util.Set;

@Data
public class ImagesWrapper {

    @ValidImage(message = "{images.error}")
    private Set<Image> images = new HashSet<>();

}
