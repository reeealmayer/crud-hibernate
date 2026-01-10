package kz.shyngys;

import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.HibernateWriterRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        WriterRepository writerRepository = new HibernateWriterRepositoryImpl();
        System.out.println(writerRepository.getById(1L));
    }
}
