package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.SearchCriteria;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.FrontEndProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PageSize;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceInterval;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.BookSortCriteria;
import misrraimsp.uned.pfg.firstmarket.converter.ConversionManager;
import misrraimsp.uned.pfg.firstmarket.exception.IsbnAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
public class BookController extends BasicController {

    private final FrontEndProperties frontEndProperties;
    private final ConversionManager conversionManager;

    @Autowired
    public BookController(UserServer userServer,
                          BookServer bookServer,
                          CatServer catServer,
                          ImageServer imageServer,
                          MessageSource messageSource,
                          OrderServer orderServer,
                          FrontEndProperties frontEndProperties,
                          ConversionManager conversionManager) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
        this.frontEndProperties = frontEndProperties;
        this.conversionManager = conversionManager;
    }

    private void populateModelToBookForm(Map<String, Object> properties, Optional<String> optPageNo, Optional<String> optPageSize, Optional<String> optSort) {
        properties.put("indentedCategories", catServer.getIndentedCategories());
        properties.put("imagesInfo", imageServer.getAllMetaInfo());
        optPageNo.ifPresent(pageNo -> properties.put("pageNo", pageNo));
        optPageSize.ifPresent(pageSize -> properties.put("pageSize", pageSize));
        optSort.ifPresent(sort -> properties.put("sort", sort));
    }

    @GetMapping("/book/{id}")
    public String showBook(@PathVariable("id") Long id,
                           Model model,
                           @AuthenticationPrincipal User authUser) {

        populateModel(model.asMap(), authUser);
        model.addAttribute("book", bookServer.findById(id));
        return "book";

    }

    @GetMapping("/admin/bookForm")
    public String showBookForm(@RequestParam(name = "id") Optional<Long> optBookId,
                               @RequestParam(name = "pageNo") Optional<String> optPageNo,
                               @RequestParam(name = "pageSize") Optional<String> optPageSize,
                               @RequestParam(name = "sort") Optional<String> optSort,
                               Model model) {

        optBookId.ifPresentOrElse(
                bookId -> {
                    Book book = bookServer.findById(bookId);
                    BookForm bookForm = conversionManager.convertBookToBookForm(book);
                    model.addAttribute("bookForm", bookForm);
                },
                () -> model.addAttribute("bookForm", new BookForm())
        );
        populateModel(model.asMap(), null);
        populateModelToBookForm(model.asMap(),optPageNo,optPageSize,optSort);
        return "bookForm";
    }

    @PostMapping("/admin/bookForm")
    public ModelAndView processBookForm(@Valid BookForm bookForm,
                                        Errors errors,
                                        ModelAndView modelAndView,
                                        @RequestParam(name = "pageNo") Optional<String> optPageNo,
                                        @RequestParam(name = "pageSize") Optional<String> optPageSize,
                                        @RequestParam(name = "sort") Optional<String> optSort) {

        if (errors.hasErrors()) {
            populateModel(modelAndView.getModel(), null);
            populateModelToBookForm(modelAndView.getModel(),optPageNo,optPageSize,optSort);
            modelAndView.setViewName("bookForm");
            return modelAndView;
        }
        try {
            Book book = conversionManager.convertBookFormToBook(bookForm);
            if (book.getId() == null){
                book = bookServer.persist(book);
                LOGGER.debug("Book(id={}) persisted", book.getId());
            } else {
                bookServer.edit(book);
                LOGGER.debug("Book(id={}) edited", book.getId());
            }
        }
        catch (IsbnAlreadyExistsException e) {
            LOGGER.debug("Trying to save a book with an already used isbn={}", bookForm.getIsbn());
            errors.rejectValue("isbn", "isbn.notUnique");
            populateModel(modelAndView.getModel(), null);
            populateModelToBookForm(modelAndView.getModel(),optPageNo,optPageSize,optSort);
            modelAndView.setViewName("bookForm");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/books");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        optPageSize.ifPresent(pageSize -> modelAndView.addObject("pageSize", pageSize));
        optSort.ifPresent(sort -> modelAndView.addObject("sort", sort));
        return modelAndView;
    }

    @PostMapping("/admin/setBookStatus")
    public ModelAndView processSetBookStatus(ModelAndView modelAndView,
                                             @RequestParam Long bookId,
                                             @RequestParam ProductStatus productStatus,
                                             @RequestParam(name = "pageNo") Optional<String> optPageNo,
                                             @RequestParam(name = "pageSize") Optional<String> optPageSize,
                                             @RequestParam(name = "sort") Optional<String> optSort) {

        bookServer.setStatus(bookId, productStatus);
        modelAndView.setViewName("redirect:/books");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        optPageSize.ifPresent(pageSize -> modelAndView.addObject("pageSize", pageSize));
        optSort.ifPresent(sort -> modelAndView.addObject("sort", sort));
        return modelAndView;
    }

    @GetMapping("/books")
    public String showBooks(@RequestParam(defaultValue = "${fm.pagination.default-index}") int pageNo,
                            @RequestParam(defaultValue = "${fm.pagination.default-size-index.search}") PageSize pageSize,
                            @RequestParam(defaultValue = "${fm.pagination.default-sort-index.book}") BookSortCriteria sort,
                            @Valid SearchCriteria searchCriteria,
                            Errors errors,
                            Model model,
                            @AuthenticationPrincipal User authUser){

        if (errors.hasErrors()) {
            searchCriteria.setQ(null);
            model.addAttribute("message", messageSource.getMessage("validation.regex.text-query", null, null));
        }

        //filter status
        searchCriteria.setExcludedStatus((userServer.hasRole(authUser, "ROLE_ADMIN")) ? null : ProductStatus.DISABLED);

        // category
        if (searchCriteria.getCategoryId() == null) { // root category by default
            searchCriteria.setCategoryId(catServer.getRootCategory().getId());
        }
        Category category = catServer.findCategoryById(searchCriteria.getCategoryId());

        // search books
        Pageable pageable = PageRequest.of(pageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
        Page<Book> bookPage = bookServer.findSearchResults(searchCriteria, pageable);
        int lastPageNo = bookPage.getTotalPages() - 1;
        if (lastPageNo > 0 && lastPageNo < pageNo) {
            pageable = PageRequest.of(lastPageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
            bookPage = bookServer.findSearchResults(searchCriteria, pageable);
        }
        // search filter criteria
        Set<Author> authors = bookServer.findTopAuthorsByCategoryId(searchCriteria.getCategoryId(), frontEndProperties.getNumOfAuthors());
        Set<Publisher> publishers = bookServer.findTopPublishersByCategoryId(searchCriteria.getCategoryId(), frontEndProperties.getNumOfPublishers());
        Set<Language> languages = bookServer.findTopLanguagesByCategoryId(searchCriteria.getCategoryId(), frontEndProperties.getNumOfLanguages());

        // load model
        populateModel(model.asMap(), authUser);
        model.addAttribute("pageOfEntities", bookPage);
        model.addAttribute("category", category);
        model.addAttribute("categorySequence", catServer.getCategorySequence(category));
        model.addAttribute("childrenCategories", catServer.getChildren(category));
        model.addAttribute("prices", PriceInterval.values());
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("languages", languages);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("cartBookRegistry", bookServer.getCartBookRegistry());
        return "books";
    }

}