package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "reference_dosimetry")
public class ReferenceDosimetry {
    @Id
    @Column(name = "audit_id")
    private String auditId;

    private String protocol;

    private String nk;

    private String comments;

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
     * @return protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return nk
     */
    public String getNk() {
        return nk;
    }

    /**
     * @param nk
     */
    public void setNk(String nk) {
        this.nk = nk;
    }

    /**
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}