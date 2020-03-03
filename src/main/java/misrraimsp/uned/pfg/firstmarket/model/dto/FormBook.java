package misrraimsp.uned.pfg.firstmarket.model.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.config.Languages;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.validation.Isbn;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.Year;

@Data
public class FormBook implements Constants {

    private Long bookId;

    @Isbn(message = "{isbn.error}")
    private String isbn;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String title;

    private Long categoryId;

    @ValidImage(message = "{image.error}")
    private Image image;

    private Long storedImageId;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorFirstName0;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String authorLastName0;

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


    public String getAuthors(){
        String authors = "";
        authors += format(authorFirstName0, authorLastName0);
        authors += format(authorFirstName1, authorLastName1);
        authors += format(authorFirstName2, authorLastName2);
        authors += format(authorFirstName3, authorLastName3);
        authors += format(authorFirstName4, authorLastName4);
        return authors;
    }

    private String format(String firstName, String lastName) {
        return (firstName.isBlank() && lastName.isBlank()) ? "" : (firstName + "," + lastName + ";");
    }
}