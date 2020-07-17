package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.core.service.UserServer;
import misrraimsp.uned.pfg.firstmarket.security.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServer userServer;
    private final MessageSource messageSource;

    @Autowired
    public SecurityConfig(UserServer userServer,
                          MessageSource messageSource) {

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

                // local-dev
                .and()
                .requiresChannel()
                .antMatchers("/**")
                .requiresSecure()

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

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                /*
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
        <script src="https://kit.fontawesome.com/9902891181.js" crossorigin="anonymous"></script>
        <script src="https://js.stripe.com/v3/"></script>
                 */

                /*
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/pretty-checkbox@3.0/dist/pretty-checkbox.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Noto+Serif+JP&display=swap">
        <link rel="stylesheet" th:href="@{/css/fmstyle.css}" type="text/css"/>
                 */

                .and()
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'" +
                                        "; " +
                                        "script-src-elem 'self' " +
                                        "https://js.stripe.com " +
                                        "https://kit.fontawesome.com " +
                                        "https://maxcdn.bootstrapcdn.com " +
                                        "https://cdnjs.cloudflare.com " +
                                        "https://ajax.googleapis.com " +
                                        "; " +
                                        "style-src-elem 'self' " +
                                        "https://cdn.jsdelivr.net " +
                                        "https://maxcdn.bootstrapcdn.com " +
                                        "https://fonts.googleapis.com")
                        )
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
                        )
                )


                //.headers()
                //.referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN)
                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
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
