package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.model.search.Filter;
import org.springframework.stereotype.Service;

@Service
public class FilterServer {
    public Filter create(Category category) {
        Filter filter = new Filter();
        filter.setCategory(category);
        return filter;
    }
}
