package kz.shyngys.db;

import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Writer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static Session session;

    private HibernateSessionFactoryUtil() {
    }

    public static Session getSession() {
        if (session == null) {
            try {
                session = getSessionFactory().openSession();
            } catch (Exception e) {
                throw new RuntimeException("Не удалось создать соединение с БД", e);
            }
        }
        return session;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Label.class);
                configuration.addAnnotatedClass(Post.class);
                configuration.addAnnotatedClass(Writer.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Не удалось создать SessionFactory!" + e);
            }
        }
        return sessionFactory;
    }
}