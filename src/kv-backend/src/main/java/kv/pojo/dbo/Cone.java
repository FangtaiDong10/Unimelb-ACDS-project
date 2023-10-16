package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;

public class Cone {
    @Id
    @Column(name = "cone_id")
    private String coneId;

    @Column(name = "cone_id_alt")
    private String coneIdAlt;

    private String shape;

    @Column(name = "open_closed")
    private String openClosed;

    private Double ssd;

    @Column(name = "field_area")
    private Double fieldArea;

    @Column(name = "field_dimension_1")
    private Double fieldDimension1;

    @Column(name = "field_dimension_2")
    private Double fieldDimension2;

    @Column(name = "field_diameter")
    private Double fieldDiameter;

    @Column(name = "end_thickness")
    private Double endThickness;

    /**
     * @return cone_id
     */
    public String getConeId() {
        return coneId;
    }

    /**
     * @param coneId
     */
    public void setConeId(String coneId) {
        this.coneId = coneId;
    }

    /**
     * @return cone_id_alt
     */
    public String getConeIdAlt() {
        return coneIdAlt;
    }

    /**
     * @param coneIdAlt
     */
    public void setConeIdAlt(String coneIdAlt) {
        this.coneIdAlt = coneIdAlt;
    }

    /**
     * @return shape
     */
    public String getShape() {
        return shape;
    }

    /**
     * @param shape
     */
    public void setShape(String shape) {
        this.shape = shape;
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
     * @return field_area
     */
    public Double getFieldArea() {
        return fieldArea;
    }

    /**
     * @param fieldArea
     */
    public void setFieldArea(Double fieldArea) {
        this.fieldArea = fieldArea;
    }

    /**
     * @return field_dimension_1
     */
    public Double getFieldDimension1() {
        return fieldDimension1;
    }

    /**
     * @param fieldDimension1
     */
    public void setFieldDimension1(Double fieldDimension1) {
        this.fieldDimension1 = fieldDimension1;
    }

    /**
     * @return field_dimension_2
     */
    public Double getFieldDimension2() {
        return fieldDimension2;
    }

    /**
     * @param fieldDimension2
     */
    public void setFieldDimension2(Double fieldDimension2) {
        this.fieldDimension2 = fieldDimension2;
    }

    /**
     * @return field_diameter
     */
    public Double getFieldDiameter() {
        return fieldDiameter;
    }

    /**
     * @param fieldDiameter
     */
    public void setFieldDiameter(Double fieldDiameter) {
        this.fieldDiameter = fieldDiameter;
    }

    /**
     * @return end_thickness
     */
    public Double getEndThickness() {
        return endThickness;
    }

    /**
     * @param endThickness
     */
    public void setEndThickness(Double endThickness) {
        this.endThickness = endThickness;
    }
}