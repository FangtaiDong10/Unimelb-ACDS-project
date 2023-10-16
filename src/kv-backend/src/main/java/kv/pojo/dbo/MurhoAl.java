package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "murho_al")
public class MurhoAl {
    @Id
    @Column(name = "murho_al_id")
    private Integer murhoAlId;

    @Column(name = "hvl_al")
    private Double hvlAl;

    private Double murho;

    @Column(name = "date_updated")
    private Date dateUpdated;

    /**
     * @return murho_al_id
     */
    public Integer getMurhoAlId() {
        return murhoAlId;
    }

    /**
     * @param murhoAlId
     */
    public void setMurhoAlId(Integer murhoAlId) {
        this.murhoAlId = murhoAlId;
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
     * @return murho
     */
    public Double getMurho() {
        return murho;
    }

    /**
     * @param murho
     */
    public void setMurho(Double murho) {
        this.murho = murho;
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