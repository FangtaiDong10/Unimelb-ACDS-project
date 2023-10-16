package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "treatment_machine")
public class TreatmentMachine {
    @Id
    @Column(name = "machine_id")
    private String machineId;

    private String manufacturer;

    @Column(name = "unit_model")
    private String unitModel;

    @Column(name = "unit_serial_number")
    private String unitSerialNumber;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "tube_insert_type")
    private String tubeInsertType;

    @Column(name = "tube_serial_number")
    private String tubeSerialNumber;

    /**
     * @return machine_id
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @return unit_model
     */
    public String getUnitModel() {
        return unitModel;
    }

    /**
     * @param unitModel
     */
    public void setUnitModel(String unitModel) {
        this.unitModel = unitModel;
    }

    /**
     * @return unit_serial_number
     */
    public String getUnitSerialNumber() {
        return unitSerialNumber;
    }

    /**
     * @param unitSerialNumber
     */
    public void setUnitSerialNumber(String unitSerialNumber) {
        this.unitSerialNumber = unitSerialNumber;
    }

    /**
     * @return local_name
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * @param localName
     */
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    /**
     * @return tube_insert_type
     */
    public String getTubeInsertType() {
        return tubeInsertType;
    }

    /**
     * @param tubeInsertType
     */
    public void setTubeInsertType(String tubeInsertType) {
        this.tubeInsertType = tubeInsertType;
    }

    /**
     * @return tube_serial_number
     */
    public String getTubeSerialNumber() {
        return tubeSerialNumber;
    }

    /**
     * @param tubeSerialNumber
     */
    public void setTubeSerialNumber(String tubeSerialNumber) {
        this.tubeSerialNumber = tubeSerialNumber;
    }
}