<template>
  <div id="identification" style="padding: 20px">
    <div class="operate-wrapper">
      <!-- <el-switch
        v-model="isEdit"
        active-text="Edit"
        inactive-text="View"
        style="padding-bottom: 15px"
      >
      </el-switch> -->
    </div>
    <!-- identification Form -->
    <identificationForm :identification="identification" :is-edit="isEdit" />
    <!-- submit button -->
    <!-- <el-button type="primary" @click.prevent="submit" v-if="isEdit"
      >Submit</el-button
    > -->
  </div>
</template>

<script>
import identificationForm from "./identificationForm/identificationForm";
import { getIdentification } from "@/api/api";

export default {
  name: "identification",

  data() {
    return {
      isEdit: false,

      // Basic Information
      identification: {
        additional_comments: [],

        audit_information: {
          date: "",
          audit_id: "asd",
          facility_id: "",
        },

        treatment_machine: {
          manufacturer: "",
          unit_model: "",
          unit_serial_number: "",
          local_name: "",
          tube_insert_type: "",
          tube_serial_number: "",
        },

        // Facility Information including representatives
        facility_information: {
          organisation_name: "",
          oncology_service: "",
          oncology_facility: "",
          facility_representatives: [],

          physical_address: {
            building: "",
            street: "",
            suburb: "",
            state_name: "",
            post_code: "",
          },
          reporting_address: {
            building: "",
            street: "",
            suburb: "",
            state_name: "",
            post_code: "",
          },
        },

        // Reference Dosimetry
        reference_dosimetry: {
          protocol: "",
          nk: "",
          comments: "",
        },
        kv_reference_dosimetry: {
          protocol: "",
          reference_chamber: "",
          air_phantom: "",
          reference_depth: "",
          comments: "",
        },

        // Beams and Cones
        // beams
        beam_data: [
          {
            beam_ID: "",
            nom_energy: "",
            scd: "",
            field_size_at_scd: "",
            hvl_nominal_mm_ai: "",
            hvl_nominal_mm_cu: "",
            hvl_measured_mm_ai: "",
            hvl_measured_mm_cu: "",
          },
        ],

        // reference cone
        reference_cone: [
          {
            reference_cone_id_alt: "",
            ssd: "",
            depth: "",
            geometry_shape: "",
            geometry_measurement: "",
            open_closed: "",
            thickness: "",
            dosp_ssd: "",
          },
        ],

        // cone
        cone: [
          {
            cone_id: "",
            cone_id_alt: "1",
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "",
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "",
                dosp_ssd: "",
                nom_dose_output: "",
              },
            ],
          },
          {
            cone_id: "",
            cone_id_alt: "1",
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "",
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "",
                dosp_ssd: "",
                nom_dose_output: "",
              },
            ],
          },
          {
            cone_id: "a",
            cone_id_alt: "1",
            open_closed: "a",
            end_thickness: "a",
            ssd: "a",
            dose_output_unit: "a",
            shape: "a",
            field_diameter: "a",
            field_area: "a",
            field_dimension_1: "a",
            field_dimension_2: "a",
            beams_data: [
              {
                beam_id: "a",
                dosp_ssd: "a",
                nom_dose_output: "a",
              },
            ],
          },
        ],
      },
    };
  },
  methods: {
    // a string param -> val
    camelcaseToUnderscore(val) {
      let str = val;
      const regular = /[A-Z]/g;
      let arr = str.split("");
      // initialize a container
      let i = [];

      arr.forEach((item, index) => {
        if (regular.test(item)) {
          // get index it the char is UpperCase
          i.push(index);
        }
      });
      // console.log(i)

      // e.g above for hvlTestCase --> i=[3,7]
      // (x, index) --> foreach (3,0), (7,1) do function
      // this used to determine where to insert '_'
      i = i.map((x, index) => {
        // new i arr is [3, 8]
        return x + index;
      });

      // splice() to restructure
      i.forEach((item) => {
        arr.splice(item, 0, "_");
      });

      let result = arr.join("").toLowerCase();

      return result;
    },

    // getData
    async getPageData() {
      // console.log(this.camelcaseToUnderscore("fieldSizeAtScd"))
      const res = await getIdentification(this.$route.params.auditNumber);
      if (res.success) {
        // Audit_Info
        this.identification.audit_information.audit_id =
          res.data.auditInformation.auditId;
        this.identification.audit_information.date =
          res.data.auditInformation.date;
        this.identification.audit_information.facility_id =
          res.data.auditInformation.facilityId;

        // Treatment_Machine
        this.identification.treatment_machine.manufacturer =
          res.data.treatmentMachine.manufacturer;
        this.identification.treatment_machine.unit_model =
          res.data.treatmentMachine.unitModel;
        this.identification.treatment_machine.unit_serial_number =
          res.data.treatmentMachine.unitSerialNumber;
        this.identification.treatment_machine.local_name =
          res.data.treatmentMachine.localName;
        this.identification.treatment_machine.tube_insert_type =
          res.data.treatmentMachine.tubeInsertType;
        this.identification.treatment_machine.tube_serial_number =
          res.data.treatmentMachine.tubeSerialNumber;

        // Facility_Info
        this.identification.facility_information.organisation_name =
          res.data.facilityInformation.organisationName;
        this.identification.facility_information.oncology_service =
          res.data.facilityInformation.oncologyService;
        this.identification.facility_information.oncology_facility =
          res.data.facilityInformation.oncologyFacility;
        // representatives iteration
        const representatives = res.data.facilityRepresentativeList;
        Object.keys(representatives).forEach((key) => {
          const rept = representatives[key];
          const first_name = rept.firstName;
          const last_name = rept.lastName;
          delete rept.firstName;
          delete rept.lastName;
          const okrept = { first_name, last_name, ...rept };

          this.identification.facility_information.facility_representatives.push(
            okrept
          );
        });
        //physical address
        const physicalAdd = res.data.physicalAddress;
        const state_name = physicalAdd.stateName;
        const post_code = physicalAdd.postCode;
        delete physicalAdd.stateName;
        delete physicalAdd.postCode;
        const physicalok = { state_name, post_code, ...physicalAdd };
        this.identification.facility_information.physical_address = physicalok;
        //reporting address
        const reportingAdd = res.data.reportingAddress;
        const rstate_name = reportingAdd.stateName;
        const rpost_code = reportingAdd.postCode;
        delete reportingAdd.stateName;
        delete reportingAdd.postCode;
        const reportingok = {
          state_name: rstate_name,
          post_code: rpost_code,
          ...reportingAdd,
        };
        this.identification.facility_information.reporting_address =
          reportingok;

        // Reference Dosimetry
        this.identification.reference_dosimetry = res.data.referenceDosimetry;
        this.identification.kv_reference_dosimetry.protocol =
          res.data.kvReferenceDosimetry.protocol;
        this.identification.kv_reference_dosimetry.reference_chamber =
          res.data.kvReferenceDosimetry.referenceChamber;
        this.identification.kv_reference_dosimetry.air_phantom =
          res.data.kvReferenceDosimetry.airPhantom;
        this.identification.kv_reference_dosimetry.reference_depth =
          res.data.kvReferenceDosimetry.referenceDepth;
        this.identification.kv_reference_dosimetry.comments =
          res.data.kvReferenceDosimetry.comments;

        // Beams and Cones
        // Beams Reading
        const beamdata = res.data.beamDataList;
        const assignedBeams = [];
        for (const beam of beamdata) {
          const obj = new Object();
          Object.keys(beam).forEach((key) => {
            if (key !== "beamId" && key !== "referenceConeId") {
              if (key !== "beamIdAlt") {
                const newkey = this.camelcaseToUnderscore(key);
                // console.log(newkey)
                obj[newkey] = beam[key];
              } else {
                obj.beam_ID = beam[key];
              }
            }
          });
          assignedBeams.push(obj);
        }
        // console.log(assignedBeams)
        this.identification.beam_data = assignedBeams;

        // ReferenceCone Reading
        const referenceConeList = res.data.referenceConeList;
        const assignedRC = [];
        for (const rc of referenceConeList) {
          const obj = new Object();
          Object.keys(rc).forEach((key) => {
            if (key !== "referenceConeId" && key !== "nomEnergy") {
              const underScoreKey = this.camelcaseToUnderscore(key);
              obj[underScoreKey] = rc[key];
            }
          });
          assignedRC.push(obj);
        }
        this.identification.reference_cone = assignedRC;

        // Applicator/Cone 1 -->
        // Manipulate: 1-->coneList, 2-->auditBeamInputsList
        // Data attributes extract attention:
        //     1: for auditBeamsInputsList, doseOutputUnit -> to parent node
        //     2: for coneList, fieldDimension1 and fieldDimension2 -> concat numbers
        const auditBeamsInputsList = res.data.auditBeamInputsList;
        const coneList = res.data.coneList;
        // assigned Applicator/Cone ConeAlt
        const assignedAC = [];
        // for every cone object in coneList
        for (const cone of coneList) {
          const aConeObj = new Object();
          Object.keys(cone).forEach((key) => {
            if (key !== "fieldDimension1" && key !== "fieldDimension2") {
              const underScoreKey = this.camelcaseToUnderscore(key);
              aConeObj[underScoreKey] = cone[key];
            } else {
              const prekey = key.slice(0, -1);
              const postkey = key.slice(-1);
              const concatUnderScoreKey = this.camelcaseToUnderscore(
                prekey
              ).concat("_", postkey);
              aConeObj[concatUnderScoreKey] = cone[key];
            }
          });

          const beamsDataList = [];
          // construct two more attributes: 1 dose_output_unit; 2 beams_data
          for (const audit of auditBeamsInputsList) {
            // console.log("ok1");
            if (audit.coneId === aConeObj.cone_id) {
              // console.log("ok");
              const beamsData = new Object();
              Object.keys(audit).forEach((key) => {
                if (key === "doseOutputUnit") {
                  // ISSUE! need to make sure!
                  aConeObj[this.camelcaseToUnderscore(key)] = audit[key];
                }
                if (
                  key === "beamId" ||
                  key === "dospSsd" ||
                  key === "nomDoseOutput"
                ) {
                  beamsData[this.camelcaseToUnderscore(key)] = audit[key];
                }
              });
              // put beamsData object to beamsDataList
              beamsDataList.push(beamsData);
            }
            
          }

          // manipulate aConeObj
          aConeObj["beams_data"] = beamsDataList;
          // console.log(aConeObj)
          // push to assignedAC list
          assignedAC.push(aConeObj);
        }        
        
        // assign assignedAC to this.cone
        this.identification.cone = assignedAC;
      } else {
        this.getPageData();
      }
    },
  },
  mounted() {
    this.getPageData();
  },
  components: {
    identificationForm,
  },
};
</script>

<style scoped>
.table {
  color: red !important;
}
</style>
