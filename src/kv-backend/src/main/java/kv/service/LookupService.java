package kv.service;

import kv.common.KVException;
import kv.config.MainConfig;
import kv.mapper.*;
import kv.pojo.dbo.*;
import kv.pojo.vo.lookup.FarmerVO;
import kv.pojo.vo.lookup.PlaneparallelVO;
import kv.utils.ExcelUtils;
import kv.utils.ListUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class LookupService {
    private final BwAlCuMapper bwAlCuMapper;
    private final CustomMapper customMapper;
    private final MurhoAlMapper murhoAlMapper;
    private final MurhoCuMapper murhoCuMapper;
    private final PstemMeasuredMapper pstemMeasuredMapper;
    private final BwDiameterMapper bwDiameterMapper;
    private final BwHvlAlMapper bwHvlAlMapper;
    private final BwHvlCuMapper bwHvlCuMapper;
    private final BwSsdMapper bwSsdMapper;
    private final BeamPlaneparallelChamberMapper beamPlaneparallelChamberMapper;
    private final BeamPlaneparallelListMapper beamPlaneparallelListMapper;
    private final BeamPlaneparallelNkMapper beamPlaneparallelNkMapper;
    private final ChambersListMapper chambersListMapper;
    private final BeamFarmerListMapper beamFarmerListMapper;
    private final BeamFarmerChamberMapper beamFarmerChamberMapper;
    private final SqlSessionFactory sqlSessionFactory;

    public LookupService(BwAlCuMapper bwAlCuMapper,
                         CustomMapper customMapper,
                         MurhoAlMapper murhoAlMapper,
                         MurhoCuMapper murhoCuMapper,
                         PstemMeasuredMapper pstemMeasuredMapper,
                         BwDiameterMapper bwDiameterMapper,
                         BwHvlAlMapper bwHvlAlMapper,
                         BwHvlCuMapper bwHvlCuMapper,
                         BwSsdMapper bwSsdMapper,
                         BeamPlaneparallelChamberMapper beamPlaneparallelChamberMapper,
                         BeamPlaneparallelListMapper beamPlaneparallelListMapper,
                         BeamPlaneparallelNkMapper beamPlaneparallelNkMapper,
                         ChambersListMapper chambersListMapper,
                         BeamFarmerListMapper beamFarmerListMapper,
                         BeamFarmerChamberMapper beamFarmerChamberMapper,
                         SqlSessionFactory sqlSessionFactory) {
        this.bwAlCuMapper = bwAlCuMapper;
        this.customMapper = customMapper;
        this.murhoAlMapper = murhoAlMapper;
        this.murhoCuMapper = murhoCuMapper;
        this.pstemMeasuredMapper = pstemMeasuredMapper;
        this.bwDiameterMapper = bwDiameterMapper;
        this.bwHvlAlMapper = bwHvlAlMapper;
        this.bwHvlCuMapper = bwHvlCuMapper;
        this.bwSsdMapper = bwSsdMapper;
        this.beamPlaneparallelChamberMapper = beamPlaneparallelChamberMapper;
        this.beamPlaneparallelListMapper = beamPlaneparallelListMapper;
        this.beamPlaneparallelNkMapper = beamPlaneparallelNkMapper;
        this.chambersListMapper = chambersListMapper;
        this.beamFarmerListMapper = beamFarmerListMapper;
        this.beamFarmerChamberMapper = beamFarmerChamberMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<BwAlCu> queryBw() {
        Example example = new Example(BwAlCu.class);
        example.orderBy("dateUpdated").desc();

        return bwAlCuMapper.selectByExample(example);
    }

    public List<FarmerVO> queryFarmer() {
        return customMapper.queryAllFarmer();
    }

    public List<MurhoAl> queryMurhoAl() {
        Example example = new Example(MurhoAl.class);
        example.orderBy("dateUpdated").desc();

        return murhoAlMapper.selectByExample(example);
    }

    public List<MurhoCu> queryMurhoCu() {
        Example example = new Example(MurhoCu.class);
        example.orderBy("dateUpdated").desc();

        return murhoCuMapper.selectByExample(example);
    }

    public List<PlaneparallelVO> queryPlaneparallel() {
        return customMapper.queryAllPlaneparallel();
    }

    public List<PstemMeasured> queryPstem() {
        Example example = new Example(PstemMeasured.class);
        example.orderBy("dateUpdated").desc();

        return pstemMeasuredMapper.selectByExample(example);
    }

    public List<BwAlCu> readBw(XSSFSheet sheet) {
        List<BwAlCu> bwAlCuList = new ArrayList<>();

        // dateUpdated
        Date dateUpdated = ExcelUtils.getJavaDate(ExcelUtils.getCell(sheet, MainConfig.BW_DATE_UPDATED_ADDRESS));
        // type
        String type = ExcelUtils.getCell(sheet, MainConfig.BW_TYPE_ADDRESS).toString();

        XSSFRow sheetRow;
        int rowIdx = MainConfig.BW_ROW_START - 1;
        while ((sheetRow = sheet.getRow(rowIdx++)) != null) {
            // hvl
            XSSFCell hvlCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.BW_HVL_COL));
            if (!ExcelUtils.isCellNumeric(hvlCell)) continue;
            double hvlValue = hvlCell.getNumericCellValue();

            for (int i = ExcelUtils.getColIdx(MainConfig.BW_COL_START); i < sheetRow.getLastCellNum(); i++) {
                // bw
                XSSFCell bwCell = sheetRow.getCell(i);
                if (!ExcelUtils.isCellNumeric(bwCell)) continue;
                double bw = bwCell.getNumericCellValue();

                // diameter
                XSSFCell diameterCell = sheet.getRow(MainConfig.BW_DIAMETER_ROW - 1).getCell(i);
                if (!ExcelUtils.isCellNumeric(diameterCell)) KVException.display();
                double diameter = diameterCell.getNumericCellValue();

                // ssd
                int colIdx = i;
                XSSFCell ssdCell;
                while (ExcelUtils.isCellEmpty(ssdCell =
                        sheet.getRow(MainConfig.BW_SSD_ROW - 1).getCell(colIdx))) {
                    colIdx--;
                }
                if (!ExcelUtils.isCellNumeric(ssdCell)) KVException.display();
                double ssd = ssdCell.getNumericCellValue();

                // build BwAlCu
                BwAlCu bwAlCu = new BwAlCu();
                bwAlCu.setType(type);

                if (MainConfig.BW_TYPE_VALUE_AL.equals(type)) {
                    bwAlCu.setHvlAl(hvlValue);
                } else if (MainConfig.BW_TYPE_VALUE_CU.equals(type)) {
                    bwAlCu.setHvlCu(hvlValue);
                } else {
                    KVException.display();
                }

                bwAlCu.setSsd(ssd);
                bwAlCu.setDiameter(diameter);
                bwAlCu.setBw(bw);
                bwAlCu.setDateUpdated(dateUpdated);

                bwAlCuList.add(bwAlCu);
            }
        }

        return bwAlCuList;
    }

    public List<FarmerVO> readFarmer(XSSFSheet sheet) {
        List<FarmerVO> farmerVOList = new ArrayList<>();

        // read dateUpdated
        Date dateUpdated = ExcelUtils.getJavaDate(ExcelUtils.getCell(sheet, MainConfig.FARMER_DATE_UPDATED_ADDRESS));

        XSSFRow sheetRow;
        int rowIdx = MainConfig.FARMER_ROW_START - 1;
        while ((sheetRow = sheet.getRow(rowIdx++)) != null) {
            // read the beam id value.
            XSSFCell beamFarmerIdCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.FARMER_BEAM_FARMER_ID_COL));
            if (ExcelUtils.isCellEmpty(beamFarmerIdCell)) continue;
            String beamFarmerId = beamFarmerIdCell.toString();

            // read the kvp value.
            XSSFCell kvCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.FARMER_KV_COL));
            if (!ExcelUtils.isCellNumeric(kvCell)) KVException.display();
            double kv = kvCell.getNumericCellValue();

            // read the hvlMeasuredMmCu value.
            double hvlMeasuredMmCu = 0;
            XSSFCell hvlMeasuredMmCuCell =
                    sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.FARMER_HVL_MEASURED_MM_CU_COL));
            if (ExcelUtils.isCellNumeric(hvlMeasuredMmCuCell)) {
                hvlMeasuredMmCu = hvlMeasuredMmCuCell.getNumericCellValue();
            }

            // read the hvlMeasuredMmAl value.
            double hvlMeasuredMmAl = 0;
            XSSFCell hvlMeasuredMmAlCell =
                    sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.FARMER_HVL_MEASURED_MM_AL_COL));
            if (ExcelUtils.isCellNumeric(hvlMeasuredMmAlCell)) {
                hvlMeasuredMmAl = hvlMeasuredMmAlCell.getNumericCellValue();
            }

            // display exception when both hvlMeasuredMmCu and hvlMeasuredMmAl is zero;
            if (hvlMeasuredMmCu == 0 && hvlMeasuredMmAl == 0) KVException.display();

            for (int i = ExcelUtils.getColIdx(MainConfig.FARMER_COL_START); i < sheetRow.getLastCellNum(); i++) {
                // read the nk value
                XSSFCell nkValueCell = sheetRow.getCell(i);
                if (!ExcelUtils.isCellNumeric(nkValueCell)) continue;
                double nkValue = nkValueCell.getNumericCellValue();

                // read chamber name
                XSSFCell chamberNameCell = sheet.getRow(MainConfig.FARMER_CHAMBER_NAME_ROW - 1).getCell(i);
                if (ExcelUtils.isCellEmpty(chamberNameCell)) KVException.display();
                String chamberName = chamberNameCell.toString();

                // read chamber sn
                XSSFCell chamberSnCell = sheet.getRow(MainConfig.FARMER_CHAMBER_SN_ROW - 1).getCell(i);
                if (ExcelUtils.isCellEmpty(chamberSnCell)) KVException.display();
                String chamberSn = chamberSnCell.toString().replace(".0", "");

                // build FarmerVO
                FarmerVO farmerVO = new FarmerVO();
                farmerVO.setBeamFarmerId(beamFarmerId);
                farmerVO.setDateUpdated(dateUpdated);
                farmerVO.setKv(kv);
                farmerVO.setHvlMeasuredMmCu(hvlMeasuredMmCu);
                farmerVO.setHvlMeasuredMmAl(hvlMeasuredMmAl);
                farmerVO.setChamberName(chamberName);
                farmerVO.setChamberSn(chamberSn); //String
                farmerVO.setNkValue(nkValue);

                farmerVOList.add(farmerVO);
            }
        }

        return farmerVOList;
    }

    public List<MurhoAl> readMurhoAl(XSSFSheet sheet) {
        List<MurhoAl> murhoAlList = new ArrayList<>();

        Date dateUpdated = ExcelUtils.getJavaDate(ExcelUtils.getCell(sheet, MainConfig.MURHO_DATE_UPDATED_ADDRESS));

        XSSFRow sheetRow;
        int rowIdx = MainConfig.MURHO_ROW_START - 1;
        while ((sheetRow = sheet.getRow(rowIdx++)) != null) {
            XSSFCell hvlmmAlCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.MURHO_HVL_COL));
            if (!ExcelUtils.isCellNumeric(hvlmmAlCell)) continue;
            double hvlmmAl = hvlmmAlCell.getNumericCellValue();

            XSSFCell murhoValueCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.MURHO_VALUE_COL));
            if (!ExcelUtils.isCellNumeric(murhoValueCell)) continue;
            double murhoValue = murhoValueCell.getNumericCellValue();

            MurhoAl murhoAl = new MurhoAl();
            murhoAl.setMurho(murhoValue);
            murhoAl.setHvlAl(hvlmmAl);
            murhoAl.setDateUpdated(dateUpdated);

            murhoAlList.add(murhoAl);
        }

        return murhoAlList;
    }

    public List<MurhoCu> readMurhoCu(XSSFSheet sheet) {
        List<MurhoCu> murhoCuList = new ArrayList<>();

        Date dateUpdated = ExcelUtils.getJavaDate(ExcelUtils.getCell(sheet, MainConfig.MURHO_DATE_UPDATED_ADDRESS));

        XSSFRow sheetRow;
        int rowIdx = MainConfig.MURHO_ROW_START - 1;
        while ((sheetRow = sheet.getRow(rowIdx++)) != null) {
            XSSFCell hvlmmCuCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.MURHO_HVL_COL));
            if (!ExcelUtils.isCellNumeric(hvlmmCuCell)) continue;
            double hvlmmCu = hvlmmCuCell.getNumericCellValue();

            XSSFCell murhoValueCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.MURHO_VALUE_COL));
            if (!ExcelUtils.isCellNumeric(murhoValueCell)) continue;
            double murhoValue = murhoValueCell.getNumericCellValue();

            MurhoCu murhoCu = new MurhoCu();
            murhoCu.setMurho(murhoValue);
            murhoCu.setHvlCu(hvlmmCu);
            murhoCu.setDateUpdated(dateUpdated);

            murhoCuList.add(murhoCu);
        }

        return murhoCuList;
    }

    public List<PlaneparallelVO> readPlaneparallel(XSSFSheet sheet) {
        List<PlaneparallelVO> planeparallelVOList = new ArrayList<>();

        // read the updated date of planeparallel sheet
        Date dateUpdated = ExcelUtils.getJavaDate(ExcelUtils.getCell(sheet, MainConfig.PP_DATE_UPDATED_ADDRESS));

        XSSFRow sheetRow;
        int rowIdx = MainConfig.PP_ROW_START - 1;
        while ((sheetRow = sheet.getRow(rowIdx++)) != null) {
            // read beamPlaneparallelId
            XSSFCell beamPpIdCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.PP_BEAM_PP_ID_COL));
            if (ExcelUtils.isCellEmpty(beamPpIdCell)) continue;
            String beamPpId = beamPpIdCell.toString();

            // read kv
            XSSFCell kvCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.PP_KV_COL));
            if (!ExcelUtils.isCellNumeric(kvCell)) KVException.display();
            double kv = kvCell.getNumericCellValue();

            // read hvlMeasuredMmAl
            XSSFCell hvlCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.PP_HVL_COL));
            if (!ExcelUtils.isCellNumeric(hvlCell)) KVException.display();
            double hvlMeasuredMmAl = hvlCell.getNumericCellValue();

            for (int i = ExcelUtils.getColIdx(MainConfig.PP_COL_START); i < sheetRow.getLastCellNum(); i++) {
                // read nk value
                XSSFCell ppCell = sheetRow.getCell(i);
                if (!ExcelUtils.isCellNumeric(ppCell)) continue;
                double nkValue = ppCell.getNumericCellValue();

                // read chamberName
                XSSFCell chamberNameCell = sheet.getRow(MainConfig.PP_CHAMBER_NAME_ROW - 1).getCell(i);
                if (ExcelUtils.isCellEmpty(chamberNameCell)) KVException.display();
                String chamberName = chamberNameCell.toString();

                // read chamberSn
                XSSFCell chamberSnCell = sheet.getRow(MainConfig.PP_CHAMBER_SN_ROW - 1).getCell(i);
                if (ExcelUtils.isCellEmpty(chamberSnCell)) KVException.display();
                String chamberSn = chamberSnCell.toString().replace(".0", "");

                // build PlaneparallelVO
                PlaneparallelVO planeparallelVO = new PlaneparallelVO();
                planeparallelVO.setBeamPlaneparallelId(beamPpId);
                planeparallelVO.setDateUpdated(dateUpdated);
                planeparallelVO.setChamberName(chamberName);
                planeparallelVO.setChamberSn(chamberSn);
                planeparallelVO.setKv(kv);
                planeparallelVO.setNkValue(nkValue);
                planeparallelVO.setHvlMeasuredMmAl(hvlMeasuredMmAl);

                planeparallelVOList.add(planeparallelVO);
            }
        }

        return planeparallelVOList;
    }

    public List<PstemMeasured> readPstem(XSSFSheet sheet) {
        List<PstemMeasured> pstemMeasuredList = new ArrayList<>();

        // read the updated data of pstem_measured sheet
        Date dateUpdated = ExcelUtils.getJavaDate(ExcelUtils.getCell(sheet, MainConfig.PSTEM_DATE_UPDATED_ADDRESS));

        // read the option and record as requested data type
        XSSFCell optionCell = ExcelUtils.getCell(sheet, MainConfig.PSTEM_OPTION_ADDRESS);
        if (ExcelUtils.isCellEmpty(optionCell)) KVException.display();

        String option = optionCell.toString();
        if (StringUtils.startsWithIgnoreCase(option, MainConfig.PSTEM_OPTION_VALUE_UNITY)) {
            option = MainConfig.PSTEM_OPTION_VALUE_UNITY;
        } else if (StringUtils.startsWithIgnoreCase(option, MainConfig.PSTEM_OPTION_VALUE_MEASURED)) {
            option = MainConfig.PSTEM_OPTION_VALUE_MEASURED;
        } else {
            KVException.display();
        }

        XSSFRow sheetRow;
        int rowIdx = MainConfig.PSTEM_ROW_START - 1;
        while ((sheetRow = sheet.getRow(rowIdx++)) != null) {
            // read beamId
            XSSFCell beamIdCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.PSTEM_BEAM_ID_COL));
            if (ExcelUtils.isCellEmpty(beamIdCell)) continue;
            String beamID = beamIdCell.toString();

            // read hvl
            XSSFCell hvlCell = sheetRow.getCell(ExcelUtils.getColIdx(MainConfig.PSTEM_HVL_COL));
            if (!ExcelUtils.isCellNumeric(hvlCell)) KVException.display();
            double hvlmmAl = hvlCell.getNumericCellValue();

            int colIdxStart = ExcelUtils.getColIdx(MainConfig.PSTEM_COL_START);
            for (int i = colIdxStart; i < sheetRow.getLastCellNum(); i++) {
                // read pstem
                XSSFCell pstemCell = sheetRow.getCell(i);
                if (!ExcelUtils.isCellNumeric(pstemCell)) continue;
                double pstem = pstemCell.getNumericCellValue();

                // read diameter
                XSSFCell diameterCell = sheet.getRow(MainConfig.PSTEM_DIAMETER_ROW - 1).getCell(i);
                if (!ExcelUtils.isCellNumeric(diameterCell)) KVException.display();
                double diameter = diameterCell.getNumericCellValue();

                // read chapter ID
                int colIdx = i;
                XSSFCell chapterIdCell;
                while (ExcelUtils.isCellEmpty(chapterIdCell =
                        sheet.getRow(MainConfig.PSTEM_CHAPTER_ID_ROW - 1).getCell(colIdx))) {
                    colIdx--;
                    if (colIdx < colIdxStart) KVException.display();
                }
                String chapterID = chapterIdCell.toString().replace(".0", "");

                // get beamPpChamberId
                String beamPpChamberId = beamID + "-" + chapterID;

                // build PstemMeasured
                PstemMeasured pstemMeasured = new PstemMeasured();
                pstemMeasured.setPstemOption(option);
                pstemMeasured.setPstemValue(pstem);
                pstemMeasured.setDiameter(diameter);
                pstemMeasured.setHvlMeasuredMmAl(hvlmmAl);
                pstemMeasured.setDateUpdated(dateUpdated);
                pstemMeasured.setBeamPpChamberId(beamPpChamberId);
                pstemMeasuredList.add(pstemMeasured);
            }
        }

        return pstemMeasuredList;
    }

    public void saveBw(List<BwAlCu> bwAlCuList) {
        if (ListUtils.isEmpty(bwAlCuList)) return;

        // Check whether a dataset has been uploaded for the day
        // If yes, delete all then replace
        Date dateUpdated = bwAlCuList.get(0).getDateUpdated();

        Example example = new Example(BwAlCu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, dateUpdated);
        bwAlCuMapper.deleteByExample(example);

        // get all diameter
        Set<Double> bwDiameterSet = new HashSet<>();
        for (BwDiameter bwDiameter : bwDiameterMapper.selectAll()) {
            bwDiameterSet.add(bwDiameter.getDiameter());
        }

        // get all hvlAl
        Set<Double> bwHvlAlSet = new HashSet<>();
        for (BwHvlAl bwHvlAl : bwHvlAlMapper.selectAll()) {
            bwHvlAlSet.add(bwHvlAl.getHvlAl());
        }

        // get all hvlCu
        Set<Double> bwHvlCuSet = new HashSet<>();
        for (BwHvlCu bwHvlCu : bwHvlCuMapper.selectAll()) {
            bwHvlCuSet.add(bwHvlCu.getHvlCu());
        }

        // get all ssd
        Set<Double> bwSsdAlSet = new HashSet<>();
        Set<Double> bwSsdBothSet = new HashSet<>();
        for (BwSsd bwSsd : bwSsdMapper.selectAll()) {
            if (MainConfig.BW_SSD_REF_VALUE_AL.equals(bwSsd.getRef())) {
                bwSsdAlSet.add(bwSsd.getSsd());
            } else {
                bwSsdBothSet.add(bwSsd.getSsd());
            }
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            BwAlCuMapper bwAlCuMapperBatch = sqlSession.getMapper(BwAlCuMapper.class);

            List<List<BwAlCu>> lists = ListUtils.splitList(bwAlCuList, 500);
            for (List<BwAlCu> list : lists) {
                for (BwAlCu bwAlCu : list) {
                    // check diameter
                    if (!bwDiameterSet.contains(bwAlCu.getDiameter())) {
                        BwDiameter bwDiameter = new BwDiameter();
                        bwDiameter.setDiameter(bwAlCu.getDiameter());
                        bwDiameterMapper.insert(bwDiameter);

                        bwDiameterSet.add(bwAlCu.getDiameter());
                    }

                    if (bwAlCu.getHvlAl() != null) {
                        // type is al
                        // check hvlAl
                        if (!bwHvlAlSet.contains(bwAlCu.getHvlAl())) {
                            BwHvlAl bwHvlAl = new BwHvlAl();
                            bwHvlAl.setHvlAl(bwAlCu.getHvlAl());
                            bwHvlAlMapper.insert(bwHvlAl);

                            bwHvlAlSet.add(bwAlCu.getHvlAl());
                        }

                        // check ssd
                        if (!bwSsdAlSet.contains(bwAlCu.getSsd()) && !bwSsdBothSet.contains(bwAlCu.getSsd())) {
                            BwSsd bwSsd = new BwSsd();
                            bwSsd.setSsd(bwAlCu.getSsd());
                            bwSsd.setRef(MainConfig.BW_SSD_REF_VALUE_AL);
                            bwSsdMapper.insert(bwSsd);

                            bwSsdAlSet.add(bwAlCu.getSsd());
                        }

                    } else {
                        // type is cu
                        // check hvlCu
                        if (!bwHvlCuSet.contains(bwAlCu.getHvlCu())) {
                            BwHvlCu bwHvlCu = new BwHvlCu();
                            bwHvlCu.setHvlCu(bwAlCu.getHvlCu());
                            bwHvlCuMapper.insert(bwHvlCu);

                            bwHvlCuSet.add(bwAlCu.getHvlCu());
                        }

                        // check ssd
                        if (!bwSsdBothSet.contains(bwAlCu.getSsd())) {
                            BwSsd bwSsd = new BwSsd();
                            bwSsd.setSsd(bwAlCu.getSsd());
                            bwSsd.setRef(MainConfig.BW_SSD_REF_VALUE_BOTH);
                            if (bwSsdAlSet.contains(bwAlCu.getSsd())) {
                                // ref: al -> both
                                bwSsdMapper.updateByPrimaryKey(bwSsd);
                                bwSsdAlSet.remove(bwAlCu.getSsd());
                            } else {
                                bwSsdMapper.insert(bwSsd);
                            }

                            bwSsdBothSet.add(bwAlCu.getSsd());
                        }
                    }

                    bwAlCuMapperBatch.insertSelective(bwAlCu);
                }
                sqlSession.commit();
            }
        }
    }

    public void saveFarmer(List<FarmerVO> farmerVOList) {
        if (ListUtils.isEmpty(farmerVOList)) return;

        // Check whether a dataset has been uploaded for the day
        // If yes, delete all then replace
        Date dateUpdated = farmerVOList.get(0).getDateUpdated();
        Example example = new Example(BeamFarmerChamber.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, dateUpdated);
        beamFarmerChamberMapper.deleteByExample(example);

        // get all farmer chambers in chamberList
        Set<String> chambersSnSet = new HashSet<>();
        for (ChambersList chambersList : chambersListMapper.selectAll()) {
            chambersSnSet.add(chambersList.getChamberSn());
        }

        // get all beamFarmerId in beamFarmerList
        Set<String> beamFarmerIdSet = new HashSet<>();
        for (BeamFarmerList beamFarmerList : beamFarmerListMapper.selectAll()) {
            beamFarmerIdSet.add(beamFarmerList.getBeamFarmerId());
        }

        List<BeamFarmerChamber> beamFarmerChamberList = new ArrayList<>(farmerVOList.size());

        for (FarmerVO farmerVO : farmerVOList) {
            // Check if the chamber SN already in the chamber_list database
            if (!chambersSnSet.contains(farmerVO.getChamberSn())) {
                ChambersList chambersList = new ChambersList();
                chambersList.setChamberSn(farmerVO.getChamberSn());
                chambersList.setChamberName(farmerVO.getChamberName());
                chambersList.setChamberType(MainConfig.FARMER_CHAMBER_TYPE_VALUE); // default read as "farmer"
                chambersListMapper.insert(chambersList);
                chambersSnSet.add(farmerVO.getChamberSn());
            }

            // Check if the beamFarmerID already in the beam_farmer_list database
            if (!beamFarmerIdSet.contains(farmerVO.getBeamFarmerId())) {
                BeamFarmerList beamFarmerList = new BeamFarmerList();
                beamFarmerList.setBeamFarmerId(farmerVO.getBeamFarmerId());
                beamFarmerList.setKv(farmerVO.getKv());
                beamFarmerList.setHvlMeasuredMmAl(farmerVO.getHvlMeasuredMmAl());
                beamFarmerList.setHvlMeasuredMmCu(farmerVO.getHvlMeasuredMmCu());
                beamFarmerListMapper.insert(beamFarmerList);
                beamFarmerIdSet.add(farmerVO.getBeamFarmerId());
            }

            //
            BeamFarmerChamber beamFarmerChamber = new BeamFarmerChamber();
            beamFarmerChamber.setBeamFarmerId(farmerVO.getBeamFarmerId());
            beamFarmerChamber.setChamberSn(farmerVO.getChamberSn());
            beamFarmerChamber.setDateUpdated(farmerVO.getDateUpdated());
            beamFarmerChamber.setNkValue(farmerVO.getNkValue());
            beamFarmerChamberList.add(beamFarmerChamber);
        }

        List<List<BeamFarmerChamber>> lists = ListUtils.splitList(beamFarmerChamberList, 100);
        for (List<BeamFarmerChamber> list : lists) {
            beamFarmerChamberMapper.insertList(list);
        }
    }

    public void saveMurhoAl(List<MurhoAl> murhoAlList) {
        List<List<MurhoAl>> lists = ListUtils.splitList(murhoAlList, 100);
        for (List<MurhoAl> list : lists) {
            murhoAlMapper.insertList(list);
        }
    }

    public void saveMurhoCu(List<MurhoCu> murhoCuList) {
        List<List<MurhoCu>> lists = ListUtils.splitList(murhoCuList, 100);
        for (List<MurhoCu> list : lists) {
            murhoCuMapper.insertList(list);
        }
    }

    public void savePlaneparallel(List<PlaneparallelVO> planeparallelVOList) {
        if (ListUtils.isEmpty(planeparallelVOList)) return;

        // check whether there are same date data in beam_planeparallel_nk table
        // if yes, delet them
        Date dataUpdated = planeparallelVOList.get(0).getDateUpdated();
        Example example = new Example(BeamPlaneparallelNk.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(MainConfig.FIELD_NAME_DATE_UPDATED, dataUpdated);
        beamPlaneparallelNkMapper.deleteByExample(example);

        // get Chamber Sn in chamber_list table
        Set<String> chamberSnSet = new HashSet<>();
        List<ChambersList> chambersLists = chambersListMapper.selectAll();
        for (ChambersList chambersList : chambersLists) {
            chamberSnSet.add(chambersList.getChamberSn());
        }

        // get beam ID in beam_planeparallel_list table
        Set<String> beamPlaneparallelIdSet = new HashSet<>();
        List<BeamPlaneparallelList> beamPlaneparallelLists = beamPlaneparallelListMapper.selectAll();
        for (BeamPlaneparallelList beamPlaneparallelList : beamPlaneparallelLists) {
            beamPlaneparallelIdSet.add(beamPlaneparallelList.getBeamPlaneparallelId());
        }

        // get beam pp chamber ID in beam_planeparallel_chamber table
        Set<String> beamPpChamberIdSet = new HashSet<>();
        List<BeamPlaneparallelChamber> beamPlaneparallelChambers = beamPlaneparallelChamberMapper.selectAll();
        for (BeamPlaneparallelChamber beamPlaneparallelChamber : beamPlaneparallelChambers) {
            beamPpChamberIdSet.add(beamPlaneparallelChamber.getBeamPpChamberId());
        }

        List<BeamPlaneparallelNk> beamPlaneparallelNkList = new ArrayList<>();
        for (PlaneparallelVO planeparallelVO : planeparallelVOList) {
            String beamId = planeparallelVO.getBeamPlaneparallelId();
            String chamberSn = planeparallelVO.getChamberSn();
            String chamberName = planeparallelVO.getChamberName();
            Date dateUpdated = planeparallelVO.getDateUpdated();
            Double kv = planeparallelVO.getKv();
            Double hvl = planeparallelVO.getHvlMeasuredMmAl();
            Double nk = planeparallelVO.getNkValue();
            String beamPpChamberId = beamId + "-" + chamberSn;

            // check whether chamber SN existis in the database
            // write the chamber record, if it does not exist
            if (!chamberSnSet.contains(chamberSn)) {
                ChambersList chambersList = new ChambersList();
                chambersList.setChamberSn(chamberSn);
                chambersList.setChamberName(chamberName);
                chambersList.setChamberType(MainConfig.PP_CHAMBER_TYPE_VALUE);
                chambersListMapper.insert(chambersList);
                chamberSnSet.add(chamberSn);
            }

            // check whether beam planeparallel ID exists in the database
            // write the beam record, if it does not exist
            if (!beamPlaneparallelIdSet.contains(beamId)) {
                BeamPlaneparallelList beamPlaneparallelList = new BeamPlaneparallelList();
                beamPlaneparallelList.setBeamPlaneparallelId(beamId);
                beamPlaneparallelList.setKv(kv);
                beamPlaneparallelList.setHvlMeasuredMmAl(hvl);
                beamPlaneparallelListMapper.insert(beamPlaneparallelList);
                beamPlaneparallelIdSet.add(beamId);
            }

            // check whether beam-chamber ID exists in the database
            // write the beam-chamber record, if it does not exist
            if (!beamPpChamberIdSet.contains(beamPpChamberId)) {
                BeamPlaneparallelChamber beamPlaneparallelChamber = new BeamPlaneparallelChamber();
                beamPlaneparallelChamber.setBeamPpChamberId(beamPpChamberId);
                beamPlaneparallelChamber.setBeamPlaneparallelId(beamId);
                beamPlaneparallelChamber.setChamberSn(chamberSn);
                beamPlaneparallelChamberMapper.insert(beamPlaneparallelChamber);
                beamPpChamberIdSet.add(beamPpChamberId);
            }

            // insert value into beam_planeparallel_nk table
            BeamPlaneparallelNk beamPlaneparallelNk = new BeamPlaneparallelNk();
            beamPlaneparallelNk.setBeamPpChamberId(beamPpChamberId);
            beamPlaneparallelNk.setNkValue(nk);
            beamPlaneparallelNk.setDateUpdated(dateUpdated);
            beamPlaneparallelNkList.add(beamPlaneparallelNk);
        }

        List<List<BeamPlaneparallelNk>> lists = ListUtils.splitList(beamPlaneparallelNkList, 100);
        for (List<BeamPlaneparallelNk> list : lists) {
            beamPlaneparallelNkMapper.insertList(list);
        }
    }

    public void savePstem(List<PstemMeasured> pstemMeasuredList) {
        List<List<PstemMeasured>> lists = ListUtils.splitList(pstemMeasuredList, 100);
        for (List<PstemMeasured> list : lists) {
            pstemMeasuredMapper.insertList(list);
        }
    }
}
