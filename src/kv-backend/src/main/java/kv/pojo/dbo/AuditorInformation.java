package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "auditor_information")
public class AuditorInformation {
    @Id
    @Column(name = "auditor_id")
    private String auditorId;

    @Column(name = "auditor_name")
    private String auditorName;

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
     * @return auditor_name
     */
    public String getAuditorName() {
        return auditorName;
    }

    /**
     * @param auditorName
     */
    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
}