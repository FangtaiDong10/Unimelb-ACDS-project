package kv.controller;

import kv.common.KVJsonResult;
import kv.config.MainConfig;
import kv.pojo.dbo.BwAlCu;
import kv.pojo.dbo.MurhoAl;
import kv.pojo.dbo.MurhoCu;
import kv.pojo.dbo.PstemMeasured;
import kv.pojo.vo.lookup.FarmerVO;
import kv.pojo.vo.lookup.PlaneparallelVO;
import kv.service.LookupService;
import kv.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lookup")
public class LookupController {
    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/bw")
    public KVJsonResult queryBw() {
        return KVJsonResult.ok(lookupService.queryBw());
    }

    @GetMapping("/farmer")
    public KVJsonResult queryFarmer() {
        return KVJsonResult.ok(lookupService.queryFarmer());
    }

    @GetMapping("/murho-al")
    public KVJsonResult queryMurhoAl() {
        return KVJsonResult.ok(lookupService.queryMurhoAl());
    }

    @GetMapping("/murho-cu")
    public KVJsonResult queryMurhoCu() {
        return KVJsonResult.ok(lookupService.queryMurhoCu());
    }

    @GetMapping("/planeparallel")
    public KVJsonResult queryPlaneparallel() {
        return KVJsonResult.ok(lookupService.queryPlaneparallel());
    }

    @GetMapping("/pstem")
    public KVJsonResult queryPstem() {
        return KVJsonResult.ok(lookupService.queryPstem());
    }

    @PostMapping("/upload")
    @Transactional
    public KVJsonResult uploadLookup(@RequestParam("file") MultipartFile multipartFile) {
        XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);

        // Bw
        XSSFSheet bwSheet = workBook.getSheet(MainConfig.SHEET_NAME_BW);
        if (bwSheet != null) {
            List<BwAlCu> bwAlCuList = lookupService.readBw(bwSheet);
            lookupService.saveBw(bwAlCuList);
        }

        // farmer
        XSSFSheet farmerSheet = workBook.getSheet(MainConfig.SHEET_NAME_FARMER);
        if (farmerSheet != null) {
            List<FarmerVO> farmerVOList = lookupService.readFarmer(farmerSheet);
            lookupService.saveFarmer(farmerVOList);
        }

        // murhoAl
        XSSFSheet murhoAlSheet = workBook.getSheet(MainConfig.SHEET_NAME_MURHOAL);
        if (murhoAlSheet != null) {
            List<MurhoAl> murhoAlList = lookupService.readMurhoAl(murhoAlSheet);
            lookupService.saveMurhoAl(murhoAlList);
        }

        // murhoCu
        XSSFSheet murhoCuSheet = workBook.getSheet(MainConfig.SHEET_NAME_MURHOCU);
        if (murhoCuSheet != null) {
            List<MurhoCu> murhoCuList = lookupService.readMurhoCu(murhoCuSheet);
            lookupService.saveMurhoCu(murhoCuList);
        }

        // planeparallel
        XSSFSheet planeparallelSheet = workBook.getSheet(MainConfig.SHEET_NAME_PLANEPARALLEL);
        if (planeparallelSheet != null) {
            List<PlaneparallelVO> planeparallelVOList = lookupService.readPlaneparallel(planeparallelSheet);
            lookupService.savePlaneparallel(planeparallelVOList);
        }

        // pstem
        XSSFSheet pstemSheet = workBook.getSheet(MainConfig.SHEET_NAME_PSTEM);
        if (pstemSheet != null) {
            List<PstemMeasured> pstemMeasuredList = lookupService.readPstem(pstemSheet);
            lookupService.savePstem(pstemMeasuredList);
        }

        return KVJsonResult.ok();
    }
}
