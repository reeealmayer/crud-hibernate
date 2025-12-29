package kz.shyngys;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConnectionTest {
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        
        try {
            // Создаём SessionFactory
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
            
            System.out.println("✓ SessionFactory создана успешно!");
            
            // Открываем сессию
            session = sessionFactory.openSession();
            System.out.println("✓ Session открыта успешно!");
            
            // Выполняем простой SQL запрос для проверки подключения
            session.doWork(connection -> {
                System.out.println("✓ Подключение к БД установлено!");
                System.out.println("Database: " + connection.getCatalog());
                System.out.println("URL: " + connection.getMetaData().getURL());
            });
            
            System.out.println("\n✓✓✓ Hibernate успешно подключился к MySQL! ✓✓✓");
            
        } catch (Exception e) {
            System.err.println("✗ Ошибка подключения:");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}