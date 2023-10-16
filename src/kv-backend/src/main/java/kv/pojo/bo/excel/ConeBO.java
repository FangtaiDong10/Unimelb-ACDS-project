package kv.pojo.bo.excel;

import java.util.List;

public class ConeBO {
    private String coneId;
    private String coneIdAlt;
    private String shape;
    private String openClosed;
    private String endThickness;
    private String ssd;
    private String doseOutputUnit;
    private String fieldDiameter;
    private String fieldArea;
    private String fieldDimension1;
    private String fieldDimension2;
    private List<ConeBeanBO> beamsData;

    public String getConeId() {
        return coneId;
    }

    public void setConeId(String coneId) {
        this.coneId = coneId;
    }

    public String getConeIdAlt() {
        return coneIdAlt;
    }

    public void setConeIdAlt(String coneIdAlt) {
        this.coneIdAlt = coneIdAlt;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getOpenClosed() {
        return openClosed;
    }

    public void setOpenClosed(String openClosed) {
        this.openClosed = openClosed;
    }

    public String getEndThickness() {
        return endThickness;
    }

    public void setEndThickness(String endThickness) {
        this.endThickness = endThickness;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public String getDoseOutputUnit() {
        return doseOutputUnit;
    }

    public void setDoseOutputUnit(String doseOutputUnit) {
        this.doseOutputUnit = doseOutputUnit;
    }

    public String getFieldDiameter() {
        return fieldDiameter;
    }

    public void setFieldDiameter(String fieldDiameter) {
        this.fieldDiameter = fieldDiameter;
    }

    public String getFieldArea() {
        return fieldArea;
    }

    public void setFieldArea(String fieldArea) {
        this.fieldArea = fieldArea;
    }

    public String getFieldDimension1() {
        return fieldDimension1;
    }

    public void setFieldDimension1(String fieldDimension1) {
        this.fieldDimension1 = fieldDimension1;
    }

    public String getFieldDimension2() {
        return fieldDimension2;
    }

    public void setFieldDimension2(String fieldDimension2) {
        this.fieldDimension2 = fieldDimension2;
    }

    public List<ConeBeanBO> getBeamsData() {
        return beamsData;
    }

    public void setBeamsData(List<ConeBeanBO> beamsData) {
        this.beamsData = beamsData;
    }
}
