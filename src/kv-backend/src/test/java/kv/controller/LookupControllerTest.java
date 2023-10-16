package kv.controller;

import kv.common.KVException;
import kv.common.KVJsonResult;
import kv.pojo.dbo.BwAlCu;
import kv.pojo.dbo.MurhoAl;
import kv.pojo.dbo.MurhoCu;
import kv.pojo.dbo.PstemMeasured;
import kv.pojo.vo.lookup.FarmerVO;
import kv.pojo.vo.lookup.PlaneparallelVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LookupControllerTest {
    @Autowired
    private LookupController lookupController;

    @Test
    void queryBw() {
        // TC 1.1: Read Bw lookup tables
        KVJsonResult result = lookupController.queryBw();
        assertEquals(true, result.getSuccess());
        List<BwAlCu> bwAlCuList = (List<BwAlCu>) result.getData();
        assertEquals(3059, bwAlCuList.size());
    }

    @Test
    void queryFarmer() {
        // TC 2.1: Read farmer lookup tables
        KVJsonResult result = lookupController.queryFarmer();
        assertEquals(true, result.getSuccess());
        List<FarmerVO> farmerVOList = (List<FarmerVO>) result.getData();
        assertEquals(177, farmerVOList.size());
    }

    @Test
    void queryMurhoAl() {
        // TC 3.1: Read MurhoAl lookup tables
        KVJsonResult result = lookupController.queryMurhoAl();
        assertEquals(true, result.getSuccess());
        List<MurhoAl> murhoAlList = (List<MurhoAl>) result.getData();
        assertEquals(46, murhoAlList.size());
    }

    @Test
    void queryMurhoCu() {
        // TC 4.1: Read MurhoCu lookup tables
        KVJsonResult result = lookupController.queryMurhoCu();
        assertEquals(true, result.getSuccess());
        List<MurhoCu> murhoCuList = (List<MurhoCu>) result.getData();
        assertEquals(13, murhoCuList.size());
    }

    @Test
        // TC 5.1: Query Planeparallel lookup tables
    void queryPlaneparallel() {
        // the interface of queryPlaneparallel sent successfully.
        KVJsonResult result = lookupController.queryPlaneparallel();
        assertEquals(true, result.getSuccess());
        // the size of data stored in the database is correct.
        List<PlaneparallelVO> planeparallelVOList = (List<PlaneparallelVO>) result.getData();
        assertEquals(14, planeparallelVOList.size());
    }

    @Test
        // TC 6.1: Query Pstem lookup tables
    void queryPstem() {
        // the interface of queryPstem sent successfully.
        KVJsonResult result = lookupController.queryPstem();
        assertEquals(true, result.getSuccess());
        // the size of data stored in the database is correct.
        List<PstemMeasured> pstemMeasuredList = (List<PstemMeasured>) result.getData();
        assertEquals(84, pstemMeasuredList.size());
    }

    @Test
    void uploadLookup() throws IOException {
        // a flag for true if it Successfully catch the exception
        boolean flag;

        // TC 5.2: Read Planeparallel Excel Sheet
        FileInputStream fileInputStreamPlaneparallel =
                new FileInputStream("../../data samples/lookup-templates/planeparallel_template.xlsx");
        MockMultipartFile multipartFilePlaneparallel = new MockMultipartFile("file", fileInputStreamPlaneparallel);
        flag = false;
        try {
            lookupController.uploadLookup(multipartFilePlaneparallel);
        } catch (KVException e) {
            flag = true;
        }
        assertTrue(flag);

        // TC 6.2: Read Pstem Excel Sheet
        FileInputStream fileInputStreamPstem =
                new FileInputStream("../../data samples/lookup-templates/pstem_template.xlsx");
        MockMultipartFile multipartFilePstem = new MockMultipartFile("file", fileInputStreamPstem);
        flag = false;
        // catch the exception when diameter is not Numeric
        try {
            lookupController.uploadLookup(multipartFilePstem);
        } catch (KVException e) {
            flag = true;
        }
        assertTrue(flag);
    }
}