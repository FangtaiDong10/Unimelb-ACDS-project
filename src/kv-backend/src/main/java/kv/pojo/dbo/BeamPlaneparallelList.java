package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "beam_planeparallel_list")
public class BeamPlaneparallelList {
    @Id
    @Column(name = "beam_planeparallel_id")
    private String beamPlaneparallelId;

    @Column(name = "kV")
    private Double kv;

    @Column(name = "hvl_measured_mm_al")
    private Double hvlMeasuredMmAl;

    /**
     * @return beam_planeparallel_id
     */
    public String getBeamPlaneparallelId() {
        return beamPlaneparallelId;
    }

    /**
     * @param beamPlaneparallelId
     */
    public void setBeamPlaneparallelId(String beamPlaneparallelId) {
        this.beamPlaneparallelId = beamPlaneparallelId;
    }

    /**
     * @return kV
     */
    public Double getKv() {
        return kv;
    }

    /**
     * @param kv
     */
    public void setKv(Double kv) {
        this.kv = kv;
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
}