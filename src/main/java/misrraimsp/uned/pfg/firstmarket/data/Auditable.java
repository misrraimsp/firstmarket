package misrraimsp.uned.pfg.firstmarket.data;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedBy
    protected String lastModifiedBy;

    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

    @Transient
    private final String datePattern = "yyyy-MM-dd";

    @Transient
    private final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public String getFormattedCreatedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern(datePattern));
    }

    public String getFormattedCreatedDateTime() {
        return createdDate.format(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    public String getFormattedLastModifiedDate() {
        return lastModifiedDate.format(DateTimeFormatter.ofPattern(datePattern));
    }

    public String getFormattedLastModifiedDateTime() {
        return lastModifiedDate.format(DateTimeFormatter.ofPattern(dateTimePattern));
    }
}
