package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.config.Languages;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormBook;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

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

}