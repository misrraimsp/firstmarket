package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Languages;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceIntervals;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPattern;

import java.util.Set;

@Data
public class SearchCriteria {

    private Long categoryId;
    private Set<PriceIntervals> priceId;
    private Set<Long> authorId;
    private Set<Long> publisherId;
    private Set<Languages> languageId;

    @ValidPattern(pattern = "textQuery", message = "{validation.regex.text-query}")
    private String q;

}
