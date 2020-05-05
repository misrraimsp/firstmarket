package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.AuthorRepository;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorServer {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServer(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author persist(Author author) {
        author.setFirstName(author.getFirstName().toUpperCase());
        author.setLastName(author.getLastName().toUpperCase());
        Author storedAuthor = this.findByFirstNameAndLastName(author.getFirstName(), author.getLastName());
        return (storedAuthor != null) ? storedAuthor : authorRepository.save(author);
    }

    public Author findByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Set<Author> persist(Set<Author> authors) {
        Set<Author> savedAuthors = new HashSet<>();
        authors.forEach(author -> savedAuthors.add(this.persist(author)));
        return savedAuthors;
    }

    public Set<Author> findTopAuthorsByCategoryId(Long categoryId, int numTopAuthors) {
        Set<Long> authorIds = authorRepository.findTopIdsByCategoryId(categoryId, numTopAuthors);
        return (authorIds.isEmpty()) ? new HashSet<>() : authorRepository.findAuthorsByIds(authorIds);
    }

}
