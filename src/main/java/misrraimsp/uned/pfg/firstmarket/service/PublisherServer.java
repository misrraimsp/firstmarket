package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PublisherRepository;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServer {

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherServer(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher persist(Publisher publisher) {
        Publisher storedPublisher = this.findByName(publisher.getName());
        return (storedPublisher != null) ? storedPublisher : publisherRepository.save(publisher);
    }

    public Publisher findByName(String publisherName) {
        return publisherRepository.findByName(publisherName);
    }

    /*
    public List<Publisher> findAll() {
        return (List<Publisher>) publisherRepository.findAll();
    }
     */

    public List<Publisher> findTopPublishersByBookIds(List<Long> bookIds, int numTopPublishers) {
        List<Long> publisherIds = publisherRepository.findTopIdsByBookIds(bookIds, numTopPublishers);
        return publisherRepository.findPublishersByIds(publisherIds);
    }
}
