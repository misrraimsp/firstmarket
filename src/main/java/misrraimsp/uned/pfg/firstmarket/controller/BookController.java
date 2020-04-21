package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.FrontEndProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Languages;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceIntervals;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class BookController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private BookServer bookServer;
    private CatServer catServer;
    private ImageServer imageServer;
    private UserServer userServer;

    private FrontEndProperties frontEndProperties;

    @Autowired
    public BookController(BookServer bookServer,
                          CatServer catServer,
                          ImageServer imageServer,
                          UserServer userServer,
                          FrontEndProperties frontEndProperties) {

        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
        this.userServer = userServer;

        this.frontEndProperties = frontEndProperties;
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User authUser){
        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("book", bookServer.findById(id));
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "book";
    }

    @GetMapping("/admin/bookForm")
    public String showBookForm(@RequestParam(name = "id") Optional<Long> optBookId, Model model){
        AtomicBoolean badIdArgument = new AtomicBoolean(false);
        optBookId.ifPresentOrElse(
                bookId -> {
                    try {
                        Book book = bookServer.findById(bookId);
                        model.addAttribute("bookForm", bookServer.convertBookToBookForm(book));
                    } catch (IllegalArgumentException e) {
                        badIdArgument.set(true);
                    }
                },
                () -> model.addAttribute("bookForm", new BookForm())
        );
        if (badIdArgument.get()) {
            return "redirect:/books";
        }
        populateModelToBookForm(model);
        return "bookForm";
    }

    @PostMapping("/admin/bookForm")
    public String processBookForm(@Valid BookForm bookForm, Errors errors, Model model){
        if (errors.hasErrors()) {
            populateModelToBookForm(model);
            return "bookForm";
        }
        try {
            Book book = bookServer.convertBookFormToBook(bookForm);
            if (book.getId() == null){
                bookServer.persist(book);
            } else {
                bookServer.edit(book);
            }
        } catch (IsbnAlreadyExistsException e) {
            errors.rejectValue("isbn", "isbn.notUnique");
            populateModelToBookForm(model);
            return "bookForm";
        }
        return "redirect:/books";
    }

    private void populateModelToBookForm(Model model) {
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        model.addAttribute("languages", Languages.values());
    }

    @GetMapping("/admin/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookServer.deleteById(id);
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
                            @RequestParam(required = false) String q, //TODO validate this param
                            Model model,
                            @AuthenticationPrincipal User authUser){

        LOGGER.trace("this is a trace message");
        LOGGER.debug("this is a debug message");
        LOGGER.info("this is an info message - {}", pageSize);
        LOGGER.warn("this is a warn message");
        LOGGER.error("this is an error message");

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                Integer.parseInt(pageSize),
                Sort.by("price").descending().and(Sort.by("id").ascending()));

        // root category if not specified
        if (categoryId == null) {
            categoryId = catServer.getRootCategory().getId();
        }

        Page<Book> books = bookServer.findSearchResults(categoryId, priceId, authorId, publisherId, languageId, q, pageable);
        Set<Author> authors = bookServer.findTopAuthorsByCategoryId(categoryId, frontEndProperties.getNumOfAuthors());
        Set<Publisher> publishers = bookServer.findTopPublishersByCategoryId(categoryId, frontEndProperties.getNumOfPublishers());
        Set<Languages> languages = bookServer.findTopLanguagesByCategoryId(categoryId, frontEndProperties.getNumOfLanguages());
        Category category = catServer.findCategoryById(categoryId);
        List<Category> childrenCategories = catServer.getChildren(category);
        List<Category> categorySequence = catServer.getCategorySequence(category);

        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("pageOfEntities", books);
        model.addAttribute("category", category);
        model.addAttribute("categorySequence", categorySequence);
        model.addAttribute("childrenCategories", childrenCategories);
        model.addAttribute("prices", PriceIntervals.values());
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("languages", languages);
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "books";
    }

}