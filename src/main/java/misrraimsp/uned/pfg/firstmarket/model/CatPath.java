package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity
@IdClass(CatPathId.class)
public class CatPath {

	@Id
	private String ancestor;

	@Id
	private String descendant;

	private int path_length;

}
