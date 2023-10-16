package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "physical_address")
public class PhysicalAddress {
    @Id
    @Column(name = "physical_address_id")
    private String physicalAddressId;

    @Column(name = "facility_id")
    private String facilityId;

    @Column(name = "input_date")
    private Date inputDate;

    private String building;

    private String street;

    private String suburb;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "post_code")
    private String postCode;

    /**
     * @return physical_address_id
     */
    public String getPhysicalAddressId() {
        return physicalAddressId;
    }

    /**
     * @param physicalAddressId
     */
    public void setPhysicalAddressId(String physicalAddressId) {
        this.physicalAddressId = physicalAddressId;
    }

    /**
     * @return facility_id
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     * @param facilityId
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * @return input_date
     */
    public Date getInputDate() {
        return inputDate;
    }

    /**
     * @param inputDate
     */
    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    /**
     * @return building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * @param building
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return suburb
     */
    public String getSuburb() {
        return suburb;
    }

    /**
     * @param suburb
     */
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    /**
     * @return state_name
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * @param stateName
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * @return post_code
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postCode
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}