package quickbit.configuration;

import quickbit.core.model.SchemaLocation;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

@Configuration
public class FlywaySlaveInitializer {

    private final DataSource dataSource;
    private final List<SchemaLocation> locations;

    @Autowired
    public FlywaySlaveInitializer(
        DataSource dataSources,
        List<SchemaLocation> locations
    ) {
        this.dataSource = dataSources;
        this.locations = locations;
    }

    @PostConstruct
    public void migrateFlyway() {
        locations.forEach(location -> {
            Flyway flyway = Flyway
                .configure()
                .dataSource(dataSource)
                .locations(location.getLocation())
                .table(location.getTableName())
                .baselineOnMigrate(true)
                .outOfOrder(true)
                .load();
            flyway.migrate();
        });
    }
}