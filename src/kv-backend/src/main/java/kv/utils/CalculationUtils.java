package kv.utils;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kv.common.KVException;
import kv.pojo.dbo.BeamData;

import static kv.service.CalculationService.TYPE_AL;
import static kv.service.CalculationService.TYPE_CU;

public class CalculationUtils {
    // Avoid object instantiation
    private CalculationUtils(){}
    
    /** 
     * @param lookupTarget1
     * @param lookupTarget2
     * @param lookupRef1
     * @param lookupRef2
     * @param inputBeamVal
     * @return double
     */
    public static double interpolation (double lookupTarget1,
                                double lookupTarget2,
                                double lookupRef1,
                                double lookupRef2,
                                double inputBeamVal) {
        if (lookupTarget1 == lookupTarget2){
            return lookupTarget1;
        }
        if (lookupRef1 == lookupRef2) {
            KVException.display(); 
        }
        
        return lookupTarget1 - (((lookupRef1 - inputBeamVal) * (lookupTarget1 - lookupTarget2)) / (lookupRef1 - lookupRef2));
    }

    
    /** 
     * @param bwOpen
     * @return double
     */
    public static double calculateKClosedCone(double bwOpen){
        return (1 + (bwOpen - 1) * 1.032) / bwOpen;
    }

    
    /** 
     * @param dataObject
     * @return Map<String, Double>
     */
    public static Map<String,Double> getHVLAlCuAsMap(Object dataObject){
        if(!(dataObject instanceof BeamData)){
            return new HashMap<>();
        }
        BeamData beam = (BeamData)dataObject;
        Map<String,Double> result = new HashMap<>(); 
        if(beam.getHvlMeasuredMmAl() != null){
            result.put(TYPE_AL,beam.getHvlMeasuredMmAl());
        }
        if(beam.getHvlMeasuredMmCu() != null){
            result.put(TYPE_CU,beam.getHvlMeasuredMmCu());
        }
        return result;
    }
    public static boolean isValueInBound(double target, double lowerBound, double upperBound){
        if(Double.isNaN(target)||Double.isNaN(lowerBound)||Double.isNaN(upperBound)) return false;
        else if (Double.isInfinite(target)||Double.isInfinite(lowerBound)||Double.isInfinite(upperBound)) return false;
        else
            return (target>lowerBound || target<upperBound);
    }
    // lazy print method. It only works for object that has no null properties.
    // otherwise, you will need to declare JsonProperties and JsonInclude tag.
    public static String objectToJson(Object obj){
        ObjectMapper mapper = new ObjectMapper();
        String result = new String("");
        try {
            result = mapper.writeValueAsString(obj);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
