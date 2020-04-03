package misrraimsp.uned.pfg.firstmarket.adt;

import lombok.Data;

@Data
public class MailMessage {

    private String to;
    private String subject;
    private String text;
}
