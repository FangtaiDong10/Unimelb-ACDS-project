package kv.pojo.bo.calculation.intermediateResults;

public class CccResultBO {
    private String id;
    private double kClosedCone;

    public CccResultBO() {
    }

    public CccResultBO(String id, double kClosedCone) {
        this.id = id;
        this.kClosedCone = kClosedCone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getkClosedCone() {
        return kClosedCone;
    }

    public void setkClosedCone(double kClosedCone) {
        this.kClosedCone = kClosedCone;
    }
}
