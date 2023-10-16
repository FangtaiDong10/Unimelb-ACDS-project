package kv.pojo.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "facility_information")
public class FacilityInformation {
    @Id
    @Column(name = "facility_id")
    private String facilityId;

    @Column(name = "organisation_name")
    private String organisationName;

    @Column(name = "oncology_service")
    private String oncologyService;

    @Column(name = "oncology_facility")
    private String oncologyFacility;

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
     * @return organisation_name
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * @param organisationName
     */
    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    /**
     * @return oncology_service
     */
    public String getOncologyService() {
        return oncologyService;
    }

    /**
     * @param oncologyService
     */
    public void setOncologyService(String oncologyService) {
        this.oncologyService = oncologyService;
    }

    /**
     * @return oncology_facility
     */
    public String getOncologyFacility() {
        return oncologyFacility;
    }

    /**
     * @param oncologyFacility
     */
    public void setOncologyFacility(String oncologyFacility) {
        this.oncologyFacility = oncologyFacility;
    }
}