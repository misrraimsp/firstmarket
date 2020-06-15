package misrraimsp.uned.pfg.firstmarket.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MailClient {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;
    private final MailProperties mailProperties;

    @Autowired
    public MailClient(JavaMailSender javaMailSender,
                      MailContentBuilder mailContentBuilder,
                      MailProperties mailProperties) {

        this.javaMailSender = javaMailSender;
        this.mailContentBuilder = mailContentBuilder;
        this.mailProperties = mailProperties;
    }

    public void prepareAndSend(String template, Map<String,Object> properties, String recipient, String subject) {
        MimeMessagePreparator mimeMessagePreparator = prepare(template,properties,recipient,subject);
        try {
            javaMailSender.send(mimeMessagePreparator);
            LOGGER.debug("Email successfully sent (to:{}, template:{})", recipient, template);
        } catch (MailException e) {
            LOGGER.warn("Error while trying to send email (to:{}, template:{}). Retry again", recipient, template, e);
            this.retryPrepareAndSend(template,properties,recipient,subject);
        }
    }

    private void retryPrepareAndSend(String template, Map<String,Object> properties, String recipient, String subject) {
        MimeMessagePreparator mimeMessagePreparator = prepare(template,properties,recipient,subject);
        try {
            javaMailSender.send(mimeMessagePreparator);
            LOGGER.debug("Retry sending success (to:{}, template:{})", recipient, template);
        } catch (MailException e) {
            LOGGER.error("Error while retrying to send email (to:{}, template:{})", recipient, template, e);
        }
    }

    private MimeMessagePreparator prepare(String template, Map<String,Object> properties, String recipient, String subject){
        return mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(mailContentBuilder.build(template, properties), true);
        };
    }

}
