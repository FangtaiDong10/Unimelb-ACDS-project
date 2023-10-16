package kv.controller;

import com.fasterxml.jackson.databind.JsonNode;
import kv.common.KVException;
import kv.common.KVJsonResult;
import kv.common.KVPageInfo;
import kv.config.MainConfig;
import kv.pojo.bo.excel.*;
import kv.pojo.dbo.*;
import kv.service.AuditService;
import kv.service.CalculationService;
import kv.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {
    private final AuditService auditService;
    private final CalculationService calculationService;
    private final SimpleDateFormat simpleDateFormat;

    public AuditController(AuditService auditService,
                           CalculationService calculationService,
                           SimpleDateFormat simpleDateFormat) {
        this.auditService = auditService;
        this.calculationService = calculationService;
        this.simpleDateFormat = simpleDateFormat;
    }

    @PostMapping("/upload")
    @Transactional
    public KVJsonResult upload(@RequestParam("file") MultipartFile multipartFile) {
        XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
        XSSFSheet sheet = workBook.getSheet(MainConfig.SHEET_NAME_IDENTIFICATION);

        // read date from excel
        AuditInfoBO auditInfoBO = auditService.readAuditInfo(sheet);
        List<RefConeBO> refConeBOList = auditService.readRefCone(sheet);
        List<CommentBO> commentBOList = auditService.readRefConeComments(sheet);
        List<ConeBO> coneBOList = auditService.readCone(sheet);
        List<BeamQualityBO> beamQualityBOList = auditService.readBeamQuality(sheet);

        // Preparation before saving into database
        TreatmentMachine treatmentMachine = auditService.getTreatmentMachine(auditInfoBO);
        FacilityInformation facilityInformation = auditService.getFacilityInformation(auditInfoBO);
        PhysicalAddress physicalAddress = auditService.getPhysicalAddress(auditInfoBO, facilityInformation);
        ReportingAddress reportingAddress = auditService.getReportingAddress(auditInfoBO, facilityInformation);
        List<FacilityRepresentative> facilityRepresentativeList =
                auditService.getFacilityRepresentative(auditInfoBO, facilityInformation);
        AuditInformation auditInformation =
                auditService.getAuditInformation(auditInfoBO, facilityInformation, treatmentMachine);
        List<Cone> coneList = auditService.getCone(coneBOList);
        List<ReferenceCone> referenceConeList = auditService.getReferenceCone(refConeBOList, auditInformation);
        List<BeamData> beamDataList = auditService.getBeamData(beamQualityBOList, referenceConeList);
        List<AuditBeamInputs> auditBeamInputsList =
                auditService.getAuditBeamInputs(auditInformation, coneList, coneBOList);
        ReferenceDosimetry referenceDosimetry = auditService.getReferenceDosimetry(auditInformation, auditInfoBO);
        KvReferenceDosimetry kvReferenceDosimetry = auditService.getKvReferenceDosimetry(auditInformation, auditInfoBO);
        AuditHistory auditHistory = auditService.getAuditHistory(auditInformation);
        List<BackResult> backResultList =
                calculationService.getBackResult(beamDataList, auditBeamInputsList, coneList);

        // save data into database
        auditService.saveTreatmentMachine(treatmentMachine);
        auditService.saveFacilityInformation(facilityInformation);
        auditService.savePhysicalAddress(physicalAddress);
        auditService.saveReportingAddress(reportingAddress);
        auditService.saveFacilityRepresentative(facilityRepresentativeList);
        auditService.saveAuditInformation(auditInformation);
        auditService.saveCone(coneList);
        auditService.saveReferenceCone(referenceConeList);
        auditService.saveBeamData(beamDataList);
        auditService.saveAuditBeamInputs(auditBeamInputsList);
        auditService.saveReferenceDosimetry(referenceDosimetry);
        auditService.saveKvReferenceDosimetry(kvReferenceDosimetry);
        auditService.saveAuditHistory(auditHistory);
        auditService.saveBackResult(backResultList);

        return KVJsonResult.ok();
    }

    @GetMapping("/case")
    public KVJsonResult queryAuditCases(@RequestParam(required = false) String auditId,
                                        @RequestParam(required = false) String clinic,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(required = false) String date,
                                        @RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (!StringUtils.hasText(auditId)) auditId = "";
        if (!StringUtils.hasText(clinic)) clinic = "";
        if (!StringUtils.hasText(status)) status = "";
        if (!StringUtils.hasText(date)) {
            date = "";
        } else {
            try {
                simpleDateFormat.parse(date);
            } catch (ParseException e) {
                KVException.display();
            }
        }

        KVPageInfo kvPageInfo = auditService.queryAuditCases(auditId, clinic, status, date, page, pageSize);
        return KVJsonResult.ok(kvPageInfo);
    }

    @PostMapping("/case/{auditId}")
    @Transactional
    public KVJsonResult editAuditCase(@PathVariable String auditId, @RequestParam String status) {
        if (!(MainConfig.AUDIT_STATUS_PROGRESSING.equals(status) || MainConfig.AUDIT_STATUS_FINISHED.equals(status)))
            KVException.display();

        auditService.editAuditCase(auditId, status);
        return KVJsonResult.ok();
    }

    @DeleteMapping("/case/{auditId}")
    @Transactional
    public KVJsonResult deleteAuditCase(@PathVariable String auditId) {
        auditService.deleteAuditCase(auditId);
        return KVJsonResult.ok();
    }

    @GetMapping("/workDataSheet1/{auditId}")
    public KVJsonResult queryWorkDataSheet1(@PathVariable String auditId) {
        return KVJsonResult.ok(auditService.queryWorkDataSheet1(auditId));
    }

    @GetMapping("/workDataSheet2/{auditId}")
    public KVJsonResult queryWorkDataSheet2(@PathVariable String auditId) {
        return KVJsonResult.ok(auditService.queryWorkDataSheet2(auditId));
    }

    @GetMapping("/beamInfo/{auditId}")
    public KVJsonResult queryBeamInfo(@PathVariable String auditId) {
        return KVJsonResult.ok(auditService.queryBeamInfo(auditId));
    }

    @GetMapping("/dataFrontEnd/{auditId}")
    public KVJsonResult queryDataFrontEnd(@PathVariable String auditId) {
        return KVJsonResult.ok(auditService.queryDataFrontEnd(auditId));
    }

    @PostMapping("/dataFrontEnd/{auditId}")
    @Transactional
    public KVJsonResult saveDataFrontEnd(@PathVariable String auditId, @RequestBody JsonNode data) {
        auditService.saveDataFrontEnd(auditId, data);
        return KVJsonResult.ok();
    }

    @GetMapping("/identification/{auditId}")
    public KVJsonResult queryIdentification(@PathVariable String auditId) {
        return KVJsonResult.ok(auditService.queryIdentification(auditId));
    }

    @GetMapping("/warning/{auditId}")
    public KVJsonResult queryWarnings(@PathVariable String auditId) {
        return KVJsonResult.ok(auditService.queryWarnings(auditId));
    }
}
