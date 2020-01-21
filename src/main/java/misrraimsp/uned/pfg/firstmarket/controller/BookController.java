package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.data.ImageRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.service.CategoryServer;
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

    private BookRepository bookRepository;
    private CategoryServer categoryServer;
    private ImageRepository imageRepository;

    @Autowired
    public BookController(BookRepository bookRepository, CategoryServer categoryServer,
                          ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.categoryServer = categoryServer;
        this.imageRepository= imageRepository;
    }

    @GetMapping("/admin/books")
    public String showBooks(Model model){
        model.addAttribute("title", "Books Manager");
        model.addAttribute("books", bookRepository.findAll());
        return "books";
    }

    @GetMapping("/admin/newBook")
    public String showNewBookForm(Model model){
        model.addAttribute("title", "New Book");
        model.addAttribute("book", new Book());
        model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
        return "newBook";
    }

    @PostMapping("/admin/newBook")
    public String processNewBook(@Valid Book book, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
            return "newBook";
        }
        imageRepository.save(book.getImage());
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/editBook/{id}")
    public String showEditBookForm(@PathVariable("id") Long id, Model model){
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id: " + id));
        model.addAttribute("title", "Edit Book");
        model.addAttribute("book", book);
        model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
        return "editBook";
    }

    @PostMapping("/admin/editBook")
    public String processEditBook(@Valid Book book, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
            return "editBook";
        }
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/admin/deleteBook/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }

}