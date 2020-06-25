package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PublisherRepository;
import misrraimsp.uned.pfg.firstmarket.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import misrraimsp.uned.pfg.firstmarket.model.projection.PublisherView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServer {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherServer(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher findById(Long publisherId) {
        return publisherRepository.findById(publisherId).orElseThrow(() ->
                new EntityNotFoundByIdException(publisherId,Publisher.class.getSimpleName()));
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

    public List<PublisherView> findTopPublisherViewsByCategoryId(Long categoryId, int numTopPublishers) {
        return publisherRepository.findTopPublisherViewsByCategoryId(categoryId,numTopPublishers);
    }

}
