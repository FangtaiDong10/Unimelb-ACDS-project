package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "kv_reference_dosimetry")
public class KvReferenceDosimetry {
    @Id
    @Column(name = "audit_id")
    private String auditId;

    private String protocol;

    @Column(name = "reference_chamber")
    private String referenceChamber;

    @Column(name = "air_phantom")
    private String airPhantom;

    @Column(name = "reference_depth")
    private String referenceDepth;

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
     * @return reference_chamber
     */
    public String getReferenceChamber() {
        return referenceChamber;
    }

    /**
     * @param referenceChamber
     */
    public void setReferenceChamber(String referenceChamber) {
        this.referenceChamber = referenceChamber;
    }

    /**
     * @return air_phantom
     */
    public String getAirPhantom() {
        return airPhantom;
    }

    /**
     * @param airPhantom
     */
    public void setAirPhantom(String airPhantom) {
        this.airPhantom = airPhantom;
    }

    /**
     * @return reference_depth
     */
    public String getReferenceDepth() {
        return referenceDepth;
    }

    /**
     * @param referenceDepth
     */
    public void setReferenceDepth(String referenceDepth) {
        this.referenceDepth = referenceDepth;
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