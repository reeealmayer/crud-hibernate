package kz.shyngys;

import kz.shyngys.controller.PostController;
import kz.shyngys.controller.WriterController;
import kz.shyngys.domain.Label;
import kz.shyngys.domain.Post;
import kz.shyngys.domain.Status;
import kz.shyngys.repository.PostRepository;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.HibernatePostRepositoryImpl;
import kz.shyngys.repository.impl.HibernateWriterRepositoryImpl;
import kz.shyngys.service.PostService;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.PostServiceImpl;
import kz.shyngys.service.impl.WriterServiceImpl;
import kz.shyngys.util.PostMapper;
import kz.shyngys.view.PostView;
import kz.shyngys.view.WriterView;

public class Main {
    public static void main(String[] args) {
        PostRepository postRepository = new HibernatePostRepositoryImpl();
        PostService postService = new PostServiceImpl(postRepository);
        PostController postController = new PostController(postService);
        PostView postView = new PostView(postController);
        postView.showMenu();
    }
}
