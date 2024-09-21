package quickbit.core.model;

public class SchemaLocation {
    private final String location;
    private final  String tableName;

    public SchemaLocation() {
        this.location = "classpath:db/migration";
        this.tableName = "flyway_schema_history";
    }

    public String getLocation() {
        return location;
    }

    public String getTableName() {
        return tableName;
    }
}
