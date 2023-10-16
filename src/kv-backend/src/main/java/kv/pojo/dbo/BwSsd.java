package kv.pojo.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bw_ssd")
public class BwSsd {
    @Id
    private Double ssd;

    private String ref;

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
     * @return ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref
     */
    public void setRef(String ref) {
        this.ref = ref;
    }
}