package ru.hh.performance_review.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hh.nab.hibernate.NabHibernateProdConfig;
import ru.hh.nab.starter.NabProdConfig;

@Configuration
@Import({
        NabHibernateProdConfig.class,
        NabProdConfig.class,
        CommonConfig.class,
        LiquibaseConfig.class,
        CustomSecurityConfiguration.class
})
public class ProdConfig {

}
