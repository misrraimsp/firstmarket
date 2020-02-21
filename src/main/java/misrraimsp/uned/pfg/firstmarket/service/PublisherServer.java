package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PublisherRepository;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherServer {

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherServer(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher persist(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher findByName(String publisherName) {
        return publisherRepository.findByName(publisherName);
    }
}
