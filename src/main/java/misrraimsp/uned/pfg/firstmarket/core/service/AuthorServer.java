package misrraimsp.uned.pfg.firstmarket.core.service;

import misrraimsp.uned.pfg.firstmarket.core.data.AuthorRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Author;
import misrraimsp.uned.pfg.firstmarket.core.model.projection.AuthorView;
import misrraimsp.uned.pfg.firstmarket.util.exception.EntityNotFoundByIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorServer {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServer(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findById(Long authorId) throws EntityNotFoundByIdException {
        return authorRepository.findById(authorId).orElseThrow(() ->
                new EntityNotFoundByIdException(authorId,Author.class.getSimpleName())
        );
    }

    public Author persist(Author author) {
        LOGGER.debug("Trying to persist this author(id={}, firstname={}, lastname={})",
                author.getId(),
                author.getFirstName(),
                author.getLastName());
        Author storedAuthor = this.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        if (storedAuthor != null) {
            LOGGER.debug("But author already exists(id={}, firstname={}, lastname={})",
                    storedAuthor.getId(),
                    storedAuthor.getFirstName(),
                    storedAuthor.getLastName());
            return storedAuthor;
        }
        else {
            Author persistedAuthor = authorRepository.save(author);
            LOGGER.debug("And author persisted(id={}, firstname={}, lastname={}) successfully",
                    persistedAuthor.getId(),
                    persistedAuthor.getFirstName(),
                    persistedAuthor.getLastName());
            return persistedAuthor;
        }
    }

    public Author findByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Set<Author> persist(Set<Author> authors) {
        Set<Author> savedAuthors = new HashSet<>();
        authors.forEach(author -> savedAuthors.add(this.persist(author)));
        return savedAuthors;
    }

    public List<AuthorView> findTopAuthorViewsByCategoryId(Long categoryId, int numTopAuthors) {
        return authorRepository.findTopAuthorViewsByCategoryId(categoryId,numTopAuthors);
    }

}
