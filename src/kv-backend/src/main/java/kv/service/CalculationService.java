package kv.service;

import kv.common.KVException;
import kv.config.MainConfig;
import kv.mapper.*;
import kv.pojo.bo.calculation.InputConesTableBO;
import kv.pojo.bo.calculation.QueryPlaneparallelResultBO;
import kv.pojo.bo.calculation.intermediateResults.*;
import kv.pojo.dbo.*;
import kv.utils.ListUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static kv.utils.CalculationUtils.*;

@Service
public class CalculationService {
    // constants
    public static final String TYPE_AL = "al";
    public static final String TYPE_CU = "cu";
    public static final String UPPER_BOUND = "upper_bound";
    public static final String LOWER_BOUND = "lower_bound";
    public static final String EXACT_BOUND = "exact_bound";

    // query from database
    public String[] CHAMBER_SN_FARMER;
    public String[] CHAMBER_SN_PP;
    public String[] CHAMBER_SN_ALL;
    public double[] DIAMETER_AXIS;
    public double[] SSD_AL_AXIS;
    public double[] SSD_CU_AXIS;
    public double[] HVL_AL_AXIS;
    public double[] HVL_CU_AXIS;

    private final CustomMapper customMapper;
    private final BeamFarmerChamberMapper beamFarmerChamberMapper;
    private final BeamFarmerListMapper beamFarmerListMapper;
    private final BeamPlaneparallelNkMapper beamPlaneparallelNkMapper;
    private final BwAlCuMapper bwAlCuMapper;
    private final PstemMeasuredMapper pstemMeasuredMapper;
    private final ChambersListMapper chambersListMapper;
    private final BwDiameterMapper bwDiameterMapper;
    private final BwSsdMapper bwSsdMapper;
    private final BwHvlAlMapper bwHvlAlMapper;
    private final BwHvlCuMapper bwHvlCuMapper;
    private final SimpleDateFormat simpleDateFormat;


    public CalculationService(CustomMapper customMapper,
                              BeamFarmerChamberMapper beamFarmerChamberMapper,
                              BeamFarmerListMapper beamFarmerListMapper,
                              BeamPlaneparallelNkMapper beamPlaneparallelNkMapper,
                              BwAlCuMapper bwAlCuMapper,
                              PstemMeasuredMapper pstemMeasuredMapper,
                              ChambersListMapper chambersListMapper,
                              BwDiameterMapper bwDiameterMapper,
                              BwSsdMapper bwSsdMapper,
                              BwHvlAlMapper bwHvlAlMapper,
                              BwHvlCuMapper bwHvlCuMapper,
                              SimpleDateFormat simpleDateFormat) {
        this.customMapper = customMapper;
        this.beamFarmerChamberMapper = beamFarmerChamberMapper;
        this.beamFarmerListMapper = beamFarmerListMapper;
        this.beamPlaneparallelNkMapper = beamPlaneparallelNkMapper;
        this.bwAlCuMapper = bwAlCuMapper;
        this.pstemMeasuredMapper = pstemMeasuredMapper;
        this.chambersListMapper = chambersListMapper;
        this.bwDiameterMapper = bwDiameterMapper;
        this.bwSsdMapper = bwSsdMapper;
        this.bwHvlAlMapper = bwHvlAlMapper;
        this.bwHvlCuMapper = bwHvlCuMapper;
        this.simpleDateFormat = simpleDateFormat;
    }

    /**
     * @param beamDataList    List of BeamData coming from the Excel reading operation (or from Database query)
     * @param auditBeamInputs List of AuditBeamInputs from the Excel reading operation (or from Database query)
     * @param coneList        List of Cone from the Excel reading operation (or from Database query)
     * @description The main calculation function, which calculates nk,bw,murho,ccc,pstem. It returns a list of BackResult
     * @see "services/calculation/utility/start_calculate.py" in VIOLET_initial
     */
    public List<BackResult> getBackResult(List<BeamData> beamDataList, List<AuditBeamInputs> auditBeamInputs, List<Cone> coneList) {
        init();

        List<BackResult> backResults = new ArrayList<>();
        List<InputConesTableBO> coneDataList = queryInputConeDataList(coneList);
        NkResultBO nkResult = calculateNkValue(beamDataList);
        List<BwResultBO> bwResults = calculateBwValue(beamDataList, coneDataList);
        List<MurhoResultBO> murhoResults = calculateMurhoValue(beamDataList);
        List<CccResultBO> cccresults = calculateCccValue(bwResults, coneDataList, beamDataList);
        List<PstemResultBO> pstemResults = calculatePstemValue(beamDataList, coneDataList, auditBeamInputs);
        for (AuditBeamInputs inputs : auditBeamInputs) {
            String beanConeId = inputs.getBeamId() + "_" + inputs.getConeId();
            for (Map<String, String> nk : nkResult.getNkResult()) {
                if (inputs.getBeamId().equals(nk.get("id"))) {
                    for (String chamber : CHAMBER_SN_ALL) {
                        if (nk.containsKey(chamber)) {
                            BwResultBO bwResult = findBwResById(bwResults, beanConeId);
                            if (bwResult == null)
                                break; // Exception where target bwResult is not there, jump to next nk results.
                            BackResult result = new BackResult();
                            result.setBackResultId(inputs.getInputId() + "-" + chamber);
                            result.setInputId(inputs.getInputId());
                            result.setChamberSn(chamber);
                            result.setNk(Double.parseDouble(nk.get(chamber)));
                            result.setBwCombined(bwResult.getData().get("Bw_Combined"));
                            if (bwResult.getData().containsKey("Bw_Al"))
                                result.setBwAl(bwResult.getData().get("Bw_Al"));
                            if (bwResult.getData().containsKey("Bw_Cu"))
                                result.setBwCu(bwResult.getData().get("Bw_Cu"));
                            MurhoResultBO murhoResult = findMurhoById(murhoResults, inputs.getBeamId());
                            // TODO: revise the following calculation, why non-existing value is set to 1.0
                            if (murhoResult != null) {
                                // murho results could be not found.
                                result.setMurho(murhoResult.getMurho());
                            } else result.setMurho(1.0);
                            CccResultBO cccResult = findCCCById(cccresults, beanConeId);
                            if (cccResult != null) {
                                // ccc results could be not found.
                                result.setkClosedCone(cccResult.getkClosedCone());
                            } else result.setkClosedCone(1.0);
                            PstemResultBO pstemResult = findPstemById(pstemResults, beanConeId, chamber);
                            if (pstemResult != null) {
                                // pstem results could be not found.
                                result.setPstem(pstemResult.getPstem());
                            } else result.setPstem(1.0);
                            String warning = findWarningById(nkResult, inputs.getBeamId());
                            result.setWarning(warning);
                            backResults.add(result);
                        }
                    }
                }
            }
        }
        return backResults;
    }

    public void init() {
        // init chamber
        List<String> farmerChamberSnList = new ArrayList<>();
        List<String> ppChambersSnList = new ArrayList<>();
        List<String> allChamberSnList = new ArrayList<>();

        for (ChambersList chambersList : chambersListMapper.selectAll()) {
            allChamberSnList.add(chambersList.getChamberSn());
            if (MainConfig.FARMER_CHAMBER_TYPE_VALUE.equals(chambersList.getChamberType())) {
                farmerChamberSnList.add(chambersList.getChamberSn());
            } else if (MainConfig.PP_CHAMBER_TYPE_VALUE.equals(chambersList.getChamberType())) {
                ppChambersSnList.add(chambersList.getChamberSn());
            } else {
                KVException.display();
            }
        }

        CHAMBER_SN_FARMER = farmerChamberSnList.toArray(new String[0]);
        CHAMBER_SN_PP = ppChambersSnList.toArray(new String[0]);
        CHAMBER_SN_ALL = allChamberSnList.toArray(new String[0]);

        // init diameter
        List<Double> diameterList = new ArrayList<>();
        for (BwDiameter bwDiameter : bwDiameterMapper.selectAll()) {
            diameterList.add(bwDiameter.getDiameter());
        }
        DIAMETER_AXIS = ListUtils.toDoubleArrayAndSortAsc(diameterList);

        // init ssd
        List<Double> ssdAlList = new ArrayList<>();
        List<Double> ssdCuList = new ArrayList<>();

        for (BwSsd bwSsd : bwSsdMapper.selectAll()) {
            ssdAlList.add(bwSsd.getSsd());
            if (MainConfig.BW_SSD_REF_VALUE_BOTH.equals(bwSsd.getRef())) {
                ssdCuList.add(bwSsd.getSsd());
            }
        }

        SSD_AL_AXIS = ListUtils.toDoubleArrayAndSortAsc(ssdAlList);
        SSD_CU_AXIS = ListUtils.toDoubleArrayAndSortAsc(ssdCuList);

        // init hvl
        List<Double> hvlAlList = new ArrayList<>();
        for (BwHvlAl bwHvlAl : bwHvlAlMapper.selectAll()) {
            hvlAlList.add(bwHvlAl.getHvlAl());
        }
        HVL_AL_AXIS = ListUtils.toDoubleArrayAndSortAsc(hvlAlList);

        List<Double> hvlCuList = new ArrayList<>();
        for (BwHvlCu bwHvlCu : bwHvlCuMapper.selectAll()) {
            hvlCuList.add(bwHvlCu.getHvlCu());
        }
        HVL_CU_AXIS = ListUtils.toDoubleArrayAndSortAsc(hvlCuList);
    }

    private BwResultBO findBwResById(List<BwResultBO> target, String beanConeId) {
        for (BwResultBO res : target) {
            if (res.getId().equals(beanConeId))
                return res;
        }
        return null;
    }

    private MurhoResultBO findMurhoById(List<MurhoResultBO> target, String beamId) {
        for (MurhoResultBO res : target) {
            if (res.getBeamId().equals(beamId))
                return res;
        }
        return null;
    }

    private CccResultBO findCCCById(List<CccResultBO> target, String beanConeId) {
        for (CccResultBO res : target) {
            if (res.getId().equals(beanConeId))
                return res;
        }
        return null;
    }

    private PstemResultBO findPstemById(List<PstemResultBO> target, String beanConeId, String chamber) {
        for (PstemResultBO res : target) {
            if (res.getBeamConeId().equals(beanConeId) && res.getChamberSn().equals(chamber))
                return res;
        }
        return null;
    }

    private String findWarningById(NkResultBO results, String beamId) {
        for (Map<String, String> res : results.getNkWarning()) {
            if (res.get("beam_id").equals(beamId))
                return res.get("message");
        }
        return null;
    }

    /*
     * pstem calculations
     * */
    public List<PstemResultBO> calculatePstemValue(List<BeamData> beamDataList, List<InputConesTableBO> cones, List<AuditBeamInputs> auditBeamInputs) {
        List<PstemResultBO> results = new ArrayList<>();
        for (BeamData beam : beamDataList) {
            double hvlAl;
            if (beam.getHvlMeasuredMmAl() != null) hvlAl = beam.getHvlMeasuredMmAl();
            else continue;
            // What is 2.204?
            if (hvlAl <= 2.204) {
                List<String> matchedConeId = new ArrayList<>();
                List<InputConesTableBO> matchedCones = new ArrayList<>();

                for (AuditBeamInputs a : auditBeamInputs) {
                    if (a.getBeamId() != null && a.getBeamId().equals(beam.getBeamId())) {
                        matchedConeId.add(a.getConeId());
                    }
                }
                for (InputConesTableBO c : cones) {
                    if (matchedConeId.contains(c.getConeId())) {
                        matchedCones.add(c);
                    }
                }
                if (matchedCones.isEmpty() || matchedConeId.isEmpty()) KVException.display();
                for (InputConesTableBO c : matchedCones) {
                    for (String chamber : CHAMBER_SN_PP) {
                        double diameter = c.getDiameter();
                        if (diameter > 10) {
                            // extrapolation?
                            diameter = 10;
                        }
                        //TODO: Too many requests, should have optimized later
                        PstemMeasured lowerRef = queryDiameterBoundaryByAnchor(chamber, diameter, LOWER_BOUND);
                        PstemMeasured upperRef = queryDiameterBoundaryByAnchor(chamber, diameter, UPPER_BOUND);
                        if (hvlAl < 0.2) hvlAl = 0.2; //What is this?
                        PstemMeasured latest = queryLatestPstem();
                        PstemMeasured lowerRefLowerBound = queryHVLBoundaryByPstem(latest, lowerRef, chamber, hvlAl, LOWER_BOUND);
                        PstemMeasured lowerRefUpperBound = queryHVLBoundaryByPstem(latest, lowerRef, chamber, hvlAl, UPPER_BOUND);
                        PstemMeasured upperRefLowerBound = queryHVLBoundaryByPstem(latest, upperRef, chamber, hvlAl, LOWER_BOUND);
                        PstemMeasured upperRefUpperBound = queryHVLBoundaryByPstem(latest, upperRef, chamber, hvlAl, UPPER_BOUND);
                        double firstInterpolation = interpolation(
                                lowerRefLowerBound.getPstemValue(),
                                upperRefLowerBound.getPstemValue(),
                                lowerRefLowerBound.getDiameter(),
                                upperRefLowerBound.getDiameter(),
                                diameter
                        );
                        double secondInterpolation = interpolation(
                                lowerRefUpperBound.getPstemValue(),
                                upperRefUpperBound.getPstemValue(),
                                lowerRefUpperBound.getDiameter(),
                                upperRefUpperBound.getDiameter(),
                                diameter
                        );
                        double finalInterpolation = interpolation(
                                firstInterpolation,
                                secondInterpolation,
                                lowerRefLowerBound.getHvlMeasuredMmAl(),
                                lowerRefUpperBound.getHvlMeasuredMmAl(),
                                hvlAl
                        );
//                        System.out.println("DEBUG---1st lower: "+lowerRefLowerBound.getPstemValue());
//                        System.out.println("DEBUG---2nd lower: "+upperRefLowerBound.getPstemValue());
//                        System.out.println("DEBUG---1st_interpo: "+firstInterpolation);
//                        System.out.println("DEBUG---2nd_interpo: "+secondInterpolation);
//                        System.out.println("DEBUG---fin_interpo: "+finalInterpolation);
                        PstemResultBO result = new PstemResultBO(beam.getBeamId() + "_" + c.getConeId(), chamber, finalInterpolation);
                        results.add(result);
                    }
                }
            }
        }
        return results;
    }

    public PstemMeasured queryHVLBoundaryByPstem(PstemMeasured latest, PstemMeasured ref, String chamber, double hvlAl, String bound) {
        Example example = new Example(PstemMeasured.class);
        if (UPPER_BOUND.equals(bound)) {
            example.createCriteria()
                    .andGreaterThanOrEqualTo("hvlMeasuredMmAl", hvlAl)
                    .andLike("beamPpChamberId", "%" + chamber)
                    .andEqualTo("pstemOption", "measured")
                    .andEqualTo("diameter", ref.getDiameter())
                    .andEqualTo("dateUpdated", latest.getDateUpdated());
            example.orderBy("hvlMeasuredMmAl");
        } else if (LOWER_BOUND.equals(bound)) {
//            System.out.println("DEBUG--- d_ref:"+ref.getDiameter());
//            System.out.println("DEBUG--- chamber:"+chamber);
//            System.out.println("DEBUG--- Date:"+latest.getDateUpdated().toString());
//            System.out.println("DEBUG--- hvl_al:"+hvlAl);
            example.createCriteria()
                    .andLessThanOrEqualTo("hvlMeasuredMmAl", hvlAl)
                    .andLike("beamPpChamberId", "%" + chamber)
                    .andEqualTo("pstemOption", "measured")
                    .andEqualTo("diameter", ref.getDiameter())
                    .andEqualTo("dateUpdated", latest.getDateUpdated());
            example.orderBy("hvlMeasuredMmAl").desc();
        } else return null;
        List<PstemMeasured> results = pstemMeasuredMapper.selectByExample(example);
        if (results.size() == 0) return null;
        else return results.get(0);
    }

    public PstemMeasured queryLatestPstem() {
        Example example = new Example(PstemMeasured.class);
        example.createCriteria()
                .andEqualTo("pstemOption", "measured");
        example.orderBy("dateUpdated").desc();
        List<PstemMeasured> results = pstemMeasuredMapper.selectByExample(example);
        if (results.size() == 0) return null;
        else return results.get(0);
    }

    public PstemMeasured queryDiameterBoundaryByAnchor(String chamber, double anchorDiameter, String bound) {
        Example example = new Example(PstemMeasured.class);
        if (UPPER_BOUND.equals(bound)) {
            example.createCriteria()
                    .andGreaterThanOrEqualTo("diameter", anchorDiameter)
                    .andLike("beamPpChamberId", "%" + chamber)
                    .andEqualTo("pstemOption", "measured");
            example.orderBy("diameter");
        } else if (LOWER_BOUND.equals(bound)) {
            example.createCriteria()
                    .andLessThanOrEqualTo("diameter", anchorDiameter)
                    .andLike("beamPpChamberId", "%" + chamber)
                    .andEqualTo("pstemOption", "measured");
            example.orderBy("diameter").desc();
        }
        List<PstemMeasured> results = pstemMeasuredMapper.selectByExample(example);
        if (results.size() == 0) return null;
        else return results.get(0);
    }

    /*
     * ccc calculations
     * */
    public List<CccResultBO> calculateCccValue(List<BwResultBO> bwResults, List<InputConesTableBO> cones, List<BeamData> beams) {
        List<CccResultBO> results = new ArrayList<>();
        for (InputConesTableBO cone : cones) {
            for (BeamData beam : beams) {
                StringJoiner joiner = new StringJoiner("_");
                joiner.add(beam.getBeamId());
                joiner.add(cone.getConeId());
                String id = joiner.toString();
                CccResultBO cccResultBO = new CccResultBO();
                if (cone.getOpenClosed().equals("Open")) {
                    cccResultBO.setkClosedCone(1.000); // what does this 1.000 mean?
                } else if (cone.getOpenClosed().equals("Closed")) {
                    BwResultBO targetBO = null;
                    synchronized (this) {
                        for (BwResultBO b : bwResults) {
                            if (b.getId().equals(id)) {
                                targetBO = b;
                                break;
                            }
                        }
                    }
                    if (targetBO == null) KVException.display();
                    try {
                        double bwVal = targetBO.getData().get("Bw_Combined");
                        cccResultBO.setkClosedCone(calculateKClosedCone(bwVal));
                    } catch (Exception ignored) {
                        System.out.println("DEBUG --- something wrong in getKclosedCone");
                    }
                } else KVException.display();
                cccResultBO.setId(id);
                results.add(cccResultBO);
            }
        }
        return results;
    }

    /*
     * murho calculations
     */
    public List<MurhoResultBO> calculateMurhoValue(List<BeamData> beamDataList) {
        List<MurhoResultBO> results = new ArrayList<>();
        List<MurhoAl> murhoAlsByLatestDate = queryMurhoAlByLatestDate();
        List<MurhoCu> murhoCusByLatestDate = queryMurhoCuByLatestDate();

        for (BeamData beam : beamDataList) {
            double alMurho = Double.NaN, cuMurho = Double.NaN;
            if (beam.getHvlMeasuredMmAl() != null) alMurho = murhoCalculation(beam, murhoAlsByLatestDate, null);
            if (beam.getHvlMeasuredMmCu() != null) cuMurho = murhoCalculation(beam, null, murhoCusByLatestDate);
            if (!Double.isNaN(alMurho) && !Double.isNaN(cuMurho)) {
                MurhoResultBO result = generateMurhoResult(beam, (alMurho + cuMurho) / 2);
                result.setAlMurho(alMurho);
                result.setCuMurho(cuMurho);
                result.setHvlMeasuredAl(beam.getHvlMeasuredMmAl());
                result.setHvlMeasuredCu(beam.getHvlMeasuredMmCu());
                results.add(result);
            } else if (Double.isNaN(alMurho) && !Double.isNaN(cuMurho)) {
                MurhoResultBO result = generateMurhoResult(beam, cuMurho);
                result.setCuMurho(cuMurho);
                result.setHvlMeasuredCu(beam.getHvlMeasuredMmCu());
                results.add(result);
            } else if (!Double.isNaN(alMurho) && Double.isNaN(cuMurho)) {
                MurhoResultBO result = generateMurhoResult(beam, alMurho);
                result.setAlMurho(alMurho);
                result.setHvlMeasuredAl(beam.getHvlMeasuredMmAl());
                results.add(result);
            } else KVException.display();
        }
        return results;
    }

    public MurhoResultBO generateMurhoResult(BeamData beam, double result) {
        MurhoResultBO res = new MurhoResultBO();
        res.setBeamId(beam.getBeamId());
        res.setMurho(result);
        res.setBeamId(beam.getBeamId());
        res.setKvp(beam.getNomEnergy());
        return res;
    }

    public double murhoCalculation(BeamData beam, List<MurhoAl> murhoAls, List<MurhoCu> murhoCus) {
        // one of murhoAls and murhoCus should be null
        if ((murhoAls == null && murhoCus == null) || (murhoAls != null && murhoCus != null)) {
//            System.out.println("DEBUG---both lists are null");
            return Double.NaN;
        }
        String type = murhoAls != null ? TYPE_AL : TYPE_CU;
        // presorted list by sql.
        double maxHvl = 0, minHvl = 0, beamHvl = 0;
        double[] hvlList, murhoList;
        if (type.equals(TYPE_CU) && beam.getHvlMeasuredMmCu() != null) {
            hvlList = new double[murhoCus.size()];
            murhoList = new double[murhoCus.size()];
            int hvlListIndex = 0;
            for (MurhoCu murhoCu : murhoCus) {
                if (Objects.equals(beam.getHvlMeasuredMmCu(), murhoCu.getHvlCu())) {
                    return beam.getHvlMeasuredMmCu();
                }
                hvlList[hvlListIndex] = murhoCu.getHvlCu();
                murhoList[hvlListIndex] = murhoCu.getMurho();
                hvlListIndex++;
            }
            // presorted list by sql.
            maxHvl = murhoCus.get(murhoCus.size() - 1).getHvlCu();
            minHvl = murhoCus.get(0).getHvlCu();
            beamHvl = beam.getHvlMeasuredMmCu();
        } else if (type.equals(TYPE_AL) && beam.getHvlMeasuredMmAl() != null) {
            hvlList = new double[murhoAls.size()];
            murhoList = new double[murhoAls.size()];
            int hvlListIndex = 0;
            for (MurhoAl murhoAl : murhoAls) {
                if (Objects.equals(beam.getHvlMeasuredMmAl(), murhoAl.getHvlAl())) {
                    return beam.getHvlMeasuredMmAl();
                }
                hvlList[hvlListIndex] = murhoAl.getHvlAl();
                murhoList[hvlListIndex] = murhoAl.getMurho();
                hvlListIndex++;
            }
            // presorted list by sql.
            maxHvl = murhoAls.get(murhoAls.size() - 1).getHvlAl();
            minHvl = murhoAls.get(0).getHvlAl();
            beamHvl = beam.getHvlMeasuredMmAl();
        } else {
//            System.out.println("DEBUG---wrong data input!");
            return Double.NaN;
        }
//        System.out.println("DEBUG -- maxHvl:"+maxHvl);
//        System.out.println("DEBUG -- minHvl:"+minHvl);
//        System.out.println("DEBUG -- beamHvl:"+beamHvl);
        if (isValueInBound(beamHvl, minHvl, maxHvl)) {
            for (int i = 0; i < hvlList.length; i++) {
                if (beamHvl - hvlList[i] < 0) {
                    if (i == 0) {
//                        System.out.println("DEBUG--- first value match");
                        return Double.NaN; // Just in case.
                    }
//                    System.out.println("DEBUG -- hvlList[i]:"+hvlList[i]);
//                    System.out.println("DEBUG -- murhoList[i]:"+murhoList[i]);
//                    System.out.println("DEBUG -- hvlList[i-1]:"+hvlList[i-1]);
//                    System.out.println("DEBUG -- murhoList[i-1]:"+murhoList[i-1]);
//                    System.out.println("DEBUG -- beamHvl:"+beamHvl);
                    return interpolation(
                            murhoList[i - 1],
                            murhoList[i],
                            hvlList[i - 1],
                            hvlList[i],
                            beamHvl
                    );
                }
            }
        } else {
            // implement extrap here if necessary.
//            System.out.println("DEBUG---out of bound");
            return Double.NaN;
        }
//        System.out.println("DEBUG---default exit");
        return Double.NaN;
    }

    public List<MurhoAl> queryMurhoAlByLatestDate() {
        return customMapper.queryMurhoAlByLatestDate();
    }

    public List<MurhoCu> queryMurhoCuByLatestDate() {
        return customMapper.queryMurhoCuByLatestDate();
    }

    /*
        bw calculations
     */
    public List<BwResultBO> calculateBwValue(List<BeamData> beamDataList, List<InputConesTableBO> coneDataList) {
        List<BwResultBO> results = new ArrayList<>();
        List<BeamData> beams = beamDataList;
        List<InputConesTableBO> cones = coneDataList;
        for (InputConesTableBO cone : cones) {
            for (BeamData beam : beams) {
                List<Double> bwRes = new ArrayList<>();
                Map<String, Double> resultMap = new HashMap<>();
                Map<String, Double> hVLMap = getHVLAlCuAsMap(beam); // example: {"Al":1.0,"Cu":0.1}
//                System.out.println("DEBUG-- map: "+hVLMap.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
//                        .collect(Collectors.joining("|")));
                if (hVLMap.containsKey(TYPE_AL)) {
                    double alResult = bwCalculation(cone, beam, TYPE_AL);
                    bwRes.add(alResult);
                    resultMap.put("Bw_Al", alResult);
                }
                if (hVLMap.containsKey(TYPE_CU)) {
                    double cuResult = bwCalculation(cone, beam, TYPE_CU);
                    bwRes.add(cuResult);
                    resultMap.put("Bw_Cu", cuResult);
                }
                double sum = bwRes.stream().mapToDouble(a -> a).sum();
                resultMap.put("Bw_Combined", sum / bwRes.size());
                BwResultBO res = new BwResultBO(beam.getBeamId() + "_" + cone.getConeId(), resultMap);
                results.add(res);
            }
        }
        return results;
    }

    public double bwCalculation(InputConesTableBO cone, BeamData beam, String type) {
//        System.out.println("DEBUG --- params: ssd:"+cone.getSsd()+
//                ", diameter:"+cone.getDiameter()+
//                ", hvl:"+(TYPE_AL.equals(type)?beam.getHvlMeasuredMmAl():beam.getHvlMeasuredMmCu())+
//                ", type:"+type);
        double[] ssdAxis = null;
        double[] hvlAxis = null;
        double hvl = Double.NaN;
        double ssd = cone.getSsd();
        double diameter = cone.getDiameter();
        if (TYPE_AL.equals(type)) {
            ssdAxis = SSD_AL_AXIS;
            hvlAxis = HVL_AL_AXIS;
            hvl = beam.getHvlMeasuredMmAl();
        } else if (TYPE_CU.equals(type)) {
            ssdAxis = SSD_CU_AXIS;
            hvlAxis = HVL_CU_AXIS;
            hvl = beam.getHvlMeasuredMmCu();
        } else {
            KVException.display();
            return Double.NaN;
        }
//        System.out.println("DEBUG -- type: "+type);
        if (
                !isValueInBound(ssd, ssdAxis[0], ssdAxis[ssdAxis.length - 1])
                        || !isValueInBound(diameter, DIAMETER_AXIS[0], DIAMETER_AXIS[DIAMETER_AXIS.length - 1])
                        || !isValueInBound(hvl, hvlAxis[0], hvlAxis[hvlAxis.length - 1])
        ) {
            //"Illegal inputs of ssd = %f, hvl = %f, diameter = %f and type = %s
            // which are out of range of the lookup "
            //            "table." % (ssd, hvl, diameter, type)
            KVException.display();
            return Double.NaN;
        }
        if (ssd < 10.0 && hvl > 4.0) {
            // "Illegal inputs of ssd = %f, hvl = %f, diameter = %f and type = %s
            // which are out of range of the lookup "
            //            "table." % (ssd, hvl, diameter, type)
            KVException.display();
            return Double.NaN;
        }
        Map<String, Double> ssdRange = getRange(ssd, ssdAxis);
        Map<String, Double> hvlRange = getRange(hvl, hvlAxis);
        Map<String, Double> diameterRange = getRange(diameter, DIAMETER_AXIS);
        if (ssdRange.containsKey(EXACT_BOUND) && hvlRange.containsKey(EXACT_BOUND) && diameterRange.containsKey(EXACT_BOUND)) {
//            System.out.println("DEBUG -- exact bound found for ssd,hvl and diameter, return exact value");
            return queryLatestBwAlCu(type, ssdRange.get(EXACT_BOUND), diameterRange.get(EXACT_BOUND), hvlRange.get(EXACT_BOUND)).getSsd();
        }
        List<Double> ssdValues = ssdRange.values().stream().map(v -> v.doubleValue()).collect(Collectors.toList());
        List<Double> hvlValues = hvlRange.values().stream().map(v -> v.doubleValue()).collect(Collectors.toList());
        List<Double> diameterValues = diameterRange.values().stream().map(v -> v.doubleValue()).collect(Collectors.toList());
//        System.out.println("DEBUG: result size ssd:"+ssdValues.size());
//        System.out.println("DEBUG: result size hvl:"+hvlValues.size());
//        System.out.println("DEBUG: result size dia:"+diameterValues.size());
        Collections.sort(ssdValues);
        Collections.sort(hvlValues);
        Collections.sort(diameterValues);
        // in case that exact bound happens, extend the list with exact bound value.
        // TODO: This+below for loop may be optimized
        if (ssdValues.size() == 1) ssdValues.add(ssdValues.get(0));
        if (hvlValues.size() == 1) hvlValues.add(hvlValues.get(0));
        if (diameterValues.size() == 1) diameterValues.add(diameterValues.get(0));

//        System.out.println("------trimed-----");
//        System.out.println("DEBUG: result size ssd:"+ssdValues.size());
//        System.out.println("DEBUG: result size hvl:"+hvlValues.size());
//        System.out.println("DEBUG: result size dia:"+diameterValues.size());
        double[][][] requiredValues = new double[2][2][2];
        int i = 0, j = 0, k = 0;
        for (Double s : ssdValues) {
            for (Double h : hvlValues) {
                for (Double d : diameterValues) {
//                  System.out.println("i:"+i+",j:"+j+",k:"+k);
                    requiredValues[i][j][k] = queryLatestBwAlCu(type, s, d, h).getBw();
                    k += 1;
                }
                k = 0;
                j += 1;
            }
            j = 0;
            i += 1;
        }
        double hvl1 = interpolation(
                requiredValues[0][0][0],
                requiredValues[0][1][0],
                hvlValues.get(0),
                hvlValues.get(1),
                hvl);
        double hvl2 = interpolation(
                requiredValues[0][0][1],
                requiredValues[0][1][1],
                hvlValues.get(0),
                hvlValues.get(1),
                hvl);
        double hvl3 = interpolation(
                requiredValues[1][0][0],
                requiredValues[1][1][0],
                hvlValues.get(0),
                hvlValues.get(1),
                hvl);
        double hvl4 = interpolation(
                requiredValues[1][0][1],
                requiredValues[1][1][1],
                hvlValues.get(0),
                hvlValues.get(1),
                hvl);
//        System.out.println("DEBUG--- hvl 1,2,3,4: "+hvl1+","+hvl2+","+hvl3+","+hvl4);
//        System.out.println("DEBUG--- diameterValues: "+diameterValues.get(0)+","+diameterValues.get(1));
//        System.out.println("DEBUG--- diameter: "+diameter);

        double d1 = interpolation(hvl1, hvl2, diameterValues.get(0), diameterValues.get(1), diameter);
        double d2 = interpolation(hvl3, hvl4, diameterValues.get(0), diameterValues.get(1), diameter);
//        System.out.println("DEBUG--- d 1,2: "+d1+","+d2);
        double resultSSD = interpolation(d1, d2, ssdValues.get(0), ssdValues.get(1), ssd);
//        System.out.println("DEBUG--- resultant ssd: "+resultSSD);
        return resultSSD;
    }

    public BwAlCu queryLatestBwAlCu(String type, double ssd, double diameter, double hvl) {
        Example example = new Example(BwAlCu.class);
//        System.out.println("DEBUG---type:"+type+",ssd:"+ssd+",diameter:"+diameter+",hvl:"+hvl);
        String hvlFieldName = TYPE_CU.equals(type) ? "hvlCu" : "hvlAl";
        example.createCriteria()
                .andEqualTo("type", type)
                .andEqualTo("ssd", ssd)
                .andEqualTo("diameter", diameter)
                .andEqualTo(hvlFieldName, hvl);
        example.orderBy("dateUpdated").desc();
        List<BwAlCu> results = bwAlCuMapper.selectByExample(example);
        if (results.size() == 0) return null;
        else return results.get(0);
    }

    // Renamed function from "check_exist" to getRange.
    public Map<String, Double> getRange(double target, double[] axis) {
        Map<String, Double> boundary = new HashMap<>();
        for (int i = 0; i < axis.length; i++) {
            if (axis[i] == target) {
                boundary.put(EXACT_BOUND, axis[i]);
                return boundary;
            } else if (axis[i] > target) {
                boundary.put(UPPER_BOUND, axis[i]);
                boundary.put(LOWER_BOUND, i == 0 ? 0 : axis[i - 1]);
                return boundary;
            }
        }
        return boundary;
    }
    /*
     nk calculations
     */

    /**
     * @param beamDataList Object
     * @description Calculate the NK value
     * @see "services/calculation/utility/nk_value.py" in VIOLET_initial
     */
    public NkResultBO calculateNkValue(List<BeamData> beamDataList) {
        NkResultBO result = new NkResultBO();
        List<BeamData> beams = beamDataList;
        List<Map<String, String>> calculationResult = new ArrayList<>();
        List<Map<String, String>> warnMessageList = new ArrayList<>();
        for (BeamData beam : beams) {
            Map<String, Double> hVLMap = getHVLAlCuAsMap(beam); // example: {"Al":1.0,"Cu":0.1}
            if (hVLMap.containsKey(TYPE_AL) && hVLMap.containsKey(TYPE_CU)) {
                NkIntermediateResultBO farmerBeamsAl = selectFromFarmer(beam, TYPE_AL);
                NkIntermediateResultBO farmerBeamsCu = selectFromFarmer(beam, TYPE_CU);
                if ((farmerBeamsAl.getExtrapolation())) {
                    Map<String, String> warnMessage = new HashMap<>();
                    warnMessage.put("beam_id", beam.getBeamId());
                    warnMessage.put("message", "Extrapolation_Nk_Al");
                    warnMessageList.add(warnMessage);
                }
                if ((farmerBeamsCu.getExtrapolation())) {
                    Map<String, String> warnMessage = new HashMap<>();
                    warnMessage.put("beam_id", beam.getBeamId());
                    warnMessage.put("message", "Extrapolation_Nk_Cu");
                    warnMessageList.add(warnMessage);
                }
                for (String chamber : CHAMBER_SN_FARMER) {
                    Map<String, String> singleResult = new HashMap<>();
                    double nkAl = interpolation(farmerBeamsAl.getFirstOrLowerBeam().get("nk_" + chamber),
                            farmerBeamsAl.getSecondOrUpperBeam().get("nk_" + chamber),
                            farmerBeamsAl.getFirstOrLowerBeam().get(TYPE_AL),
                            farmerBeamsAl.getSecondOrUpperBeam().get(TYPE_AL),
                            beam.getHvlMeasuredMmAl());
                    double nkCu = interpolation(farmerBeamsCu.getFirstOrLowerBeam().get("nk_" + chamber),
                            farmerBeamsCu.getSecondOrUpperBeam().get("nk_" + chamber),
                            farmerBeamsCu.getFirstOrLowerBeam().get(TYPE_CU),
                            farmerBeamsCu.getSecondOrUpperBeam().get(TYPE_CU),
                            beam.getHvlMeasuredMmCu());
                    singleResult.put("id", beam.getBeamId());
                    singleResult.put(chamber, String.valueOf((nkAl + nkCu) / 2));
                    calculationResult.add(singleResult);
                }
            } else if (hVLMap.containsKey(TYPE_AL)) {
                NkIntermediateResultBO farmerBeamsAl = selectFromFarmer(beam, TYPE_AL);
                if (farmerBeamsAl.getExtrapolation()) {
                    Map<String, String> warningMessage = new HashMap<>();
                    warningMessage.put("beam_id", beam.getBeamId());
                    warningMessage.put("message", "Extrapolation_Nk_Al");
                    warnMessageList.add(warningMessage);
                }
                generateInterpolationResults(CHAMBER_SN_FARMER, calculationResult, beam, farmerBeamsAl, TYPE_AL);
            } else if (hVLMap.containsKey(TYPE_CU)) {
                NkIntermediateResultBO farmerBeamsCu = selectFromFarmer(beam, TYPE_CU);
                if (farmerBeamsCu.getExtrapolation()) {
                    Map<String, String> warningMessage = new HashMap<>();
                    warningMessage.put("beam_id", beam.getBeamId());
                    warningMessage.put("message", "Extrapolation_Nk_Cu");
                    warnMessageList.add(warningMessage);
                }
                generateInterpolationResults(CHAMBER_SN_FARMER, calculationResult, beam, farmerBeamsCu, TYPE_CU);
            }
            // TODO: what is this 2.204 ????
            if (hVLMap.containsKey(TYPE_AL) && beam.getHvlMeasuredMmAl() <= 2.204) {
                NkIntermediateResultBO planeparallelResult = selectFromPlaneparallel(beam, TYPE_AL); // it is always TYPE_AL
                generateInterpolationResults(CHAMBER_SN_PP, calculationResult, beam, planeparallelResult, TYPE_AL);
            }
        }
        result.setNkResult(calculationResult);
        result.setNkWarning(warnMessageList);
        return result;
    }

    private void generateInterpolationResults(String[] chambers, List<Map<String, String>> calculationResult, BeamData beam, NkIntermediateResultBO planeparallelResult, String type) {
        for (String chamber : chambers) {
            Map<String, String> singleResult = new HashMap<>();
            double interpolationResult = interpolation(planeparallelResult.getFirstOrLowerBeam().get("nk_" + chamber),
                    planeparallelResult.getSecondOrUpperBeam().get("nk_" + chamber),
                    planeparallelResult.getFirstOrLowerBeam().get(type),
                    planeparallelResult.getSecondOrUpperBeam().get(type),
                    type.equals(TYPE_AL) ? beam.getHvlMeasuredMmAl() : beam.getHvlMeasuredMmCu());
            singleResult.put("id", beam.getBeamId());
            singleResult.put(chamber, String.valueOf(interpolationResult));
            calculationResult.add(singleResult);
        }
    }

    public NkIntermediateResultBO selectFromFarmer(BeamData beam, String type) {
        BeamFarmerChamber latestBeamFarmerChamber = queryLatestFarmerLookupTable();
        if (latestBeamFarmerChamber == null) KVException.display();
        NkIntermediateResultBO result = new NkIntermediateResultBO();
        Double kvp = beam.getNomEnergy();
        Double hvl = type.equals(TYPE_AL) ? beam.getHvlMeasuredMmAl() : beam.getHvlMeasuredMmCu();
        //check if Kvp not bounded in the existing kvp or nomEmergy
        Example beamFarmerListExample = new Example(BeamFarmerList.class);
        // Note: the nomEnergy is equivalent to kvp according to start_calculate.py
        beamFarmerListExample.createCriteria().andEqualTo("kv", beam.getNomEnergy());
        // if no exact kv has found, use closest kvp instead
        if (beamFarmerListMapper.selectByExample(beamFarmerListExample).size() == 0) {
//            System.out.println("DEBUG --- closest kvp has been chosen");
            kvp = customMapper.queryClosestKvp(beam.getNomEnergy());
        }
        // HVL in the scope (interpolation)
//        System.out.println("DEBUG --- kvp: "+kvp);
//        System.out.println("DEBUG --- hvl: "+hvl);
//        System.out.println("DEBUG --- type: "+type);
        boolean lowerCheck = checkHvlLower(kvp, hvl, type);
        boolean upperCheck = checkHvlUpper(kvp, hvl, type);
//        System.out.println("DEBUG --- lowerCheck: "+lowerCheck);
//        System.out.println("DEBUG --- upperCheck: "+upperCheck);

        if (lowerCheck && upperCheck) {
            Map<String, Double> lowerBeam = new HashMap<>();
            Map<String, Double> upperBeam = new HashMap<>();
            List<BeamFarmerChamber> lowerTable = queryHVLLowerTable(new FarmerInfoBO(kvp, hvl, type, latestBeamFarmerChamber.getDateUpdated()));
            List<BeamFarmerChamber> upperTable = queryHVLUpperTable(new FarmerInfoBO(kvp, hvl, type, latestBeamFarmerChamber.getDateUpdated()));
//            System.out.println("DEBUG --- lowerTable ");
            for (BeamFarmerChamber bfc : lowerTable) {
                result.setFirstOrLowerBeamId(bfc.getBeamFarmerId());
                lowerBeam.put("nk_" + bfc.getChamberSn(), bfc.getNkValue());
//                System.out.println("DEBUG --- setFirstOrLowerBeamId(bfc.getBeamFarmerId()): "+bfc.getBeamFarmerId());
//                System.out.println("DEBUG --- lowerBeam.put(\"nk_\" + bfc.getChamberSn(), bfc.getNkValue()): "+"nk_" + bfc.getChamberSn()+","+bfc.getNkValue());
            }
//            System.out.println("DEBUG --- upperTable ");
            for (BeamFarmerChamber bfc : upperTable) {
                result.setSecondOrUpperBeamId(bfc.getBeamFarmerId());
                upperBeam.put("nk_" + bfc.getChamberSn(), bfc.getNkValue());
//                System.out.println("DEBUG --- setSecondOrUpperBeamId(bfc.getBeamFarmerId()): "+bfc.getBeamFarmerId());
//                System.out.println("DEBUG --- upperBeam.put(\"nk_\" + bfc.getChamberSn(), bfc.getNkValue()): "+"nk_" + bfc.getChamberSn()+","+bfc.getNkValue());
            }
            extractHVL(type, result, lowerBeam, upperBeam);
            result.setExtrapolation(false);
            return result;
        } else {
            Map<String, Double> firstBeam = new HashMap<>();
            Map<String, Double> secondBeam = new HashMap<>();
            // HVL not in the scope (extrapolation)
            List<BeamFarmerChamber> extrapTable = new ArrayList<>();
            if (!lowerCheck) {
                Map<String, Object> input = generateFarmerInfoInput(new FarmerInfoBO(kvp, hvl, type, latestBeamFarmerChamber.getDateUpdated()));
                extrapTable = customMapper.queryLowerExtrapTable(input);
                result.setFirstOrLowerBeamId(extrapTable.get(0).getBeamFarmerId());
                result.setSecondOrUpperBeamId(extrapTable.get(1).getBeamFarmerId());
            }
            if (!upperCheck) {
                Map<String, Object> input = generateFarmerInfoInput(new FarmerInfoBO(kvp, hvl, type, latestBeamFarmerChamber.getDateUpdated()));
                extrapTable = customMapper.queryUpperExtrapTable(input);
                result.setFirstOrLowerBeamId(extrapTable.get(0).getBeamFarmerId());
                result.setSecondOrUpperBeamId(extrapTable.get(1).getBeamFarmerId());
            }
            for (BeamFarmerChamber bfc : extrapTable) {
                if (bfc.getBeamFarmerId().equals(result.getFirstOrLowerBeamId())) {
                    firstBeam.put("nk_" + bfc.getChamberSn(), bfc.getNkValue());
                }
                if (bfc.getBeamFarmerId().equals(result.getSecondOrUpperBeamId())) {
                    secondBeam.put("nk_" + bfc.getChamberSn(), bfc.getNkValue());
                }
            }
            extractHVL(type, result, firstBeam, secondBeam);
            result.setExtrapolation(true);
            return result;
        }
    }

    private void extractHVL(String type, NkIntermediateResultBO result, Map<String, Double> firstOrLowerBeam, Map<String, Double> secondOrUpperBeam) {
        BeamFarmerList lowerBeamFarmerList = queryBoundaryHvl(result.getFirstOrLowerBeamId());
        BeamFarmerList upperBeamFarmerList = queryBoundaryHvl(result.getSecondOrUpperBeamId());
        firstOrLowerBeam.put(type,
                type.equals(TYPE_AL) ?
                        lowerBeamFarmerList.getHvlMeasuredMmAl() : lowerBeamFarmerList.getHvlMeasuredMmCu()
        );
        secondOrUpperBeam.put(type,
                type.equals(TYPE_AL) ?
                        upperBeamFarmerList.getHvlMeasuredMmAl() : upperBeamFarmerList.getHvlMeasuredMmCu()
        );
        if (firstOrLowerBeam.isEmpty() || secondOrUpperBeam.isEmpty()) {
            KVException.display();
        }
        result.setFirstOrLowerBeam(firstOrLowerBeam);
        result.setSecondOrUpperBeam(secondOrUpperBeam);
    }

    //Note: the beam_id in python file refers to beam_planeparallel_id
    private NkIntermediateResultBO selectFromPlaneparallel(BeamData beam, String type) {
        Double hvl = type.equals(TYPE_AL) ? beam.getHvlMeasuredMmAl() : beam.getHvlMeasuredMmCu();
        List<QueryPlaneparallelResultBO> lowerTable = customMapper.queryBeamPlaneparallelChamberLowerTable(hvl);
        List<QueryPlaneparallelResultBO> upperTable = customMapper.queryBeamPlaneparallelChamberUpperTable(hvl);
        if (lowerTable == null || upperTable == null) KVException.display();
        Map<String, Double> lowerBeam = new HashMap<>();
        Map<String, Double> upperBeam = new HashMap<>();
        BeamPlaneparallelNk latestBeamPlaneparallelNk = queryLatestBeamPlaneparallelNk();
        if (latestBeamPlaneparallelNk == null) KVException.display();
        NkIntermediateResultBO result = new NkIntermediateResultBO();
        Date latestDate = latestBeamPlaneparallelNk.getDateUpdated();

        for (QueryPlaneparallelResultBO bpc : lowerTable) {
            BeamPlaneparallelNk tempBPNk = queryBoundaryBeamPlaneparallelNk(latestDate, bpc.getBeamPpChamberId());
            result.setFirstOrLowerBeamId(bpc.getBeamPlaneparallelId());
            lowerBeam.put(type, bpc.getHvlMeasuredMmAl());
            lowerBeam.put("nk_" + bpc.getChamberSn(), tempBPNk.getNkValue());
        }
        for (QueryPlaneparallelResultBO bpc : upperTable) {
            BeamPlaneparallelNk tempBPNk = queryBoundaryBeamPlaneparallelNk(latestDate, bpc.getBeamPpChamberId());
            result.setFirstOrLowerBeamId(bpc.getBeamPlaneparallelId());
            upperBeam.put(type, bpc.getHvlMeasuredMmAl());
            upperBeam.put("nk_" + bpc.getChamberSn(), tempBPNk.getNkValue());
        }
        if (lowerBeam.isEmpty() || upperBeam.isEmpty()) {
            KVException.display();
        }
        result.setFirstOrLowerBeam(lowerBeam);
        result.setSecondOrUpperBeam(upperBeam);
        return result;
    }

    public BeamPlaneparallelNk queryBoundaryBeamPlaneparallelNk(Date latestDate, String beamChamberId) {
        Example example = new Example(BeamPlaneparallelNk.class);
        example.createCriteria()
                .andEqualTo("beamPpChamberId", beamChamberId)
                .andEqualTo("dateUpdated", latestDate);
        return beamPlaneparallelNkMapper.selectOneByExample(example);
    }

    public BeamPlaneparallelNk queryLatestBeamPlaneparallelNk() {
        Example example = new Example(BeamFarmerChamber.class);
        example.orderBy("dateUpdated").desc();
        List<BeamPlaneparallelNk> result = beamPlaneparallelNkMapper.selectByExample(example);
        if (result.size() == 0) return null;
        else return result.get(0);
    }

    public BeamFarmerList queryBoundaryHvl(String beamFarmerId) {
        Example example = new Example(BeamFarmerList.class);
        example.createCriteria().andEqualTo("beamFarmerId", beamFarmerId);
        List<BeamFarmerList> result = beamFarmerListMapper.selectByExample(example);
        if (result.size() == 0) return null;
        else return result.get(0);
    }

    public List<BeamFarmerChamber> queryHVLLowerTable(FarmerInfoBO farmerInfoBO) {
        Map<String, Object> paramMap = generateFarmerInfoInput(farmerInfoBO);
        return customMapper.queryLowerHVLTable(paramMap);
    }

    public List<BeamFarmerChamber> queryHVLUpperTable(FarmerInfoBO farmerInfoBO) {
        Map<String, Object> paramMap = generateFarmerInfoInput(farmerInfoBO);
        return customMapper.queryUpperHVLTable(paramMap);
    }

    private Map<String, Object> generateFarmerInfoInput(FarmerInfoBO farmerInfoBO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("kvp", farmerInfoBO.getKvp());
        paramMap.put("hvl", farmerInfoBO.getHvl());
        paramMap.put("type", farmerInfoBO.getType());
        paramMap.put("date", simpleDateFormat.format(farmerInfoBO.getDateUpdated()));
        return paramMap;
    }

    public BeamFarmerChamber queryLatestFarmerLookupTable() {
        Example example = new Example(BeamFarmerChamber.class);
        example.orderBy("dateUpdated").desc();
        List<BeamFarmerChamber> result = beamFarmerChamberMapper.selectByExample(example);
        if (result.size() == 0) return null;
        else return result.get(0);
    }

    public boolean checkHvlLower(double kvp, double hvl, String type) {
        String hvlFieldName = TYPE_CU.equals(type) ? "hvlMeasuredMmCu" : "hvlMeasuredMmAl";
        Example example = new Example(BeamFarmerList.class);
        example.createCriteria()
                .andLessThanOrEqualTo(hvlFieldName, hvl)
                .andEqualTo("kv", kvp);
        return beamFarmerListMapper.selectCountByExample(example) > 0;
    }

    public boolean checkHvlUpper(double kvp, double hvl, String type) {
        String hvlFieldName = TYPE_CU.equals(type) ? "hvlMeasuredMmCu" : "hvlMeasuredMmAl";
        Example example = new Example(BeamFarmerList.class);
        example.createCriteria()
                .andGreaterThanOrEqualTo(hvlFieldName, hvl)
                .andEqualTo("kv", kvp);
        return beamFarmerListMapper.selectCountByExample(example) > 0;
    }

    public List<InputConesTableBO> queryInputConeDataList(List<Cone> coneList) {
        List<InputConesTableBO> inputConesTableBOList = new ArrayList<>(coneList.size());

        for (Cone cone : coneList) {
            InputConesTableBO inputConesTableBO = new InputConesTableBO();
            inputConesTableBO.setConeId(cone.getConeId());
            inputConesTableBO.setSsd(cone.getSsd());
            inputConesTableBO.setOpenClosed(cone.getOpenClosed());

            MainConfig.ConeShape shapeEnum = MainConfig.ConeShape.valueOf(cone.getShape());
            if (shapeEnum == MainConfig.ConeShape.Circular) {
                inputConesTableBO.setDiameter(cone.getFieldDiameter());
            } else if (shapeEnum == MainConfig.ConeShape.Square) {
                inputConesTableBO.setDiameter(2 * Math.sqrt(cone.getFieldArea() / Math.PI));
            } else if (shapeEnum == MainConfig.ConeShape.Rectangular) {
                inputConesTableBO.setDiameter(2 * Math.sqrt(
                        cone.getFieldDimension1() * cone.getFieldDimension2() / Math.PI));
            }

            inputConesTableBOList.add(inputConesTableBO);
        }

        return inputConesTableBOList;
    }

}
