package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Languages;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.PriceIntervals;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormBook;
import misrraimsp.uned.pfg.firstmarket.model.search.Filter;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
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
import java.util.Set;

@Controller
public class BookController implements Constants {

    private BookServer bookServer;
    private CatServer catServer;
    private ImageServer imageServer;
    private UserServer userServer;

    @Autowired
    public BookController(BookServer bookServer, CatServer catServer, ImageServer imageServer, UserServer userServer) {
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
        this.userServer = userServer;
    }

    @GetMapping("/admin/books")
    public String showBooks(Model model){
        model.addAttribute("books", bookServer.findAll());
        return "books";
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User authUser){
        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("book", bookServer.findById(id));
        return "book";
    }

    @GetMapping("/admin/newBook")
    public String showNewBookForm(Model model){
        model.addAttribute("formBook", new FormBook());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        model.addAllAttributes(patterns);
        model.addAllAttributes(numbers);
        model.addAttribute("languages", Languages.values());
        return "newBook";
    }

    @PostMapping("/admin/newBook")
    public String processNewBook(@Valid FormBook formBook, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAllAttributes(patterns);
            model.addAllAttributes(numbers);
            model.addAttribute("languages", Languages.values());
            return "newBook";
        }
        try {
            Book book = bookServer.convertFormBookToBook(formBook);
            bookServer.persist(book);
        }
        catch (IsbnAlreadyExistsException e) {
            errors.rejectValue("isbn", "isbn.notUnique");
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAllAttributes(patterns);
            model.addAllAttributes(numbers);
            model.addAttribute("languages", Languages.values());
            return "newBook";
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/editBook/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model){
        Book book = bookServer.findById(id);
        FormBook formBook = bookServer.convertBookToFormBook(book);
        model.addAttribute("formBook", formBook);
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        model.addAllAttributes(patterns);
        model.addAllAttributes(numbers);
        model.addAttribute("languages", Languages.values());
        return "editBook";
    }

    @PostMapping("/admin/editBook")
    public String processEditBook(@Valid FormBook formBook, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAllAttributes(patterns);
            model.addAllAttributes(numbers);
            model.addAttribute("languages", Languages.values());
            return "editBook";
        }
        try {
            Book book = bookServer.convertFormBookToBook(formBook);
            bookServer.edit(book);
        }
        catch (IsbnAlreadyExistsException e) {
            errors.rejectValue("isbn", "isbn.notUnique");
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAllAttributes(patterns);
            model.addAllAttributes(numbers);
            model.addAttribute("languages", Languages.values());
            return "editBook";
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookServer.deleteById(id);
        return "redirect:/admin/books";
    }

    @GetMapping("/books")
    public String showSearchResults(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) String pageNo,
                                    @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) String pageSize,
                                    @RequestParam(defaultValue = DEFAULT_CATEGORY_ID) Long categoryId,
                                    @RequestParam(required = false) Set<Long> priceId,
                                    @RequestParam(required = false) Set<Long> authorId,
                                    @RequestParam(required = false) Set<Long> publisherId,
                                    @RequestParam(required = false) Set<Languages> languageId,
                                    @RequestParam(required = false) String q,
                                    Model model,
                                    @AuthenticationPrincipal User authUser){

        {
            System.out.println("*** begin ***");
            System.out.println("Page Number: " + pageNo);
            System.out.println("Page Size: " + pageSize);
            System.out.println("Category: " + categoryId);
            System.out.println("Prices: " + priceId);
            System.out.println("Authors: " + authorId);
            System.out.println("Publishers: " + publisherId);
            System.out.println("Languages: " + languageId);
            System.out.println("Query: " + q);
            System.out.println("*** end ***");
        }

        Category category = catServer.findCategoryById(categoryId);

        Filter filter = new Filter();
        filter.setCategory(category);

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                Integer.parseInt(pageSize),
                Sort.by("price").descending().and(Sort.by("id").ascending()));

        Page<Book> books = bookServer.findSearchResults(categoryId, priceId, authorId, publisherId, languageId, q, pageable);

        List<Author> authors = bookServer.findTopAuthorsByCategoryId(categoryId, NUM_TOP_AUTHORS);
        List<Publisher> publishers = bookServer.findTopPublishersByCategoryId(categoryId, NUM_TOP_PUBLISHERS);
        List<Languages> languages = bookServer.findTopLanguagesByCategoryId(categoryId, NUM_TOP_LANGUAGES);

        List<Category> childrenCategories = catServer.getChildren(category);
        List<Category> categorySequence = catServer.getCategorySequence(category);

        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("pageOfBooks", books);
        model.addAttribute("category", category);
        model.addAttribute("categorySequence", categorySequence);
        model.addAttribute("childrenCategories", childrenCategories);
        model.addAttribute("prices", PriceIntervals.values());
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("languages", languages);
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("TEXT_QUERY", TEXT_QUERY);
        return "searchResults";
    }

}