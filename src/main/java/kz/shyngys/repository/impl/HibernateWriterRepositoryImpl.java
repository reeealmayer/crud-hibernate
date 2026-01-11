package kz.shyngys.repository.impl;


import kz.shyngys.db.HibernateUtil;
import kz.shyngys.domain.Writer;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.repository.WriterRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateWriterRepositoryImpl implements WriterRepository {

    @Override
    public Writer getById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Writer writer = session.createQuery("select w from Writer w left join fetch w.posts where w.id = :id", Writer.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (writer == null) {
                throw new NotFoundException("Writer не найден с id: " + id);
            }
            return writer;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении Writer по ид " + id + " " + e);
        }
    }

    @Override
    public List<Writer> getAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("select w from Writer w order by w.id", Writer.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении всех Writer " + e);
        }
    }

    @Override
    public Writer save(Writer writer) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(writer);
            transaction.commit();
            return writer;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при сохранении Writer: " + e);
        }
    }

    @Override
    public Writer update(Writer writer) {
        if (writer.getId() == null) {
            throw new IllegalArgumentException("Невозможно обновить Writer без id");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            Writer updatedWriter = session.merge(writer);
            transaction.commit();
            return updatedWriter;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении Writer: " + e);
        }
    }

    @Override
    public void deleteById(Writer writer) {
        if (writer == null || writer.getId() == null) {
            throw new IllegalArgumentException("Невозможно удалить Writer без id");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            Writer managedWriter = session.merge(writer);
            session.remove(managedWriter);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении удалении Writer: " + e);
        }
    }
}