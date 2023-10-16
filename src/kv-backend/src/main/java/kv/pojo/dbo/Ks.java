package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;

public class Ks {
    @Id
    @Column(name = "back_result_id")
    private String backResultId;

    @Column(name = "tp_temp")
    private Double tpTemp;

    @Column(name = "tp_pressure")
    private Double tpPressure;

    @Column(name = "dose_voltage")
    private Double doseVoltage;

    @Column(name = "dose_nc_1")
    private Double doseNc1;

    @Column(name = "dose_nc_2")
    private Double doseNc2;

    @Column(name = "dose_nc_3")
    private Double doseNc3;

    private Double m1;

    @Column(name = "recombination_voltage")
    private Double recombinationVoltage;

    @Column(name = "recombination_nc_1")
    private Double recombinationNc1;

    @Column(name = "recombination_nc_2")
    private Double recombinationNc2;

    @Column(name = "recombination_nc_3")
    private Double recombinationNc3;

    private Double m2;

    private Double ks;

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
     * @return tp_temp
     */
    public Double getTpTemp() {
        return tpTemp;
    }

    /**
     * @param tpTemp
     */
    public void setTpTemp(Double tpTemp) {
        this.tpTemp = tpTemp;
    }

    /**
     * @return tp_pressure
     */
    public Double getTpPressure() {
        return tpPressure;
    }

    /**
     * @param tpPressure
     */
    public void setTpPressure(Double tpPressure) {
        this.tpPressure = tpPressure;
    }

    /**
     * @return dose_voltage
     */
    public Double getDoseVoltage() {
        return doseVoltage;
    }

    /**
     * @param doseVoltage
     */
    public void setDoseVoltage(Double doseVoltage) {
        this.doseVoltage = doseVoltage;
    }

    /**
     * @return dose_nc_1
     */
    public Double getDoseNc1() {
        return doseNc1;
    }

    /**
     * @param doseNc1
     */
    public void setDoseNc1(Double doseNc1) {
        this.doseNc1 = doseNc1;
    }

    /**
     * @return dose_nc_2
     */
    public Double getDoseNc2() {
        return doseNc2;
    }

    /**
     * @param doseNc2
     */
    public void setDoseNc2(Double doseNc2) {
        this.doseNc2 = doseNc2;
    }

    /**
     * @return dose_nc_3
     */
    public Double getDoseNc3() {
        return doseNc3;
    }

    /**
     * @param doseNc3
     */
    public void setDoseNc3(Double doseNc3) {
        this.doseNc3 = doseNc3;
    }

    /**
     * @return m1
     */
    public Double getM1() {
        return m1;
    }

    /**
     * @param m1
     */
    public void setM1(Double m1) {
        this.m1 = m1;
    }

    /**
     * @return recombination_voltage
     */
    public Double getRecombinationVoltage() {
        return recombinationVoltage;
    }

    /**
     * @param recombinationVoltage
     */
    public void setRecombinationVoltage(Double recombinationVoltage) {
        this.recombinationVoltage = recombinationVoltage;
    }

    /**
     * @return recombination_nc_1
     */
    public Double getRecombinationNc1() {
        return recombinationNc1;
    }

    /**
     * @param recombinationNc1
     */
    public void setRecombinationNc1(Double recombinationNc1) {
        this.recombinationNc1 = recombinationNc1;
    }

    /**
     * @return recombination_nc_2
     */
    public Double getRecombinationNc2() {
        return recombinationNc2;
    }

    /**
     * @param recombinationNc2
     */
    public void setRecombinationNc2(Double recombinationNc2) {
        this.recombinationNc2 = recombinationNc2;
    }

    /**
     * @return recombination_nc_3
     */
    public Double getRecombinationNc3() {
        return recombinationNc3;
    }

    /**
     * @param recombinationNc3
     */
    public void setRecombinationNc3(Double recombinationNc3) {
        this.recombinationNc3 = recombinationNc3;
    }

    /**
     * @return m2
     */
    public Double getM2() {
        return m2;
    }

    /**
     * @param m2
     */
    public void setM2(Double m2) {
        this.m2 = m2;
    }

    /**
     * @return ks
     */
    public Double getKs() {
        return ks;
    }

    /**
     * @param ks
     */
    public void setKs(Double ks) {
        this.ks = ks;
    }
}