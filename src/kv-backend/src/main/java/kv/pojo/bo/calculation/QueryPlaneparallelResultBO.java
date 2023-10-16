package kv.pojo.bo.calculation;


public class QueryPlaneparallelResultBO {
    private String beamPpChamberId;
    private String beamPlaneparallelId;
    private String chamberSn;
    private Double hvlMeasuredMmAl;

    public String getBeamPpChamberId() {
        return beamPpChamberId;
    }

    public void setBeamPpChamberId(String beamPpChamberId) {
        this.beamPpChamberId = beamPpChamberId;
    }

    public String getBeamPlaneparallelId() {
        return beamPlaneparallelId;
    }

    public void setBeamPlaneparallelId(String beamPlaneparallelId) {
        this.beamPlaneparallelId = beamPlaneparallelId;
    }

    public String getChamberSn() {
        return chamberSn;
    }

    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
    }

    public Double getHvlMeasuredMmAl() {
        return hvlMeasuredMmAl;
    }

    public void setHvlMeasuredMmAl(Double hvlMeasuredMmAl) {
        this.hvlMeasuredMmAl = hvlMeasuredMmAl;
    }
}
