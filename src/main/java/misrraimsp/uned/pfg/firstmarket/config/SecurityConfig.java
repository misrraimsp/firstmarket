package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.security.CustomAuthenticationFailureHandler;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServer userServer;
    private final MessageSource messageSource;

    @Autowired
    public SecurityConfig(UserServer userServer, MessageSource messageSource) {
        this.userServer = userServer;
        this.messageSource = messageSource;
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(messageSource);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/**")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/home", "/login", "/newUser")
                .access("permitAll")

                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(customAuthenticationFailureHandler())

                .and()
                .logout()
                .logoutSuccessUrl("/home")

                .and()
                .csrf()
                .ignoringAntMatchers("/listener") // open for stripe notifications
                //.ignoringAntMatchers("/h2-console/**") // Make H2-Console non-secured; for debug purposes

                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                //.and()
                //.headers()
                //.frameOptions()
                //.sameOrigin()
        ;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userServer)
                .passwordEncoder(encoder())
        ;
    }

}
