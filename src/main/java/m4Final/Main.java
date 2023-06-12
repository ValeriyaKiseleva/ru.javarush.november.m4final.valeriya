package m4Final;

import m4Final.liquibaseSourse.Validator;
import m4Final.service.DBSessionFactory;
import m4Final.service.Service;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {

//        SessionFactory sessionFactory;
//        Properties properties = new Properties();
//        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
//        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/world");
//        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
//        properties.put(Environment.USER, "root");
//        properties.put(Environment.PASS, "root");
//        properties.put(Environment.HBM2DDL_AUTO, "validate");
//        sessionFactory = new Configuration()
//                .setProperties(properties)
//                .buildSessionFactory();


//        new DBSessionFactory().getSessionFactory();

        new Validator().connectDB();

        Service service = new Service();
        service.startService(service);

    }
}