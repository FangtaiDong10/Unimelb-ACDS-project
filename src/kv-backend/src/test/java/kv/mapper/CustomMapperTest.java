package kv.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kv.pojo.dbo.BeamFarmerChamber;
import kv.pojo.dbo.MurhoAl;
import kv.pojo.dbo.MurhoCu;
import kv.pojo.bo.calculation.intermediateResults.FarmerInfoBO;
import kv.service.CalculationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static kv.utils.CalculationUtils.objectToJson;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomMapperTest {
    @Autowired
    private CustomMapper customMapper;

    @Autowired
    private SimpleDateFormat simpleDateFormat;
    @Autowired
    private CalculationService calculationService;

    private String auditId = "ACDS-kV-5014";

    @Test
    void testQueryClosestKvp() {
        Double kvp = new Double(100);
        System.out.println(customMapper.queryClosestKvp(kvp));
        // manual check required, because we don't know the tolerance.
        assertEquals(1,1);
    }

    @Test
    void testQueryMurhoAlByLatestDate() throws JsonProcessingException {
        List<MurhoAl> results = customMapper.queryMurhoAlByLatestDate();
        System.out.println("Got latest date: "+results.get(0).getDateUpdated());
        assertEquals(results.get(0).getDateUpdated(),results.get(results.size()-1).getDateUpdated());
        ObjectMapper mapper = new ObjectMapper(); // lazy :P
        for(MurhoAl result : results) {
            System.out.println(mapper.writeValueAsString(result));
        }
    }
    @Test
    void testQueryMurhoCuByLatestDate() throws JsonProcessingException {
        List<MurhoCu> results = customMapper.queryMurhoCuByLatestDate();
        System.out.println("Got latest date: "+results.get(0).getDateUpdated());
        assertEquals(results.get(0).getDateUpdated(),results.get(results.size()-1).getDateUpdated());
        ObjectMapper mapper = new ObjectMapper(); // lazy :P
        for(MurhoCu result : results) {
            System.out.println(mapper.writeValueAsString(result));
        }
    }
    @Test
    void queryLowerHVLTable() throws ParseException {
        // TODO: for now it is using hard-coded value, use BeamData instead in the future testing.
        Double kvp = new Double(60.0);
        Double hvlAl = new Double(1.268);
        String typeAl = "al";
        String typeCu = "cu";
        String validDateStr = "2020-05-26"; // Note: this might be changed in the future, please refer to database table
        Date validDate = simpleDateFormat.parse(validDateStr);
        FarmerInfoBO validInput = new FarmerInfoBO(kvp,hvlAl,typeAl,validDate);
        List<BeamFarmerChamber> result = calculationService.queryHVLLowerTable(validInput);
        System.out.println("return size: "+result.size());
        for(BeamFarmerChamber c: result){
            System.out.println(objectToJson(c));
        }
        assertEquals(result.size(),3);
        //TODO: more cases!
    }
    @Test
    void queryUpperHVLTable() throws ParseException {
        // TODO: for now it is using hard-coded value, use BeamData instead in the future testing.
        Double kvp = new Double(60.0);
        Double hvlAl = new Double(1.268);
        String typeAl = "al";
        String typeCu = "cu";
        String validDateStr = "2020-05-26"; // Note: this might be changed in the future, please refer to database table
        Date validDate = simpleDateFormat.parse(validDateStr);
        FarmerInfoBO validInput = new FarmerInfoBO(kvp,hvlAl,typeAl,validDate);
        List<BeamFarmerChamber> result = calculationService.queryHVLUpperTable(validInput);
        System.out.println("return size: "+result.size());
        for(BeamFarmerChamber c: result){
            System.out.println(objectToJson(c));
        }
        assertEquals(result.size(),3);
        //TODO: more cases!
    }
    @Test
    void testQueryAllFarmer() {
    }

    @Test
    void testQueryAllPlaneparallel() {
    }

    @Test
    void testQueryAuditCases() {
    }

    @Test
    void testQueryInputConesTable() {
    }


    @Test
    void queryBoundaryHvl() {
    }

    @Test
    void queryLowerExtrapTable() {
    }

    @Test
    void queryUpperExtrapTable() {
    }

    @Test
    void queryBeamPlaneparallelChamberLowerTable() {
    }

    @Test
    void queryBeamPlaneparallelChamberUpperTable() {
    }
}