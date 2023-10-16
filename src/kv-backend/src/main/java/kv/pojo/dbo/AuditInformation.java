package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "audit_information")
public class AuditInformation {
    @Id
    @Column(name = "audit_id")
    private String auditId;

    @Column(name = "facility_id")
    private String facilityId;

    @Column(name = "machine_id")
    private String machineId;

    private Date date;

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
     * @return facility_id
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     * @param facilityId
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * @return machine_id
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }
}