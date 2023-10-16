package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "chambers_list")
public class ChambersList {
    @Id
    @Column(name = "chamber_SN")
    private String chamberSn;

    @Column(name = "chamber_name")
    private String chamberName;

    @Column(name = "chamber_type")
    private String chamberType;

    /**
     * @return chamber_SN
     */
    public String getChamberSn() {
        return chamberSn;
    }

    /**
     * @param chamberSn
     */
    public void setChamberSn(String chamberSn) {
        this.chamberSn = chamberSn;
    }

    /**
     * @return chamber_name
     */
    public String getChamberName() {
        return chamberName;
    }

    /**
     * @param chamberName
     */
    public void setChamberName(String chamberName) {
        this.chamberName = chamberName;
    }

    /**
     * @return chamber_type
     */
    public String getChamberType() {
        return chamberType;
    }

    /**
     * @param chamberType
     */
    public void setChamberType(String chamberType) {
        this.chamberType = chamberType;
    }
}