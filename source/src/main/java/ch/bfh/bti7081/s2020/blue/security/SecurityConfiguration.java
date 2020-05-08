package ch.bfh.bti7081.s2020.blue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/login";
  private static final String LOGIN_FAILURE_URL = "/login?error";
  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = "/login";
  private static final String LOGOUT_URL = "/logout";
  @Autowired
  private UserDetailsService databaseUserDetailsService;

  /**
   * Require login to access internal pages and configure login form.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.anonymous().and().authorizeRequests()
        // TODO Add publicly accessible routes here.
        .antMatchers("/register","/otherpublicpage","/about").permitAll();

    // Not using Spring CSRF here to be able to use plain HTML for the login page
    http.csrf().disable() //

        // Register our CustomRequestCache that saves unauthorized access attempts, so
        // the user is redirected after login.
        .requestCache().requestCache(new CustomRequestCache()) //
        // Restrict access to our application.
        .and().authorizeRequests()

        // Allow all flow internal requests.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

        // Allow all requests by logged in users.
        .anyRequest().authenticated() //

        // Configure the login page.
        .and().formLogin().loginPage(LOGIN_URL).permitAll() //
        .loginProcessingUrl(LOGIN_PROCESSING_URL) //
        .failureUrl(LOGIN_FAILURE_URL)

        // Configure logout
        .and().logout().logoutUrl(LOGOUT_URL).logoutSuccessUrl(LOGOUT_SUCCESS_URL);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        // Vaadin Flow static resources //
        "/VAADIN/**",
        // the standard favicon URI
        "/favicon.ico",
        // the robots exclusion standard
        "/robots.txt",
        // web application manifest //
        "/manifest.webmanifest",
        "/sw.js",
        "/offline-page.html",
        // (development mode) static resources //
        "/frontend/**",
        // (development mode) webjars //
        "/webjars/**",
        // (production mode) static resources //
        "/frontend-es5/**", "/frontend-es6/**");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(databaseUserDetailsService);
    authProvider.setPasswordEncoder(new BCryptPasswordEncoder());

    auth.authenticationProvider(authProvider);
  }
}
