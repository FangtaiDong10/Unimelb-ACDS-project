package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "reference_cone")
public class ReferenceCone {
    @Id
    @Column(name = "reference_cone_id")
    private String referenceConeId;

    @Column(name = "reference_cone_id_alt")
    private String referenceConeIdAlt;

    @Column(name = "nom_energy")
    private Double nomEnergy;

    private Double ssd;

    private Double depth;

    @Column(name = "geometry_shape")
    private String geometryShape;

    @Column(name = "geometry_measurement")
    private Double geometryMeasurement;

    @Column(name = "open_closed")
    private String openClosed;

    private Double thickness;

    @Column(name = "dosp_ssd")
    private String dospSsd;

    /**
     * @return reference_cone_id
     */
    public String getReferenceConeId() {
        return referenceConeId;
    }

    /**
     * @param referenceConeId
     */
    public void setReferenceConeId(String referenceConeId) {
        this.referenceConeId = referenceConeId;
    }

    /**
     * @return reference_cone_id_alt
     */
    public String getReferenceConeIdAlt() {
        return referenceConeIdAlt;
    }

    /**
     * @param referenceConeIdAlt
     */
    public void setReferenceConeIdAlt(String referenceConeIdAlt) {
        this.referenceConeIdAlt = referenceConeIdAlt;
    }

    /**
     * @return nom_energy
     */
    public Double getNomEnergy() {
        return nomEnergy;
    }

    /**
     * @param nomEnergy
     */
    public void setNomEnergy(Double nomEnergy) {
        this.nomEnergy = nomEnergy;
    }

    /**
     * @return ssd
     */
    public Double getSsd() {
        return ssd;
    }

    /**
     * @param ssd
     */
    public void setSsd(Double ssd) {
        this.ssd = ssd;
    }

    /**
     * @return depth
     */
    public Double getDepth() {
        return depth;
    }

    /**
     * @param depth
     */
    public void setDepth(Double depth) {
        this.depth = depth;
    }

    /**
     * @return geometry_shape
     */
    public String getGeometryShape() {
        return geometryShape;
    }

    /**
     * @param geometryShape
     */
    public void setGeometryShape(String geometryShape) {
        this.geometryShape = geometryShape;
    }

    /**
     * @return geometry_measurement
     */
    public Double getGeometryMeasurement() {
        return geometryMeasurement;
    }

    /**
     * @param geometryMeasurement
     */
    public void setGeometryMeasurement(Double geometryMeasurement) {
        this.geometryMeasurement = geometryMeasurement;
    }

    /**
     * @return open_closed
     */
    public String getOpenClosed() {
        return openClosed;
    }

    /**
     * @param openClosed
     */
    public void setOpenClosed(String openClosed) {
        this.openClosed = openClosed;
    }

    /**
     * @return thickness
     */
    public Double getThickness() {
        return thickness;
    }

    /**
     * @param thickness
     */
    public void setThickness(Double thickness) {
        this.thickness = thickness;
    }

    /**
     * @return dosp_ssd
     */
    public String getDospSsd() {
        return dospSsd;
    }

    /**
     * @param dospSsd
     */
    public void setDospSsd(String dospSsd) {
        this.dospSsd = dospSsd;
    }
}