package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Languages;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.*;

import javax.validation.constraints.PastOrPresent;
import java.time.Year;

@Data
public class BookForm {

    private Long bookId;

    @ValidIsbn(message = "{isbn.error}")
    private String isbn;

    @ValidTextBasic(message = "{text.basic}")
    private String title;

    private Long categoryId;

    @ValidImage(message = "{image.error}")
    private Image image;

    private Long storedImageId;

    @ValidTextBasic(message = "{text.basic}")
    private String authorFirstName0;

    @ValidTextBasic(message = "{text.basic}")
    private String authorLastName0;

    @ValidTextBasic(message = "{text.basic}")
    private String authorFirstName1;

    @ValidTextBasic(message = "{text.basic}")
    private String authorLastName1;

    @ValidTextBasic(message = "{text.basic}")
    private String authorFirstName2;

    @ValidTextBasic(message = "{text.basic}")
    private String authorLastName2;

    @ValidTextBasic(message = "{text.basic}")
    private String authorFirstName3;

    @ValidTextBasic(message = "{text.basic}")
    private String authorLastName3;

    @ValidTextBasic(message = "{text.basic}")
    private String authorFirstName4;

    @ValidTextBasic(message = "{text.basic}")
    private String authorLastName4;

    @ValidTextBasic(message = "{text.basic}")
    private String publisherName;

    @ValidTextLong(message = "{text.long}")
    private String description;

    @ValidNumPages(message = "{number.range}")
    private int numPages;

    private Languages language;

    @ValidPrice(message = "{price.notValid}")
    private String price;

    @ValidStock(message = "{number.range}")
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
