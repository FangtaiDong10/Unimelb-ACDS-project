package kv.pojo.vo.calculation;

import java.util.List;

public class WorkDataSheet1VO {
    private String coneName;
    private String nominalDoseOutputType;
    private String openClosed;
    private Double ssd;
    private List<BeamDataVO> beamsData;

    public String getConeName() {
        return coneName;
    }

    public void setConeName(String coneName) {
        this.coneName = coneName;
    }

    public String getNominalDoseOutputType() {
        return nominalDoseOutputType;
    }

    public void setNominalDoseOutputType(String nominalDoseOutputType) {
        this.nominalDoseOutputType = nominalDoseOutputType;
    }

    public String getOpenClosed() {
        return openClosed;
    }

    public void setOpenClosed(String openClosed) {
        this.openClosed = openClosed;
    }

    public Double getSsd() {
        return ssd;
    }

    public void setSsd(Double ssd) {
        this.ssd = ssd;
    }

    public List<BeamDataVO> getBeamsData() {
        return beamsData;
    }

    public void setBeamsData(List<BeamDataVO> beamsData) {
        this.beamsData = beamsData;
    }

    public static class BeamDataVO {
        private String beamNo;
        private Double kvp;
        private Double nominalHvlMmAl;
        private Double nominalHvlMmCu;
        private Double measuredHvlMmAl;
        private Double measuredHvlMmCu;
        private Double doseRate;
        private List<ChamberResultVO> chamberResultList;

        public String getBeamNo() {
            return beamNo;
        }

        public void setBeamNo(String beamNo) {
            this.beamNo = beamNo;
        }

        public Double getKvp() {
            return kvp;
        }

        public void setKvp(Double kvp) {
            this.kvp = kvp;
        }

        public Double getNominalHvlMmAl() {
            return nominalHvlMmAl;
        }

        public void setNominalHvlMmAl(Double nominalHvlMmAl) {
            this.nominalHvlMmAl = nominalHvlMmAl;
        }

        public Double getNominalHvlMmCu() {
            return nominalHvlMmCu;
        }

        public void setNominalHvlMmCu(Double nominalHvlMmCu) {
            this.nominalHvlMmCu = nominalHvlMmCu;
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

        public Double getDoseRate() {
            return doseRate;
        }

        public void setDoseRate(Double doseRate) {
            this.doseRate = doseRate;
        }

        public List<ChamberResultVO> getChamberResultList() {
            return chamberResultList;
        }

        public void setChamberResultList(List<ChamberResultVO> chamberResultList) {
            this.chamberResultList = chamberResultList;
        }
    }

    public static class ChamberResultVO {
        private String chamberSn;
        private Double muRho;
        private Double bwCombined;
        private Double bwAl;
        private Double bwCu;
        private Double kClosedCone;
        private Double kCombinedCone;

        public String getChamberSn() {
            return chamberSn;
        }

        public void setChamberSn(String chamberSn) {
            this.chamberSn = chamberSn;
        }

        public Double getMuRho() {
            return muRho;
        }

        public void setMuRho(Double muRho) {
            this.muRho = muRho;
        }

        public Double getBwCombined() {
            return bwCombined;
        }

        public void setBwCombined(Double bwCombined) {
            this.bwCombined = bwCombined;
        }

        public Double getBwAl() {
            return bwAl;
        }

        public void setBwAl(Double bwAl) {
            this.bwAl = bwAl;
        }

        public Double getBwCu() {
            return bwCu;
        }

        public void setBwCu(Double bwCu) {
            this.bwCu = bwCu;
        }

        public Double getkClosedCone() {
            return kClosedCone;
        }

        public void setkClosedCone(Double kClosedCone) {
            this.kClosedCone = kClosedCone;
        }

        public Double getkCombinedCone() {
            return kCombinedCone;
        }

        public void setkCombinedCone(Double kCombinedCone) {
            this.kCombinedCone = kCombinedCone;
        }
    }
}
