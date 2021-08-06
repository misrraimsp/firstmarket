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
                // authenticate through login form
                .formLogin()
                .loginPage("/login")
                .failureHandler(customAuthenticationFailureHandler())

                // set logout page
                .and()
                .logout()
                .logoutSuccessUrl("/home")

                // authorization
                .and()
                .authorizeRequests()
                    // protect role_admin resources
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                    // protect role_user resources
                .antMatchers("/user/**")
                .access("hasRole('ROLE_USER')")
                    // publicly open
                .antMatchers("/", "/home", "/login", "/newUser")
                .access("permitAll")

                // force HTTPS always
                .and()
                .requiresChannel()
                .antMatchers("/**")
                .requiresSecure()

                // enable csrf protection
                .and()
                .csrf()
                .ignoringAntMatchers("/listener") // open for stripe notifications
                // DEV - Make H2-Console non-secured
                //.ignoringAntMatchers("/h2-console/**")

                // always create new session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                .and()
                .headers(headers -> headers
                        // HTTP Strict Transport Security
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .preload(true)
                                .maxAgeInSeconds(31536000)
                        )
                        // Content Security Policy
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'none'" +
                                        "; " +
                                        "script-src 'self' " +
                                        "https://js.stripe.com " +
                                        "https://*.fontawesome.com " +
                                        "https://maxcdn.bootstrapcdn.com " +
                                        "https://cdnjs.cloudflare.com " +
                                        "https://ajax.googleapis.com" +
                                        "; " +
                                        "style-src 'self' " +
                                        "https://cdn.jsdelivr.net " +
                                        "https://maxcdn.bootstrapcdn.com " +
                                        "https://fonts.googleapis.com " +
                                        "https://*.fontawesome.com" +
                                        "; " +
                                        "font-src 'self' " +
                                        "https://fonts.gstatic.com " +
                                        "https://*.fontawesome.com" +
                                        "; " +
                                        "img-src 'self' data:" +
                                        "; " +
                                        "frame-src 'self' " +
                                        "https://js.stripe.com" +
                                        "; " +
                                        "connect-src 'self' " +
                                        "https://api.stripe.com " +
                                        "https://*.fontawesome.com/*"
                                )
                        )
                        // Referrer Policy
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN)
                        )
                )

                /* DEV - Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                 */
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
