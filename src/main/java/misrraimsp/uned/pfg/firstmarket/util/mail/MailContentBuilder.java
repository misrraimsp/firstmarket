package misrraimsp.uned.pfg.firstmarket.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilder {

    private final TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String template, Map<String,Object> properties) {
        Context context = new Context();
        context.setVariables(properties);
        return templateEngine.process(template, context);
    }
}
