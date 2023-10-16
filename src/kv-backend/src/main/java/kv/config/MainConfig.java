package kv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class MainConfig {
    @Value("${spring.jackson.date-format}")
    private String datePattern;

    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat(datePattern);
    }

    // Sheet name related
    public static final String SHEET_NAME_IDENTIFICATION = "Identification";
    public static final String SHEET_NAME_BW = "Bw";
    public static final String SHEET_NAME_FARMER = "Farmer-type";
    public static final String SHEET_NAME_MURHOAL = "murho_mm_al";
    public static final String SHEET_NAME_MURHOCU = "murho_mm_cu";
    public static final String SHEET_NAME_PLANEPARALLEL = "Plane-parallel";
    public static final String SHEET_NAME_PSTEM = "Pstem";

    // field name
    public static final String FIELD_NAME_DATE_UPDATED = "dateUpdated";
    public static final String FIELD_NAME_AUDIT_ID = "auditId";
    public static final String FIELD_NAME_AUDIT_DATE = "auditDate";
    public static final String FIELD_NAME_INPUT_DATE = "inputDate";
    public static final String FIELD_NAME_FACILITY_ID = "facilityId";
    public static final String FIELD_NAME_ACDS_AUDIT_NUMBER = "acdsAuditNumber";
    public static final String FIELD_NAME_BEAM_ID_ALT = "beamIdAlt";
    public static final String FIELD_NAME_BEAM_ID = "beamId";
    public static final String FIELD_NAME_REFERENCE_CONE_ID = "referenceConeId";
    public static final String FIELD_NAME_CONE_ID = "coneId";
    public static final String FIELD_NAME_INPUT_ID = "inputId";
    public static final String FIELD_NAME_BACK_RESULT_ID = "backResultId";

    // bw related
    public static final int BW_SSD_ROW = 5;
    public static final int BW_DIAMETER_ROW = 6;
    public static final int BW_ROW_START = 7;
    public static final String BW_DATE_UPDATED_ADDRESS = "A2";
    public static final String BW_TYPE_ADDRESS = "B2";
    public static final String BW_HVL_COL = "B";
    public static final String BW_COL_START = "C";
    public static final String BW_TYPE_VALUE_AL = "al";
    public static final String BW_TYPE_VALUE_CU = "cu";
    public static final String BW_SSD_REF_VALUE_AL = "al";
    public static final String BW_SSD_REF_VALUE_BOTH = "both";

    // farmer related
    public static final int FARMER_CHAMBER_NAME_ROW = 3;
    public static final int FARMER_CHAMBER_SN_ROW = 4;
    public static final int FARMER_ROW_START = 8;
    public static final String FARMER_COL_START = "E";
    public static final String FARMER_DATE_UPDATED_ADDRESS = "E1";
    public static final String FARMER_BEAM_FARMER_ID_COL = "A";
    public static final String FARMER_KV_COL = "B";
    public static final String FARMER_HVL_MEASURED_MM_CU_COL = "C";
    public static final String FARMER_HVL_MEASURED_MM_AL_COL = "D";
    public static final String FARMER_CHAMBER_TYPE_VALUE = "farmer";

    // murho related
    public static final int MURHO_ROW_START = 2;
    public static final String MURHO_HVL_COL = "C";
    public static final String MURHO_VALUE_COL = "D";
    public static final String MURHO_DATE_UPDATED_ADDRESS = "B1";

    // Planeparallel related
    public static final int PP_ROW_START = 8;
    public static final int PP_CHAMBER_NAME_ROW = 3;
    public static final int PP_CHAMBER_SN_ROW = 4;
    public static final String PP_COL_START = "D";
    public static final String PP_DATE_UPDATED_ADDRESS = "D1";
    public static final String PP_BEAM_PP_ID_COL = "A";
    public static final String PP_KV_COL = "B";
    public static final String PP_HVL_COL = "C";
    public static final String PP_CHAMBER_TYPE_VALUE = "pp";

    // pstem related
    public static final int PSTEM_ROW_START = 4;
    public static final int PSTEM_CHAPTER_ID_ROW = 1;
    public static final int PSTEM_DIAMETER_ROW = 2;
    public static final String PSTEM_COL_START = "D";
    public static final String PSTEM_BEAM_ID_COL = "A";
    public static final String PSTEM_HVL_COL = "C";
    public static final String PSTEM_DATE_UPDATED_ADDRESS = "B1";
    public static final String PSTEM_OPTION_ADDRESS = "B2";
    public static final String PSTEM_OPTION_VALUE_UNITY = "unity";
    public static final String PSTEM_OPTION_VALUE_MEASURED = "measured";

    // Audit info related
    public static final String AUDIT_STATUS_PROGRESSING = "Progressing";
    public static final String AUDIT_STATUS_FINISHED = "Finished";
    public static final Map<String, String> auditInfoCellAddressMap = new HashMap<String, String>() {{
        put(FIELD_NAME_AUDIT_DATE, "C15");
        put(FIELD_NAME_ACDS_AUDIT_NUMBER, "C17");
        put("acdsFacilityId", "C18");
        put("radiationOncologyOrg", "C20");
        put("radiationOncologyService", "C21");
        put("radiationOncologyFacility", "C22");
        put("facilityRepRole1", "C23");
        put("facilityRepTitle1", "C24");
        put("facilityRepFName1", "C25");
        put("facilityRepLName1", "C26");
        put("facilityRepPhone1", "C27");
        put("facilityRepEmail1", "C28");
        put("facilityRepRole2", "E23");
        put("facilityRepTitle2", "E24");
        put("facilityRepFName2", "E25");
        put("facilityRepLName2", "E26");
        put("facilityRepPhone2", "E27");
        put("facilityRepEmail2", "E28");
        put("physAddressLoc", "C30");
        put("physAddressSt", "C31");
        put("physAddressSuburb", "C32");
        put("physAddressState", "C33");
        put("physAddressPostcode", "C34");
        put("reportingAddressLoc", "C36");
        put("reportingAddressSt", "C37");
        put("reportingAddressSuburb", "C38");
        put("reportingAddressState", "C39");
        put("reportingAddressPostcode", "C40");
        put("machineKvUnitManufacturer", "C42");
        put("machineKvUnitModel", "C43");
        put("machineSn", "C44");
        put("machineLocalName", "C45");
        put("machineTubeInsertType", "C46");
        put("machineTubeInsertSn", "C47");
        put("refDosimetryProtocol", "C49");
        put("refDosimetryNkDetermined", "C50");
        put("refDosimetryComments", "C51");
        put("kvRefDosimetryProtocol", "C55");
        put("kvRefDosimetryRefChamber", "C56");
        put("kvRefDosimetryAirPhantom", "C57");
        put("kvRefDosimetryRefDepth", "C58");
        put("kvRefDosimetryComments", "C59");
    }};

    // Ref cone related
    public static final int REF_CONE_ROW_COUNT = 10;
    public static final int REF_CONE_ROW_START = 67;
    public static final String REF_CONE_BEAM_ID_ALT_COL = "A";
    public static final Map<String, String> refConeColMap = new HashMap<String, String>() {{
        put(FIELD_NAME_BEAM_ID_ALT, REF_CONE_BEAM_ID_ALT_COL);
        put("nomEnergy", "B");
        put("measSsd", "C");
        put("measDepth", "D");
        put("geomShape", "E");
        put("geomSize", "F");
        put("closedOpen", "G");
        put("thickness", "H");
        put("dospSsdZ", "I");
    }};

    public enum RefConeDospSsd {
        y,
        n
    }

    // Ref cone comments related
    public static final int REF_CONE_COMMENTS_ROW_START = 67;
    public static final int REF_CONE_COMMENTS_ROW_COUNT = 10;
    public static final String REF_CONE_COMMENTS_COL = "K";
    public static final String REF_CONE_COMMENTS_SOURCE = "reference_cone";

    // Cone properties related
    public static final int CONE_COUNT = 3;
    public static final int CONE_ID_ALT_ROW = 81;
    public static final int CONE_SHAPE_ROW = 82;
    public static final int CONE_OPEN_CLOSED_ROW = 83;
    public static final int CONE_END_THICKNESS_ROW = 84;
    public static final int CONE_SSD_ROW = 85;
    public static final int CONE_FIELD_DIAMETER_ROW = 86;
    public static final int CONE_FIELD_AREA_ROW = 86;
    public static final int CONE_FIELD_DIMENSION1_ROW = 86;
    public static final int CONE_FIELD_DIMENSION2_ROW = 87;
    public static final int CONE_DOSE_OUTPUT_UNIT_ROW = 90;
    public static final String CONE_COL_START = "C";
    public static final String CONE_ID_ALT_ORIGIN = "Please type";
    public static final String CONE_DOSE_OUTPUT_UNIT_GYMIN = "Gy/min";
    public static final String CONE_DOSE_OUTPUT_UNIT_CGYMIN = "cGy/min";
    public static final String CONE_DOSE_OUTPUT_UNIT_GYMU = "Gy/MU";
    public static final String CONE_DOSE_OUTPUT_UNIT_CGYMU = "cGy/MU";
    public static final Set<String> coneDoseOutputUnitSet = new HashSet<String>() {{
        add(CONE_DOSE_OUTPUT_UNIT_GYMIN);
        add(CONE_DOSE_OUTPUT_UNIT_CGYMIN);
        add(CONE_DOSE_OUTPUT_UNIT_GYMU);
        add(CONE_DOSE_OUTPUT_UNIT_CGYMU);
    }};

    public enum ConeShape {
        Circular,
        Square,
        Rectangular
    }

    public enum ConeOpenClosed {
        Open,
        Closed
    }

    // Cone beam related
    public static final int CONE_BEAM_ROW_START = 91;
    public static final int CONE_BEAM_ROW_COUNT = REF_CONE_ROW_COUNT;

    // Beam quality related
    public static final int BEAM_QUALITY_ROW_START = CONE_BEAM_ROW_START;
    public static final int BEAM_QUALITY_ROW_COUNT = REF_CONE_ROW_COUNT;
    public static final Map<String, String> beamQualityColMap = new HashMap<String, String>() {{
        put("scd", "J");
        put("fieldSizeAtScd", "K");
        put("hvlNominalMmAl", "L");
        put("hvlNominalMmCu", "M");
        put("hvlMeasuredMmAl", "N");
        put("hvlMeasuredMmCu", "O");
    }};

}
