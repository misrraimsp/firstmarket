package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Languages;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;
import misrraimsp.uned.pfg.firstmarket.validation.ValidIsbn;
import misrraimsp.uned.pfg.firstmarket.validation.ValidNumber;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPattern;

import javax.validation.constraints.PastOrPresent;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookForm {

    private Long bookId;

    @ValidIsbn
    private String isbn;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String title;

    private Long categoryId;

    @ValidImage
    private Image image;

    private Long storedImageId;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private List<String> authorsFirstName = new ArrayList<>();

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private List<String> authorsLastName = new ArrayList<>();

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String publisherName;

    @ValidPattern(pattern = "textLong", message = "{validation.regex.text-long}")
    private String description;

    @ValidNumber(name = "pages", message = "{validation.numeric.pages}")
    private int pages;

    private Languages language;

    @ValidPattern(pattern = "price", message = "{validation.regex.price}")
    private String price;

    @ValidNumber(name = "stock", message = "{validation.numeric.stock}")
    private int stock;

    @PastOrPresent(message = "{year.notValid}")
    private Year year;

}
