package kz.shyngys;

import kz.shyngys.domain.Developer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DeveloperRunner {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure().buildSessionFactory();

        DeveloperRunner developerRunner = new DeveloperRunner();

        System.out.println("Adding developer's records to DB");
        developerRunner.addDeveloper("frist", "first", "first", 1);
        developerRunner.addDeveloper("second", "second", "second", 2);

        developerRunner.updateDeveloper(1, 10);
        developerRunner.removeDeveloper(2);

        List<Developer> developers = developerRunner.listDevelopers();
        System.out.println("List of developers:");
        for (Developer developer : developers) {
            System.out.print(developer + " ");
        }

    }

    public void addDeveloper(String firstName, String lastName, String specialty, Integer experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = new Developer(firstName, lastName, specialty, experience);
        session.persist(developer);
        transaction.commit();
        session.close();
    }

    public List<Developer> listDevelopers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<Developer> developers = session.createQuery("FROM Developer").list();

        transaction.commit();
        session.close();
        return developers;
    }

    public void updateDeveloper(int developerId, int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = session.get(Developer.class, developerId);
        developer.setExperience(experience);
        session.merge(developer);
        transaction.commit();
        session.close();
    }

    public void removeDeveloper(int developerId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = session.get(Developer.class, developerId);
        session.remove(developer);
        transaction.commit();
        session.close();
    }
}
