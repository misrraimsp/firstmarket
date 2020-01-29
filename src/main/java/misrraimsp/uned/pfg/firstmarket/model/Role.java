package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import java.util.ArrayList;
//import java.util.List;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    //@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    //private List<User> users = new ArrayList<>();

}
