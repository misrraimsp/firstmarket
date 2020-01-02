package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatPathId implements Serializable {

    private String ancestor;
    private String descendant;
}
