package misrraimsp.uned.pfg.firstmarket.converter;

import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;
import misrraimsp.uned.pfg.firstmarket.exception.BookFormAuthorsConversionException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;

@Component
public class BookConverter {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ValidationRegexProperties validationRegexProperties;

    @Autowired
    public BookConverter(ValidationRegexProperties validationRegexProperties) {
        this.validationRegexProperties = validationRegexProperties;
    }

    public Book convertBookFormToBook(BookForm bookForm) throws BookFormAuthorsConversionException {
        Book book = new Book();
        book.setId(bookForm.getBookId());
        book.setStatus(this.convertBookFormStatus(bookForm.getStatus(), bookForm.getStock()));
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
        bookForm.setStatus(book.getStatus());
        bookForm.setIsbn(book.getIsbn());
        bookForm.setTitle(book.getTitle());
        bookForm.setCategoryId(book.getCategory().getId());
        bookForm.setStoredImageId(book.getImage().getId());
        //authors
        {
            if (book.getAuthors().isEmpty()) {
                bookForm.setAuthorsFirstName(Collections.singletonList(""));
                bookForm.setAuthorsLastName(Collections.singletonList(""));
            } else {
                List<String> authorsFirstName = new ArrayList<>();
                List<String> authorsLastName = new ArrayList<>();
                book.getAuthors().forEach(author -> {
                    authorsFirstName.add(author.getFirstName());
                    authorsLastName.add(author.getLastName());
                });
                bookForm.setAuthorsFirstName(authorsFirstName);
                bookForm.setAuthorsLastName(authorsLastName);
            }
        }
        bookForm.setPublisherName((book.getPublisher() != null) ? book.getPublisher().getName() : "");
        bookForm.setDescription(book.getDescription());
        bookForm.setPages(book.getPages());
        bookForm.setLanguage(book.getLanguage());
        bookForm.setPrice(book.getPrice().toString());
        bookForm.setStock(book.getStock());
        bookForm.setYear(Year.of(book.getYear()));
        return bookForm;
    }

    private ProductStatus convertBookFormStatus(@NonNull ProductStatus status, int stock) {
        if (status.equals(ProductStatus.DISABLED)) {
            return ProductStatus.DISABLED;
        }
        else {
            return (stock > 0) ? ProductStatus.OK : ProductStatus.OUT_OF_STOCK;
        }
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
        int numOfFirstNames = authorsFirstName.size();
        int numOfLastNames = authorsLastName.size();
        if (numOfFirstNames != numOfLastNames) {
            if (numOfFirstNames == 0 && numOfLastNames == 1) {
                authorsFirstName.add("");
                LOGGER.debug(
                        "Authors firstNames.size={} and lastNames.size={} does not match. Added empty string to author firstNames",
                        numOfFirstNames,
                        numOfLastNames
                );
            }
            else if (numOfFirstNames == 1 && numOfLastNames == 0) {
                authorsLastName.add("");
                LOGGER.debug(
                        "Authors firstNames.size={} and lastNames.size={} does not match. Added empty string to author lastNames",
                        numOfFirstNames,
                        numOfLastNames
                );
            }
            else {
                throw new BookFormAuthorsConversionException(
                        "Authors firstNames.size=" + numOfFirstNames + " and lastNames.size=" + numOfLastNames + " does not match in an unexpected way");
            }
        }
        Set<Author> authors = new HashSet<>();
        if (numOfFirstNames == 0) {
            return authors;
        }
        for (int i = 0; i < numOfFirstNames; i++) {
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
