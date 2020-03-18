package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.config.Languages;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import misrraimsp.uned.pfg.firstmarket.model.search.Filter;
import misrraimsp.uned.pfg.firstmarket.service.AuthorServer;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.FilterServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SearchController implements Constants {

    private FilterServer filterServer;
    private CatServer catServer;
    private BookServer bookServer;
    private AuthorServer authorServer;

    @Autowired
    public SearchController(FilterServer filterServer,
                            CatServer catServer,
                            BookServer bookServer,
                            AuthorServer authorServer) {

        this.filterServer = filterServer;
        this.catServer = catServer;
        this.bookServer = bookServer;
        this.authorServer = authorServer;
    }

    @GetMapping("/books/search/{categoryId}")
    public String showNewSearch(@PathVariable("categoryId") Long categoryId, Model model){

        Category category = catServer.findCategoryById(categoryId);
        Filter filter = new Filter();
        filter.setCategory(category);
        List<Book> books = bookServer.findWithFilter(filter);

        List<Author> authors = bookServer.findTopAuthorsInBookSet(books, NUM_TOP_AUTHORS);
        List<Publisher> publishers = bookServer.findTopPublishersInBookSet(books, NUM_TOP_PUBLISHERS);
        List<Languages> languages = bookServer.findTopLanguagesInBookSet(books);

        List<Category> childrenCategories = catServer.getChildren(category);

        model.addAttribute("books", books);
        model.addAttribute("category", category);
        model.addAttribute("childrenCategories", childrenCategories);
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("languages", languages);
        return "searchResults";
    }
}
