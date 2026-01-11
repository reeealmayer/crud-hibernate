package kz.shyngys;

import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Status;
import kz.shyngys.domain.Writer;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.HibernateWriterRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        WriterRepository writerRepository = new HibernateWriterRepositoryImpl();
//        Writer writer = new Writer();
////        writer.setId(1L);
//        writer.setFirstName("Shyngys");
//        writer.setLastName("Lee");
//        Post post = new Post();
////        post.setId(2L);
//        post.setStatus(Status.ACTIVE);
//        post.setContent("asd post");
//        writer.addPost(post);
//        Label label = new Label();
//        label.setName("Первый лэбел");
//        post.addLabel(label);
//
//        Post post2 = new Post();
////        post2.setId(3L);
//        post2.setStatus(Status.UNDER_REVIEW);
//        post2.setContent("zxc post");
//        writer.addPost(post2);
//        System.out.println(writerRepository.save(writer));
//        System.out.println(writerRepository.getById(writer.getId()));
//        Writer byId = writerRepository.getById(1L);
//        System.out.println(byId);
//        System.out.println(byId.getPosts());
        Writer writer = new Writer();
        writer.setId(2L);
        writerRepository.deleteById(writer);
    }
}
