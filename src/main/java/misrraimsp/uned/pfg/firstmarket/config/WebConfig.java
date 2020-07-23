package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.util.converter.spring.*;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableAsync
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
        registry.addConverter(new StringToPriceIntervalConverter());
        registry.addConverter(new StringToProductStatusConverter());
        registry.addConverter(new StringToBookSortCriteriaConverter());
        registry.addConverter(new StringToPageSizeConverter());
        registry.addConverter(new StringToImageSortCriteriaConverter());
        registry.addConverter(new StringToUserSortCriteriaConverter());
        registry.addConverter(new StringToOrderSortCriteriaConverter());
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(31556926);
    }

}
