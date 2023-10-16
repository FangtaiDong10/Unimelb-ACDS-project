package kv.pojo.bo.calculation.intermediateResults;

import java.util.Date;

public class FarmerInfoBO {
    private Double kvp;
    private Double hvl;
    private String type;
    private Date dateUpdated;
    public FarmerInfoBO() {
    }

    /**
     * @param kvp
     * @param hvl
     * @param type
     */
    public FarmerInfoBO(Double kvp, Double hvl, String type) {
        this.kvp = kvp;
        this.hvl = hvl;
        this.type = type;
    }
    
    /**
     * @param kvp
     * @param hvl
     * @param type
     * @param dateUpdated
     */
    public FarmerInfoBO(Double kvp, Double hvl, String type, Date dateUpdated) {
        this.kvp = kvp;
        this.hvl = hvl;
        this.type = type;
        this.dateUpdated = dateUpdated;
    }

    /**
     * @return the kvp
     */
    public Double getKvp() {
        return kvp;
    }
    /**
     * @param kvp the kvp to set
     */
    public void setKvp(Double kvp) {
        this.kvp = kvp;
    }
    /**
     * @return the hvl
     */
    public Double getHvl() {
        return hvl;
    }
    /**
     * @param hvl the hvl to set
     */
    public void setHvl(Double hvl) {
        this.hvl = hvl;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the dateUpdated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }
    /**
     * @param dateUpdated the dateUpdated to set
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
}
