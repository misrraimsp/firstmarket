package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class BookController implements Patterns {

    private BookServer bookServer;
    private CatServer catServer;
    private ImageServer imageServer;

    @Autowired
    public BookController(BookServer bookServer, CatServer catServer, ImageServer imageServer) {
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
    }

    @GetMapping("/books")
    public String showBooks(Model model){
        model.addAttribute("books", bookServer.findAll());
        return "books";
    }

    @GetMapping("/newBook")
    public String showNewBookForm(Model model){
        model.addAttribute("book", new Book());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        model.addAttribute("isbnPattern", ISBN);
        model.addAttribute("textBasicPattern", TEXT_BASIC);
        return "newBook";
    }

    @PostMapping("/newBook")
    public String processNewBook(@Valid Book book, Errors errors, Long storedImageId, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAttribute("isbnPattern", ISBN);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            return "newBook";
        }
        if (storedImageId == null){ //new image upload
            book.setImage(imageServer.persist(book.getImage()));
        }
        else {
            book.setImage(imageServer.findById(storedImageId));
        }
        try {
            bookServer.persist(book);
        }
        catch (IsbnAlreadyExistsException e) {
            errors.rejectValue("isbn", "isbn.notUnique");
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAttribute("isbnPattern", ISBN);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            return "newBook";
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/editBook/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model){
        Book book = bookServer.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("bookImageId", book.getImage().getId());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        model.addAttribute("isbnPattern", ISBN);
        model.addAttribute("textBasicPattern", TEXT_BASIC);
        return "editBook";
    }

    @PostMapping("/editBook")
    public String processEditBook(@Valid Book book, Errors errors, Long storedImageId, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("bookImageId", bookServer.findById(book.getId()).getImage().getId());
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAttribute("isbnPattern", ISBN);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            return "editBook";
        }
        if (storedImageId == null){ //new image upload
            book.setImage(imageServer.persist(book.getImage()));
        }
        else {
            book.setImage(imageServer.findById(storedImageId));
        }
        try {
            bookServer.edit(book);
        }
        catch (IsbnAlreadyExistsException e) {
            errors.rejectValue("isbn", "isbn.notUnique");
            model.addAttribute("bookImageId", bookServer.findById(book.getId()).getImage().getId());
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            model.addAttribute("isbnPattern", ISBN);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            return "editBook";
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookServer.deleteById(id);
        return "redirect:/admin/books";
    }

}