package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "beam_data")
public class BeamData {
    @Id
    @Column(name = "beam_id")
    private String beamId;

    @Column(name = "reference_cone_id")
    private String referenceConeId;

    @Column(name = "beam_id_alt")
    private String beamIdAlt;

    @Column(name = "nom_energy")
    private Double nomEnergy;

    private String scd;

    @Column(name = "field_size_at_scd")
    private String fieldSizeAtScd;

    @Column(name = "hvl_nominal_mm_al")
    private Double hvlNominalMmAl;

    @Column(name = "hvl_nominal_mm_cu")
    private Double hvlNominalMmCu;

    @Column(name = "hvl_measured_mm_al")
    private Double hvlMeasuredMmAl;

    @Column(name = "hvl_measured_mm_cu")
    private Double hvlMeasuredMmCu;

    /**
     * @return beam_id
     */
    public String getBeamId() {
        return beamId;
    }

    /**
     * @param beamId
     */
    public void setBeamId(String beamId) {
        this.beamId = beamId;
    }

    /**
     * @return reference_cone_id
     */
    public String getReferenceConeId() {
        return referenceConeId;
    }

    /**
     * @param referenceConeId
     */
    public void setReferenceConeId(String referenceConeId) {
        this.referenceConeId = referenceConeId;
    }

    /**
     * @return beam_id_alt
     */
    public String getBeamIdAlt() {
        return beamIdAlt;
    }

    /**
     * @param beamIdAlt
     */
    public void setBeamIdAlt(String beamIdAlt) {
        this.beamIdAlt = beamIdAlt;
    }

    /**
     * @return nom_energy
     */
    public Double getNomEnergy() {
        return nomEnergy;
    }

    /**
     * @param nomEnergy
     */
    public void setNomEnergy(Double nomEnergy) {
        this.nomEnergy = nomEnergy;
    }

    /**
     * @return scd
     */
    public String getScd() {
        return scd;
    }

    /**
     * @param scd
     */
    public void setScd(String scd) {
        this.scd = scd;
    }

    /**
     * @return field_size_at_scd
     */
    public String getFieldSizeAtScd() {
        return fieldSizeAtScd;
    }

    /**
     * @param fieldSizeAtScd
     */
    public void setFieldSizeAtScd(String fieldSizeAtScd) {
        this.fieldSizeAtScd = fieldSizeAtScd;
    }

    /**
     * @return hvl_nominal_mm_al
     */
    public Double getHvlNominalMmAl() {
        return hvlNominalMmAl;
    }

    /**
     * @param hvlNominalMmAl
     */
    public void setHvlNominalMmAl(Double hvlNominalMmAl) {
        this.hvlNominalMmAl = hvlNominalMmAl;
    }

    /**
     * @return hvl_nominal_mm_cu
     */
    public Double getHvlNominalMmCu() {
        return hvlNominalMmCu;
    }

    /**
     * @param hvlNominalMmCu
     */
    public void setHvlNominalMmCu(Double hvlNominalMmCu) {
        this.hvlNominalMmCu = hvlNominalMmCu;
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
}