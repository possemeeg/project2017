package com.possemeeg.project2017.webdoor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.possemeeg.project2017.webdoor.config.properties.AuthProperties;
import com.possemeeg.project2017.webdoor.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({AuthProperties.class})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            //.antMatchers("/", "/home", "/index.html").permitAll()
            .anyRequest().authenticated();
        http
            .formLogin()
            .loginPage("/login")
            .permitAll();
        http
            .logout()
            .permitAll();
        http
            .sessionManagement()
            .maximumSessions(100)
            .sessionRegistry(sessionRegistry);
            //http
            //  .authorizeRequests()
            //  .anyRequest().authenticated()
            //  .and()
            //  .formLogin()
            //  .loginPage("/login")
            //  .permitAll()
            //  .and()
            //  .logout()
            //  .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, AuthProperties authProperties) throws Exception {
      List<User> users = User.listFromResource(authProperties.getUsers());
      if (users.isEmpty()) {
        LOGGER.warn("No users configured");
        return;
      }

      PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

      InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemAuto =
      auth.inMemoryAuthentication().passwordEncoder(passwordEncoder);
      for (User user : users) {
        inMemAuto.withUser(user.user).password(user.password).roles(user.role);
      }
    }
    //@Bean
    //SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
    //    return http
    //        // Demonstrate that method security works
    //        // Best practice to use both for defense in depth
    //        .authorizeExchange()
    //        .anyExchange().authenticated()
    //        .and()
    //        .formLogin()
    //        .loginPage("/login")
    //        .and()
    //        .build();
    //}
    //@Bean
    //public MapReactiveUserDetailsService userDetailsRepository() {
    //    User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
    //    UserDetails rob = userBuilder.username("rob").password("{noop}rob").roles("USER").build();
    //    UserDetails admin = userBuilder.username("admin").password("{noop}admin").roles("USER", "ADMIN").build();
    //    return new MapReactiveUserDetailsService(rob, admin);
    //}
}

