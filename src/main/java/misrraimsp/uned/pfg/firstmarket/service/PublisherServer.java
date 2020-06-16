package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.PublisherRepository;
import misrraimsp.uned.pfg.firstmarket.exception.EntityNotFoundByIdException;
import misrraimsp.uned.pfg.firstmarket.model.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Publisher> findTopPublishersByCategoryId(Long categoryId, int numTopPublishers) {
        List<Publisher> publishers = new ArrayList<>();
        publisherRepository.findTopPublisherViewsByCategoryId(categoryId,numTopPublishers).forEach(publisherView -> {
            Publisher publisher = this.findById(publisherView.getId());
            publisher.setNumOfBooks(publisherView.getNumOfBooks());
            publishers.add(publisher);
        });
        return publishers;
    }
}
