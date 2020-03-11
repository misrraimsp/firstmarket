package misrraimsp.uned.pfg.firstmarket.model.search;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Languages;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;

import java.util.ArrayList;
import java.util.List;

@Data
public class Filter {

    Category category;
    String key;
    List<String> prices = new ArrayList<>();
    List<Author> authors = new ArrayList<>();
    List<Publisher> publishers = new ArrayList<>();
    List<Languages> languages = new ArrayList<>();

}
