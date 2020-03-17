package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.AuthorRepository;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Author> persist(List<Author> authors) {
        List<Author> savedAuthors = new ArrayList<>();
        authors.forEach(author -> savedAuthors.add(this.persist(author)));
        return savedAuthors;
    }

    /*
    public List<Author> findAll() {
        return (List<Author>) authorRepository.findAll();
    }
     */

    public List<Author> findTopAuthorsByBookIds(List<Long> bookIds, int numTopAuthors) {
        List<Long> authorIds = authorRepository.findTopIdsByBookIds(bookIds, numTopAuthors);
        return authorRepository.findAuthorsByIds(authorIds);
    }

}
