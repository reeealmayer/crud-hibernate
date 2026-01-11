package kz.shyngys.db;

import org.flywaydb.core.Flyway;

public class MigrationRunner {
    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        DBProperties.URL,
                        DBProperties.USER,
                        DBProperties.PASSWORD
                )
                .locations(DBProperties.MIGRATION_LOCATION)
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
        System.out.println("Migration completed successfully!");
    }
}