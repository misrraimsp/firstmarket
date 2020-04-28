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

@Data
public class BookForm {

    private Long bookId;

    @ValidIsbn
    private String isbn;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String title;

    private Long categoryId;

    @ValidImage
    private Image image;

    private Long storedImageId;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorFirstName0;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorLastName0;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorFirstName1;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorLastName1;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorFirstName2;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorLastName2;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorFirstName3;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorLastName3;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorFirstName4;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String authorLastName4;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String publisherName;

    @ValidPattern(pattern = "textLong", message = "{text.long}")
    private String description;

    @ValidNumber(name = "pages", message = "{validation.numeric.pages}")
    private int pages;

    private Languages language;

    @ValidPattern(pattern = "price", message = "{price.notValid}")
    private String price;

    @ValidNumber(name = "stock", message = "{validation.numeric.stock}")
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
