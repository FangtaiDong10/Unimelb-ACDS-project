package kv.pojo.bo.calculation.intermediateResults;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class NkResultBO {
    /**
     * This class defines the output of nk calculation.
     * example:
     * 1. nk_res: 
     *      [
     *          {'id': 'ACDS-kV-5014-beam1', '3587': 48.46063655710431},
                {'id': 'ACDS-kV-5014-beam1', '5447': 49.3166175139252},
                {'id': 'ACDS-kV-5014-beam1', '5448': 50.1855243241834},
                {'id': 'ACDS-kV-5014-beam1', '1508': 1026.3442832265134},
                {'id': 'ACDS-kV-5014-beam1', '858': 81.78468765873544},
                {'id': 'ACDS-kV-5014-beam2', '3587': 47.94286890990082},
                ...
            ]
        2. nk_warn:
            [
                {'beam_id': 'ACDS-kV-5014-beam4', 'message': 'Extrapolation_Nk_Al'},
                {'beam_id': 'ACDS-kV-5014-beam4', 'message': 'Extrapolation_Nk_Cu'},
                {'beam_id': 'ACDS-kV-5014-beam5', 'message': 'Extrapolation_Nk_Cu'},
                {'beam_id': 'ACDS-kV-5014-beam6', 'message': 'Extrapolation_Nk_Cu'}
            ]
     */
    private List<Map<String,String>> nkResult;
    private List<Map<String,String>> nkWarning;
    
    /**
     * 
     */
    public NkResultBO() {
    }

    /**
     * @param nkResult
     * @param nkWarning
     */
    public NkResultBO(List<Map<String, String>> nkResult, List<Map<String, String>> nkWarning) {
        this.nkResult = nkResult;
        this.nkWarning = nkWarning;
    }
    
    /**
     * @return the nkResult
     */
    public List<Map<String, String>> getNkResult() {
        return nkResult;
    }
    /**
     * @param nkResult the nkResult to set
     */
    public void setNkResult(List<Map<String, String>> nkResult) {
        this.nkResult = nkResult;
    }
    /**
     * @return the nkWarning
     */
    public List<Map<String, String>> getNkWarning() {
        return nkWarning;
    }
    /**
     * @param nkWarning the nkWarning to set
     */
    public void setNkWarning(List<Map<String, String>> nkWarning) {
        this.nkWarning = nkWarning;
    }

    public synchronized void addNkResult(Map<String, String> nKResultRecord){
        nkResult.add(nKResultRecord);
    }
    public synchronized void addNkWarning(Map<String, String> nKWarningRecord){
        nkWarning.add(nKWarningRecord);
    }

    @Override
    public String toString(){
        StringJoiner nkResultJoiner = new StringJoiner("\n");
        nkResultJoiner.add("nkResult:");
        for(Map<String,String> recordMap: nkResult){
            nkResultJoiner.add(recordMap.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining("|")));
        }
        nkResultJoiner.add("nkWarning:");
        for(Map<String,String> recordMap: nkWarning){
            nkResultJoiner.add(recordMap.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining("|")));
        }
        return nkResultJoiner.toString();
    }
}
