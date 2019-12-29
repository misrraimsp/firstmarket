package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/admin/books")
    public String books(Model model){
        model.addAttribute("books", bookRepository.findAll());
        return "books";
    }

    @GetMapping("/admin/newBook")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "newBookForm";
    }

    @PostMapping("/admin/newBook")
    public String processNewBook(@Valid Book book, Errors errors){
        if (errors.hasErrors()) {
            return "newBookForm";
        }
        bookRepository.save(book);
        return "redirect:/admin/books";
    }
}