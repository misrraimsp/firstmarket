package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PublisherRepository;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PublisherServer {

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherServer(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher persist(Publisher publisher) {
        if (publisher == null) {
            return null;
        }
        Publisher storedPublisher = this.findByName(publisher.getName());
        return (storedPublisher != null) ? storedPublisher : publisherRepository.save(publisher);
    }

    public Publisher findByName(String publisherName) {
        return publisherRepository.findByName(publisherName);
    }

    public Set<Publisher> findTopPublishersByCategoryId(Long categoryId, int numTopPublishers) {
        Set<Long> publisherIds = publisherRepository.findTopIdsByCategoryId(categoryId, numTopPublishers);
        return (publisherIds.isEmpty()) ? new HashSet<>() : publisherRepository.findPublishersByIds(publisherIds);
    }
}
