package kv.pojo.bo.calculation.intermediateResults;

public class PstemResultBO {
    /* Example:
    *[{'beam_cone_id': 'ACDS-kV-5014-beam1_ACDS-kV-5014-cone1',
     *   'chamber': '1508',
     *   'pstem': 0.9971102034425431},
     *  {'beam_cone_id': 'ACDS-kV-5014-beam1_ACDS-kV-5014-cone1',
     *   'chamber': '858',
     *   'pstem': 0.994643734917261},
     *  {'beam_cone_id': 'ACDS-kV-5014-beam1_ACDS-kV-5014-cone3',
     *   'chamber': '1508',
     *   'pstem': 1.0},
     *  {'beam_cone_id': 'ACDS-kV-5014-beam1_ACDS-kV-5014-cone3',
     *   'chamber': '858',
     *   'pstem': 1.0}]
    *
     */
    private String beamConeId;
    private String chamberSn;
    private double pstem;

    public PstemResultBO() {
    }

    public PstemResultBO(String beamConeId, String chamberSn, double pstem) {
        this.beamConeId = beamConeId;
        this.chamberSn = chamberSn;
        this.pstem = pstem;
    }

    public String getBeamConeId() {
        return beamConeId;
    }

    public void setBeamConeId(String beamConeId) {
        this.beamConeId = beamConeId;
    }

    public String getChamberSn() {
        return chamberSn;
    }

    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
    }

    public double getPstem() {
        return pstem;
    }

    public void setPstem(double pstem) {
        this.pstem = pstem;
    }
}
