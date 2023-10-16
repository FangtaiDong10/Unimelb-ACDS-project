package kv.pojo.vo.audit;

public class WarningVO {
    private String coneId;
    private String beamId;
    private String chamberSn;
    private String message;

    public String getConeId() {
        return coneId;
    }

    public void setConeId(String coneId) {
        this.coneId = coneId;
    }

    public String getBeamId() {
        return beamId;
    }

    public void setBeamId(String beamId) {
        this.beamId = beamId;
    }

    public String getChamberSn() {
        return chamberSn;
    }

    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
