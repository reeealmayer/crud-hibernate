package kz.shyngys.repository.impl;

import kz.shyngys.db.HibernateUtil;
import kz.shyngys.domain.Label;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.repository.LabelRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateLabelRepositoryImpl implements LabelRepository {

    @Override
    public Label getById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Label label = session.createQuery("select l from Label l where l.id = :id", Label.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (label == null) {
                throw new NotFoundException("Label не найден с id: " + id);
            }
            return label;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении Label по ид " + id + " " + e);
        }
    }

    @Override
    public List<Label> getAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("select l from Label l order by l.id", Label.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при всех Label");
        }
    }

    @Override
    public Label save(Label label) {
        Transaction transaction;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(label);
            transaction.commit();
            return label;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при сохранении Label " + e);
        }
    }

    @Override
    public Label update(Label label) {
        Transaction transaction;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(label);
            transaction.commit();
            return label;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при обновлении Label " + e);
        }
    }

    @Override
    public void deleteById(Label label) {
        if (label == null || label.getId() == null) {
            throw new IllegalArgumentException("Невозможно удалить Label без id");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from Label l where l.id = :id")
                    .setParameter("id", label.getId())
                    .executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при удалении Label: " + e);
        }
    }
}
