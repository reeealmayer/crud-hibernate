package kz.shyngys;

import kz.shyngys.controller.WriterController;
import kz.shyngys.repository.WriterRepository;
import kz.shyngys.repository.impl.HibernateWriterRepositoryImpl;
import kz.shyngys.service.WriterService;
import kz.shyngys.service.impl.WriterServiceImpl;
import kz.shyngys.view.WriterView;

public class Main {
    public static void main(String[] args) {
        WriterRepository writerRepository = new HibernateWriterRepositoryImpl();
        WriterService writerService = new WriterServiceImpl(writerRepository);
        WriterController writerController = new WriterController(writerService);
        WriterView writerView = new WriterView(writerController);
        writerView.showMenu();
    }
}
