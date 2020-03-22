package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import javax.persistence.*;

@Data
@Entity
@ValidImage(message = "{image.error}")
public class Image implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isDefault;

    private String name;

    private String mimeType;

    @Lob
    private byte[] data;
}
