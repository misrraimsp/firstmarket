package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.config.Languages;
import misrraimsp.uned.pfg.firstmarket.validation.Isbn;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.Year;

@Data
public class FormBook implements Constants {

    @Isbn(message = "{isbn.error}")
    private String isbn;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String title;

    private Category category;

    @ValidImage(message = "{image.error}")
    private Image image;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorFirstName1;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorLastName1;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorFirstName2;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorLastName2;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorFirstName3;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorLastName3;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorFirstName4;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorLastName4;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorFirstName5;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorLastName5;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String publisherName;

    @Pattern(regexp = TEXT_LONG, message = "{text.long}")
    private String description;

    @Min(value = MIN_NUM_PAGES, message = "{number.range}")
    @Max(value = MAX_NUM_PAGES, message = "{number.range}")
    private int numPages;

    private Languages language;

    @Pattern(regexp = PRICE, message = "{price.notValid}")
    private String price;

    @Min(value = MIN_NUM_STOCK, message = "{number.range}")
    @Max(value = MAX_NUM_STOCK, message = "{number.range}")
    private int stock;

    @PastOrPresent(message = "{year.notValid}")
    private Year year;
}
