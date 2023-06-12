package m4Final.liquibaseSourse;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.Scope;
import liquibase.database.jvm.JdbcConnection;
import liquibase.Liquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import m4Final.liquibaseSourse.dataSourse.ConnectionData;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Validator {

    public void connectDB() throws SQLException {

        Map<String, Object> config = new HashMap<>();

        Connection connection = ConnectionData.getConnection();

        try (connection) {
            Scope.child(config, () ->
            {

                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(connection));

                Liquibase liquibase = new liquibase
                        .Liquibase("liquibase/changelog.xml", new ClassLoaderResourceAccessor(), database);

                liquibase.update(new Contexts(), new LabelExpression());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
