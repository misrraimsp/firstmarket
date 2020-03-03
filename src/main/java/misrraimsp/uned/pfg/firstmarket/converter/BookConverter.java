package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormBook;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookConverter implements Constants {

    public Book convertFormBookToBook(FormBook formBook) {
        Book book = new Book();
        book.setIsbn(this.convertFormBookIsbn(formBook.getIsbn()));
        book.setTitle(formBook.getTitle());
        book.setCategory(this.convertFormBookCategoryId(formBook.getCategoryId()));
        book.setImage(this.convertFormBookImage(formBook.getStoredImageId(), formBook.getImage()));
        book.setAuthors(this.convertFormBookAuthors(formBook.getAuthors()));
        book.setPublisher(this.convertFormBookPublisher(formBook.getPublisherName()));
        book.setDescription(formBook.getDescription());
        book.setNumPages(formBook.getNumPages());
        book.setLanguage(formBook.getLanguage());
        book.setPrice(this.convertFormBookPrice(formBook.getPrice()));
        book.setStock(formBook.getStock());
        book.setYear(convertFormBookYear(formBook.getYear()));
        return book;
    }

    private String convertFormBookIsbn(String isbn) {
        return isbn.replaceAll(ISBN_FILTER, "");
    }

    private Category convertFormBookCategoryId(Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    private Image convertFormBookImage(Long storedImageId, Image image) {
        if (storedImageId == null){
            return image;
        }
        Image storedImage = new Image();
        storedImage.setId(storedImageId);
        return storedImage;
    }

    private List<Author> convertFormBookAuthors(String formBookAuthors) {
        //System.out.println("formBookAuthors: " + formBookAuthors);
        List<Author> authors = new ArrayList<>();
        if (formBookAuthors.isBlank()) {
            return authors;
        }
        for (String formBookAuthor : formBookAuthors.split(";")){
            //System.out.println("formBookAuthor: " + formBookAuthor);
            String [] authorParts = formBookAuthor.split(",");
            //System.out.println("authorParts[0]: " + authorParts[0]);
            //System.out.println("authorParts[1]: " + authorParts[1]);
            Author author = new Author();
            author.setFirstName(authorParts[0]);
            author.setLastName(authorParts[1]);
            if (!authors.contains(author)) {
                authors.add(author); // avoiding books with author repeated
            }
        }
        return authors;
    }

    private Publisher convertFormBookPublisher(String publisherName) {
        if (publisherName.isBlank()){
            return null;
        }
        Publisher publisher = new Publisher();
        publisher.setName(publisherName);
        return publisher;
    }

    private BigDecimal convertFormBookPrice(String price) {
        return (price.isBlank()) ? BigDecimal.ZERO : new BigDecimal(price);
    }

    private int convertFormBookYear(Year year) {
        return (year == null) ? 0 : year.getValue();
    }
}
