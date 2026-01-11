package kz.shyngys.repository.impl;


import kz.shyngys.db.HibernateUtil;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Writer;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.repository.WriterRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class HibernateWriterRepositoryImpl implements WriterRepository {

    @Override
    public Writer getById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Writer writer = session.createQuery("select distinct w from Writer w " +
                            "left join fetch w.posts p " +
                            "left join fetch p.labels " +
                                    "where w.id = :id"
                            , Writer.class)
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

            Writer existingWriter = session.get(Writer.class, writer.getId());

            if (existingWriter == null) {
                throw new NotFoundException("Writer не найден с id: " + writer.getId());
            }

            existingWriter.setFirstName(writer.getFirstName());
            existingWriter.setLastName(writer.getLastName());

            updatePosts(existingWriter, writer.getPosts());

            transaction.commit();
            return getById(writer.getId());
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении Writer: " + e);
        }
    }

    private void updatePosts(Writer existingWriter, List<Post> newPosts) {
        if (newPosts == null) {
            return;  // Не обновляем, если null
        }

        // 1. Удаляем посты, которых нет в новом списке
        List<Post> postsToRemove = new ArrayList<>();
        for (Post existingPost : existingWriter.getPosts()) {
            boolean found = newPosts.stream()
                    .anyMatch(p -> p.getId() != null && p.getId().equals(existingPost.getId()));
            if (!found) {
                postsToRemove.add(existingPost);
            }
        }
        postsToRemove.forEach(existingWriter::removePost);

        // 2. Добавляем или обновляем посты из нового списка
        for (Post newPost : newPosts) {
            if (newPost.getId() == null) {
                // Новый пост - добавляем
                existingWriter.addPost(newPost);
            } else {
                // Существующий пост - обновляем поля
                Post existingPost = existingWriter.getPosts().stream()
                        .filter(p -> p.getId().equals(newPost.getId()))
                        .findFirst()
                        .orElse(null);

                if (existingPost != null) {
                    existingPost.setContent(newPost.getContent());
                    existingPost.setStatus(newPost.getStatus());
                    // Обновите другие поля при необходимости
                }
            }
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
            session.createQuery("delete from Writer w where w.id = :id")
                    .setParameter("id", writer.getId())
                    .executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении удалении Writer: " + e);
        }
    }
}