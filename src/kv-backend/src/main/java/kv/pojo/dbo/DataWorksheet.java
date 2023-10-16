package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "data_worksheet")
public class DataWorksheet {
    @Id
    @Column(name = "back_result_id")
    private String backResultId;

    private Double kelec;

    @Column(name = "webline_bias_v")
    private String weblineBiasV;

    @Column(name = "daily_output")
    private Double dailyOutput;

    @Column(name = "dose_rate_nom")
    private Double doseRateNom;

    @Column(name = "k_combined")
    private Double kCombined;

    private Double kpol;

    private Double temp;

    private Double pressure;

    @Column(name = "kTP")
    private Double ktp;

    @Column(name = "Mnc_1")
    private Double mnc1;

    @Column(name = "Mnc_2")
    private Double mnc2;

    @Column(name = "Mnc_3")
    private Double mnc3;

    @Column(name = "avg_Mnc")
    private Double avgMnc;

    @Column(name = "acds_output")
    private Double acdsOutput;

    @Column(name = "facility_output")
    private Double facilityOutput;

    @Column(name = "percentage_diff")
    private Double percentageDiff;

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
     * @return kelec
     */
    public Double getKelec() {
        return kelec;
    }

    /**
     * @param kelec
     */
    public void setKelec(Double kelec) {
        this.kelec = kelec;
    }

    /**
     * @return webline_bias_v
     */
    public String getWeblineBiasV() {
        return weblineBiasV;
    }

    /**
     * @param weblineBiasV
     */
    public void setWeblineBiasV(String weblineBiasV) {
        this.weblineBiasV = weblineBiasV;
    }

    /**
     * @return daily_output
     */
    public Double getDailyOutput() {
        return dailyOutput;
    }

    /**
     * @param dailyOutput
     */
    public void setDailyOutput(Double dailyOutput) {
        this.dailyOutput = dailyOutput;
    }

    /**
     * @return dose_rate_nom
     */
    public Double getDoseRateNom() {
        return doseRateNom;
    }

    /**
     * @param doseRateNom
     */
    public void setDoseRateNom(Double doseRateNom) {
        this.doseRateNom = doseRateNom;
    }

    /**
     * @return k_combined
     */
    public Double getkCombined() {
        return kCombined;
    }

    /**
     * @param kCombined
     */
    public void setkCombined(Double kCombined) {
        this.kCombined = kCombined;
    }

    /**
     * @return kpol
     */
    public Double getKpol() {
        return kpol;
    }

    /**
     * @param kpol
     */
    public void setKpol(Double kpol) {
        this.kpol = kpol;
    }

    /**
     * @return temp
     */
    public Double getTemp() {
        return temp;
    }

    /**
     * @param temp
     */
    public void setTemp(Double temp) {
        this.temp = temp;
    }

    /**
     * @return pressure
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     * @param pressure
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    /**
     * @return kTP
     */
    public Double getKtp() {
        return ktp;
    }

    /**
     * @param ktp
     */
    public void setKtp(Double ktp) {
        this.ktp = ktp;
    }

    /**
     * @return Mnc_1
     */
    public Double getMnc1() {
        return mnc1;
    }

    /**
     * @param mnc1
     */
    public void setMnc1(Double mnc1) {
        this.mnc1 = mnc1;
    }

    /**
     * @return Mnc_2
     */
    public Double getMnc2() {
        return mnc2;
    }

    /**
     * @param mnc2
     */
    public void setMnc2(Double mnc2) {
        this.mnc2 = mnc2;
    }

    /**
     * @return Mnc_3
     */
    public Double getMnc3() {
        return mnc3;
    }

    /**
     * @param mnc3
     */
    public void setMnc3(Double mnc3) {
        this.mnc3 = mnc3;
    }

    /**
     * @return avg_Mnc
     */
    public Double getAvgMnc() {
        return avgMnc;
    }

    /**
     * @param avgMnc
     */
    public void setAvgMnc(Double avgMnc) {
        this.avgMnc = avgMnc;
    }

    /**
     * @return acds_output
     */
    public Double getAcdsOutput() {
        return acdsOutput;
    }

    /**
     * @param acdsOutput
     */
    public void setAcdsOutput(Double acdsOutput) {
        this.acdsOutput = acdsOutput;
    }

    /**
     * @return facility_output
     */
    public Double getFacilityOutput() {
        return facilityOutput;
    }

    /**
     * @param facilityOutput
     */
    public void setFacilityOutput(Double facilityOutput) {
        this.facilityOutput = facilityOutput;
    }

    /**
     * @return percentage_diff
     */
    public Double getPercentageDiff() {
        return percentageDiff;
    }

    /**
     * @param percentageDiff
     */
    public void setPercentageDiff(Double percentageDiff) {
        this.percentageDiff = percentageDiff;
    }
}