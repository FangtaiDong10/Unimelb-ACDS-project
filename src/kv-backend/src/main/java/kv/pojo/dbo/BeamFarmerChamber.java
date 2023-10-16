package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "beam_farmer_chamber")
public class BeamFarmerChamber {
    @Column(name = "beam_farmer_id")
    private String beamFarmerId;

    @Column(name = "chamber_SN")
    private String chamberSn;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "nk_value")
    private Double nkValue;

    /**
     * @return beam_farmer_id
     */
    public String getBeamFarmerId() {
        return beamFarmerId;
    }

    /**
     * @param beamFarmerId
     */
    public void setBeamFarmerId(String beamFarmerId) {
        this.beamFarmerId = beamFarmerId;
    }

    /**
     * @return chamber_SN
     */
    public String getChamberSn() {
        return chamberSn;
    }

    /**
     * @param chamberSn
     */
    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
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
}