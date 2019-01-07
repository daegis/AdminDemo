package com.hnair.wallet.admincenter.configuration;

import com.hnair.wallet.admincenter.configuration.security.CaptchaAuthenticator;
import com.hnair.wallet.admincenter.configuration.security.RedirectAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/21/2018 9:09 PM
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义验证码授权器
        UsernamePasswordAuthenticationFilter filter = new CaptchaAuthenticator();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        filter.setAuthenticationSuccessHandler(new RedirectAuthenticationSuccessHandler("/"));
        http
                .csrf().ignoringAntMatchers("/merchant/**", "/consume/**", "/operator/**").and()
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                // 替换默认拦截器，增加验证码拦截器
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 放行静态资源
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/lib/**", "/captcha/**");
    }


}
