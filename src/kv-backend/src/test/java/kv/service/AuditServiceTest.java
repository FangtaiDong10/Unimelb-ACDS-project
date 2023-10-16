package kv.service;

import kv.common.KVException;
import kv.config.MainConfig;
import kv.pojo.bo.excel.*;
import kv.pojo.dbo.*;
import kv.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuditServiceTest {
    @Autowired
    private AuditService auditService;

    @Test
    void readAuditInfo() {
        // TC 36: Read audit information in Excel
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            assertEquals("ACDS-kV-5014", auditInfoBO.getAcdsAuditNumber());
            assertEquals("Hogwarts", auditInfoBO.getAcdsFacilityId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void readRefCone() {
        // TC 37: Read reference cone in Excel
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            assertEquals(8, refConeBOList.size());
            assertEquals("ACDS-kV-5014-beam-2", refConeBOList.get(1).getBeamId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void readRefConeComments() {
        // TC 38: Read reference cone  comments in Excel
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<CommentBO> commentBOList = auditService.readRefConeComments(identificationSheet);
            assertEquals(1, commentBOList.size());
            assertEquals("Closed end cone thickness taken from https://aapm.onlinelibrary.wiley.com/doi/pdf/10.1120/jacmp.v15i4.4893", commentBOList.get(0).getComment());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void readCone() {
        // TC 39: Read cone in Excel
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<ConeBO> coneBOList = auditService.readCone(identificationSheet);
            assertEquals(3, coneBOList.size());
            assertEquals("ACDS-kV-5014-cone-1", coneBOList.get(0).getConeId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void readBeamQuality() {
        // TC 40: Read beam quality in Excel
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<BeamQualityBO> coneQuatlityBOList = auditService.readBeamQuality(identificationSheet);
            assertEquals(8, coneQuatlityBOList.size());
            assertEquals("ACDS-kV-5014-beam-1", coneQuatlityBOList.get(0).getBeamId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getTreatmentMachine() {
        // TC 41: Get the treatment machine information
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            assertEquals("Xstrahl-GM0311",treatmentMachine.getMachineId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getFacilityInformation() {
        // TC 42: Get the facility information
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            assertEquals("Hogwarts",facilityInformation.getFacilityId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getPhysicalAddress() {
        // TC 43: Get the physical address
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            PhysicalAddress physicalAddress = auditService.getPhysicalAddress(auditInfoBO,facilityInformation);
            assertEquals("Hogwarts-2021-06-23",physicalAddress.getPhysicalAddressId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getReportingAddress() {
        // TC 44: Get the reporting address
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            ReportingAddress reportingAddress = auditService.getReportingAddress(auditInfoBO,facilityInformation);
            assertEquals("Hogwarts-2021-06-23",reportingAddress.getReportingAddressId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getFacilityRepresentative() {
        // TC 45: Get the facility representative
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            List<FacilityRepresentative> facilityRepresentative = auditService.getFacilityRepresentative(auditInfoBO,facilityInformation);
            assertEquals(2,facilityRepresentative.size());
            assertEquals("Hogwarts-Recipient of Report",facilityRepresentative.get(0).getRepresentativeId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getAuditInformation() {
        // TC 46: Get the audit information
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);
            assertEquals("ACDS-kV-5014",auditInformation.getAuditId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getCone() {
        // TC 47: Get the cone
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<ConeBO> coneBOList = auditService.readCone(identificationSheet);
            List<Cone> coneList = auditService.getCone(coneBOList);
            assertEquals(3,coneList.size());
            assertEquals("ACDS-kV-5014-cone-1", coneList.get(0).getConeId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getReferenceCone() {
        // TC 48: Get the reference cone
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            List<ReferenceCone> referenceConeList = auditService.getReferenceCone(refConeBOList,auditInformation);
            assertEquals(8, refConeBOList.size());
            assertEquals("ACDS-kV-5014-beam-2", refConeBOList.get(1).getBeamId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getBeamData() {
        // TC 49: Get the beam data
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<BeamQualityBO> coneQuatlityBOList = auditService.readBeamQuality(identificationSheet);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            List<ReferenceCone> referenceConeList = auditService.getReferenceCone(refConeBOList,auditInformation);
            List<BeamData> beamDataList = auditService.getBeamData(coneQuatlityBOList,referenceConeList);
            assertEquals(8, beamDataList.size());
            assertEquals("ACDS-kV-5014-beam-1", beamDataList.get(0).getBeamId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getAuditBeamInputs() {
        // TC 50: Get the beam inputs
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<BeamQualityBO> coneQuatlityBOList = auditService.readBeamQuality(identificationSheet);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            List<ConeBO> coneBOList = auditService.readCone(identificationSheet);
            List<Cone> coneList = auditService.getCone(coneBOList);
            List<AuditBeamInputs> auditBeamInputsList = auditService.getAuditBeamInputs(auditInformation,coneList,coneBOList);
            assertEquals(14, auditBeamInputsList.size());
            assertEquals("ACDS-kV-5014-beam-1", auditBeamInputsList.get(0).getBeamId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getReferenceDosimetry() {
        // TC 51: Get the reference dosimetry
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<BeamQualityBO> coneQuatlityBOList = auditService.readBeamQuality(identificationSheet);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            ReferenceDosimetry referenceDosimetry = auditService.getReferenceDosimetry(auditInformation,auditInfoBO);
            assertEquals("ACDS-kV-5014", referenceDosimetry.getAuditId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getKvReferenceDosimetry() {
        // TC 52: Get the kv reference dosimetry
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<BeamQualityBO> coneQuatlityBOList = auditService.readBeamQuality(identificationSheet);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            KvReferenceDosimetry kvReferenceDosimetry = auditService.getKvReferenceDosimetry(auditInformation,auditInfoBO);
            assertEquals("ACDS-kV-5014", kvReferenceDosimetry.getAuditId());
        } catch (IOException e) {
            KVException.display();
        }
    }

    @Test
    void getAuditHistory() {
        // TC 53: Get the audit history
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form.xlsm"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet identificationSheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);
            List<BeamQualityBO> coneQuatlityBOList = auditService.readBeamQuality(identificationSheet);
            AuditInfoBO auditInfoBO = auditService.readAuditInfo(identificationSheet);
            FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
            TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
            AuditInformation auditInformation = auditService.getAuditInformation(auditInfoBO,facilityInformation,treatmentMachine);List<RefConeBO> refConeBOList = auditService.readRefCone(identificationSheet);
            AuditHistory auditHistory = auditService.getAuditHistory(auditInformation);
            assertEquals("ACDS-kV-5014", auditHistory.getAuditId());
        } catch (IOException e) {
            KVException.display();
        }
    }
}