package misrraimsp.uned.pfg.firstmarket.controller;

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

import javax.validation.Valid;

@Controller
public class BookController {

    private BookServer bookServer;
    private CatServer catServer;
    private ImageServer imageServer;

    @Autowired
    public BookController(BookServer bookServer, CatServer catServer, ImageServer imageServer) {
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.imageServer = imageServer;
    }

    @GetMapping("/admin/books")
    public String showBooks(Model model){
        model.addAttribute("title", "Books Manager");
        model.addAttribute("books", bookServer.findAll());
        return "books";
    }

    @GetMapping("/admin/newBook")
    public String showNewBookForm(Model model){
        model.addAttribute("title", "New Book");
        model.addAttribute("book", new Book());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        return "newBook";
    }

    @PostMapping("/admin/newBook")
    public String processNewBook(@Valid Book book, Long storedImageId, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            return "newBook";
        }
        if (storedImageId == null){ //new image upload
            book.setImage(imageServer.persistImage(book.getImage()));
        }
        else {
            book.setImage(imageServer.findById(storedImageId));
        }
        bookServer.persistBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/editBook/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("title", "Edit Book");
        model.addAttribute("book", bookServer.findById(id));
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
        return "editBook";
    }

    @PostMapping("/admin/editBook")
    public String processEditBook(@Valid Book book, Long storedImageId, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            model.addAttribute("imagesInfo", imageServer.getAllMetaInfo());
            return "editBook";
        }
        if (storedImageId == null){ //new image upload
            book.setImage(imageServer.persistImage(book.getImage()));
        }
        else {
            book.setImage(imageServer.findById(storedImageId));
        }
        bookServer.persistBook(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookServer.deleteById(id);
        return "redirect:/admin/books";
    }

}