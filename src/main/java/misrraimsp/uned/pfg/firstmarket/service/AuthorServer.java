package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.data.AuthorRepository;
import misrraimsp.uned.pfg.firstmarket.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServer {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServer(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author persist(Author author) {
        return authorRepository.save(author);
    }

    public Author findByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
