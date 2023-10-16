package kv.pojo.vo.calculation;

public class BeamInfoVO {
    private String beamId;
    private Double energy;
    private Double measuredHvlMmAl;
    private Double measuredHvlMmCu;

    public String getBeamId() {
        return beamId;
    }

    public void setBeamId(String beamId) {
        this.beamId = beamId;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getMeasuredHvlMmAl() {
        return measuredHvlMmAl;
    }

    public void setMeasuredHvlMmAl(Double measuredHvlMmAl) {
        this.measuredHvlMmAl = measuredHvlMmAl;
    }

    public Double getMeasuredHvlMmCu() {
        return measuredHvlMmCu;
    }

    public void setMeasuredHvlMmCu(Double measuredHvlMmCu) {
        this.measuredHvlMmCu = measuredHvlMmCu;
    }
}
