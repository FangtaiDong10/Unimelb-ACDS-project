package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "audit_beam_inputs")
public class AuditBeamInputs {
    @Id
    @Column(name = "input_id")
    private String inputId;

    @Column(name = "audit_id")
    private String auditId;

    @Column(name = "cone_id")
    private String coneId;

    @Column(name = "beam_id")
    private String beamId;

    @Column(name = "dosp_ssd")
    private String dospSsd;

    @Column(name = "nom_dose_output")
    private Double nomDoseOutput;

    @Column(name = "dose_output_unit")
    private String doseOutputUnit;

    /**
     * @return input_id
     */
    public String getInputId() {
        return inputId;
    }

    /**
     * @param inputId
     */
    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

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
     * @return cone_id
     */
    public String getConeId() {
        return coneId;
    }

    /**
     * @param coneId
     */
    public void setConeId(String coneId) {
        this.coneId = coneId;
    }

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
     * @return dosp_ssd
     */
    public String getDospSsd() {
        return dospSsd;
    }

    /**
     * @param dospSsd
     */
    public void setDospSsd(String dospSsd) {
        this.dospSsd = dospSsd;
    }

    /**
     * @return nom_dose_output
     */
    public Double getNomDoseOutput() {
        return nomDoseOutput;
    }

    /**
     * @param nomDoseOutput
     */
    public void setNomDoseOutput(Double nomDoseOutput) {
        this.nomDoseOutput = nomDoseOutput;
    }

    /**
     * @return dose_output_unit
     */
    public String getDoseOutputUnit() {
        return doseOutputUnit;
    }

    /**
     * @param doseOutputUnit
     */
    public void setDoseOutputUnit(String doseOutputUnit) {
        this.doseOutputUnit = doseOutputUnit;
    }
}