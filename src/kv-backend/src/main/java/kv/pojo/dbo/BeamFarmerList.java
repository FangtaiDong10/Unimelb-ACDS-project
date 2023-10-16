package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "beam_farmer_list")
public class BeamFarmerList {
    @Id
    @Column(name = "beam_farmer_id")
    private String beamFarmerId;

    @Column(name = "kV")
    private Double kv;

    @Column(name = "hvl_measured_mm_cu")
    private Double hvlMeasuredMmCu;

    @Column(name = "hvl_measured_mm_al")
    private Double hvlMeasuredMmAl;

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
     * @return hvl_measured_mm_cu
     */
    public Double getHvlMeasuredMmCu() {
        return hvlMeasuredMmCu;
    }

    /**
     * @param hvlMeasuredMmCu
     */
    public void setHvlMeasuredMmCu(Double hvlMeasuredMmCu) {
        this.hvlMeasuredMmCu = hvlMeasuredMmCu;
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