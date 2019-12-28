package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/firstmarket")
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
    public String processNewBook(Book book){
        bookRepository.save(book);
        return "redirect:books";
    }
}