package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isDefault;

    private String name;

    private String mimeType;

    @Lob //dev-h2
    private byte[] data;
}
