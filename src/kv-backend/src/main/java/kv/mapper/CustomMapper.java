package kv.mapper;

import kv.pojo.bo.calculation.QueryPlaneparallelResultBO;
import kv.pojo.dbo.BeamFarmerChamber;
import kv.pojo.dbo.MurhoAl;
import kv.pojo.dbo.MurhoCu;
import kv.pojo.vo.audit.AuditCaseVO;
import kv.pojo.vo.lookup.FarmerVO;
import kv.pojo.vo.lookup.PlaneparallelVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CustomMapper {
    List<FarmerVO> queryAllFarmer();

    List<PlaneparallelVO> queryAllPlaneparallel();

    List<AuditCaseVO> queryAuditCases(@Param("paramMap") Map<String, Object> map);

    Double queryClosestKvp(@Param("kvp") Double kvp);

    List<BeamFarmerChamber> queryLowerHVLTable(@Param("paramMap") Map<String, Object> map);

    List<BeamFarmerChamber> queryUpperHVLTable(@Param("paramMap") Map<String, Object> map);

    List<BeamFarmerChamber> queryLowerExtrapTable(@Param("paramMap") Map<String, Object> map);

    List<BeamFarmerChamber> queryUpperExtrapTable(@Param("paramMap") Map<String, Object> map);

    List<QueryPlaneparallelResultBO> queryBeamPlaneparallelChamberLowerTable(@Param("hvl") Double hvl);

    List<QueryPlaneparallelResultBO> queryBeamPlaneparallelChamberUpperTable(@Param("hvl") Double hvl);

    List<MurhoAl> queryMurhoAlByLatestDate();

    List<MurhoCu> queryMurhoCuByLatestDate();
}
