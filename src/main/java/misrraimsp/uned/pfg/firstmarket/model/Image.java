package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean isDefault;

    private String name;

    private String mimeType;

    //@Lob //dev-h2
    private byte[] data;

    private int size;

    public void setData(byte[] bytes) {
        data = bytes;
        size = bytes.length;
    }
}
