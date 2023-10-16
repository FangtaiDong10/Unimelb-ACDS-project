package kv.pojo.bo.calculation.intermediateResults;

import java.util.HashMap;
import java.util.Map;

public class MurhoResultBO {
    /*
     * Example:
     * [{'beam_id': 'ACDS-kV-5014-beam1',
     *   'kvp': 60.0,
     *   'hvl_measured_al': 1.268,
     *   'hvl_measured_cu': None,
     *   'al_murho': 1.0177733333333334,
     *   'cu_murho': None,
     *   'murho': 1.0177733333333334},
     *  {'beam_id': 'ACDS-kV-5014-beam2',
     *   'kvp': 80.0,
     *   'hvl_measured_al': 2.321,
     *   'hvl_measured_cu': None,
     *   'al_murho': 1.018963,
     *   'cu_murho': None,
     *   'murho': 1.018963},
     *  {'beam_id': 'ACDS-kV-5014-beam3',
     *   'kvp': 100.0,
     *   'hvl_measured_al': 2.881,
     *   'hvl_measured_cu': None,
     *   'al_murho': 1.020643,
     *   'cu_murho': None,
     *   'murho': 1.020643},
     *   ...
     *   ]
     */
    private String beamId;
    //allow null Double values
    private Double kvp;
    private Double hvlMeasuredAl;
    private Double hvlMeasuredCu;
    private Double alMurho;
    private Double cuMurho;
    private Double murho;

    public MurhoResultBO() {
    }

    public MurhoResultBO(String beamId, double kvp, double hvlMeasuredAl, double hvlMeasuredCu, double alMurho, double cuMurho, double murho) {
        this.beamId = beamId;
        this.kvp = kvp;
        this.hvlMeasuredAl = hvlMeasuredAl;
        this.hvlMeasuredCu = hvlMeasuredCu;
        this.alMurho = alMurho;
        this.cuMurho = cuMurho;
        this.murho = murho;
    }

    public Map<String,Object> getResultAsMap(){
        Map<String, Object> result = new HashMap<>();
        result.put("beam_id",beamId);
        result.put("kvp",kvp);
        result.put("hvlMeasuredCu",hvlMeasuredCu);
        result.put("hvlMeasuredAl",hvlMeasuredAl);
        result.put("alMurho",alMurho);
        result.put("cuMurho",cuMurho);
        result.put("murho",murho);
        return result;
    }
    public String getBeamId() {
        return beamId;
    }

    public void setBeamId(String beamId) {
        this.beamId = beamId;
    }

    public double getKvp() {
        return kvp;
    }

    public void setKvp(double kvp) {
        this.kvp = kvp;
    }

    public double getHvlMeasuredAl() {
        return hvlMeasuredAl;
    }

    public void setHvlMeasuredAl(double hvlMeasuredAl) {
        this.hvlMeasuredAl = hvlMeasuredAl;
    }

    public double getHvlMeasuredCu() {
        return hvlMeasuredCu;
    }

    public void setHvlMeasuredCu(double hvlMeasuredCu) {
        this.hvlMeasuredCu = hvlMeasuredCu;
    }

    public double getAlMurho() {
        return alMurho;
    }

    public void setAlMurho(double alMurho) {
        this.alMurho = alMurho;
    }

    public double getCuMurho() {
        return cuMurho;
    }

    public void setCuMurho(double cuMurho) {
        this.cuMurho = cuMurho;
    }

    public double getMurho() {
        return murho;
    }

    public void setMurho(double murho) {
        this.murho = murho;
    }
}
