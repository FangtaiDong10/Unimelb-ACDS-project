package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "back_result")
public class BackResult {
    @Id
    @Column(name = "back_result_id")
    private String backResultId;

    @Column(name = "input_id")
    private String inputId;

    @Column(name = "chamber_SN")
    private String chamberSn;

    private Double nk;

    @Column(name = "bw_combined")
    private Double bwCombined;

    @Column(name = "bw_al")
    private Double bwAl;

    @Column(name = "bw_cu")
    private Double bwCu;

    private Double murho;

    @Column(name = "k_closed_cone")
    private Double kClosedCone;

    private Double pstem;

    private String warning;

    /**
     * @return back_result_id
     */
    public String getBackResultId() {
        return backResultId;
    }

    /**
     * @param backResultId
     */
    public void setBackResultId(String backResultId) {
        this.backResultId = backResultId;
    }

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
     * @return nk
     */
    public Double getNk() {
        return nk;
    }

    /**
     * @param nk
     */
    public void setNk(Double nk) {
        this.nk = nk;
    }

    /**
     * @return bw_combined
     */
    public Double getBwCombined() {
        return bwCombined;
    }

    /**
     * @param bwCombined
     */
    public void setBwCombined(Double bwCombined) {
        this.bwCombined = bwCombined;
    }

    /**
     * @return bw_al
     */
    public Double getBwAl() {
        return bwAl;
    }

    /**
     * @param bwAl
     */
    public void setBwAl(Double bwAl) {
        this.bwAl = bwAl;
    }

    /**
     * @return bw_cu
     */
    public Double getBwCu() {
        return bwCu;
    }

    /**
     * @param bwCu
     */
    public void setBwCu(Double bwCu) {
        this.bwCu = bwCu;
    }

    /**
     * @return murho
     */
    public Double getMurho() {
        return murho;
    }

    /**
     * @param murho
     */
    public void setMurho(Double murho) {
        this.murho = murho;
    }

    /**
     * @return k_closed_cone
     */
    public Double getkClosedCone() {
        return kClosedCone;
    }

    /**
     * @param kClosedCone
     */
    public void setkClosedCone(Double kClosedCone) {
        this.kClosedCone = kClosedCone;
    }

    /**
     * @return pstem
     */
    public Double getPstem() {
        return pstem;
    }

    /**
     * @param pstem
     */
    public void setPstem(Double pstem) {
        this.pstem = pstem;
    }

    /**
     * @return warning
     */
    public String getWarning() {
        return warning;
    }

    /**
     * @param warning
     */
    public void setWarning(String warning) {
        this.warning = warning;
    }
}