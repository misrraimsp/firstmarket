package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.exception.BookFormAuthorsConversionException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BookConverter {

    private ValidationRegexProperties validationRegexProperties;

    @Autowired
    public BookConverter(ValidationRegexProperties validationRegexProperties) {
        this.validationRegexProperties = validationRegexProperties;
    }

    public Book convertBookFormToBook(BookForm bookForm) throws BookFormAuthorsConversionException {
        Book book = new Book();
        book.setId(bookForm.getBookId());
        book.setIsbn(this.convertBookFormIsbn(bookForm.getIsbn()));
        book.setTitle(bookForm.getTitle());
        book.setCategory(this.convertBookFormCategoryId(bookForm.getCategoryId()));
        book.setImage(this.convertBookFormImage(bookForm.getStoredImageId(), bookForm.getImage()));
        book.setAuthors(this.convertBookFormAuthors(bookForm.getAuthorsFirstName(), bookForm.getAuthorsLastName()));
        book.setPublisher(this.convertBookFormPublisher(bookForm.getPublisherName()));
        book.setDescription(bookForm.getDescription());
        book.setPages(bookForm.getPages());
        book.setLanguage(bookForm.getLanguage());
        book.setPrice(this.convertBookFormPrice(bookForm.getPrice()));
        book.setStock(bookForm.getStock());
        book.setYear(convertBookFormYear(bookForm.getYear()));
        return book;
    }

    public BookForm convertBookToBookForm(Book book) {
        BookForm bookForm = new BookForm();
        bookForm.setBookId(book.getId());
        bookForm.setIsbn(book.getIsbn());
        bookForm.setTitle(book.getTitle());
        bookForm.setCategoryId(book.getCategory().getId());
        bookForm.setStoredImageId(book.getImage().getId());
        //authors
        List<String> authorsFirstName = new ArrayList<>();
        List<String> authorsLastName = new ArrayList<>();
        book.getAuthors().forEach(author -> {
            authorsFirstName.add(author.getFirstName());
            authorsLastName.add(author.getLastName());
        });
        bookForm.setAuthorsFirstName(authorsFirstName);
        bookForm.setAuthorsLastName(authorsLastName);
        /*
        int size = authors.size();
        if (size == 1){
            bookForm.setAuthorFirstName0(authors.get(0).getFirstName());
            bookForm.setAuthorLastName0(authors.get(0).getLastName());
            bookForm.setAuthorFirstName1("");
            bookForm.setAuthorLastName1("");
            bookForm.setAuthorFirstName2("");
            bookForm.setAuthorLastName2("");
            bookForm.setAuthorFirstName3("");
            bookForm.setAuthorLastName3("");
            bookForm.setAuthorFirstName4("");
            bookForm.setAuthorLastName4("");
        }
        else if (size == 2){
            bookForm.setAuthorFirstName0(authors.get(0).getFirstName());
            bookForm.setAuthorLastName0(authors.get(0).getLastName());
            bookForm.setAuthorFirstName1(authors.get(1).getFirstName());
            bookForm.setAuthorLastName1(authors.get(1).getLastName());
            bookForm.setAuthorFirstName2("");
            bookForm.setAuthorLastName2("");
            bookForm.setAuthorFirstName3("");
            bookForm.setAuthorLastName3("");
            bookForm.setAuthorFirstName4("");
            bookForm.setAuthorLastName4("");
        }
        else if (size == 3){
            bookForm.setAuthorFirstName0(authors.get(0).getFirstName());
            bookForm.setAuthorLastName0(authors.get(0).getLastName());
            bookForm.setAuthorFirstName1(authors.get(1).getFirstName());
            bookForm.setAuthorLastName1(authors.get(1).getLastName());
            bookForm.setAuthorFirstName2(authors.get(2).getFirstName());
            bookForm.setAuthorLastName2(authors.get(2).getLastName());
            bookForm.setAuthorFirstName3("");
            bookForm.setAuthorLastName3("");
            bookForm.setAuthorFirstName4("");
            bookForm.setAuthorLastName4("");
        }
        else if (size == 4){
            bookForm.setAuthorFirstName0(authors.get(0).getFirstName());
            bookForm.setAuthorLastName0(authors.get(0).getLastName());
            bookForm.setAuthorFirstName1(authors.get(1).getFirstName());
            bookForm.setAuthorLastName1(authors.get(1).getLastName());
            bookForm.setAuthorFirstName2(authors.get(2).getFirstName());
            bookForm.setAuthorLastName2(authors.get(2).getLastName());
            bookForm.setAuthorFirstName3(authors.get(3).getFirstName());
            bookForm.setAuthorLastName3(authors.get(3).getLastName());
            bookForm.setAuthorFirstName4("");
            bookForm.setAuthorLastName4("");
        }
        else if (size == 5){
            bookForm.setAuthorFirstName0(authors.get(0).getFirstName());
            bookForm.setAuthorLastName0(authors.get(0).getLastName());
            bookForm.setAuthorFirstName1(authors.get(1).getFirstName());
            bookForm.setAuthorLastName1(authors.get(1).getLastName());
            bookForm.setAuthorFirstName2(authors.get(2).getFirstName());
            bookForm.setAuthorLastName2(authors.get(2).getLastName());
            bookForm.setAuthorFirstName3(authors.get(3).getFirstName());
            bookForm.setAuthorLastName3(authors.get(3).getLastName());
            bookForm.setAuthorFirstName4(authors.get(4).getFirstName());
            bookForm.setAuthorLastName4(authors.get(4).getLastName());
        }
        else {
            bookForm.setAuthorFirstName0("");
            bookForm.setAuthorLastName0("");
            bookForm.setAuthorFirstName1("");
            bookForm.setAuthorLastName1("");
            bookForm.setAuthorFirstName2("");
            bookForm.setAuthorLastName2("");
            bookForm.setAuthorFirstName3("");
            bookForm.setAuthorLastName3("");
            bookForm.setAuthorFirstName4("");
            bookForm.setAuthorLastName4("");
        }

         */
        //
        bookForm.setPublisherName((book.getPublisher() != null) ? book.getPublisher().getName() : "");
        bookForm.setDescription(book.getDescription());
        bookForm.setPages(book.getPages());
        bookForm.setLanguage(book.getLanguage());
        bookForm.setPrice(book.getPrice().toString());
        bookForm.setStock(book.getStock());
        bookForm.setYear(Year.of(book.getYear()));
        return bookForm;
    }

    private String convertBookFormIsbn(String isbn) {
        return isbn.replaceAll(validationRegexProperties.getIsbnFilter(), "");
    }

    private Category convertBookFormCategoryId(Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    private Image convertBookFormImage(Long storedImageId, Image image) {
        if (storedImageId == null){
            return image;
        }
        Image storedImage = new Image();
        storedImage.setId(storedImageId);
        return storedImage;
    }

    private Set<Author> convertBookFormAuthors(List<String> authorsFirstName, List<String> authorsLastName) {
        int numOfAuthors = authorsFirstName.size();
        if (numOfAuthors != authorsLastName.size()) {
            throw new BookFormAuthorsConversionException(
                    "Authors firstNames.size=" + numOfAuthors + " and lastNames.size=" + authorsLastName.size() + " are not equal");
        }
        Set<Author> authors = new HashSet<>();
        if (numOfAuthors == 0) {
            return authors;
        }
        for (int i = 0; i < numOfAuthors; i++) {
            String firstName = authorsFirstName.get(i);
            String lastName = authorsLastName.get(i);
            if (!firstName.isBlank() || !lastName.isBlank()) {
                Author author = new Author();
                author.setFirstName(firstName);
                author.setLastName(lastName);
                authors.add(author);
            }
        }
        return authors;
    }

    private Publisher convertBookFormPublisher(String publisherName) {
        if (publisherName.isBlank()){
            return null;
        }
        Publisher publisher = new Publisher();
        publisher.setName(publisherName);
        return publisher;
    }

    private BigDecimal convertBookFormPrice(String price) {
        return (price.isBlank()) ? BigDecimal.ZERO : new BigDecimal(price);
    }

    private int convertBookFormYear(Year year) {
        return (year == null) ? 0 : year.getValue();
    }
}
