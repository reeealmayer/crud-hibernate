package kz.shyngys.repository.impl;


import kz.shyngys.db.HibernateSessionFactoryUtil;
import kz.shyngys.domain.Writer;
import kz.shyngys.repository.WriterRepository;
import org.hibernate.Session;

import java.util.List;

public class HibernateWriterRepositoryImpl implements WriterRepository {

    @Override
    public Writer getById(Long id) {
        try (Session session = HibernateSessionFactoryUtil.getSession()) {
            return session.get(Writer.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении Writer по ид " + id + " " + e);
        }
    }

    @Override
    public List<Writer> getAll() {
        return List.of();
    }

    @Override
    public Writer save(Writer writer) {
        return null;
    }

    @Override
    public Writer update(Writer writer) {
        return null;
    }

    @Override
    public void deleteById(Writer writer) {

    }
}