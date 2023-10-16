package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "murho_cu")
public class MurhoCu {
    @Id
    @Column(name = "murho_cu_id")
    private Integer murhoCuId;

    @Column(name = "hvl_cu")
    private Double hvlCu;

    private Double murho;

    @Column(name = "date_updated")
    private Date dateUpdated;

    /**
     * @return murho_cu_id
     */
    public Integer getMurhoCuId() {
        return murhoCuId;
    }

    /**
     * @param murhoCuId
     */
    public void setMurhoCuId(Integer murhoCuId) {
        this.murhoCuId = murhoCuId;
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