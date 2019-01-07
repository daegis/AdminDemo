package com.hnair.wallet.admincenter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 7/24/2018 10:05 AM
 */
@Configuration
public class ThymeleafConfiguration {

    @Autowired
    private ITemplateResolver templateResolver;

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new SpringSecurityDialect());
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver);
        return engine;
    }
}
