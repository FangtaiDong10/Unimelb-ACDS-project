package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "auditor_assignment")
public class AuditorAssignment {
    @Column(name = "auditor_id")
    private String auditorId;

    @Column(name = "audit_id")
    private String auditId;

    /**
     * @return auditor_id
     */
    public String getAuditorId() {
        return auditorId;
    }

    /**
     * @param auditorId
     */
    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

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
}