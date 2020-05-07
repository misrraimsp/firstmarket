package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.FrontEndProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Languages;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceIntervals;
import misrraimsp.uned.pfg.firstmarket.exception.*;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BookController extends BasicController {

    private FrontEndProperties frontEndProperties;

    @Autowired
    public BookController(UserServer userServer,
                          BookServer bookServer,
                          CatServer catServer,
                          ImageServer imageServer,
                          MessageSource messageSource,
                          FrontEndProperties frontEndProperties) {

        super(userServer, bookServer, catServer, imageServer, messageSource);
        this.frontEndProperties = frontEndProperties;
    }

    private void populateModelToBookForm(Model model) {
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        model.addAttribute("languages", Languages.values());
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable("id") Long id,
                           Model model,
                           @AuthenticationPrincipal User authUser) {

        try {
            populateModel(model, authUser);
            model.addAttribute("book", bookServer.findById(id));
            return "book";
        }
        catch (BookNotFoundException e) {
            LOGGER.warn("Trying to access a non-existent Book", e);
            return "redirect:/home";
        }

    }

    @GetMapping("/admin/bookForm")
    public String showBookForm(@RequestParam(name = "id") Optional<Long> optBookId,
                               Model model) {

        AtomicBoolean bookNotFound = new AtomicBoolean(false);
        optBookId.ifPresentOrElse(
                bookId -> {
                    try {
                        Book book = bookServer.findById(bookId);
                        BookForm bookForm = bookServer.convertBookToBookForm(book);
                        model.addAttribute("bookForm", bookForm);
                    }
                    catch (BookNotFoundException e) {
                        LOGGER.warn("Trying to edit a non-existent Book", e);
                        bookNotFound.set(true);
                    }
                },
                () -> model.addAttribute("bookForm", new BookForm())
        );
        if (bookNotFound.get()) {
            return "redirect:/home";
        }
        populateModel(model, null);
        populateModelToBookForm(model);
        return "bookForm";
    }

    @PostMapping("/admin/bookForm")
    public String processBookForm(@Valid BookForm bookForm,
                                  Errors errors,
                                  Model model) {

        if (errors.hasErrors()) {
            populateModel(model, null);
            populateModelToBookForm(model);
            return "bookForm";
        }
        try {
            Book book = bookServer.convertBookFormToBook(bookForm);
            if (book.getId() == null){
                book = bookServer.persist(book);
                LOGGER.trace("Book persisted (id={})", book.getId());
            } else {
                bookServer.edit(book);
                LOGGER.trace("Book edited (id={})", book.getId());
            }
        }
        catch (IsbnAlreadyExistsException e) {
            LOGGER.debug("trying to save a book with an already used isbn={}", bookForm.getIsbn(), e);
            errors.rejectValue("isbn", "isbn.notUnique");
            populateModel(model, null);
            populateModelToBookForm(model);
            return "bookForm";
        }
        catch (BookFormAuthorsConversionException e) {
            LOGGER.error("BookForm conversion error", e);
            return "redirect:/home";
        }
        catch (ImageNotFoundException e) {
            LOGGER.error("Trying to persist an Image-with-id that is not in the database searching by its id", e);
            return "redirect:/home";
        }
        catch (BadImageException e) {
            LOGGER.error("Trying to persist an image without data or id", e);
            return "redirect:/home";
        }
        catch (BookNotFoundException e) {
            LOGGER.error("Trying to edit a Book that is not in the database searching by its id");
            return "redirect:/home";
        }
        return "redirect:/books";
    }

    @GetMapping("/admin/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        if (bookServer.existsBook(id)) {
            bookServer.deleteById(id);
            LOGGER.trace("Book deleted (id={})", id);
        }
        else {
            LOGGER.warn("Trying to delete a non-existent Book (id={})", id);
        }
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String showBooks(@RequestParam(defaultValue = "${pagination.default-index}") String pageNo,
                            @RequestParam(defaultValue = "${pagination.default-size.book-search}") String pageSize,
                            @RequestParam(required = false) Long categoryId,
                            @RequestParam(required = false) Set<String> priceId,
                            @RequestParam(required = false) Set<Long> authorId,
                            @RequestParam(required = false) Set<Long> publisherId,
                            @RequestParam(required = false) Set<Languages> languageId,
                            @RequestParam(required = false) String q,
                            @Value(value = "${validation.regex.text-query:.*}") String qRegex,
                            Model model,
                            @AuthenticationPrincipal User authUser){

        // ad hoc validation
        boolean qIsValid = true;
        if (q != null) {
            Pattern pattern = Pattern.compile(qRegex);
            Matcher matcher = pattern.matcher(q);
            qIsValid = matcher.matches();
            LOGGER.debug("String({}) validation with patternName(textQuery) (regex={}) has been {}", q, qRegex, qIsValid);
            if (!qIsValid) {
                q = null;
            }
        }

        // paging
        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                Integer.parseInt(pageSize),
                Sort.by("price").descending().and(Sort.by("id").ascending()));

        // category
        Category category;
        try {
            // root category by default
            if (categoryId == null) {
                categoryId = catServer.getRootCategory().getId();
            }
            category = catServer.findCategoryById(categoryId);
        }
        catch (NoRootCategoryException nrc) {
            LOGGER.error("Root category not found", nrc);
            return "redirect:/home";
        }
        catch (CategoryNotFoundException e) {
            LOGGER.warn("Trying to find books with a non-existent Category. Root category set by default", e);
            return "redirect:/books";
        }

        // search
        Page<Book> books = bookServer.findSearchResults(categoryId, priceId, authorId, publisherId, languageId, q, pageable);
        Set<Author> authors = bookServer.findTopAuthorsByCategoryId(categoryId, frontEndProperties.getNumOfAuthors());
        Set<Publisher> publishers = bookServer.findTopPublishersByCategoryId(categoryId, frontEndProperties.getNumOfPublishers());
        Set<Languages> languages = bookServer.findTopLanguagesByCategoryId(categoryId, frontEndProperties.getNumOfLanguages());

        // load model
        populateModel(model, authUser);
        model.addAttribute("pageOfEntities", books);
        model.addAttribute("category", category);
        model.addAttribute("categorySequence", catServer.getCategorySequence(category));
        model.addAttribute("childrenCategories", catServer.getChildren(category));
        model.addAttribute("prices", PriceIntervals.values());
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("languages", languages);
        if (!qIsValid) {
            model.addAttribute("message", messageSource.getMessage("validation.regex.text-query", null, null));
        }
        return "books";
    }

}