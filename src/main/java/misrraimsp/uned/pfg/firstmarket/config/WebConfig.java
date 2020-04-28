package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.converter.MultipartFileToImageConverter;
import misrraimsp.uned.pfg.firstmarket.converter.StringToDeletionReasonConverter;
import misrraimsp.uned.pfg.firstmarket.converter.StringToLanguageConverter;
import misrraimsp.uned.pfg.firstmarket.converter.StringToYearConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        //viewControllerRegistry.addViewController("/admin");
        //viewControllerRegistry.addViewController("/login");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MultipartFileToImageConverter());
        registry.addConverter(new StringToYearConverter());
        registry.addConverter(new StringToLanguageConverter());
        registry.addConverter(new StringToDeletionReasonConverter());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Override
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

}
