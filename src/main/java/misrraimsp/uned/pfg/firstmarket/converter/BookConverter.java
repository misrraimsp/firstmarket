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
        book.setId(formBook.getBookId());
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

    public FormBook convertBookToFormBook(Book book) {
        FormBook formBook = new FormBook();
        formBook.setBookId(book.getId());
        formBook.setIsbn(book.getIsbn());
        formBook.setTitle(book.getTitle());
        formBook.setCategoryId(book.getCategory().getId());
        formBook.setStoredImageId(book.getImage().getId());
        //authors
        List<Author> authors = book.getAuthors();
        int size = authors.size();
        if (size == 1){
            formBook.setAuthorFirstName0(authors.get(0).getFirstName());
            formBook.setAuthorLastName0(authors.get(0).getLastName());
            formBook.setAuthorFirstName1("");
            formBook.setAuthorLastName1("");
            formBook.setAuthorFirstName2("");
            formBook.setAuthorLastName2("");
            formBook.setAuthorFirstName3("");
            formBook.setAuthorLastName3("");
            formBook.setAuthorFirstName4("");
            formBook.setAuthorLastName4("");
        }
        else if (size == 2){
            formBook.setAuthorFirstName0(authors.get(0).getFirstName());
            formBook.setAuthorLastName0(authors.get(0).getLastName());
            formBook.setAuthorFirstName1(authors.get(1).getFirstName());
            formBook.setAuthorLastName1(authors.get(1).getLastName());
            formBook.setAuthorFirstName2("");
            formBook.setAuthorLastName2("");
            formBook.setAuthorFirstName3("");
            formBook.setAuthorLastName3("");
            formBook.setAuthorFirstName4("");
            formBook.setAuthorLastName4("");
        }
        else if (size == 3){
            formBook.setAuthorFirstName0(authors.get(0).getFirstName());
            formBook.setAuthorLastName0(authors.get(0).getLastName());
            formBook.setAuthorFirstName1(authors.get(1).getFirstName());
            formBook.setAuthorLastName1(authors.get(1).getLastName());
            formBook.setAuthorFirstName2(authors.get(2).getFirstName());
            formBook.setAuthorLastName2(authors.get(2).getLastName());
            formBook.setAuthorFirstName3("");
            formBook.setAuthorLastName3("");
            formBook.setAuthorFirstName4("");
            formBook.setAuthorLastName4("");
        }
        else if (size == 4){
            formBook.setAuthorFirstName0(authors.get(0).getFirstName());
            formBook.setAuthorLastName0(authors.get(0).getLastName());
            formBook.setAuthorFirstName1(authors.get(1).getFirstName());
            formBook.setAuthorLastName1(authors.get(1).getLastName());
            formBook.setAuthorFirstName2(authors.get(2).getFirstName());
            formBook.setAuthorLastName2(authors.get(2).getLastName());
            formBook.setAuthorFirstName3(authors.get(3).getFirstName());
            formBook.setAuthorLastName3(authors.get(3).getLastName());
            formBook.setAuthorFirstName4("");
            formBook.setAuthorLastName4("");
        }
        else if (size == 5){
            formBook.setAuthorFirstName0(authors.get(0).getFirstName());
            formBook.setAuthorLastName0(authors.get(0).getLastName());
            formBook.setAuthorFirstName1(authors.get(1).getFirstName());
            formBook.setAuthorLastName1(authors.get(1).getLastName());
            formBook.setAuthorFirstName2(authors.get(2).getFirstName());
            formBook.setAuthorLastName2(authors.get(2).getLastName());
            formBook.setAuthorFirstName3(authors.get(3).getFirstName());
            formBook.setAuthorLastName3(authors.get(3).getLastName());
            formBook.setAuthorFirstName4(authors.get(4).getFirstName());
            formBook.setAuthorLastName4(authors.get(4).getLastName());
        }
        else {
            formBook.setAuthorFirstName0("");
            formBook.setAuthorLastName0("");
            formBook.setAuthorFirstName1("");
            formBook.setAuthorLastName1("");
            formBook.setAuthorFirstName2("");
            formBook.setAuthorLastName2("");
            formBook.setAuthorFirstName3("");
            formBook.setAuthorLastName3("");
            formBook.setAuthorFirstName4("");
            formBook.setAuthorLastName4("");
        }
        //
        formBook.setPublisherName((book.getPublisher() != null) ? book.getPublisher().getName() : "");
        formBook.setDescription(book.getDescription());
        formBook.setNumPages(book.getNumPages());
        formBook.setLanguage(book.getLanguage());
        formBook.setPrice(book.getPrice().toString());
        formBook.setStock(book.getStock());
        formBook.setYear(Year.of(book.getYear()));
        return formBook;
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
