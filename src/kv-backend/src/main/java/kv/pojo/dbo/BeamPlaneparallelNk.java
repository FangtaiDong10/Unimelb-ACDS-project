package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "beam_planeparallel_nk")
public class BeamPlaneparallelNk {
    @Column(name = "beam_pp_chamber_id")
    private String beamPpChamberId;

    @Column(name = "nk_value")
    private Double nkValue;

    @Column(name = "date_updated")
    private Date dateUpdated;

    /**
     * @return beam_pp_chamber_id
     */
    public String getBeamPpChamberId() {
        return beamPpChamberId;
    }

    /**
     * @param beamPpChamberId
     */
    public void setBeamPpChamberId(String beamPpChamberId) {
        this.beamPpChamberId = beamPpChamberId;
    }

    /**
     * @return nk_value
     */
    public Double getNkValue() {
        return nkValue;
    }

    /**
     * @param nkValue
     */
    public void setNkValue(Double nkValue) {
        this.nkValue = nkValue;
    }

    /**
     * @return date_updated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * @param dateUpdated
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}