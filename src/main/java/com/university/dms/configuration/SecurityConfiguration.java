package com.university.dms.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, DataSource dataSource) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login", "/index", "/register", "/authentication/register").permitAll()
                .antMatchers( "/img/**","/css/**","/resources/**", "/static/**", "/static/css/**", "/static/js/**", "/static/img/**", "/video/**").permitAll()
                .antMatchers( "/css/**", "/js/**", "/img/**", "/video/**").permitAll()
                .antMatchers("/main", "/main/**").permitAll()
                .and().formLogin()
                .loginPage("/login").failureUrl("/login?error=true");
//                .and().logout()
//                .logoutSuccessUrl("/index");

    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/login", "/register", "/home")
//                .permitAll()
//                .antMatchers("/img/**", "/css/**", "/resources/**", "/static/**", "/static/css/**", "/static/js/**", "/static/img/**", "/video/**").permitAll()
//                .antMatchers("/css/**", "/js/**", "/img/**", "/video/**").permitAll()
//                .antMatchers("/account/**").hasAnyAuthority("CUSTOMER", "ADMIN")
//                .and()
//                //Setting HTTPS for my account
//                .requiresChannel().anyRequest().requiresSecure()
//                .and()
//
//                //Login configurations
//                .formLogin().defaultSuccessUrl("/account/home")
//                .loginPage("/authentication/login")
//                .failureUrl("/login?error=true")
//                //logout configurations
//                .and()
//                .logout()
//                .logoutSuccessUrl("/index");
//
//
//    }

}