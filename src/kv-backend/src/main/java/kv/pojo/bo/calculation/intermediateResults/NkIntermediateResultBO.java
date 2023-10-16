package kv.pojo.bo.calculation.intermediateResults;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class NkIntermediateResultBO {
    /*
    Expected return result from selectFromFarmer() and selectFromPlaneparallel()
    There are 3 cases:
    
    a. Interpolation decision branch:

    - lower_beam:  {'id': 'NXJ60', 
    'nk_3587': 49.8986656857429, 
    'nk_5447': 50.7091582474078, 
    'nk_5448': 51.5333222138005, 
    'hvl_al': 0.788813791651093}

    - upper_beam:  {'id': 'NXK60', 
    'nk_3587': 48.3988130247751, 
    'nk_5447': 49.2567496118489, 
    'nk_5448': 50.1275799997309, 
    'hvl_al': 1.28860110150315}

    - extrapolation: false -> because it is interpolation branch


    b. extrapolation branch
    - first_beam:  {'id': 'NXB120', 
    'nk_3587': 47.7093008745159, 
    'nk_5447': 47.8597516857949, 
    'nk_5448': 48.5210001193312, 
    'hvl_al': 5.55599697591481}
    - second_beam:  {'id': 'NXC120', 
    'nk_3587': 47.7411047431016, 
    'nk_5447': 47.8406595597577, 
    'nk_5448': 48.4969968164535, 
    'hvl_al': 6.37697784783916}
    - extrapolation: true -> because it is extrapolation branch


    c. result from select_from_planeparallel()
    - lower_beam:  {'id': 'RT5', 'hvl_al': 0.806, 'nk_1508': 1033.92690609361, 'nk_858': 82.0878589140421}
    - upper_beam:  {'id': 'RT6', 'hvl_al': 1.304, 'nk_1508': 1025.75342949661, 'nk_858': 81.7610639245557}
    */ 
    private Map<String,Double> firstOrLowerBeam;
    private String firstOrLowerBeamId;
    private Map<String,Double> secondOrUpperBeam;
    private String secondOrUpperBeamId;
    private Boolean extrapolation;
    
    /**
     * 
     */
    public NkIntermediateResultBO() {
    }
    /**
     * @param firstOrLowerBeam
     * @param firstOrLowerBeamId
     * @param secondOrUpperBeam
     * @param secondOrUpperBeamId
     * @param extrapolation
     */
    public NkIntermediateResultBO(Map<String, Double> firstOrLowerBeam, String firstOrLowerBeamId,
                                  Map<String, Double> secondOrUpperBeam, String secondOrUpperBeamId, Boolean extrapolation) {
        this.firstOrLowerBeam = firstOrLowerBeam;
        this.firstOrLowerBeamId = firstOrLowerBeamId;
        this.secondOrUpperBeam = secondOrUpperBeam;
        this.secondOrUpperBeamId = secondOrUpperBeamId;
        this.extrapolation = extrapolation;
    }

    /**
     * @return the firstOrLowerBeam
     */
    public Map<String, Double> getFirstOrLowerBeam() {
        return firstOrLowerBeam;
    }
    /**
     * @param firstOrLowerBeam the firstOrLowerBeam to set
     */
    public void setFirstOrLowerBeam(Map<String, Double> firstOrLowerBeam) {
        this.firstOrLowerBeam = firstOrLowerBeam;
    }
    /**
     * @return the firstOrLowerBeamId
     */
    public String getFirstOrLowerBeamId() {
        return firstOrLowerBeamId;
    }
    /**
     * @param firstOrLowerBeamId the firstOrLowerBeamId to set
     */
    public void setFirstOrLowerBeamId(String firstOrLowerBeamId) {
        this.firstOrLowerBeamId = firstOrLowerBeamId;
    }
    /**
     * @return the secondOrUpperBeam
     */
    public Map<String, Double> getSecondOrUpperBeam() {
        return secondOrUpperBeam;
    }
    /**
     * @param secondOrUpperBeam the secondOrUpperBeam to set
     */
    public void setSecondOrUpperBeam(Map<String, Double> secondOrUpperBeam) {
        this.secondOrUpperBeam = secondOrUpperBeam;
    }
    /**
     * @return the secondOrUpperBeamId
     */
    public String getSecondOrUpperBeamId() {
        return secondOrUpperBeamId;
    }
    /**
     * @param secondOrUpperBeamId the secondOrUpperBeamId to set
     */
    public void setSecondOrUpperBeamId(String secondOrUpperBeamId) {
        this.secondOrUpperBeamId = secondOrUpperBeamId;
    }
    
    /**
     * @return the extrapolation
     */
    public Boolean getExtrapolation() {
        return extrapolation;
    }

    /**
     * @param extrapolation the extrapolation to set
     */
    public void setExtrapolation(Boolean extrapolation) {
        this.extrapolation = extrapolation;
    }

    @Override
    public String toString(){
        StringJoiner nkResultJoiner = new StringJoiner("\n");
        nkResultJoiner.add("Select-from-farmer, first or lower id: "+firstOrLowerBeamId);
        nkResultJoiner.add(getFirstOrLowerBeam().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
                    .collect(Collectors.joining("|")));
        nkResultJoiner.add("Select-from-farmer, second or upper id: "+secondOrUpperBeamId);
        nkResultJoiner.add(getSecondOrUpperBeam().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining("|")));
        nkResultJoiner.add("extrapolation? "+String.valueOf(getExtrapolation()));
        return nkResultJoiner.toString();
    }
}
