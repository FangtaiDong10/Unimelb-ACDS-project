package kv.pojo.vo.calculation;

import java.util.List;

public class WorkDataSheet2VO {
    private String coneName;
    private BasicDataVO basicData;
    private List<BeamDataVO> beamsData;

    public String getConeName() {
        return coneName;
    }

    public void setConeName(String coneName) {
        this.coneName = coneName;
    }

    public BasicDataVO getBasicData() {
        return basicData;
    }

    public void setBasicData(BasicDataVO basicData) {
        this.basicData = basicData;
    }

    public List<BeamDataVO> getBeamsData() {
        return beamsData;
    }

    public void setBeamsData(List<BeamDataVO> beamsData) {
        this.beamsData = beamsData;
    }

    public static class BasicDataVO {
        private Boolean electronContaminationCheck;
        private Double ssd;
        private String doseType;
        private String electrometerSn;
        private Integer channel;
        private String weblineRange;
        private String chamberSn;

        public Boolean getElectronContaminationCheck() {
            return electronContaminationCheck;
        }

        public void setElectronContaminationCheck(Boolean electronContaminationCheck) {
            this.electronContaminationCheck = electronContaminationCheck;
        }

        public Double getSsd() {
            return ssd;
        }

        public void setSsd(Double ssd) {
            this.ssd = ssd;
        }

        public String getDoseType() {
            return doseType;
        }

        public void setDoseType(String doseType) {
            this.doseType = doseType;
        }

        public String getElectrometerSn() {
            return electrometerSn;
        }

        public void setElectrometerSn(String electrometerSn) {
            this.electrometerSn = electrometerSn;
        }

        public Integer getChannel() {
            return channel;
        }

        public void setChannel(Integer channel) {
            this.channel = channel;
        }

        public String getWeblineRange() {
            return weblineRange;
        }

        public void setWeblineRange(String weblineRange) {
            this.weblineRange = weblineRange;
        }

        public String getChamberSn() {
            return chamberSn;
        }

        public void setChamberSn(String chamberSn) {
            this.chamberSn = chamberSn;
        }
    }

    public static class BeamDataVO {
        private String beamNo;
        private Double kv;
        private List<WorkDataSheet2VO.ChamberResultVO> chamberResultList;

        public String getBeamNo() {
            return beamNo;
        }

        public void setBeamNo(String beamNo) {
            this.beamNo = beamNo;
        }

        public Double getKv() {
            return kv;
        }

        public void setKv(Double kv) {
            this.kv = kv;
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
        private Double nk;
        private Double muRho;
        private Double bw;
        private Double kClosedCone;
        private Double pstemAir;

        public String getChamberSn() {
            return chamberSn;
        }

        public void setChamberSn(String chamberSn) {
            this.chamberSn = chamberSn;
        }

        public Double getNk() {
            return nk;
        }

        public void setNk(Double nk) {
            this.nk = nk;
        }

        public Double getMuRho() {
            return muRho;
        }

        public void setMuRho(Double muRho) {
            this.muRho = muRho;
        }

        public Double getBw() {
            return bw;
        }

        public void setBw(Double bw) {
            this.bw = bw;
        }

        public Double getkClosedCone() {
            return kClosedCone;
        }

        public void setkClosedCone(Double kClosedCone) {
            this.kClosedCone = kClosedCone;
        }

        public Double getPstemAir() {
            return pstemAir;
        }

        public void setPstemAir(Double pstemAir) {
            this.pstemAir = pstemAir;
        }
    }
}
