package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "beam_planeparallel_chamber")
public class BeamPlaneparallelChamber {
    @Id
    @Column(name = "beam_pp_chamber_id")
    private String beamPpChamberId;

    @Column(name = "beam_planeparallel_id")
    private String beamPlaneparallelId;

    @Column(name = "chamber_SN")
    private String chamberSn;

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
}