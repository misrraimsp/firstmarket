package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.BookStatus;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceInterval;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPattern;

import java.util.Set;

@Data
public class SearchCriteria {

    private Long categoryId;
    private Set<PriceInterval> priceId;
    private Set<Long> authorId;
    private Set<Long> publisherId;
    private Set<Language> languageId;

    @ValidPattern(pattern = "textQuery", message = "{validation.regex.text-query}")
    private String q;

    private BookStatus excludedStatus;

}
