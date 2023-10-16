package kv.service;

import kv.common.KVException;
import kv.config.MainConfig;
import kv.mapper.*;
import kv.pojo.dbo.*;
import kv.pojo.vo.lookup.FarmerVO;
import kv.pojo.vo.lookup.PlaneparallelVO;
import kv.utils.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LookupServiceTest {
    @Autowired
    private LookupService lookupService;
    @Autowired
    private BeamPlaneparallelNkMapper beamPlaneparallelNkMapper;
    @Autowired
    private PstemMeasuredMapper pstemMeasuredMapper;
    @Autowired
    private BwAlCuMapper bwAlCuMapper;
    @Autowired
    private BeamFarmerChamberMapper beamFarmerChamberMapper;
    @Autowired
    private MurhoAlMapper murhoAlMapper;
    @Autowired
    private MurhoCuMapper murhoCuMapper;

    @Test
    void queryBw() {
        // TC 1.1: Read Bw lookup tables
        List<BwAlCu> result = lookupService.queryBw();
        assertNotNull(result);
        assertEquals(3059, result.size());
    }

    @Test
    void queryFarmer() {
        // TC 2.1: Read farmer lookup tables
        List<FarmerVO> result = lookupService.queryFarmer();
        assertNotNull(result);
        assertEquals(177, result.size());
    }

    @Test
    void queryMurhoAl() {
        // TC 3.1: Read MurhoAl lookup tables
        List<MurhoAl> result = lookupService.queryMurhoAl();
        assertNotNull(result);
        assertEquals(46, result.size());
    }

    @Test
    void queryMurhoCu() {
        // TC 4.1: Read MurhoCu lookup tables
        List<MurhoCu> result = lookupService.queryMurhoCu();
        assertNotNull(result);
        assertEquals(13, result.size());
    }

    @Test
    void queryPlaneparallel() {
        // TC 5.1: Query Planeparallel lookup tables
        List<PlaneparallelVO> result = lookupService.queryPlaneparallel();
        assertNotNull(result);
    }

    @Test
    void queryPstem() {
        // TC 6.1: Query Pstem lookup tables
        List<PlaneparallelVO> result = lookupService.queryPlaneparallel();
        assertNotNull(result);
    }

    @Test
    void readBw() {
        // TC 1.2: Read Bw Excel Sheet
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/bw_template.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet bwSheet = workBook.getSheet(MainConfig.SHEET_NAME_BW);
            List<BwAlCu> bwAlCuList = lookupService.readBw(bwSheet);
            assertEquals(1302, bwAlCuList.size());
            BwAlCu bwAlCu = bwAlCuList.get(0);
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate1 = dateFormat1.parse("2020-05-26");
            assertEquals(myDate1, bwAlCu.getDateUpdated());
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readFarmer() {
        // TC 2.2: Read Farmer Excel Sheet
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/farmer_template.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet farmerSheet = workBook.getSheet(MainConfig.SHEET_NAME_FARMER);
            List<FarmerVO> FarmerList = lookupService.readFarmer(farmerSheet);
            assertEquals(177, FarmerList.size());
            FarmerVO farmer = FarmerList.get(0);
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate1 = dateFormat1.parse("2021-08-31][");
            assertEquals(myDate1, farmer.getDateUpdated());
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readMurhoAl() {
        // TC 3.2: Read MurhoAl Excel Sheet
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/murho_al_template.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet murhoalSheet = workBook.getSheet(MainConfig.SHEET_NAME_MURHOAL);
            List<MurhoAl> murhoAlList = lookupService.readMurhoAl(murhoalSheet);
            assertEquals(23, murhoAlList.size());
            MurhoAl murhoAl = murhoAlList.get(0);
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate1 = dateFormat1.parse("2021-10-07");
            assertEquals(myDate1, murhoAl.getDateUpdated());
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readMurhoCu() {
        // TC 4.2: Read MurhoCu Excel Sheet
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/murho_cu_template.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet murhocuSheet = workBook.getSheet(MainConfig.SHEET_NAME_MURHOCU);
            List<MurhoCu> murhoCuList = lookupService.readMurhoCu(murhocuSheet);
            assertEquals(13, murhoCuList.size());
            MurhoCu murhoCu = murhoCuList.get(0);
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate1 = dateFormat1.parse("2021-10-07");
            assertEquals(myDate1, murhoCu.getDateUpdated());
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void readPlaneparallel() throws IOException, InvalidFormatException {
        // TC 5.2: Read Planeparallel Excel Sheet
        XSSFWorkbook workbook =
                new XSSFWorkbook(new File("../../data samples/lookup-templates/planeparallel_template.xlsx"));
        XSSFSheet sheet = workbook.getSheet(MainConfig.SHEET_NAME_PLANEPARALLEL);

        List<PlaneparallelVO> planeparallelVOList = lookupService.readPlaneparallel(sheet);
        assertNotNull(planeparallelVOList);
        assertEquals(14, planeparallelVOList.size());

        List<PlaneparallelVO> planeparallelVOListTest = new ArrayList<>();
        Date dateUpdated = HSSFDateUtil.getJavaDate(01012022);
        PlaneparallelVO planeparallelVO = new PlaneparallelVO();
        planeparallelVO.setBeamPlaneparallelId("RT8");
        planeparallelVO.setDateUpdated(dateUpdated);
        planeparallelVO.setChamberName("PTW 2222");
        planeparallelVO.setChamberSn("1528");
        planeparallelVO.setKv(20.00);
        planeparallelVO.setNkValue(2222.2222);
        planeparallelVO.setHvlMeasuredMmAl(2.233);

        planeparallelVOListTest.add(planeparallelVO);
        assertEquals(1, planeparallelVOListTest.size());
    }

    @Test
    void readPstem() throws IOException, InvalidFormatException {
        // TC 6.2: Read Pstem Excel Sheet
        XSSFWorkbook workbook =
                new XSSFWorkbook(new File("../../data samples/lookup-templates/pstem_template.xlsx"));
        XSSFSheet sheet = workbook.getSheet(MainConfig.SHEET_NAME_PSTEM);

        List<PstemMeasured> pstemMeasuredList = lookupService.readPstem(sheet);
        assertNotNull(pstemMeasuredList);
        assertEquals(42, pstemMeasuredList.size());

        List<PstemMeasured> pstemMeasuredListTest = new ArrayList<>();
        Date dateUpdated = HSSFDateUtil.getJavaDate(01012022);
        PstemMeasured pstemMeasured = new PstemMeasured();
        pstemMeasured.setPstemOption("UNITY");
        pstemMeasured.setPstemValue(1.00);
        pstemMeasured.setDiameter(1.4);
        pstemMeasured.setHvlMeasuredMmAl(0.2);
        pstemMeasured.setDateUpdated(dateUpdated);
        pstemMeasured.setBeamPpChamberId("RT8");
        pstemMeasuredListTest.add(pstemMeasured);

        assertEquals(1, pstemMeasuredListTest.size());
    }

    @Test
    void saveBw() {
        // TC 1.3: Save Bw lookup tables
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/bw_template_test.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet bwSheet = workBook.getSheet(MainConfig.SHEET_NAME_BW);
            List<BwAlCu> bwList = lookupService.readBw(bwSheet);
            lookupService.saveBw(bwList);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat1.parse("2022-01-01");
            Example example = new Example(BwAlCu.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, currentDate);
            int delSize = bwAlCuMapper.deleteByExample(example);
            assertEquals(28, delSize);
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveFarmer() {
        // TC 2.3: Save Farmer lookup tables
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/farmer_template_test.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet farmerSheet = workBook.getSheet(MainConfig.SHEET_NAME_FARMER);
            List<FarmerVO> farmerVOList = lookupService.readFarmer(farmerSheet);
            lookupService.saveFarmer(farmerVOList);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat1.parse("2022-01-01");
            Example example = new Example(BeamFarmerChamber.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, currentDate);
            int delSize = beamFarmerChamberMapper.deleteByExample(example);
            assertEquals(30, delSize);
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveMurhoAl() {
        // TC 3.3: Save MurhoAl  lookup tables
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/murho_al_template_test.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet murhoAlSheet = workBook.getSheet(MainConfig.SHEET_NAME_MURHOAL);
            List<MurhoAl> murhoAlList = lookupService.readMurhoAl(murhoAlSheet);
            lookupService.saveMurhoAl(murhoAlList);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat1.parse("2022-01-01");
            Example example = new Example(MurhoAl.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, currentDate);
            int delSize = murhoAlMapper.deleteByExample(example);
            assertEquals(5, delSize);
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveMurhoCu() {
        // TC 4.3: Save MurhoCu  lookup tables
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("C:/UniMelb/COMP90082/KV/VIOLET/services/lookup/lookup-templates/murho_cu_template_test.xlsx"));
            MockMultipartFile multipartFile = new MockMultipartFile("file", inputStream);
            XSSFWorkbook workBook = ExcelUtils.getWorkBook(multipartFile);
            XSSFSheet murhoCuSheet = workBook.getSheet(MainConfig.SHEET_NAME_MURHOCU);
            List<MurhoCu> murhoCuList = lookupService.readMurhoCu(murhoCuSheet);
            lookupService.saveMurhoCu(murhoCuList);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat1.parse("2022-01-01");
            Example example = new Example(MurhoCu.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, currentDate);
            int delSize = murhoCuMapper.deleteByExample(example);
            assertEquals(5, delSize);
        } catch (IOException e) {
            KVException.display();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void savePlaneparallel() {
        //TC 5.3: Save Planeparallel lookup tables
        try {
            XSSFWorkbook workbook =
                    new XSSFWorkbook(new File("../../data samples/lookup-templates/planeparallel_template.xlsx"));
            XSSFSheet sheet = workbook.getSheet(MainConfig.SHEET_NAME_PLANEPARALLEL);
            List<PlaneparallelVO> planeparallelVOList = lookupService.readPlaneparallel(sheet);
            lookupService.savePlaneparallel(planeparallelVOList);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat1.parse("2022-01-01");
            Example example = new Example(PlaneparallelVO.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, currentDate);
            int delSize = beamPlaneparallelNkMapper.deleteByExample(example);
            assertEquals(5, delSize);
        } catch (IOException | ParseException e) {
            KVException.display();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    void savePstem() {
        //TC 6.3: Save Pstem lookup tables
        try {
            XSSFWorkbook workbook =
                    new XSSFWorkbook(new File("../../data samples/lookup-templates/pstem_template.xlsx"));
            XSSFSheet sheet = workbook.getSheet(MainConfig.SHEET_NAME_PSTEM);
            List<PstemMeasured> pstemMeasuredList = lookupService.readPstem(sheet);
            lookupService.savePstem(pstemMeasuredList);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = dateFormat1.parse("2022-01-01");
            Example example = new Example(PstemMeasured.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, currentDate);
            int delSize = pstemMeasuredMapper.deleteByExample(example);
            assertEquals(5, delSize);
        } catch (IOException | ParseException e) {
            KVException.display();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}