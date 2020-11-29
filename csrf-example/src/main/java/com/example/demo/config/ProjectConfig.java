package com.example.demo.config;

import com.example.demo.security.CsrfTokenLoggerFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        http.csrf().disable(); // GENERALLY DON'T DO THIS :)

        http.csrf(c -> {
            c.ignoringAntMatchers("/csrfdiabled/**");
//            c.csrfTokenRepository(new CustomCsrfTokenRepository());
        });

        http.addFilterAfter(new CsrfTokenLoggerFilter(), CsrfFilter.class);
    }
}
