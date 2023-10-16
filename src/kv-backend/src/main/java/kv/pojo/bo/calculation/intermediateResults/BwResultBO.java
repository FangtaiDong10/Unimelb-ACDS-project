package kv.pojo.bo.calculation.intermediateResults;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BwResultBO {
    private String id;
    private Map<String,Double> data;
    /**
    example:
    {'ACDS-kV-5014-beam1_ACDS-kV-5014-cone1': {'Bw_Al': 1.1804400000000002,'Bw_Combined': 1.1804400000000002},
 'ACDS-kV-5014-beam2_ACDS-kV-5014-cone1': {'Bw_Al': 1.25726, 'Bw_Combined': 1.25726},
 'ACDS-kV-5014-beam3_ACDS-kV-5014-cone1': {'Bw_Al': 1.2908600000000001,'Bw_Combined': 1.2908600000000001},
 'ACDS-kV-5014-beam4_ACDS-kV-5014-cone1': {'Bw_Al': 1.3512300000000002, 'Bw_Cu': 1.35205,'Bw_Combined': 1.3516400000000002},
     ...}
     */
    public BwResultBO() {
    }
    public BwResultBO(String id) {
        this.id = id;
    }
    public BwResultBO(String id, Map<String, Double> data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
    }
    public synchronized void addData(String itemName, Double data){
        this.data.put(itemName,data);
    }
    public synchronized void removeData(String itemName, Double data){
        this.data.put(itemName,data);
    }

    @Override
    public String toString(){
        StringJoiner bwResultJoiner = new StringJoiner("\n");
        bwResultJoiner.add(id);
        bwResultJoiner.add(data.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
                    .collect(Collectors.joining("|")));
        return bwResultJoiner.toString();
    }
}
