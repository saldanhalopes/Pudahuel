package cl.euro.pudahuel.columnas.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("cl.euro.pudahuel.columnas.domain")
@EnableJpaRepositories("cl.euro.pudahuel.columnas.repos")
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableEnversRepositories
@EnableWebSecurity
@EnableMethodSecurity
public class DomainConfig {

}
