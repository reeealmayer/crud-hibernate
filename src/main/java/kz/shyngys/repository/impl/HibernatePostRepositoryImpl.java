package kz.shyngys.repository.impl;


import kz.shyngys.db.HibernateUtil;
import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Status;
import kz.shyngys.exception.NotFoundException;
import kz.shyngys.repository.PostRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class HibernatePostRepositoryImpl implements PostRepository {

    @Override
    public Post getById(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Post post = session.createQuery("select p from Post p left join fetch p.labels where p.id = :id", Post.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (post == null) {
                throw new NotFoundException("Post не найден с id: " + id);
            }
            return post;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении Post по ид " + id + " " + e);
        }
    }

    @Override
    public List<Post> getAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery(
                    "select distinct p from Post p " +
                            "left join fetch p.labels",
                    Post.class
            ).getResultList();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при получении всех Post: " + e);
        }
    }

    @Override
    public Post save(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(post);
            transaction.commit();
            return post;
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при сохранении Post " + e);
        }
    }

    @Override
    public Post update(Post post) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            Post persistentPost = session.get(Post.class, post.getId());
            if (persistentPost == null) {
                throw new NotFoundException("Post не найден с id: " + post.getId());
            }

            persistentPost.setContent(post.getContent());
            persistentPost.setStatus(post.getStatus());

            if (post.getWriter() != null) {
                persistentPost.setWriter(post.getWriter());
            }

            syncLabels(persistentPost, post.getLabels(), session);

            transaction.commit();
            return persistentPost;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении Post", e);
        }
    }

    @Override
    public void deleteById(Post post) {
        if (post == null || post.getId() == null) {
            throw new IllegalArgumentException("Невозможно удалить Post без id");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            Post persistentPost = session.get(Post.class, post.getId());
            if (persistentPost == null) {
                throw new NotFoundException("Post не найден с ид: " + post.getId());
            }
            persistentPost.setStatus(Status.DELETED);
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException("Ошибка при удалении Post: " + e);
        }
    }

    private void syncLabels(Post persistentPost, Set<Label> newLabels, Session session) {

        for (Label label : newLabels) {

            Label managedLabel;

            if (label.getId() != null) {
                // label уже существует
                managedLabel = session.get(Label.class, label.getId());
                if (managedLabel == null) {
                    throw new IllegalStateException(
                            "Label с id " + label.getId() + " не найден"
                    );
                }
            } else {
                // ищем по name
                Label existingByName = findLabelByName(session, label.getName());

                if (existingByName != null) {
                    managedLabel = existingByName;
                } else {
                    session.persist(label);
                    managedLabel = label;
                }
            }

            // add-only логика
            if (!persistentPost.getLabels().contains(managedLabel)) {
                persistentPost.addLabel(managedLabel);
            }
        }
    }

    private Label findLabelByName(Session session, String name) {
        return session.createQuery(
                        "select l from Label l where l.name = :name",
                        Label.class
                )
                .setParameter("name", name)
                .uniqueResult();
    }
}