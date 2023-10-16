package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "input_data_front_end")
public class InputDataFrontEnd {
    @Id
    @Column(name = "audit_id")
    private String auditId;

    private String table1;

    private String table2;

    @Column(name = "KS_table_json")
    private String ksTableJson;

    /**
     * @return audit_id
     */
    public String getAuditId() {
        return auditId;
    }

    /**
     * @param auditId
     */
    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    /**
     * @return table1
     */
    public String getTable1() {
        return table1;
    }

    /**
     * @param table1
     */
    public void setTable1(String table1) {
        this.table1 = table1;
    }

    /**
     * @return table2
     */
    public String getTable2() {
        return table2;
    }

    /**
     * @param table2
     */
    public void setTable2(String table2) {
        this.table2 = table2;
    }

    /**
     * @return KS_table_json
     */
    public String getKsTableJson() {
        return ksTableJson;
    }

    /**
     * @param ksTableJson
     */
    public void setKsTableJson(String ksTableJson) {
        this.ksTableJson = ksTableJson;
    }
}