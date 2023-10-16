package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "audit_history")
public class AuditHistory {
    @Id
    @Column(name = "audit_id")
    private String auditId;

    private String status;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

    /**
     * @return audit_id
     */
    public String getAuditId() {
        return auditId;
    }

    /**
     * @param auditId
     */
    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return last_updated_date
     */
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * @param lastUpdatedDate
     */
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}