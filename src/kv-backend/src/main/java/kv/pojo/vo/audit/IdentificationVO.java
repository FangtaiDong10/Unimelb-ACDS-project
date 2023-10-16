package kv.pojo.vo.audit;

import kv.pojo.dbo.*;

import java.util.List;

public class IdentificationVO {
    private AuditInformation auditInformation;
    private FacilityInformation facilityInformation;
    private List<FacilityRepresentative> facilityRepresentativeList;
    private PhysicalAddress physicalAddress;
    private ReportingAddress reportingAddress;
    private TreatmentMachine treatmentMachine;
    private ReferenceDosimetry referenceDosimetry;
    private KvReferenceDosimetry kvReferenceDosimetry;
    private List<ReferenceCone> referenceConeList;
    private List<Cone> coneList;
    private List<AuditBeamInputs> auditBeamInputsList;
    private List<BeamData> beamDataList;

    public AuditInformation getAuditInformation() {
        return auditInformation;
    }

    public void setAuditInformation(AuditInformation auditInformation) {
        this.auditInformation = auditInformation;
    }

    public FacilityInformation getFacilityInformation() {
        return facilityInformation;
    }

    public void setFacilityInformation(FacilityInformation facilityInformation) {
        this.facilityInformation = facilityInformation;
    }

    public List<FacilityRepresentative> getFacilityRepresentativeList() {
        return facilityRepresentativeList;
    }

    public void setFacilityRepresentativeList(List<FacilityRepresentative> facilityRepresentativeList) {
        this.facilityRepresentativeList = facilityRepresentativeList;
    }

    public PhysicalAddress getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public ReportingAddress getReportingAddress() {
        return reportingAddress;
    }

    public void setReportingAddress(ReportingAddress reportingAddress) {
        this.reportingAddress = reportingAddress;
    }

    public TreatmentMachine getTreatmentMachine() {
        return treatmentMachine;
    }

    public void setTreatmentMachine(TreatmentMachine treatmentMachine) {
        this.treatmentMachine = treatmentMachine;
    }

    public ReferenceDosimetry getReferenceDosimetry() {
        return referenceDosimetry;
    }

    public void setReferenceDosimetry(ReferenceDosimetry referenceDosimetry) {
        this.referenceDosimetry = referenceDosimetry;
    }

    public KvReferenceDosimetry getKvReferenceDosimetry() {
        return kvReferenceDosimetry;
    }

    public void setKvReferenceDosimetry(KvReferenceDosimetry kvReferenceDosimetry) {
        this.kvReferenceDosimetry = kvReferenceDosimetry;
    }

    public List<ReferenceCone> getReferenceConeList() {
        return referenceConeList;
    }

    public void setReferenceConeList(List<ReferenceCone> referenceConeList) {
        this.referenceConeList = referenceConeList;
    }

    public List<Cone> getConeList() {
        return coneList;
    }

    public void setConeList(List<Cone> coneList) {
        this.coneList = coneList;
    }

    public List<AuditBeamInputs> getAuditBeamInputsList() {
        return auditBeamInputsList;
    }

    public void setAuditBeamInputsList(List<AuditBeamInputs> auditBeamInputsList) {
        this.auditBeamInputsList = auditBeamInputsList;
    }

    public List<BeamData> getBeamDataList() {
        return beamDataList;
    }

    public void setBeamDataList(List<BeamData> beamDataList) {
        this.beamDataList = beamDataList;
    }
}
