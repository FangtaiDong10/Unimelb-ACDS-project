package kv.pojo.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "bw_diameter")
public class BwDiameter {
    @Id
    private Double diameter;

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
}