package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "pstem_measured")
public class PstemMeasured {
    @Column(name = "beam_pp_chamber_id")
    private String beamPpChamberId;

    @Column(name = "pstem_option")
    private String pstemOption;

    private Double diameter;

    @Column(name = "hvl_measured_mm_al")
    private Double hvlMeasuredMmAl;

    @Column(name = "pstem_value")
    private Double pstemValue;

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
     * @return pstem_option
     */
    public String getPstemOption() {
        return pstemOption;
    }

    /**
     * @param pstemOption
     */
    public void setPstemOption(String pstemOption) {
        this.pstemOption = pstemOption;
    }

    /**
     * @return diameter
     */
    public Double getDiameter() {
        return diameter;
    }

    /**
     * @param diameter
     */
    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return hvl_measured_mm_al
     */
    public Double getHvlMeasuredMmAl() {
        return hvlMeasuredMmAl;
    }

    /**
     * @param hvlMeasuredMmAl
     */
    public void setHvlMeasuredMmAl(Double hvlMeasuredMmAl) {
        this.hvlMeasuredMmAl = hvlMeasuredMmAl;
    }

    /**
     * @return pstem_value
     */
    public Double getPstemValue() {
        return pstemValue;
    }

    /**
     * @param pstemValue
     */
    public void setPstemValue(Double pstemValue) {
        this.pstemValue = pstemValue;
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