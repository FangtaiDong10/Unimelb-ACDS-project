package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bw_hvl_al")
public class BwHvlAl {
    @Id
    @Column(name = "hvl_al")
    private Double hvlAl;

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
}