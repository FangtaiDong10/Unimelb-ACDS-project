package kv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import kv.common.KVException;
import kv.common.KVPageInfo;
import kv.config.MainConfig;
import kv.mapper.*;
import kv.pojo.bo.excel.*;
import kv.pojo.dbo.*;
import kv.pojo.vo.audit.AuditCaseVO;
import kv.pojo.vo.audit.IdentificationVO;
import kv.pojo.vo.audit.WarningVO;
import kv.pojo.vo.calculation.BeamInfoVO;
import kv.pojo.vo.calculation.WorkDataSheet1VO;
import kv.pojo.vo.calculation.WorkDataSheet2VO;
import kv.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuditService {
    private final SimpleDateFormat simpleDateFormat;
    private final TreatmentMachineMapper treatmentMachineMapper;
    private final FacilityInformationMapper facilityInformationMapper;
    private final PhysicalAddressMapper physicalAddressMapper;
    private final ReportingAddressMapper reportingAddressMapper;
    private final FacilityRepresentativeMapper facilityRepresentativeMapper;
    private final AuditInformationMapper auditInformationMapper;
    private final ConeMapper coneMapper;
    private final ReferenceConeMapper referenceConeMapper;
    private final BeamDataMapper beamDataMapper;
    private final AuditBeamInputsMapper auditBeamInputsMapper;
    private final ReferenceDosimetryMapper referenceDosimetryMapper;
    private final KvReferenceDosimetryMapper kvReferenceDosimetryMapper;
    private final AuditHistoryMapper auditHistoryMapper;
    private final BackResultMapper backResultMapper;
    private final InputDataFrontEndMapper inputDataFrontEndMapper;
    private final CustomMapper customMapper;
    private final ObjectMapper objectMapper;

    public AuditService(SimpleDateFormat simpleDateFormat,
                        TreatmentMachineMapper treatmentMachineMapper,
                        FacilityInformationMapper facilityInformationMapper,
                        PhysicalAddressMapper physicalAddressMapper,
                        ReportingAddressMapper reportingAddressMapper,
                        FacilityRepresentativeMapper facilityRepresentativeMapper,
                        AuditInformationMapper auditInformationMapper,
                        ConeMapper coneMapper,
                        ReferenceConeMapper referenceConeMapper,
                        BeamDataMapper beamDataMapper,
                        AuditBeamInputsMapper auditBeamInputsMapper,
                        ReferenceDosimetryMapper referenceDosimetryMapper,
                        KvReferenceDosimetryMapper kvReferenceDosimetryMapper,
                        AuditHistoryMapper auditHistoryMapper,
                        BackResultMapper backResultMapper,
                        InputDataFrontEndMapper inputDataFrontEndMapper,
                        CustomMapper customMapper,
                        ObjectMapper objectMapper) {
        this.simpleDateFormat = simpleDateFormat;
        this.treatmentMachineMapper = treatmentMachineMapper;
        this.facilityInformationMapper = facilityInformationMapper;
        this.physicalAddressMapper = physicalAddressMapper;
        this.reportingAddressMapper = reportingAddressMapper;
        this.facilityRepresentativeMapper = facilityRepresentativeMapper;
        this.auditInformationMapper = auditInformationMapper;
        this.coneMapper = coneMapper;
        this.referenceConeMapper = referenceConeMapper;
        this.beamDataMapper = beamDataMapper;
        this.auditBeamInputsMapper = auditBeamInputsMapper;
        this.referenceDosimetryMapper = referenceDosimetryMapper;
        this.kvReferenceDosimetryMapper = kvReferenceDosimetryMapper;
        this.auditHistoryMapper = auditHistoryMapper;
        this.backResultMapper = backResultMapper;
        this.inputDataFrontEndMapper = inputDataFrontEndMapper;
        this.customMapper = customMapper;
        this.objectMapper = objectMapper;
    }

    public AuditInfoBO readAuditInfo(XSSFSheet sheet) {
        AuditInfoBO auditInfoBO = new AuditInfoBO();
        Class<? extends AuditInfoBO> auditInfoBOClass = auditInfoBO.getClass();

        for (String fieldName : MainConfig.auditInfoCellAddressMap.keySet()) {
            try {
                Field field = auditInfoBOClass.getDeclaredField(fieldName);
                field.setAccessible(true);

                XSSFCell cell = ExcelUtils.getCell(sheet, MainConfig.auditInfoCellAddressMap.get(fieldName));
                if (MainConfig.FIELD_NAME_AUDIT_DATE.equals(fieldName)) {
                    Date date = ExcelUtils.getJavaDate(cell);
                    field.set(auditInfoBO, date);
                } else {
                    field.set(auditInfoBO, cell.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                KVException.display();
            }
        }

        return auditInfoBO;
    }

    public List<RefConeBO> readRefCone(XSSFSheet sheet) {
        List<RefConeBO> refConeBOList = new ArrayList<>();
        Class<RefConeBO> refConeBOClass = RefConeBO.class;

        // get audit number
        String acdsAuditNumber = ExcelUtils.getCell(sheet,
                MainConfig.auditInfoCellAddressMap.get(MainConfig.FIELD_NAME_ACDS_AUDIT_NUMBER)).toString();

        for (int i = 0; i < MainConfig.REF_CONE_ROW_COUNT; i++) {
            int row = MainConfig.REF_CONE_ROW_START + i;

            // Check if the row has data before reading.
            String beamIdAltCol = MainConfig.REF_CONE_BEAM_ID_ALT_COL;
            String beamIdAltAddress = beamIdAltCol + row;
            XSSFCell beamIdAltCell = ExcelUtils.getCell(sheet, beamIdAltAddress);
            if (ExcelUtils.isCellEmpty(beamIdAltCell)) continue;

            // start read RefConeBO
            RefConeBO refConeBO = new RefConeBO();

            // set beamId
            refConeBO.setBeamId(acdsAuditNumber + "-beam-" + (i + 1));

            // set other fields
            for (String fieldName : MainConfig.refConeColMap.keySet()) {
                String col = MainConfig.refConeColMap.get(fieldName);
                String address = col + row;

                try {
                    Field field = refConeBOClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    XSSFCell cell = ExcelUtils.getCell(sheet, address);
                    field.set(refConeBO, cell.toString());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    KVException.display();
                }
            }

            refConeBOList.add(refConeBO);
        }

        return refConeBOList;
    }

    public List<CommentBO> readRefConeComments(XSSFSheet sheet) {
        List<CommentBO> commentBOList = new ArrayList<>();

        for (int i = 0; i < MainConfig.REF_CONE_COMMENTS_ROW_COUNT; i++) {
            int row = MainConfig.REF_CONE_COMMENTS_ROW_START + i;
            String address = MainConfig.REF_CONE_COMMENTS_COL + row;
            XSSFCell cell = ExcelUtils.getCell(sheet, address);

            if (ExcelUtils.isCellEmpty(cell)) continue;

            CommentBO commentBO = new CommentBO();
            commentBO.setComment(cell.toString());
            commentBO.setSource(MainConfig.REF_CONE_COMMENTS_SOURCE);
            commentBOList.add(commentBO);
        }

        return commentBOList;
    }

    public List<ConeBO> readCone(XSSFSheet sheet) {
        List<ConeBO> coneBOList = new ArrayList<>(MainConfig.CONE_COUNT);

        // get audit number
        String acdsAuditNumber = ExcelUtils.getCell(sheet,
                MainConfig.auditInfoCellAddressMap.get(MainConfig.FIELD_NAME_ACDS_AUDIT_NUMBER)).toString();

        for (int i = 0; i < MainConfig.CONE_COUNT; i++) {
            int colIdx = ExcelUtils.getColIdx(MainConfig.CONE_COL_START) + 2 * i;

            // coneIdAlt
            XSSFCell coneIdAltCell = sheet.getRow(MainConfig.CONE_ID_ALT_ROW - 1).getCell(colIdx++);
            if (ExcelUtils.isCellEmpty(coneIdAltCell) ||
                    StringUtils.startsWithIgnoreCase(coneIdAltCell.toString(), MainConfig.CONE_ID_ALT_ORIGIN))
                continue;   // go to next cone

            ConeBO coneBO = new ConeBO();
            coneBO.setConeIdAlt(coneIdAltCell.toString());

            // coneId
            String coneId = acdsAuditNumber + "-cone-" + (i + 1);
            coneBO.setConeId(coneId);

            // openClosed
            String openClosed = sheet.getRow(MainConfig.CONE_OPEN_CLOSED_ROW - 1).getCell(colIdx).toString();
            coneBO.setOpenClosed(openClosed);

            // endThickness
            String endThickness =
                    sheet.getRow(MainConfig.CONE_END_THICKNESS_ROW - 1).getCell(colIdx).toString();
            coneBO.setEndThickness(endThickness);

            // shape and related
            String shape = sheet.getRow(MainConfig.CONE_SHAPE_ROW - 1).getCell(colIdx).toString();
            coneBO.setShape(shape);

            MainConfig.ConeShape shapeEnum = MainConfig.ConeShape.valueOf(shape);
            if (shapeEnum == MainConfig.ConeShape.Circular) {
                // fieldDiameter
                String fieldDiameter =
                        sheet.getRow(MainConfig.CONE_FIELD_DIAMETER_ROW - 1).getCell(colIdx).toString();
                coneBO.setFieldDiameter(fieldDiameter);
            } else if (shapeEnum == MainConfig.ConeShape.Square) {
                // fieldArea
                String fieldArea = sheet.getRow(MainConfig.CONE_FIELD_AREA_ROW - 1).getCell(colIdx).toString();
                coneBO.setFieldArea(fieldArea);
            } else if (shapeEnum == MainConfig.ConeShape.Rectangular) {
                // fieldDimension 1 & 2
                String fieldDimension1 =
                        sheet.getRow(MainConfig.CONE_FIELD_DIMENSION1_ROW - 1).getCell(colIdx).toString();
                String fieldDimension2 =
                        sheet.getRow(MainConfig.CONE_FIELD_DIMENSION2_ROW - 1).getCell(colIdx).toString();
                coneBO.setFieldDimension1(fieldDimension1);
                coneBO.setFieldDimension2(fieldDimension2);
            }

            // ssd
            String ssd = sheet.getRow(MainConfig.CONE_SSD_ROW - 1).getCell(colIdx).toString();
            coneBO.setSsd(ssd);

            // doseOutputUnit
            String doseOutputUnit =
                    sheet.getRow(MainConfig.CONE_DOSE_OUTPUT_UNIT_ROW - 1).getCell(colIdx).toString();
            coneBO.setDoseOutputUnit(doseOutputUnit);

            // beamsData
            coneBO.setBeamsData(readConeBean(sheet, colIdx - 1));

            coneBOList.add(coneBO);
        }

        return coneBOList;
    }

    private List<ConeBeanBO> readConeBean(XSSFSheet sheet, int colIdx) {
        List<ConeBeanBO> coneBeanBOList = new ArrayList<>(MainConfig.CONE_BEAM_ROW_COUNT);

        // get audit number
        String acdsAuditNumber = ExcelUtils.getCell(sheet,
                MainConfig.auditInfoCellAddressMap.get(MainConfig.FIELD_NAME_ACDS_AUDIT_NUMBER)).toString();

        for (int i = 0; i < MainConfig.CONE_BEAM_ROW_COUNT; i++) {
            int row = MainConfig.CONE_BEAM_ROW_START + i;

            // dospSsd
            XSSFCell dospSsdCell = sheet.getRow(row - 1).getCell(colIdx);
            if (ExcelUtils.isCellEmpty(dospSsdCell)) continue;

            // nomDoseOutput
            XSSFCell nomDoseOutputCell = sheet.getRow(row - 1).getCell(colIdx + 1);
            if (ExcelUtils.isCellEmpty(nomDoseOutputCell)) continue;

            ConeBeanBO coneBeanBO = new ConeBeanBO();
            // beamId
            coneBeanBO.setBeamId(acdsAuditNumber + "-beam-" + (i + 1));
            coneBeanBO.setDospSsd(dospSsdCell.toString());
            coneBeanBO.setNomDoseOutput(nomDoseOutputCell.toString());

            coneBeanBOList.add(coneBeanBO);
        }

        return coneBeanBOList;
    }

    public List<BeamQualityBO> readBeamQuality(XSSFSheet sheet) {
        List<BeamQualityBO> beamQualityBOList = new ArrayList<>(MainConfig.BEAM_QUALITY_ROW_COUNT);
        Class<BeamQualityBO> beamQualityBOClass = BeamQualityBO.class;

        // get audit number
        String acdsAuditNumber = ExcelUtils.getCell(sheet,
                MainConfig.auditInfoCellAddressMap.get(MainConfig.FIELD_NAME_ACDS_AUDIT_NUMBER)).toString();

        for (int i = 0; i < MainConfig.BEAM_QUALITY_ROW_COUNT; i++) {
            int row = MainConfig.BEAM_QUALITY_ROW_START + i;

            // Skip if the row has no data
            String beamIdAltAddress = MainConfig.REF_CONE_BEAM_ID_ALT_COL + (MainConfig.REF_CONE_ROW_START + i);
            XSSFCell beamIdAltCell = ExcelUtils.getCell(sheet, beamIdAltAddress);
            if (ExcelUtils.isCellEmpty(beamIdAltCell)) continue;

            BeamQualityBO beamQualityBO = new BeamQualityBO();
            // set beamId
            beamQualityBO.setBeamId(acdsAuditNumber + "-beam-" + (i + 1));

            // set other fields
            for (String fieldName : MainConfig.beamQualityColMap.keySet()) {
                String col = MainConfig.beamQualityColMap.get(fieldName);

                try {
                    Field field = beamQualityBOClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    XSSFCell cell = ExcelUtils.getCell(sheet, col + row);
                    field.set(beamQualityBO, cell.toString());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    KVException.display();
                }
            }

            beamQualityBOList.add(beamQualityBO);
        }

        return beamQualityBOList;
    }

    public TreatmentMachine getTreatmentMachine(AuditInfoBO auditInfoBO) {
        // manufacturer
        String manufacturer = auditInfoBO.getMachineKvUnitManufacturer();
        if (!StringUtils.hasText(manufacturer)) KVException.display();

        // unitSerialNumber
        String unitSerialNumber = auditInfoBO.getMachineSn();
        if (!StringUtils.hasText(unitSerialNumber)) KVException.display();

        // machineId
        String machineId = manufacturer + "-" + unitSerialNumber;

        // unitModel
        String unitModel = auditInfoBO.getMachineKvUnitModel();
        if (!StringUtils.hasText(unitModel)) KVException.display();

        // localName
        String localName = auditInfoBO.getMachineLocalName();
        if (!StringUtils.hasText(localName)) KVException.display();

        // tubeInsertType
        String tubeInsertType = auditInfoBO.getMachineTubeInsertType();
        if (!StringUtils.hasText(tubeInsertType)) KVException.display();

        // tubeSerialNumber
        String tubeSerialNumber = auditInfoBO.getMachineTubeInsertSn();
        if (!StringUtils.hasText(tubeSerialNumber)) KVException.display();

        TreatmentMachine treatmentMachine = new TreatmentMachine();
        treatmentMachine.setMachineId(machineId);
        treatmentMachine.setManufacturer(manufacturer);
        treatmentMachine.setUnitModel(unitModel);
        treatmentMachine.setUnitSerialNumber(unitSerialNumber);
        treatmentMachine.setLocalName(localName);
        treatmentMachine.setTubeInsertType(tubeInsertType);
        treatmentMachine.setTubeSerialNumber(tubeSerialNumber);

        return treatmentMachine;
    }

    public FacilityInformation getFacilityInformation(AuditInfoBO auditInfoBO) {
        // facilityId
        String facilityId = auditInfoBO.getAcdsFacilityId();
        if (!StringUtils.hasText(facilityId)) KVException.display();

        // organisationName
        String organisationName = auditInfoBO.getRadiationOncologyOrg();
        if (!StringUtils.hasText(organisationName)) KVException.display();

        // oncologyService
        String oncologyService = auditInfoBO.getRadiationOncologyService();
        if (!StringUtils.hasText(oncologyService)) KVException.display();

        // oncologyFacility
        String oncologyFacility = auditInfoBO.getRadiationOncologyFacility();
        if (!StringUtils.hasText(oncologyFacility)) KVException.display();

        FacilityInformation facilityInformation = new FacilityInformation();
        facilityInformation.setFacilityId(facilityId);
        facilityInformation.setOrganisationName(organisationName);
        facilityInformation.setOncologyService(oncologyService);
        facilityInformation.setOncologyFacility(oncologyFacility);

        return facilityInformation;
    }

    public PhysicalAddress getPhysicalAddress(AuditInfoBO auditInfoBO, FacilityInformation facilityInformation) {
        // facilityId
        String facilityId = facilityInformation.getFacilityId();

        // inputDate
        Date inputDate = auditInfoBO.getAuditDate();
        if (inputDate == null) KVException.display();

        // physicalAddressId
        String physicalAddressId = facilityId + "-" + simpleDateFormat.format(inputDate);

        // building
        String building = auditInfoBO.getPhysAddressLoc();
        if (!StringUtils.hasText(building)) KVException.display();

        // street
        String street = auditInfoBO.getPhysAddressSt();
        if (!StringUtils.hasText(street)) KVException.display();

        // suburb
        String suburb = auditInfoBO.getPhysAddressSuburb();
        if (!StringUtils.hasText(suburb)) KVException.display();

        // stateName
        String stateName = auditInfoBO.getPhysAddressState();
        if (!StringUtils.hasText(stateName)) KVException.display();

        // postCode
        String postCode = auditInfoBO.getPhysAddressPostcode();
        if (!StringUtils.hasText(postCode)) KVException.display();
        postCode = postCode.replace(".0", "");

        PhysicalAddress physicalAddress = new PhysicalAddress();
        physicalAddress.setPhysicalAddressId(physicalAddressId);
        physicalAddress.setFacilityId(facilityId);
        physicalAddress.setInputDate(inputDate);
        physicalAddress.setBuilding(building);
        physicalAddress.setStreet(street);
        physicalAddress.setSuburb(suburb);
        physicalAddress.setStateName(stateName);
        physicalAddress.setPostCode(postCode);

        return physicalAddress;
    }

    public ReportingAddress getReportingAddress(AuditInfoBO auditInfoBO, FacilityInformation facilityInformation) {
        // facilityId
        String facilityId = facilityInformation.getFacilityId();

        // inputDate
        Date inputDate = auditInfoBO.getAuditDate();
        if (inputDate == null) KVException.display();

        // reportingAddressId
        String reportingAddressId = facilityId + "-" + simpleDateFormat.format(inputDate);

        // building
        String building = auditInfoBO.getReportingAddressLoc();
        if (!StringUtils.hasText(building)) KVException.display();

        // street
        String street = auditInfoBO.getReportingAddressSt();
        if (!StringUtils.hasText(street)) KVException.display();

        // suburb
        String suburb = auditInfoBO.getReportingAddressSuburb();
        if (!StringUtils.hasText(suburb)) KVException.display();

        // stateName
        String stateName = auditInfoBO.getReportingAddressState();
        if (!StringUtils.hasText(stateName)) KVException.display();

        // postCode
        String postCode = auditInfoBO.getReportingAddressPostcode();
        if (!StringUtils.hasText(postCode)) KVException.display();
        postCode = postCode.replace(".0", "");

        ReportingAddress reportingAddress = new ReportingAddress();
        reportingAddress.setReportingAddressId(reportingAddressId);
        reportingAddress.setFacilityId(facilityId);
        reportingAddress.setInputDate(inputDate);
        reportingAddress.setBuilding(building);
        reportingAddress.setStreet(street);
        reportingAddress.setSuburb(suburb);
        reportingAddress.setStateName(stateName);
        reportingAddress.setPostCode(postCode);

        return reportingAddress;
    }

    public List<FacilityRepresentative> getFacilityRepresentative(AuditInfoBO auditInfoBO, FacilityInformation facilityInformation) {
        // TODO support multiple representatives
        List<FacilityRepresentative> facilityRepresentativeList = new ArrayList<>();

        boolean hasRep2 = false;

        // facilityId
        String facilityId = facilityInformation.getFacilityId();

        // role
        String role1 = auditInfoBO.getFacilityRepRole1();
        String role2 = auditInfoBO.getFacilityRepRole2();
        if (!StringUtils.hasText(role1)) KVException.display();
        if (StringUtils.hasText(role2)) hasRep2 = true;

        // representativeId
        String representativeId1 = facilityId + "-" + role1;
        String representativeId2 = hasRep2 ? facilityId + "-" + role2 : null;

        // title
        String title1 = auditInfoBO.getFacilityRepTitle1();
        String title2 = auditInfoBO.getFacilityRepTitle2();
        if (!StringUtils.hasText(title1)) KVException.display();
        if (hasRep2 && !StringUtils.hasText(title2)) KVException.display();

        // firstName
        String firstName1 = auditInfoBO.getFacilityRepFName1();
        String firstName2 = auditInfoBO.getFacilityRepFName2();
        if (!StringUtils.hasText(firstName1)) KVException.display();
        if (hasRep2 && !StringUtils.hasText(firstName2)) KVException.display();

        // lastName
        String lastName1 = auditInfoBO.getFacilityRepLName1();
        String lastName2 = auditInfoBO.getFacilityRepLName2();
        if (!StringUtils.hasText(lastName1)) KVException.display();
        if (hasRep2 && !StringUtils.hasText(lastName2)) KVException.display();

        // phone
        String phone1 = auditInfoBO.getFacilityRepPhone1();
        String phone2 = auditInfoBO.getFacilityRepPhone2();
        if (!StringUtils.hasText(phone1)) KVException.display();
        if (hasRep2 && !StringUtils.hasText(phone2)) KVException.display();

        // email
        String email1 = auditInfoBO.getFacilityRepEmail1();
        String email2 = auditInfoBO.getFacilityRepEmail2();
        if (!StringUtils.hasText(email1)) KVException.display();
        if (hasRep2 && !StringUtils.hasText(email2)) KVException.display();

        FacilityRepresentative facilityRepresentative1 = new FacilityRepresentative();
        facilityRepresentative1.setRepresentativeId(representativeId1);
        facilityRepresentative1.setFacilityId(facilityId);
        facilityRepresentative1.setRole(role1);
        facilityRepresentative1.setTitle(title1);
        facilityRepresentative1.setFirstName(firstName1);
        facilityRepresentative1.setLastName(lastName1);
        facilityRepresentative1.setPhone(phone1);
        facilityRepresentative1.setEmail(email1);
        facilityRepresentativeList.add(facilityRepresentative1);

        if (!hasRep2) return facilityRepresentativeList;

        FacilityRepresentative facilityRepresentative2 = new FacilityRepresentative();
        facilityRepresentative2.setRepresentativeId(representativeId2);
        facilityRepresentative2.setFacilityId(facilityId);
        facilityRepresentative2.setRole(role2);
        facilityRepresentative2.setTitle(title2);
        facilityRepresentative2.setFirstName(firstName2);
        facilityRepresentative2.setLastName(lastName2);
        facilityRepresentative2.setPhone(phone2);
        facilityRepresentative2.setEmail(email2);
        facilityRepresentativeList.add(facilityRepresentative2);

        return facilityRepresentativeList;
    }

    public AuditInformation getAuditInformation(AuditInfoBO auditInfoBO,
                                                FacilityInformation facilityInformation,
                                                TreatmentMachine treatmentMachine) {
        // auditId
        String auditId = auditInfoBO.getAcdsAuditNumber();
        if (!StringUtils.hasText(auditId)) KVException.display();

        // facilityId
        String facilityId = facilityInformation.getFacilityId();

        // machineId
        String machineId = treatmentMachine.getMachineId();

        // date
        Date date = auditInfoBO.getAuditDate();
        if (date == null) KVException.display();

        AuditInformation auditInformation = new AuditInformation();
        auditInformation.setAuditId(auditId);
        auditInformation.setFacilityId(facilityId);
        auditInformation.setMachineId(machineId);
        auditInformation.setDate(date);

        return auditInformation;
    }

    public List<Cone> getCone(List<ConeBO> coneBOList) {
        List<Cone> coneList = new ArrayList<>(coneBOList.size());

        for (ConeBO coneBO : coneBOList) {
            // nullable fields
            Double fieldDiameter = null;
            Double fieldArea = null;
            Double fieldDimension1 = null;
            Double fieldDimension2 = null;
            Double endThickness = null;

            // coneId
            String coneId = coneBO.getConeId();
            if (!StringUtils.hasText(coneId)) KVException.display();

            // coneIdAlt
            String coneIdAlt = coneBO.getConeIdAlt();
            if (!StringUtils.hasText(coneIdAlt)) KVException.display();

            // shape
            String shape = coneBO.getShape();
            MainConfig.ConeShape shapeEnum = MainConfig.ConeShape.valueOf(shape);
            if (shapeEnum == MainConfig.ConeShape.Circular) {
                // fieldDiameter
                fieldDiameter = Double.parseDouble(coneBO.getFieldDiameter());
            } else if (shapeEnum == MainConfig.ConeShape.Square) {
                // fieldArea
                fieldArea = Double.parseDouble(coneBO.getFieldArea());
            } else if (shapeEnum == MainConfig.ConeShape.Rectangular) {
                // fieldDimension 1 & 2
                fieldDimension1 = Double.parseDouble(coneBO.getFieldDimension1());
                fieldDimension2 = Double.parseDouble(coneBO.getFieldDimension2());
            }

            // openClosed
            String openClosed = coneBO.getOpenClosed();
            MainConfig.ConeOpenClosed openClosedEnum = MainConfig.ConeOpenClosed.valueOf(openClosed);
            if (openClosedEnum == MainConfig.ConeOpenClosed.Closed) {
                // endThickness
                endThickness = Double.parseDouble(coneBO.getEndThickness());
            }

            // ssd
            double ssd = Double.parseDouble(coneBO.getSsd());

            Cone cone = new Cone();
            // not null fields
            cone.setConeId(coneId);
            cone.setConeIdAlt(coneIdAlt);
            cone.setShape(shape);
            cone.setOpenClosed(openClosed);
            cone.setSsd(ssd);
            // nullable fields
            cone.setFieldArea(fieldArea);
            cone.setFieldDimension1(fieldDimension1);
            cone.setFieldDimension2(fieldDimension2);
            cone.setFieldDiameter(fieldDiameter);
            cone.setEndThickness(endThickness);

            coneList.add(cone);
        }

        return coneList;
    }

    public List<ReferenceCone> getReferenceCone(List<RefConeBO> refConeBOList, AuditInformation auditInformation) {
        List<ReferenceCone> referenceConeList = new ArrayList<>(refConeBOList.size());

        int i = 1;
        for (RefConeBO refConeBO : refConeBOList) {
            // referenceConeIdAlt
            String referenceConeIdAlt = refConeBO.getBeamIdAlt();
            if (!StringUtils.hasText(referenceConeIdAlt)) KVException.display();

            // referenceConeId
            String referenceConeId = auditInformation.getAuditId() + "-refcone-" + i++;

            // nomEnergy
            double nomEnergy = Double.parseDouble(refConeBO.getNomEnergy());

            // ssd
            double ssd = Double.parseDouble(refConeBO.getMeasSsd());

            // depth
            double depth = Double.parseDouble(refConeBO.getMeasDepth());

            // geometryShape
            String geometryShape = refConeBO.getGeomShape();
            if (!StringUtils.hasText(geometryShape)) KVException.display();

            // geometryMeasurement
            double geometryMeasurement = 0;
            String geomSize = refConeBO.getGeomSize().replaceAll("[^\\d.]", "");
            if (StringUtils.hasText(geomSize)) {
                geometryMeasurement = Double.parseDouble(geomSize);
            }

            // openClosed
            String openClosed = MainConfig.ConeOpenClosed.valueOf(refConeBO.getClosedOpen()).toString();

            // thickness
            double thickness = 0;
            String thicknessStr = refConeBO.getThickness().replaceAll("[^\\d.]", "");
            if (StringUtils.hasText(thicknessStr)) {
                thickness = Double.parseDouble(thicknessStr);
            }

            // dospSsd
            String dospSsd = MainConfig.RefConeDospSsd.valueOf(refConeBO.getDospSsdZ().toLowerCase()).toString();

            ReferenceCone referenceCone = new ReferenceCone();
            referenceCone.setReferenceConeId(referenceConeId);
            referenceCone.setReferenceConeIdAlt(referenceConeIdAlt);
            referenceCone.setNomEnergy(nomEnergy);
            referenceCone.setSsd(ssd);
            referenceCone.setDepth(depth);
            referenceCone.setGeometryShape(geometryShape);
            referenceCone.setGeometryMeasurement(geometryMeasurement);
            referenceCone.setOpenClosed(openClosed);
            referenceCone.setThickness(thickness);
            referenceCone.setDospSsd(dospSsd);
            referenceConeList.add(referenceCone);
        }

        return referenceConeList;
    }

    public List<BeamData> getBeamData(List<BeamQualityBO> beamQualityBOList, List<ReferenceCone> referenceConeList) {
        List<BeamData> beamDataList = new ArrayList<>(beamQualityBOList.size());

        for (int i = 0; i < beamQualityBOList.size(); i++) {
            BeamQualityBO beamQualityBO = beamQualityBOList.get(i);
            ReferenceCone referenceCone = referenceConeList.get(i);

            // nullable fields
            Double hvlNominalMmAl = null;
            Double hvlNominalMmCu = null;
            Double hvlMeasuredMmAl = null;
            Double hvlMeasuredMmCu = null;

            // beamId
            String beamId = beamQualityBO.getBeamId();
            if (!StringUtils.hasText(beamId)) KVException.display();

            // referenceConeId
            String referenceConeId = referenceCone.getReferenceConeId();

            // beamIdAlt
            String beamIdAlt = referenceCone.getReferenceConeIdAlt();

            // nomEnergy
            Double nomEnergy = referenceCone.getNomEnergy();

            // scd
            String scd = beamQualityBO.getScd();
            if (!StringUtils.hasText(scd)) KVException.display();

            // fieldSizeAtScd
            String fieldSizeAtScd = beamQualityBO.getFieldSizeAtScd();
            if (!StringUtils.hasText(fieldSizeAtScd)) KVException.display();

            // hvlNominalMmAl
            try {
                hvlNominalMmAl = Double.valueOf(beamQualityBO.getHvlNominalMmAl());
            } catch (Exception ignored) {
            }

            // hvlNominalMmCu
            try {
                hvlNominalMmCu = Double.valueOf(beamQualityBO.getHvlNominalMmCu());
            } catch (Exception ignored) {
            }

            // hvlMeasuredMmAl
            try {
                hvlMeasuredMmAl = Double.valueOf(beamQualityBO.getHvlMeasuredMmAl());
            } catch (Exception ignored) {
            }

            // hvlMeasuredMmCu
            try {
                hvlMeasuredMmCu = Double.valueOf(beamQualityBO.getHvlMeasuredMmCu());
            } catch (Exception ignored) {
            }

            BeamData beamData = new BeamData();
            beamData.setBeamId(beamId);
            beamData.setReferenceConeId(referenceConeId);
            beamData.setBeamIdAlt(beamIdAlt);
            beamData.setNomEnergy(nomEnergy);
            beamData.setScd(scd);
            beamData.setFieldSizeAtScd(fieldSizeAtScd);
            beamData.setHvlNominalMmAl(hvlNominalMmAl);
            beamData.setHvlNominalMmCu(hvlNominalMmCu);
            beamData.setHvlMeasuredMmAl(hvlMeasuredMmAl);
            beamData.setHvlMeasuredMmCu(hvlMeasuredMmCu);

            beamDataList.add(beamData);
        }

        return beamDataList;
    }

    public List<AuditBeamInputs> getAuditBeamInputs(AuditInformation auditInformation,
                                                    List<Cone> coneList,
                                                    List<ConeBO> coneBOList) {
        List<AuditBeamInputs> auditBeamInputsList = new ArrayList<>();

        // auditId
        String auditId = auditInformation.getAuditId();

        int count = 0;
        for (int i = 0; i < coneList.size(); i++) {
            Cone cone = coneList.get(i);
            ConeBO coneBO = coneBOList.get(i);

            for (ConeBeanBO coneBeanBO : coneBO.getBeamsData()) {
                count++;

                // inputId
                String inputId = auditId + "-InputAudit-" + count;

                // coneId
                String coneId = cone.getConeId();

                // beamId
                String beamId = coneBeanBO.getBeamId();
                if (!StringUtils.hasText(beamId)) KVException.display();

                // dospSsd
                String dospSsd = MainConfig.RefConeDospSsd.valueOf(coneBeanBO.getDospSsd()).toString();

                // nomDoseOutput
                double nomDoseOutput = Double.parseDouble(coneBeanBO.getNomDoseOutput());

                // doseOutputUnit
                String doseOutputUnit = coneBO.getDoseOutputUnit();
                if (!MainConfig.coneDoseOutputUnitSet.contains(doseOutputUnit)) KVException.display();

                AuditBeamInputs auditBeamInputs = new AuditBeamInputs();
                auditBeamInputs.setInputId(inputId);
                auditBeamInputs.setAuditId(auditId);
                auditBeamInputs.setConeId(coneId);
                auditBeamInputs.setBeamId(beamId);
                auditBeamInputs.setDospSsd(dospSsd);
                auditBeamInputs.setNomDoseOutput(nomDoseOutput);
                auditBeamInputs.setDoseOutputUnit(doseOutputUnit);

                auditBeamInputsList.add(auditBeamInputs);
            }
        }

        return auditBeamInputsList;
    }

    public ReferenceDosimetry getReferenceDosimetry(AuditInformation auditInformation, AuditInfoBO auditInfoBO) {
        // auditId
        String auditId = auditInformation.getAuditId();

        // protocol
        String protocol = auditInfoBO.getRefDosimetryProtocol();
        if (!StringUtils.hasText(protocol)) KVException.display();

        // nk
        String nk = auditInfoBO.getRefDosimetryNkDetermined();
        if (!StringUtils.hasText(nk)) KVException.display();

        // comments (nullable)
        String comments = auditInfoBO.getRefDosimetryComments();
        if (!StringUtils.hasText(comments)) comments = null;

        ReferenceDosimetry referenceDosimetry = new ReferenceDosimetry();
        referenceDosimetry.setAuditId(auditId);
        referenceDosimetry.setProtocol(protocol);
        referenceDosimetry.setNk(nk);
        referenceDosimetry.setComments(comments);

        return referenceDosimetry;
    }

    public KvReferenceDosimetry getKvReferenceDosimetry(AuditInformation auditInformation, AuditInfoBO auditInfoBO) {
        // auditId
        String auditId = auditInformation.getAuditId();

        // protocol
        String protocol = auditInfoBO.getKvRefDosimetryProtocol();
        if (!StringUtils.hasText(protocol)) KVException.display();

        // referenceChamber
        String referenceChamber = auditInfoBO.getKvRefDosimetryRefChamber();
        if (!StringUtils.hasText(referenceChamber)) KVException.display();

        // airPhantom
        String airPhantom = auditInfoBO.getKvRefDosimetryAirPhantom();
        if (!StringUtils.hasText(airPhantom)) KVException.display();

        // referenceDepth
        String referenceDepth = auditInfoBO.getKvRefDosimetryRefDepth();
        if (!StringUtils.hasText(referenceDepth)) KVException.display();

        // comments (nullable)
        String comments = auditInfoBO.getKvRefDosimetryComments();
        if (!StringUtils.hasText(comments)) comments = null;

        KvReferenceDosimetry kvReferenceDosimetry = new KvReferenceDosimetry();
        kvReferenceDosimetry.setAuditId(auditId);
        kvReferenceDosimetry.setProtocol(protocol);
        kvReferenceDosimetry.setReferenceChamber(referenceChamber);
        kvReferenceDosimetry.setAirPhantom(airPhantom);
        kvReferenceDosimetry.setReferenceDepth(referenceDepth);
        kvReferenceDosimetry.setComments(comments);

        return kvReferenceDosimetry;
    }

    public AuditHistory getAuditHistory(AuditInformation auditInformation) {
        // auditId
        String auditId = auditInformation.getAuditId();

        // status
        String status = MainConfig.AUDIT_STATUS_PROGRESSING;

        // lastUpdatedDate
        Date lastUpdatedDate = new Date();

        AuditHistory auditHistory = new AuditHistory();
        auditHistory.setAuditId(auditId);
        auditHistory.setStatus(status);
        auditHistory.setLastUpdatedDate(lastUpdatedDate);

        return auditHistory;
    }

    public void saveTreatmentMachine(TreatmentMachine treatmentMachine) {
        if (treatmentMachineMapper.existsWithPrimaryKey(treatmentMachine.getMachineId())) {
            treatmentMachineMapper.updateByPrimaryKey(treatmentMachine);
        } else {
            treatmentMachineMapper.insert(treatmentMachine);
        }
    }

    public void saveFacilityInformation(FacilityInformation facilityInformation) {
        if (facilityInformationMapper.existsWithPrimaryKey(facilityInformation.getFacilityId())) {
            facilityInformationMapper.updateByPrimaryKey(facilityInformation);
        } else {
            facilityInformationMapper.insert(facilityInformation);
        }
    }

    public void savePhysicalAddress(PhysicalAddress physicalAddress) {
        if (physicalAddressMapper.existsWithPrimaryKey(physicalAddress.getPhysicalAddressId())) {
            physicalAddressMapper.updateByPrimaryKey(physicalAddress);
        } else {
            physicalAddressMapper.insert(physicalAddress);
        }
    }

    public void saveReportingAddress(ReportingAddress reportingAddress) {
        if (reportingAddressMapper.existsWithPrimaryKey(reportingAddress.getReportingAddressId())) {
            reportingAddressMapper.updateByPrimaryKey(reportingAddress);
        } else {
            reportingAddressMapper.insert(reportingAddress);
        }
    }

    public void saveFacilityRepresentative(List<FacilityRepresentative> facilityRepresentativeList) {
        for (FacilityRepresentative facilityRepresentative : facilityRepresentativeList) {
            if (facilityRepresentativeMapper.existsWithPrimaryKey(facilityRepresentative.getRepresentativeId())) {
                facilityRepresentativeMapper.updateByPrimaryKey(facilityRepresentative);
            } else {
                facilityRepresentativeMapper.insert(facilityRepresentative);
            }
        }
    }

    public void saveAuditInformation(AuditInformation auditInformation) {
        if (auditInformationMapper.existsWithPrimaryKey(auditInformation.getAuditId())) {
            auditInformationMapper.updateByPrimaryKey(auditInformation);
        } else {
            auditInformationMapper.insert(auditInformation);
        }
    }

    public void saveCone(List<Cone> coneList) {
        for (Cone cone : coneList) {
            if (coneMapper.existsWithPrimaryKey(cone.getConeId())) {
                coneMapper.updateByPrimaryKeySelective(cone);
            } else {
                coneMapper.insertSelective(cone);
            }
        }
    }

    public void saveReferenceCone(List<ReferenceCone> referenceConeList) {
        for (ReferenceCone referenceCone : referenceConeList) {
            if (referenceConeMapper.existsWithPrimaryKey(referenceCone.getReferenceConeId())) {
                referenceConeMapper.updateByPrimaryKey(referenceCone);
            } else {
                referenceConeMapper.insert(referenceCone);
            }
        }
    }

    public void saveBeamData(List<BeamData> beamDataList) {
        for (BeamData beamData : beamDataList) {
            if (beamDataMapper.existsWithPrimaryKey(beamData.getBeamId())) {
                beamDataMapper.updateByPrimaryKeySelective(beamData);
            } else {
                beamDataMapper.insertSelective(beamData);
            }
        }
    }

    public void saveAuditBeamInputs(List<AuditBeamInputs> auditBeamInputsList) {
        for (AuditBeamInputs auditBeamInputs : auditBeamInputsList) {
            if (auditBeamInputsMapper.existsWithPrimaryKey(auditBeamInputs.getInputId())) {
                auditBeamInputsMapper.updateByPrimaryKey(auditBeamInputs);
            } else {
                auditBeamInputsMapper.insert(auditBeamInputs);
            }
        }
    }

    public void saveReferenceDosimetry(ReferenceDosimetry referenceDosimetry) {
        if (referenceDosimetryMapper.existsWithPrimaryKey(referenceDosimetry.getAuditId())) {
            referenceDosimetryMapper.updateByPrimaryKey(referenceDosimetry);
        } else {
            referenceDosimetryMapper.insert(referenceDosimetry);
        }
    }

    public void saveKvReferenceDosimetry(KvReferenceDosimetry kvReferenceDosimetry) {
        if (kvReferenceDosimetryMapper.existsWithPrimaryKey(kvReferenceDosimetry.getAuditId())) {
            kvReferenceDosimetryMapper.updateByPrimaryKey(kvReferenceDosimetry);
        } else {
            kvReferenceDosimetryMapper.insert(kvReferenceDosimetry);
        }
    }

    public void saveAuditHistory(AuditHistory auditHistory) {
        if (auditHistoryMapper.existsWithPrimaryKey(auditHistory.getAuditId())) {
            auditHistoryMapper.updateByPrimaryKey(auditHistory);
        } else {
            auditHistoryMapper.insert(auditHistory);
        }
    }

    public void saveBackResult(List<BackResult> backResultList) {
        for (BackResult backResult : backResultList) {
            if (backResultMapper.existsWithPrimaryKey(backResult.getBackResultId())) {
                backResultMapper.updateByPrimaryKeySelective(backResult);
            } else {
                backResultMapper.insertSelective(backResult);
            }
        }
    }

    public KVPageInfo queryAuditCases(String auditId, String clinic, String status, String date,
                                      Integer page, Integer pageSize) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("auditId", auditId);
        paramMap.put("clinic", clinic);
        paramMap.put("status", status);
        paramMap.put("date", date);

        PageHelper.startPage(page, pageSize);
        List<AuditCaseVO> auditCaseVOList = customMapper.queryAuditCases(paramMap);

        return KVPageInfo.build(auditCaseVOList);
    }

    public void editAuditCase(String auditId, String status) {
        if (auditHistoryMapper.existsWithPrimaryKey(auditId)) {
            AuditHistory auditHistory = new AuditHistory();
            auditHistory.setAuditId(auditId);
            auditHistory.setStatus(status);
            auditHistoryMapper.updateByPrimaryKeySelective(auditHistory);
        } else {
            KVException.display();
        }
    }

    public List<WorkDataSheet1VO> queryWorkDataSheet1(String auditId) {
        List<WorkDataSheet1VO> workDataSheet1VOList = new ArrayList<>();

        // get all Cone
        Example example = new Example(Cone.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_CONE_ID, auditId + "%");

        List<Cone> coneList = coneMapper.selectByExample(example);

        // get all AuditBeamInputs
        example = new Example(AuditBeamInputs.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc().orderBy(MainConfig.FIELD_NAME_BEAM_ID).asc();
        criteria = example.createCriteria();
        for (Cone cone : coneList) {
            criteria.orEqualTo(MainConfig.FIELD_NAME_CONE_ID, cone.getConeId());
        }

        List<AuditBeamInputs> auditBeamInputsList = auditBeamInputsMapper.selectByExample(example);
        Map<String, List<AuditBeamInputs>> auditBeamInputsMap = new HashMap<>();

        for (AuditBeamInputs auditBeamInputs : auditBeamInputsList) {
            String coneId = auditBeamInputs.getConeId();
            if (!auditBeamInputsMap.containsKey(coneId))
                auditBeamInputsMap.put(coneId, new ArrayList<>());

            auditBeamInputsMap.get(coneId).add(auditBeamInputs);
        }

        // get all BackResults
        example = new Example(BackResult.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_INPUT_ID, auditId + "%");

        Map<String, List<BackResult>> backResultMap = new HashMap<>();
        for (BackResult backResult : backResultMapper.selectByExample(example)) {
            String inputId = backResult.getInputId();
            if (!backResultMap.containsKey(inputId)) {
                backResultMap.put(inputId, new ArrayList<>());
            }
            backResultMap.get(inputId).add(backResult);
        }

        // get all BeamData
        example = new Example(BeamData.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_BEAM_ID, auditId + "%");

        List<BeamData> beamDataList = beamDataMapper.selectByExample(example);
        Map<String, BeamData> beamDataMap = new HashMap<>();
        for (BeamData beamData : beamDataList) {
            beamDataMap.put(beamData.getBeamId(), beamData);
        }

        for (Cone cone : coneList) {
            WorkDataSheet1VO workDataSheet1VO = new WorkDataSheet1VO();

            List<AuditBeamInputs> coneAuditBeamInputsList = auditBeamInputsMap.get(cone.getConeId());

            workDataSheet1VO.setConeName("Cone - " + cone.getConeIdAlt());
            workDataSheet1VO.setNominalDoseOutputType(coneAuditBeamInputsList.get(0).getDoseOutputUnit());
            workDataSheet1VO.setOpenClosed(cone.getOpenClosed());
            workDataSheet1VO.setSsd(cone.getSsd());

            // set beams data
            List<WorkDataSheet1VO.BeamDataVO> beamDataVOList = new ArrayList<>();
            for (AuditBeamInputs auditBeamInputs : coneAuditBeamInputsList) {
                List<BackResult> backResultList = backResultMap.get(auditBeamInputs.getInputId());
                BeamData beamData = beamDataMap.get(auditBeamInputs.getBeamId());

                WorkDataSheet1VO.BeamDataVO beamDataVO = new WorkDataSheet1VO.BeamDataVO();
                beamDataVO.setBeamNo(beamData.getBeamIdAlt());
                beamDataVO.setKvp(beamData.getNomEnergy());
                beamDataVO.setNominalHvlMmAl(beamData.getHvlNominalMmAl());
                beamDataVO.setNominalHvlMmCu(beamData.getHvlNominalMmCu());
                beamDataVO.setMeasuredHvlMmAl(beamData.getHvlMeasuredMmAl());
                beamDataVO.setMeasuredHvlMmCu(beamData.getHvlMeasuredMmCu());
                beamDataVO.setDoseRate(auditBeamInputs.getNomDoseOutput());

                // set chamber result
                List<WorkDataSheet1VO.ChamberResultVO> chamberResultList = new ArrayList<>();
                for (BackResult backResult : backResultList) {
                    WorkDataSheet1VO.ChamberResultVO chamberResultVO = new WorkDataSheet1VO.ChamberResultVO();
                    chamberResultVO.setChamberSn(backResult.getChamberSn());
                    chamberResultVO.setMuRho(backResult.getMurho());
                    chamberResultVO.setBwCombined(backResult.getBwCombined());
                    chamberResultVO.setBwAl(backResult.getBwAl());
                    chamberResultVO.setBwCu(backResult.getBwCu());
                    chamberResultVO.setkClosedCone(backResult.getkClosedCone());
                    chamberResultVO.setkCombinedCone(backResult.getMurho()
                            * backResult.getkClosedCone()
                            * backResult.getBwCombined());

                    chamberResultList.add(chamberResultVO);
                }

                beamDataVO.setChamberResultList(chamberResultList);

                beamDataVOList.add(beamDataVO);
            }

            workDataSheet1VO.setBeamsData(beamDataVOList);
            workDataSheet1VOList.add(workDataSheet1VO);
        }

        return workDataSheet1VOList;
    }

    public List<WorkDataSheet2VO> queryWorkDataSheet2(String auditId) {
        List<WorkDataSheet2VO> workDataSheet2VOList = new ArrayList<>();

        // get all Cone
        Example example = new Example(Cone.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_CONE_ID, auditId + "%");

        List<Cone> coneList = coneMapper.selectByExample(example);

        // get all AuditBeamInputs
        example = new Example(AuditBeamInputs.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc().orderBy(MainConfig.FIELD_NAME_BEAM_ID).asc();
        criteria = example.createCriteria();
        for (Cone cone : coneList) {
            criteria.orEqualTo(MainConfig.FIELD_NAME_CONE_ID, cone.getConeId());
        }

        List<AuditBeamInputs> auditBeamInputsList = auditBeamInputsMapper.selectByExample(example);
        Map<String, List<AuditBeamInputs>> auditBeamInputsMap = new HashMap<>();

        for (AuditBeamInputs auditBeamInputs : auditBeamInputsList) {
            String coneId = auditBeamInputs.getConeId();
            if (!auditBeamInputsMap.containsKey(coneId))
                auditBeamInputsMap.put(coneId, new ArrayList<>());

            auditBeamInputsMap.get(coneId).add(auditBeamInputs);
        }

        // get all BackResults
        example = new Example(BackResult.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_INPUT_ID, auditId + "%");

        Map<String, List<BackResult>> backResultMap = new HashMap<>();
        for (BackResult backResult : backResultMapper.selectByExample(example)) {
            String inputId = backResult.getInputId();
            if (!backResultMap.containsKey(inputId)) {
                backResultMap.put(inputId, new ArrayList<>());
            }
            backResultMap.get(inputId).add(backResult);
        }

        // get all BeamData
        example = new Example(BeamData.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_BEAM_ID, auditId + "%");

        List<BeamData> beamDataList = beamDataMapper.selectByExample(example);
        Map<String, BeamData> beamDataMap = new HashMap<>();
        for (BeamData beamData : beamDataList) {
            beamDataMap.put(beamData.getBeamId(), beamData);
        }

        for (Cone cone : coneList) {
            List<AuditBeamInputs> coneAuditBeamInputsList = auditBeamInputsMap.get(cone.getConeId());

            WorkDataSheet2VO workDataSheet2VO = new WorkDataSheet2VO();
            workDataSheet2VO.setConeName("Cone - " + cone.getConeIdAlt());

            // set basic data
            WorkDataSheet2VO.BasicDataVO basicDataVO = new WorkDataSheet2VO.BasicDataVO();
            basicDataVO.setElectronContaminationCheck(false);
            basicDataVO.setSsd(cone.getSsd());
            basicDataVO.setDoseType(coneAuditBeamInputsList.get(0).getDoseOutputUnit());
            basicDataVO.setElectrometerSn("194");
            basicDataVO.setChannel(1);
            basicDataVO.setWeblineRange("High");
            basicDataVO.setChamberSn("3587");

            workDataSheet2VO.setBasicData(basicDataVO);

            // set beams data
            List<WorkDataSheet2VO.BeamDataVO> beamDataVOList = new ArrayList<>();
            for (AuditBeamInputs auditBeamInputs : coneAuditBeamInputsList) {
                List<BackResult> backResultList = backResultMap.get(auditBeamInputs.getInputId());
                BeamData beamData = beamDataMap.get(auditBeamInputs.getBeamId());

                WorkDataSheet2VO.BeamDataVO beamDataVO = new WorkDataSheet2VO.BeamDataVO();
                beamDataVO.setBeamNo(beamData.getBeamIdAlt());
                beamDataVO.setKv(beamData.getNomEnergy());

                // set chamber result
                List<WorkDataSheet2VO.ChamberResultVO> chamberResultList = new ArrayList<>();
                for (BackResult backResult : backResultList) {
                    WorkDataSheet2VO.ChamberResultVO chamberResultVO = new WorkDataSheet2VO.ChamberResultVO();
                    chamberResultVO.setChamberSn(backResult.getChamberSn());
                    chamberResultVO.setNk(backResult.getNk());
                    chamberResultVO.setMuRho(backResult.getMurho());
                    chamberResultVO.setBw(backResult.getBwCombined());
                    chamberResultVO.setkClosedCone(backResult.getkClosedCone());
                    chamberResultVO.setPstemAir(backResult.getPstem());
                    chamberResultList.add(chamberResultVO);
                }
                beamDataVO.setChamberResultList(chamberResultList);

                beamDataVOList.add(beamDataVO);
            }

            workDataSheet2VO.setBeamsData(beamDataVOList);
            workDataSheet2VOList.add(workDataSheet2VO);
        }

        return workDataSheet2VOList;
    }

    public List<BeamInfoVO> queryBeamInfo(String auditId) {
        List<BeamInfoVO> beamInfoVOList = new ArrayList<>();

        // get all AuditBeamInputs
        Example example = new Example(AuditBeamInputs.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_AUDIT_ID, auditId);

        List<AuditBeamInputs> auditBeamInputsList = auditBeamInputsMapper.selectByExample(example);
        Set<String> beamIdSet = new HashSet<>();
        for (AuditBeamInputs auditBeamInputs : auditBeamInputsList) {
            beamIdSet.add(auditBeamInputs.getBeamId());
        }

        // get all BeamData
        example = new Example(BeamData.class);
        example.orderBy(MainConfig.FIELD_NAME_BEAM_ID).asc();
        criteria = example.createCriteria();
        for (String beamId : beamIdSet) {
            criteria.orEqualTo(MainConfig.FIELD_NAME_BEAM_ID, beamId);
        }

        List<BeamData> beamDataList = beamDataMapper.selectByExample(example);

        for (BeamData beamData : beamDataList) {
            BeamInfoVO beamInfoVO = new BeamInfoVO();
            beamInfoVO.setBeamId(beamData.getBeamIdAlt());
            beamInfoVO.setEnergy(beamData.getNomEnergy());
            beamInfoVO.setMeasuredHvlMmAl(beamData.getHvlMeasuredMmAl());
            beamInfoVO.setMeasuredHvlMmCu(beamData.getHvlMeasuredMmCu());

            beamInfoVOList.add(beamInfoVO);
        }

        return beamInfoVOList;
    }

    public JsonNode queryDataFrontEnd(String auditId) {
        InputDataFrontEnd inputDataFrontEnd = inputDataFrontEndMapper.selectByPrimaryKey(auditId);

        JsonNode jsonNode = null;
        if (inputDataFrontEnd != null && StringUtils.hasText(inputDataFrontEnd.getTable1())) {
            try {
                jsonNode = objectMapper.readTree(inputDataFrontEnd.getTable1());
            } catch (JsonProcessingException e) {
                KVException.display();
            }
        }

        return jsonNode;
    }

    public void saveDataFrontEnd(String auditId, JsonNode data) {
        InputDataFrontEnd inputDataFrontEnd = new InputDataFrontEnd();
        inputDataFrontEnd.setAuditId(auditId);
        inputDataFrontEnd.setTable1(data.toString());

        if (inputDataFrontEndMapper.existsWithPrimaryKey(auditId)) {
            inputDataFrontEndMapper.updateByPrimaryKeySelective(inputDataFrontEnd);
        } else {
            inputDataFrontEndMapper.insertSelective(inputDataFrontEnd);
        }
    }

    public IdentificationVO queryIdentification(String auditId) {
        AuditInformation auditInformation = auditInformationMapper.selectByPrimaryKey(auditId);
        if (auditInformation == null) KVException.display();
        String facilityId = auditInformation.getFacilityId();
        String machineId = auditInformation.getMachineId();

        // get facilityInformation
        FacilityInformation facilityInformation =
                facilityInformationMapper.selectByPrimaryKey(facilityId);

        // get facilityRepresentativeList
        Example example = new Example(FacilityRepresentative.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_FACILITY_ID, facilityId);
        List<FacilityRepresentative> facilityRepresentativeList = facilityRepresentativeMapper.selectByExample(example);

        // get physicalAddress
        example = new Example(PhysicalAddress.class);
        example.orderBy(MainConfig.FIELD_NAME_INPUT_DATE).desc();
        criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_FACILITY_ID, facilityId);
        PhysicalAddress physicalAddress = physicalAddressMapper.selectByExample(example).get(0);

        // get reportingAddress
        example = new Example(ReportingAddress.class);
        example.orderBy(MainConfig.FIELD_NAME_INPUT_DATE).desc();
        criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_FACILITY_ID, facilityId);
        ReportingAddress reportingAddress = reportingAddressMapper.selectByExample(example).get(0);

        // get treatmentMachine
        TreatmentMachine treatmentMachine = treatmentMachineMapper.selectByPrimaryKey(machineId);

        // get referenceDosimetry
        ReferenceDosimetry referenceDosimetry =
                referenceDosimetryMapper.selectByPrimaryKey(auditId);

        // get kvReferenceDosimetry
        KvReferenceDosimetry kvReferenceDosimetry =
                kvReferenceDosimetryMapper.selectByPrimaryKey(auditId);

        // get referenceConeList
        example = new Example(ReferenceCone.class);
        example.orderBy(MainConfig.FIELD_NAME_REFERENCE_CONE_ID).asc();
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_REFERENCE_CONE_ID, auditId + "%");
        List<ReferenceCone> referenceConeList = referenceConeMapper.selectByExample(example);

        // get coneList
        example = new Example(Cone.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc();
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_CONE_ID, auditId + "%");
        List<Cone> coneList = coneMapper.selectByExample(example);

        // get auditBeamInputsList
        example = new Example(AuditBeamInputs.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc().orderBy(MainConfig.FIELD_NAME_BEAM_ID).asc();
        criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_AUDIT_ID, auditId);
        List<AuditBeamInputs> auditBeamInputsList = auditBeamInputsMapper.selectByExample(example);

        // get beamDataList
        example = new Example(BeamData.class);
        example.orderBy(MainConfig.FIELD_NAME_BEAM_ID).asc();
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_BEAM_ID, auditId + "%");
        List<BeamData> beamDataList = beamDataMapper.selectByExample(example);

        IdentificationVO identificationVO = new IdentificationVO();
        identificationVO.setAuditInformation(auditInformation);
        identificationVO.setFacilityInformation(facilityInformation);
        identificationVO.setFacilityRepresentativeList(facilityRepresentativeList);
        identificationVO.setPhysicalAddress(physicalAddress);
        identificationVO.setReportingAddress(reportingAddress);
        identificationVO.setTreatmentMachine(treatmentMachine);
        identificationVO.setReferenceDosimetry(referenceDosimetry);
        identificationVO.setKvReferenceDosimetry(kvReferenceDosimetry);
        identificationVO.setReferenceConeList(referenceConeList);
        identificationVO.setConeList(coneList);
        identificationVO.setAuditBeamInputsList(auditBeamInputsList);
        identificationVO.setBeamDataList(beamDataList);

        return identificationVO;
    }

    public List<WarningVO> queryWarnings(String auditId) {
        List<WarningVO> warningVOList = new ArrayList<>();

        // get all AuditBeamInputs
        Example example = new Example(AuditBeamInputs.class);
        example.orderBy(MainConfig.FIELD_NAME_CONE_ID).asc().orderBy(MainConfig.FIELD_NAME_BEAM_ID).asc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_AUDIT_ID, auditId);
        List<AuditBeamInputs> auditBeamInputsList = auditBeamInputsMapper.selectByExample(example);

        // get all BackResults
        example = new Example(BackResult.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_INPUT_ID, auditId + "%");

        Map<String, List<BackResult>> backResultMap = new HashMap<>();
        for (BackResult backResult : backResultMapper.selectByExample(example)) {
            String inputId = backResult.getInputId();
            if (!backResultMap.containsKey(inputId)) {
                backResultMap.put(inputId, new ArrayList<>());
            }
            backResultMap.get(inputId).add(backResult);
        }

        for (AuditBeamInputs auditBeamInputs : auditBeamInputsList) {
            String coneId = auditBeamInputs.getConeId();
            String beamId = auditBeamInputs.getBeamId();
            String inputId = auditBeamInputs.getInputId();

            if (backResultMap.containsKey(inputId)) {
                for (BackResult backResult : backResultMap.get(inputId)) {
                    String chamberSn = backResult.getChamberSn();
                    String warning = backResult.getWarning();

                    if (StringUtils.hasText(warning)) {
                        WarningVO warningVO = new WarningVO();
                        warningVO.setConeId(coneId);
                        warningVO.setBeamId(beamId);
                        warningVO.setChamberSn(chamberSn);
                        warningVO.setMessage(warning);

                        warningVOList.add(warningVO);
                    }
                }
            }
        }

        return warningVOList;
    }

    public void deleteAuditCase(String auditId) {
        // Check if audit case exists
        if (!auditInformationMapper.existsWithPrimaryKey(auditId)) {
            KVException.display();
        }

        // delete InputDataFrontEnd
        inputDataFrontEndMapper.deleteByPrimaryKey(auditId);

        // delete BackResult
        Example example = new Example(BackResult.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_BACK_RESULT_ID, auditId + "%");
        backResultMapper.deleteByExample(example);

        // delete AuditHistory
        auditHistoryMapper.deleteByPrimaryKey(auditId);

        // delete KvReferenceDosimetry
        kvReferenceDosimetryMapper.deleteByPrimaryKey(auditId);

        // delete ReferenceDosimetry
        referenceDosimetryMapper.deleteByPrimaryKey(auditId);

        // delete AuditBeamInputs
        example = new Example(AuditBeamInputs.class);
        criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_AUDIT_ID, auditId);
        auditBeamInputsMapper.deleteByExample(example);

        // delete BeamData
        example = new Example(BeamData.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_BEAM_ID, auditId + "%");
        beamDataMapper.deleteByExample(example);

        // delete ReferenceCone
        example = new Example(ReferenceCone.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_REFERENCE_CONE_ID, auditId + "%");
        referenceConeMapper.deleteByExample(example);

        // delete Cone
        example = new Example(Cone.class);
        criteria = example.createCriteria();
        criteria.andLike(MainConfig.FIELD_NAME_CONE_ID, auditId + "%");
        coneMapper.deleteByExample(example);

        // delete AuditInformation
        auditInformationMapper.deleteByPrimaryKey(auditId);

        // facility related:
        // FacilityRepresentative
        // ReportingAddress
        // PhysicalAddress
        // FacilityInformation
        // TreatmentMachine

        // note: facility related data will not be deleted
    }
}
