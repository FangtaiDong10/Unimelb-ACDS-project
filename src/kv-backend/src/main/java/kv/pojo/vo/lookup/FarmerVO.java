package kv.pojo.vo.lookup;

import java.util.Date;

public class FarmerVO {
    private String beamFarmerId;
    private Double kv;
    private Double hvlMeasuredMmCu;
    private Double hvlMeasuredMmAl;
    private String chamberSn;
    private Date dateUpdated;
    private Double nkValue;
    private String chamberName;

    public String getBeamFarmerId() {
        return beamFarmerId;
    }

    public void setBeamFarmerId(String beamFarmerId) {
        this.beamFarmerId = beamFarmerId;
    }

    public Double getKv() {
        return kv;
    }

    public void setKv(Double kv) {
        this.kv = kv;
    }

    public Double getHvlMeasuredMmCu() {
        return hvlMeasuredMmCu;
    }

    public void setHvlMeasuredMmCu(Double hvlMeasuredMmCu) {
        this.hvlMeasuredMmCu = hvlMeasuredMmCu;
    }

    public Double getHvlMeasuredMmAl() {
        return hvlMeasuredMmAl;
    }

    public void setHvlMeasuredMmAl(Double hvlMeasuredMmAl) {
        this.hvlMeasuredMmAl = hvlMeasuredMmAl;
    }

    public String getChamberSn() {
        return chamberSn;
    }

    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Double getNkValue() {
        return nkValue;
    }

    public void setNkValue(Double nkValue) {
        this.nkValue = nkValue;
    }

    public String getChamberName() {
        return chamberName;
    }

    public void setChamberName(String chamberName) {
        this.chamberName = chamberName;
    }
}
