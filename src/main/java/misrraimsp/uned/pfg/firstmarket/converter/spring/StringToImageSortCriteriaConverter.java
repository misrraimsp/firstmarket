package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.ImageSortCriteria;
import misrraimsp.uned.pfg.firstmarket.exception.NoSortCriteriaException;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToImageSortCriteriaConverter implements Converter<String, ImageSortCriteria> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public ImageSortCriteria convert(String s) {
        if (ImageSortCriteria.values().length == 0) throw new NoSortCriteriaException(Image.class.getSimpleName());
        try {
            int index = Integer.parseInt(s);
            int maxIndex = ImageSortCriteria.values().length - 1;
            if (index > maxIndex) {
                LOGGER.warn("ImageSortCriteria index({}) out-of-bound({}). Defaults to {}", index, maxIndex, ImageSortCriteria.DEFAULT);
                return ImageSortCriteria.DEFAULT;
            }
            return ImageSortCriteria.values()[index];
        }
        catch (NumberFormatException e) {
            LOGGER.error("ImageSortCriteria index string({}) can not be converted due to format exception. Defaults to {}", s, ImageSortCriteria.DEFAULT, e);
            return ImageSortCriteria.DEFAULT;
        }
    }
}
