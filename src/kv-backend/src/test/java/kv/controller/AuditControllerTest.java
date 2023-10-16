package kv.controller;

import kv.common.KVException;
import kv.common.KVJsonResult;
import kv.common.KVPageInfo;
import kv.pojo.dbo.MurhoAl;
import kv.pojo.vo.audit.AuditCaseVO;
import kv.pojo.vo.audit.IdentificationVO;
import kv.pojo.vo.audit.WarningVO;
import kv.pojo.vo.calculation.BeamInfoVO;
import kv.pojo.vo.calculation.WorkDataSheet1VO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
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
class AuditControllerTest {
    @Autowired
    private AuditController auditController;

    @Test
    void upload() throws IOException {
        // TC 25: upload identification excel
        // a flag for true if it Successfully catch the exception
        boolean flag;
        try {
        FileInputStream fileInputStreamSf =
                new FileInputStream("C:/UniMelb/COMP90082/KV/VIOLET/services/excel/excel-data/identification-form-springfield.xlsx");
        MockMultipartFile multipartFileSf = new MockMultipartFile("file", fileInputStreamSf);
        flag = false;
        auditController.upload(multipartFileSf);
        } catch (KVException e) {
            flag = true;
        }
        assertTrue(flag);
    }

    @Test
    void queryAuditCases() throws ParseException {
        // TC 26: Query audit cases
        KVJsonResult result = auditController.queryAuditCases("ACDS-kV-5014","Hogwarts","Progressing","2022-05-17",1,10);
        assertEquals(true, result.getSuccess());
        KVPageInfo AuditCaseVOList = (KVPageInfo) result.getData();
        assertEquals(1, AuditCaseVOList.getList().size());
    }

    @Test
    void editAuditCase() {
        // TC 27: Edit audit cases
        KVJsonResult result = auditController.editAuditCase("ACDS-kV-5014","Finished");
        assertEquals(true, result.getSuccess());
        KVJsonResult result1 = auditController.queryAuditCases("ACDS-kV-5014","Hogwarts","Finished","2022-05-17",1,10);
        KVPageInfo auditCaseVOList = (KVPageInfo) result1.getData();
        assertEquals(1, auditCaseVOList.getList().size());
        KVJsonResult result2 = auditController.editAuditCase("ACDS-kV-5014","Progressing");
    }

    @Test
    void queryWorkDataSheet1() {
        // TC 29: Query work datasheet1
        KVJsonResult result = auditController.queryWorkDataSheet1("ACDS-kV-5014");
        assertEquals(true, result.getSuccess());
        List<WorkDataSheet1VO> workDataSheet1VOList = (List<WorkDataSheet1VO>) result.getData();
        assertEquals(3, workDataSheet1VOList.size());
    }

    @Test
    void queryWorkDataSheet2() {
        // TC 30: Query work datasheet2
        KVJsonResult result = auditController.queryWorkDataSheet2("ACDS-kV-5014");
        assertEquals(true, result.getSuccess());
        List<WorkDataSheet1VO> workDataSheet1VOList = (List<WorkDataSheet1VO>) result.getData();
        assertEquals(3, workDataSheet1VOList.size());
    }

    @Test
    void queryBeamInfo() {
        // TC 31: Query beam information
        KVJsonResult result = auditController.queryBeamInfo("ACDS-kV-5014");
        assertEquals(true, result.getSuccess());
        List<BeamInfoVO> beamInfoVOList = (List<BeamInfoVO>) result.getData();
        assertEquals(8, beamInfoVOList.size());
    }

    @Test
    void queryDataFrontEnd() {
        // TC 32: Query data from the frontend table
        KVJsonResult result = auditController.queryDataFrontEnd("ACDS-kV-5014");
        assertEquals(true, result.getSuccess());
    }

    @Test
    void queryIdentification() {
        // TC 34: Query idenfication
        KVJsonResult result = auditController.queryIdentification("ACDS-kV-5014");
        assertEquals(true, result.getSuccess());
        IdentificationVO identificationVO = (IdentificationVO) result.getData();
        assertEquals("ACDS-kV-5014", identificationVO.getAuditInformation().getAuditId());
    }

    @Test
    void queryWarnings() {
        // TC 35: Query warnings
        KVJsonResult result = auditController.queryWarnings("ACDS-kV-5014");
        assertEquals(true, result.getSuccess());
        List<WarningVO> warningVOList = (List<WarningVO>) result.getData();
        assertEquals(18,warningVOList.size());
    }
}