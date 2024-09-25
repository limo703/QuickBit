package quickbit.core.configuration;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String driverClassName;

    public DataSourceConfig(
        @Value("${spring.datasource.url}") String dbUrl,
        @Value("${spring.datasource.username}") String dbUsername,
        @Value("${spring.datasource.password}") String dbPassword,
        @Value("${spring.datasource.driver-class-name}") String driverClassName
    ) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.driverClassName = driverClassName;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
            .create()
            .url(dbUrl)
            .username(dbUsername)
            .password(dbPassword)
            .driverClassName(driverClassName)
            .type(PGSimpleDataSource.class)
            .build();
    }
}
