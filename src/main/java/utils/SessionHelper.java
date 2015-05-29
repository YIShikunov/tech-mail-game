package utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionHelper {

    private static SessionFactory sessionFactory;

    private static boolean testingMode = false;

    public static SessionFactory createSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        Configuration configuration = new Configuration();
        configuration.addResource("hibernate.cfg.xml");
        if (testingMode)
            configuration.setProperty("connection.url", configuration.getProperty("connection.url") + "test");
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    public static void enableTestingMode() {
        testingMode = true;
    }
}
