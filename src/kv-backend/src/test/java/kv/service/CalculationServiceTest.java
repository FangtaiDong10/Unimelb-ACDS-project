package kv.service;

import kv.mapper.AuditBeamInputsMapper;
import kv.mapper.BeamDataMapper;
import kv.mapper.ConeMapper;
import kv.mapper.CustomMapper;
import kv.pojo.bo.calculation.InputConesTableBO;
import kv.pojo.bo.calculation.intermediateResults.*;
import kv.pojo.dbo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kv.service.CalculationService.*;
import static kv.utils.CalculationUtils.objectToJson;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculationServiceTest {
    @Autowired
    private CustomMapper customMapper;
    @Autowired
    private AuditBeamInputsMapper auditBeamInputsMapper;
    @Autowired
    private BeamDataMapper beamDataMapper;
    @Autowired
    private ConeMapper coneMapper;
    @Autowired
    private CalculationService calculationService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    private String auditId = "ACDS-kV-5014";

    @BeforeEach
    void init() {
        calculationService.init();
    }

    /*
    Helper methods
     */
    public List<BeamData> queryBeamDataList(String auditId) {
        Example example = new Example(BeamData.class);
        example.createCriteria().andLike("beamId", auditId + "%");
        return beamDataMapper.selectByExample(example);
    }
    public List<AuditBeamInputs> queryAuditBeamInputs(String auditId) {
        Example example = new Example(AuditBeamInputs.class);
        example.createCriteria().andLike("auditId", auditId + "%");
        return auditBeamInputsMapper.selectByExample(example);
    }
    public List<Cone> queryConeList(String auditId) {
        Example example = new Example(Cone.class);
        example.createCriteria().andLike("coneId", auditId + "%");
        return coneMapper.selectByExample(example);
    }
    @Test
    void testGetBackResult() {
        // TC 7: Get a list of BackResults
        List<BeamData> beamDataList = queryBeamDataList(auditId);
//        List<InputConesTableBO> coneDataList = calculationService.queryInputConeDataList(auditId);
        List<AuditBeamInputs> auditBeamInputs = queryAuditBeamInputs(auditId);
        List<BackResult> results = calculationService.getBackResult(beamDataList,auditBeamInputs,queryConeList(auditId));
        for(BackResult result: results){
            System.out.println(objectToJson(result));
        }
    }

    @Test
    void testQueryBoundaryBeamPlaneparallelNk() throws ParseException {
        // TC 8: Query the specific beam planeparallel Nk
        String validChamberId = "RT5-1508";
        String invalidChamberId = "asdfasdfasdf";
        String validDateStr = "2020-05-26"; // Note: this might be changed in the future, please refer to database table
        Date validDate = simpleDateFormat.parse(validDateStr);
        Date current = new Date();
        // Case 1: Given bad Date AND bad chamber id. Expected: Failure
        assertNull(calculationService.queryBoundaryBeamPlaneparallelNk(current, invalidChamberId));
        // Case 2: Given good Date AND good chamber id. Expected: Success (1 record).
        BeamPlaneparallelNk BPNk = calculationService.queryBoundaryBeamPlaneparallelNk(validDate, validChamberId);
        assertNotNull(BPNk);
        assertEquals(BPNk.getBeamPpChamberId(), validChamberId);
        assertEquals(BPNk.getDateUpdated(), validDate);
    }

    @Test
    void testQueryLatestBeamPlaneparallelNk() throws ParseException {
        // TC 9: Query the latest beam planeparallel Nk
        String validDateStr = "2020-05-26"; // Note: this might be changed in the future, please refer to database table
        Date validDate = simpleDateFormat.parse(validDateStr);
        Date current = new Date();
        Date query = calculationService.queryLatestBeamPlaneparallelNk().getDateUpdated();
        // Case 1: Given bad Date. Expected: Failure
        assertNotEquals(query, current);
        // Case 2: Given good Date Expected: Success (1 record).
        assertEquals(query, validDate);
        System.out.println("Query Date: " + query.toString());
    }

    @Test
    void testQueryBoundaryHvl() {
        // TC 10: Query the specific hvl
        String validFarmerId = "NXB140";
        String invalidFarmerId = "asdfasdfsafasd";
        BeamFarmerList valid = calculationService.queryBoundaryHvl(validFarmerId);
        BeamFarmerList invalid = calculationService.queryBoundaryHvl(invalidFarmerId);
        // Case 1: Given bad farmer id, expected: failure
        assertNull(invalid);
        // Case 2: Given good farmer id, expected: success (1~more records)
        assertNotNull(valid);
        assertEquals(valid.getBeamFarmerId(), validFarmerId);
    }

    @Test
    void testQueryLatestFarmerLookupTable() throws ParseException {
        // TC 11: Query the latest farmer lookup table
        BeamFarmerChamber result = calculationService.queryLatestFarmerLookupTable();
        System.out.println("query date: " + result.getDateUpdated());
        String validDateStr = "2020-05-26"; // Note: this might be changed in the future, please refer to database table
        Date validDate = simpleDateFormat.parse(validDateStr);
        assertEquals(validDate, result.getDateUpdated());
    }

    @Test
    void testCheckHvlLower() {
        // TC 12:  Check whether there is data that has lower Hvl
        boolean invalidResult = calculationService.checkHvlLower(new Double(0.01), new Double(0.0001), "al");
        boolean validResultAl = calculationService.checkHvlLower(new Double(60.0), new Double(0.788813791651093), "al");
        boolean validResultCu = calculationService.checkHvlLower(new Double(200.0), new Double(0.73230337407774), "cu");

        assertFalse(invalidResult);
        assertTrue(validResultAl);
        assertTrue(validResultCu);
    }

    @Test
    void testCheckHvlUpper() {
        // TC 13:  Check whether there is data that has higher Hvl
        boolean invalidResult = calculationService.checkHvlUpper(new Double(100000), new Double(0.0001), "al");
        boolean validResultAl = calculationService.checkHvlUpper(new Double(60.0), new Double(2.80586258945384), "al");
        boolean validResultCu = calculationService.checkHvlUpper(new Double(120.0), new Double(0.229716813754473), "cu");

        assertFalse(invalidResult);
        assertTrue(validResultAl);
        assertTrue(validResultCu);
    }

    @Test
    void testQueryBeamDataList() {
        /*
        beams:  [{'beam_id': 'ACDS-kV-5014-beam1', 'kvp': 60.0, 'hvl_measured_al': 1.268, 'hvl_measured_cu': None},
         {'beam_id': 'ACDS-kV-5014-beam2', 'kvp': 80.0, 'hvl_measured_al': 2.321, 'hvl_measured_cu': None},
          {'beam_id': 'ACDS-kV-5014-beam3', 'kvp': 100.0, 'hvl_measured_al': 2.881, 'hvl_measured_cu': None},
           {'beam_id': 'ACDS-kV-5014-beam4', 'kvp': 120.0, 'hvl_measured_al': 5.123, 'hvl_measured_cu': 0.227},
            {'beam_id': 'ACDS-kV-5014-beam5', 'kvp': 150.0, 'hvl_measured_al': None, 'hvl_measured_cu': 0.339},
             {'beam_id': 'ACDS-kV-5014-beam6', 'kvp': 180.0, 'hvl_measured_al': None, 'hvl_measured_cu': 0.504},
              {'beam_id': 'ACDS-kV-5014-beam7', 'kvp': 200.0, 'hvl_measured_al': None, 'hvl_measured_cu': 1.042},
        {'beam_id': 'ACDS-kV-5014-beam8', 'kvp': 250.0, 'hvl_measured_al': None, 'hvl_measured_cu': 2.117}]
         */
        List<BeamData> beams = queryBeamDataList(auditId);
        for (BeamData b : beams) {
            System.out.println("--------------------");
            System.out.println("db ID: " + b.getBeamIdAlt());
            System.out.println("beam_id: " + b.getBeamId());
            System.out.println("hvl_measured_al: " + b.getHvlMeasuredMmAl());
            System.out.println("hvl_measured_cu: " + b.getHvlMeasuredMmCu());
            System.out.println("field_size_at_scd: " + b.getFieldSizeAtScd());
            System.out.println("hvl_nominal_mm_al: " + b.getHvlNominalMmAl());
            System.out.println("hvl_nominal_mm_cu: " + b.getHvlNominalMmCu());
            System.out.println("norm_energy(kvp): " + b.getNomEnergy());
            System.out.println("ref_cone_id: " + b.getReferenceConeId());
            System.out.println("scd: " + b.getScd());
        }
        // Manual check required.
        assertTrue(beams.size() > 0);
    }

    @Test
    void testQueryInputConeDataList() {
        // TC 14: Query the Input cone data list
        /*
        cones:  [{'cone_id': 'ACDS-kV-5014-cone1', 'SSD': 30.0, 'open': 'Open', 'diameter': 10.0},
        {'cone_id': 'ACDS-kV-5014-cone2', 'SSD': 50.0, 'open': 'Closed', 'diameter': 11.283791670955125},
        {'cone_id': 'ACDS-kV-5014-cone3', 'SSD': 30.0, 'open': 'Open', 'diameter': 5.0}]
         */
        List<InputConesTableBO> result = calculationService.queryInputConeDataList(queryConeList(auditId));
        for (InputConesTableBO r : result) {
            System.out.println("-----------------------");
            System.out.println("cone_id:" + r.getConeId());
            System.out.println("SSD:" + r.getSsd());
            System.out.println("open:" + r.getOpenClosed());
            System.out.println("diameter:" + r.getDiameter());
        }
    }


    @Test
    void testSelectFromFarmer() {
        // TC 15: Select data from the farmer table
        /*
        TRUE RESULTS:
        1. interpolation, and type == 'al', the first result in the notebook
        lower_beam:  {'id': 'NXJ60',
        'nk_3587': 49.8986656857429,
        'nk_5447': 50.7091582474078,
        'nk_5448': 51.5333222138005,
        'hvl_al': 0.788813791651093}

        upper_beam:  {'id': 'NXK60',
         'nk_3587': 48.3988130247751,
          'nk_5447': 49.2567496118489,
           'nk_5448': 50.1275799997309,
            'hvl_al': 1.28860110150315}
        2. extrapolation, and type == 'cu', the first result in this case comes from the input beam_id == 'ACDS-kV-5014-beam4'
        first_beam:  {'id': 'NXB120',
        'nk_3587': 47.7093008745159,
        'nk_5447': 47.8597516857949,
        'nk_5448': 48.5210001193312,
        'hvl_cu': 0.229716813754473}

        second_beam:  {'id': 'NXC120',
        'nk_3587': 47.7411047431016,
        'nk_5447': 47.8406595597577,
        'nk_5448': 48.4969968164535,
        'hvl_cu': 0.280623113903494}

        3. extrapolation, and type == 'al', the result in this case comes from the input beam_id == 'ACDS-kV-5014-beam4'
        first_beam:  {'id': 'NXB120',
        'nk_3587': 47.7093008745159,
        'nk_5447': 47.8597516857949,
        'nk_5448': 48.5210001193312,
        'hvl_al': 5.55599697591481}

        second_beam:  {'id': 'NXC120',
        'nk_3587': 47.7411047431016,
        'nk_5447': 47.8406595597577,
        'nk_5448': 48.4969968164535,
        'hvl_al': 6.37697784783916}
         */
        List<BeamData> beams = queryBeamDataList(auditId);
        NkIntermediateResultBO resultAlInterpolation = calculationService.selectFromFarmer(beams.get(0), "al");
//        NkIntermediateResultBO resultCu = calculationService.selectFromFarmer(beams.get(0),"cu"); // check Exception in this case.
        NkIntermediateResultBO resultCuExtrapolation = calculationService.selectFromFarmer(beams.get(3), "cu");
        NkIntermediateResultBO resultAlExtrapolation = calculationService.selectFromFarmer(beams.get(3), "al");

        System.out.println("-------Case 1--------");
        System.out.println(resultAlInterpolation.toString());
        System.out.println("-------Case 2--------");
        System.out.println(resultCuExtrapolation.toString());
        System.out.println("-------Case 3--------");
        System.out.println(resultAlExtrapolation.toString());
    }

    @Test
    void testCalculateNkValue() {
        // TC 16: Calculate Nk value
        /*
        [{'id': 'ACDS-kV-5014-beam1', '3587': 48.46063655710431},
 {'id': 'ACDS-kV-5014-beam1', '5447': 49.3166175139252},
 {'id': 'ACDS-kV-5014-beam1', '5448': 50.1855243241834},
 {'id': 'ACDS-kV-5014-beam1', '1508': 1026.3442832265134},
 {'id': 'ACDS-kV-5014-beam1', '858': 81.78468765873544},
 {'id': 'ACDS-kV-5014-beam2', '3587': 47.94286890990082},
 {'id': 'ACDS-kV-5014-beam2', '5447': 48.524572005234006},
 {'id': 'ACDS-kV-5014-beam2', '5448': 49.383501031746604},
 {'id': 'ACDS-kV-5014-beam3', '3587': 47.86858146481437},
 {'id': 'ACDS-kV-5014-beam3', '5447': 48.37514529391168},
 {'id': 'ACDS-kV-5014-beam3', '5448': 49.17541128953733},
 {'id': 'ACDS-kV-5014-beam4', '3587': 47.700065299542416},
 {'id': 'ACDS-kV-5014-beam4', '5447': 47.865295878035056},
 {'id': 'ACDS-kV-5014-beam4', '5448': 48.52797047586945},
 {'id': 'ACDS-kV-5014-beam5', '3587': 47.776676734587745},
 {'id': 'ACDS-kV-5014-beam5', '5447': 47.79644246440765},
 {'id': 'ACDS-kV-5014-beam5', '5448': 48.39220900286462},
 {'id': 'ACDS-kV-5014-beam6', '3587': 47.88665365480139},
 {'id': 'ACDS-kV-5014-beam6', '5447': 47.797055655719454},
 {'id': 'ACDS-kV-5014-beam6', '5448': 48.260378971135914},
 {'id': 'ACDS-kV-5014-beam7', '3587': 48.04839420952718},
 {'id': 'ACDS-kV-5014-beam7', '5447': 47.87388553788194},
 {'id': 'ACDS-kV-5014-beam7', '5448': 48.237610540021045},
 {'id': 'ACDS-kV-5014-beam8', '3587': 48.11434907238819},
 {'id': 'ACDS-kV-5014-beam8', '5447': 47.900958741345605},
 {'id': 'ACDS-kV-5014-beam8', '5448': 48.14402421165017}]

 Warning messages:
 [{'beam_id': 'ACDS-kV-5014-beam4', 'message': 'Extrapolation_Nk_Al'},
 {'beam_id': 'ACDS-kV-5014-beam4', 'message': 'Extrapolation_Nk_Cu'},
 {'beam_id': 'ACDS-kV-5014-beam5', 'message': 'Extrapolation_Nk_Cu'},
 {'beam_id': 'ACDS-kV-5014-beam6', 'message': 'Extrapolation_Nk_Cu'}]
         */
        List<BeamData> beamDataList = queryBeamDataList(auditId);
        System.out.println("size of beam list: " + beamDataList.size());
        NkResultBO nkResult = calculationService.calculateNkValue(beamDataList);
        System.out.println(nkResult);
    }

    @Test
    void testQueryLatestBwAlCu() {
        // TC 17: Query the latest BwAlCu data
        String validTypeAl = TYPE_AL;
        String validTypeCu = TYPE_CU;
        String invalidType = "something random!!";
        // Note: below "valid" results are coming from getRange()
        double validHvlAl = 1.2;
        double validHvlCu = 0.2;
        double validSSD = 30.0;
        double validDiameter = 10.0;
        System.out.println("valid input params: hvl:" + validHvlAl + ",ssd:" + validSSD + ", diameter:" + validDiameter);

        BwAlCu result = calculationService.queryLatestBwAlCu(validTypeAl, validSSD, validDiameter, validHvlAl);
        System.out.println(result.getBw());
        assertEquals(result.getBw(), 1.175);

        result = calculationService.queryLatestBwAlCu(invalidType, validSSD, validDiameter, validHvlAl);
        assertNull(result);
        /*
        ssd_list: [30.0, 30.0]
        hvl_list: [0.2, 0.3]
        diameter_list: [10.0, 10.0]
        (0, 0, 0)
        ssd_list: 30.0
        hvl_list: 0.2
        diameter_list: 10.0
        ------select_from_DB()------
        bw_lookup_value:  [(1.348, )]
        * */
        result = calculationService.queryLatestBwAlCu(validTypeCu, validSSD, validDiameter, validHvlCu);
        System.out.println(result.getBw());
        assertEquals(result.getBw(), 1.348);
    }

    @Test
    void testGetRange() {
        // TC 18: get the target boundary
//        Map<String,Double> getRange(double target, double[] axis)
        double validTargetHvl = 1.268; // This is the raw input hvl in the first example output in calculation() called by cal_Bw_value()
        double validTargetSSD = 30.0;
        double invalid = Double.NaN;
        // all valid, expect upper and lower bound value
        Map<String, Double> result = calculationService.getRange(validTargetHvl, calculationService.HVL_AL_AXIS);
        assertFalse(result.containsKey(EXACT_BOUND));
        assertTrue(result.containsKey(UPPER_BOUND) || result.containsKey(UPPER_BOUND));
        System.out.println("found upper bound value: " + result.get(UPPER_BOUND));
        System.out.println("found lower bound value: " + result.get(LOWER_BOUND));
        // all valid, expect exact value
        result = calculationService.getRange(validTargetSSD, calculationService.SSD_AL_AXIS);
        assertTrue(result.containsKey(EXACT_BOUND));
        assertFalse(result.containsKey(UPPER_BOUND) || result.containsKey(UPPER_BOUND));
        System.out.println("found exact bound value: " + result.get(EXACT_BOUND));
        // invalid, expect empty map.
        result = calculationService.getRange(invalid, calculationService.SSD_AL_AXIS);
        assertTrue(result.isEmpty());
        assertFalse(result.containsKey(UPPER_BOUND) || result.containsKey(UPPER_BOUND) || result.containsKey(EXACT_BOUND));
    }

    @Test
    void testCalculateBwValue() {
        // TC 19: Calculate Bw value
        List<BeamData> beams = queryBeamDataList(auditId);
        List<InputConesTableBO> cones = calculationService.queryInputConeDataList(queryConeList(auditId));
        List<BwResultBO> bwResults = calculationService.calculateBwValue(beams, cones);
        for (BwResultBO bw : bwResults) {
            System.out.println(bw);
            System.out.println("---------------");
        }
    }

    @Test
    void testBwCalculation() {
        // TC 20: Calculate Bw value with specified inputs
        List<BeamData> beams = queryBeamDataList(auditId);
        List<InputConesTableBO> cones = calculationService.queryInputConeDataList(queryConeList(auditId));
        System.out.println("first group of params: ssd:" + cones.get(0).getSsd() +
                ", diameter:" + cones.get(0).getDiameter() +
                ", hvl:" + beams.get(0).getHvlMeasuredMmAl() +
                ", type:" + TYPE_AL);
        double alResult = calculationService.bwCalculation(cones.get(0), beams.get(0), TYPE_AL);
        System.out.println(alResult);
        assertEquals(alResult, 1.1804400000000002); // from the first iteration result.

        System.out.println("second group of params: ssd:" + cones.get(1).getSsd() +
                ", diameter:" + cones.get(1).getDiameter() +
                ", hvl:" + beams.get(1).getHvlMeasuredMmAl() +
                ", type:" + TYPE_AL);
        alResult = calculationService.bwCalculation(cones.get(1), beams.get(1), TYPE_AL);
        System.out.println(alResult);
        assertEquals(alResult, 1.2715756358224166); // from the input params: ssd->50.0, diameter->11.283791670955125, hvl->2.321, type->Al

    }

    @Test
    void testCalculateMurhoValue() {
        // TC 21: Calculate Murho value
        List<MurhoAl> murhoAlsByLatestDate = calculationService.queryMurhoAlByLatestDate();
        List<MurhoCu> murhoCusByLatestDate = calculationService.queryMurhoCuByLatestDate();
        List<BeamData> beamDataList = queryBeamDataList(auditId);
        List<MurhoResultBO> murhoResults = calculationService.calculateMurhoValue(beamDataList);
        for(MurhoResultBO res:murhoResults){
            // manual check required.
            // because it contains null, the json mapping wont work.
            System.out.println(res.getResultAsMap().entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining("|")));
        }
    }

    @Test
    void testMurhoCalculation() {
        // TC 22: Calculate Murho value with specified inputs
        List<MurhoAl> murhoAlsByLatestDate = calculationService.queryMurhoAlByLatestDate();
        List<MurhoCu> murhoCusByLatestDate = calculationService.queryMurhoCuByLatestDate();
        List<BeamData> beamDataList = queryBeamDataList(auditId);
        BeamData firstBeam = beamDataList.get(0);
        BeamData thirdBeam = beamDataList.get(3); // the first cu beam data.
        System.out.println("first beam input");
        System.out.println(objectToJson(firstBeam));
        System.out.println("latest murhoAl");
        for(MurhoAl a: murhoAlsByLatestDate){
            System.out.println(objectToJson(a));
        }
        System.out.println("latest murhoCu");
        for(MurhoCu a: murhoCusByLatestDate){
            System.out.println(objectToJson(a));
        }
        System.out.println("beams");
        for(BeamData a: beamDataList){
            System.out.println(objectToJson(a));
        }
        double invalidResult = calculationService.murhoCalculation(firstBeam,murhoAlsByLatestDate,murhoCusByLatestDate);
        System.out.println(invalidResult);
        assertEquals(invalidResult,Double.NaN);
        invalidResult = calculationService.murhoCalculation(firstBeam,null,null);
        System.out.println(invalidResult);
        assertEquals(invalidResult,Double.NaN);
        invalidResult = calculationService.murhoCalculation(firstBeam,null,murhoCusByLatestDate);
        System.out.println(invalidResult);
        assertEquals(invalidResult,Double.NaN);

        double validResultAl = calculationService.murhoCalculation(firstBeam,murhoAlsByLatestDate,null);
        System.out.println(validResultAl);
        assertEquals(1.0177733333333334,validResultAl);

        double validResultCu = calculationService.murhoCalculation(thirdBeam,null,murhoCusByLatestDate);
        System.out.println(validResultCu);
        assertEquals(1.02989,validResultCu);

    }

    @Test
    void testCalculateCccValue() {
        // TC 23: Calculate ccc value
        List<BeamData> beamDataList = queryBeamDataList(auditId);
        List<InputConesTableBO> cones = calculationService.queryInputConeDataList(queryConeList(auditId));
        List<BwResultBO> bwResults = calculationService.calculateBwValue(beamDataList, cones);
        List<CccResultBO> results = calculationService.calculateCccValue(bwResults,cones,beamDataList);
        for(CccResultBO r : results){
            System.out.println(objectToJson(r));
        }
    }

    @Test
    void testCalculatePstemValue() {
        // TC 24: Calculate pstem value
        List<BeamData> beamDataList = queryBeamDataList(auditId);
        List<InputConesTableBO> coneDataList = calculationService.queryInputConeDataList(queryConeList(auditId));
        List<AuditBeamInputs> auditBeamInputs = queryAuditBeamInputs(auditId);
        List<PstemResultBO> pstemResults = calculationService.calculatePstemValue(beamDataList,coneDataList,auditBeamInputs);
        for(PstemResultBO p : pstemResults){
            System.out.println(objectToJson(p));
        }
    }
}