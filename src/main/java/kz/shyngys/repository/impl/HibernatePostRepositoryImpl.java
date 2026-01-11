package kz.shyngys.repository.impl;


import kz.shyngys.db.HibernateUtil;
import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
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
            Post post = session.createQuery("select distinct p from Post p left join fetch p.labels where p.id = :id", Post.class)
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
            List<Post> posts = session.createQuery(
                    "select p from Post p order by p.id",
                    Post.class
            ).getResultList();

            posts.forEach(p -> p.getLabels().size());

            return posts;
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
            session.createQuery("delete from Post p where p.id = :id")
                    .setParameter("id", post.getId())
                    .executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении удалении Post: " + e);
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


//    private Long savePost(Post post) throws SQLException {
//        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_POST)) {
//            ps.setString(1, post.getContent());
//            ps.setString(2, post.getStatus().name());
//            ps.setLong(3, post.getWriter().getId());
//
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Создание Post не удалось, ни одна строка не добавлена");
//            }
//
//            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    return generatedKeys.getLong(1);
//                }
//                throw new SQLException("Создание Post не удалось, id не получен");
//            }
//        }
//    }
//
//    private void updatePost(Post post) throws SQLException {
//        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_UPDATE_POST)) {
//            ps.setString(1, post.getContent());
//            ps.setString(2, post.getStatus().name());
//            ps.setLong(3, post.getWriter().getId());
//            ps.setLong(4, post.getId());
//
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows == 0) {
//                throw new NotFoundException("Post с id " + post.getId() + " не найден для обновления");
//            }
//        }
//    }
//
//    private void savePostLabels(Long postId, List<Label> labels) throws SQLException {
//        for (Label label : labels) {
//            Long labelId = getOrCreateLabel(label.getName());
//            label.setId(labelId);
//            linkPostLabel(postId, labelId);
//        }
//    }
//
//    private void deletePostLabels(Long postId) throws SQLException {
//        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_DELETE_POST_LABELS)) {
//            ps.setLong(1, postId);
//            ps.executeUpdate();
//        }
//    }
//
//    private Long getOrCreateLabel(String name) throws SQLException {
//        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_GET_LABEL_BY_NAME)) {
//            ps.setString(1, name);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getLong("id");
//                }
//            }
//        }
//
//        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatementWithGeneratedKeys(SQL_INSERT_LABEL)) {
//            ps.setString(1, name);
//
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Создание Label не удалось");
//            }
//
//            try (ResultSet keys = ps.getGeneratedKeys()) {
//                if (keys.next()) {
//                    return keys.getLong(1);
//                }
//                throw new SQLException("Не удалось получить ID label");
//            }
//        }
//    }
//
//    private void linkPostLabel(Long postId, Long labelId) throws SQLException {
//        try (PreparedStatement ps = DatabaseUtils.getTransactionPreparedStatement(SQL_INSERT_POST_LABEL)) {
//            ps.setLong(1, postId);
//            ps.setLong(2, labelId);
//
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Не удалось связать Post и Label");
//            }
//        }
//    }
}