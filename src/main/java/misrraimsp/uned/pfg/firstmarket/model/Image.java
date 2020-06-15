package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Image extends BasicEntity {

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
