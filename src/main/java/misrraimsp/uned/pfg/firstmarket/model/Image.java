package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;

import javax.persistence.*;

@Data
@Entity
public class Image implements Patterns {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isDefault;

    private String name;

    private String mimeType;

    @Lob
    private byte[] data;
}
