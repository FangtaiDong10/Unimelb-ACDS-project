package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bw_hvl_cu")
public class BwHvlCu {
    @Id
    @Column(name = "hvl_cu")
    private Double hvlCu;

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
}