package misrraimsp.uned.pfg.firstmarket.util.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;
import misrraimsp.uned.pfg.firstmarket.core.model.Image;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidImage;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidIsbn;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidNumber;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidPattern;

import javax.validation.constraints.PastOrPresent;
import java.time.Year;
import java.util.Collections;
import java.util.List;

@Data
public class BookForm {

    public BookForm() {
        authorsFirstName = Collections.singletonList("");
        authorsLastName = Collections.singletonList("");
        pages = 1;
        year = Year.now();
        language = Language.SPANISH;
        status = ProductStatus.OUT_OF_STOCK;
    }

    private Long bookId;

    private ProductStatus status;

    @ValidIsbn
    private String isbn;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String title;

    private Long categoryId;

    @ValidImage
    private Image image;

    private Long storedImageId;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private List<String> authorsFirstName;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private List<String> authorsLastName;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String publisherName;

    @ValidPattern(pattern = "textLong", message = "{validation.regex.text-long}")
    private String description;

    @ValidNumber(name = "pages", message = "{validation.numeric.pages}")
    private int pages;

    private Language language;

    @ValidPattern(pattern = "price", message = "{validation.regex.price}")
    private String price;

    @ValidNumber(name = "stock", message = "{validation.numeric.stock}")
    private int stock;

    @PastOrPresent(message = "{year.notValid}")
    private Year year;

}
