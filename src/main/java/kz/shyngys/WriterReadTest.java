package kz.shyngys;

import kz.shyngys.domain.Writer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.List;

public class WriterReadTest {
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            sessionFactory = configuration.buildSessionFactory();
            
            System.out.println("=== SessionFactory создана успешно ===\n");
            
            // 3. Чтение всех Writers
            System.out.println("\n=== 2. Чтение всех Writers ===");
            readAllWriters(sessionFactory);
            

        } catch (Exception e) {
            System.err.println("✗ Ошибка:");
            e.printStackTrace();
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
    

    // Чтение всех Writers
    private static void readAllWriters(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        
        try {
            List<Writer> writers = session.createQuery("FROM Writer", Writer.class).list();
            
            System.out.println("✓ Найдено writers: " + writers.size());
            for (Writer writer : writers) {
                System.out.println(writer);
            }
            
        } finally {
            session.close();
        }
    }
    

}