package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.config.Languages;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import misrraimsp.uned.pfg.firstmarket.model.search.Filter;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.FilterServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController implements Constants {

    private FilterServer filterServer;
    private CatServer catServer;
    private BookServer bookServer;

    @Autowired
    public SearchController(FilterServer filterServer,
                            CatServer catServer,
                            BookServer bookServer) {

        this.filterServer = filterServer;
        this.catServer = catServer;
        this.bookServer = bookServer;
    }

    @GetMapping("/books/search/{categoryId}")
    public String showNewSearch(@RequestParam(defaultValue = "0") String pageNo,
                                @PathVariable("categoryId") Long categoryId,
                                Model model){

        Category category = catServer.findCategoryById(categoryId);
        Filter filter = new Filter();
        filter.setCategory(category);

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                PAGE_SIZE,
                Sort.by("price").descending().and(Sort.by("id").ascending()));

        Page<Book> books = bookServer.findWithFilter(filter, pageable);

        List<Author> authors = bookServer.findTopAuthorsByCategoryId(categoryId, NUM_TOP_AUTHORS);
        List<Publisher> publishers = bookServer.findTopPublishersByCategoryId(categoryId, NUM_TOP_PUBLISHERS);
        List<Languages> languages = bookServer.findTopLanguagesByCategoryId(categoryId, NUM_TOP_LANGUAGES);

        List<Category> childrenCategories = catServer.getChildren(category);

        model.addAttribute("pageOfBooks", books);
        model.addAttribute("category", category);
        model.addAttribute("childrenCategories", childrenCategories);
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("languages", languages);
        return "searchResults";
    }
}
