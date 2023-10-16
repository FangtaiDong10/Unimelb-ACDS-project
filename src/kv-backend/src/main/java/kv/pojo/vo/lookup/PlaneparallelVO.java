package kv.pojo.vo.lookup;

import java.util.Date;

public class PlaneparallelVO {
    private String beamPlaneparallelId;
    private Double kv;
    private Double hvlMeasuredMmAl;
    private Double nkValue;
    private Date dateUpdated;
    private String chamberSn;
    private String chamberName;

    public String getBeamPlaneparallelId() {
        return beamPlaneparallelId;
    }

    public void setBeamPlaneparallelId(String beamPlaneparallelId) {
        this.beamPlaneparallelId = beamPlaneparallelId;
    }

    public Double getKv() {
        return kv;
    }

    public void setKv(Double kv) {
        this.kv = kv;
    }

    public Double getHvlMeasuredMmAl() {
        return hvlMeasuredMmAl;
    }

    public void setHvlMeasuredMmAl(Double hvlMeasuredMmAl) {
        this.hvlMeasuredMmAl = hvlMeasuredMmAl;
    }

    public Double getNkValue() {
        return nkValue;
    }

    public void setNkValue(Double nkValue) {
        this.nkValue = nkValue;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getChamberSn() {
        return chamberSn;
    }

    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
    }

    public String getChamberName() {
        return chamberName;
    }

    public void setChamberName(String chamberName) {
        this.chamberName = chamberName;
    }
}
