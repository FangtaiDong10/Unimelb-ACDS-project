package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "bw_al_cu")
public class BwAlCu {
    @Id
    @Column(name = "bw_id")
    private Integer bwId;

    private String type;

    @Column(name = "hvl_cu")
    private Double hvlCu;

    @Column(name = "hvl_al")
    private Double hvlAl;

    private Double ssd;

    private Double diameter;

    private Double bw;

    @Column(name = "date_updated")
    private Date dateUpdated;

    /**
     * @return bw_id
     */
    public Integer getBwId() {
        return bwId;
    }

    /**
     * @param bwId
     */
    public void setBwId(Integer bwId) {
        this.bwId = bwId;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return hvl_cu
     */
    public Double getHvlCu() {
        return hvlCu;
    }

    /**
     * @param hvlCu
     */
    public void setHvlCu(Double hvlCu) {
        this.hvlCu = hvlCu;
    }

    /**
     * @return hvl_al
     */
    public Double getHvlAl() {
        return hvlAl;
    }

    /**
     * @param hvlAl
     */
    public void setHvlAl(Double hvlAl) {
        this.hvlAl = hvlAl;
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
     * @return diameter
     */
    public Double getDiameter() {
        return diameter;
    }

    /**
     * @param diameter
     */
    public void setDiameter(Double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return bw
     */
    public Double getBw() {
        return bw;
    }

    /**
     * @param bw
     */
    public void setBw(Double bw) {
        this.bw = bw;
    }

    /**
     * @return date_updated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * @param dateUpdated
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}