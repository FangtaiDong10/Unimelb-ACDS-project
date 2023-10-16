<template>
  <div id="caseSpecification" style="padding: 20px">
    <el-tabs type="border-card">
      <!-- Data WorkSheet -->
      <el-tab-pane label="Worksheet">
        <el-container>
          <el-header height="100px"
            ><br />
            <h1 style="font-size: 50px">Cone Calculation</h1>
            <br
          /></el-header>
          <el-main>
            <el-container>
              <el-main>
                <el-collapse v-model="activeNames">
                  <!-- <el-collapse-item name="1" v-model="activeNames">
                 <template slot="title">
                   <h1  style="color: #3A71A8">Representatives</h1>
                 </template>
                 <representatives />
               </el-collapse-item> -->

                  <el-collapse-item name="2" v-model="activeNames">
                    <template slot="title">
                      <h1 style="font-size: 30px; color: #3a71a8">
                        Overview Table
                      </h1>
                    </template>
                    <overview-table
                      ref="overviewTable"
                      :update-facility-output="updateFacilityOutput"
                    />
                  </el-collapse-item>

                  <el-collapse-item name="3" v-model="activeNames">
                    <template slot="title">
                      <h1 style="font-size: 30px; color: #3a71a8">
                        Comparision
                      </h1>
                    </template>
                    <comparision ref="comparision" />
                    <!-- <kskpol /> -->
                  </el-collapse-item>

                  <!-- <el-collapse-item name="4" v-model="activeNames">
                 <template slot="title">
                   <h1  style="color: #3A71A8">OSLD Irradiations</h1>
                 </template>
                 <irradiations />
               </el-collapse-item> -->

                  <el-collapse-item name="5" v-model="activeNames">
                    <template slot="title">
                      <h1 style="font-size: 30px; color: #3a71a8">
                        Cone Calculation
                      </h1>
                    </template>
                    <coneCalculation
                      ref="coneCalculation"
                      @sync-kv-and-kpol="syncKK"
                    />
                  </el-collapse-item>
                </el-collapse>
              </el-main>
            </el-container>
          </el-main>
        </el-container>
      </el-tab-pane>

      <!-- HVL -->
      <el-tab-pane label="HVL">
        <el-container>
          <el-header height="150px"
            ><br />
            <h1 style="font-size: 50px">HVL</h1>
            <br
          /></el-header>
          <el-main>
            <hvl ref="hvl" />
          </el-main>
        </el-container>
      </el-tab-pane>

      <!-- Cone Factors -->
      <el-tab-pane label="Cone Factor">
        <el-container>
          <el-header height="150px"
            ><br />
            <h1 style="font-size: 50px">Cone Factor</h1>
            <br
          /></el-header>
          <cone-factors ref="coneFactors" />
        </el-container>
      </el-tab-pane>

      <!-- Ks -->
      <el-tab-pane label="Ks">
        <el-container>
          <el-header height="100px"
            ><br />
            <h1 style="font-size: 50px">Ks</h1>
            <br
          /></el-header>
          <ks ref="ks" :update-ks-and-kpol="setKsAndKpol" />
        </el-container>
      </el-tab-pane>
    </el-tabs>

    <br />

    <!-- Save Button -->
    <el-button type="primary" @click="save">Save</el-button>
  </div>
</template>

<script>
// Data WorkSheet
import overviewTable from "./dataWorkSheet/overviewTable";
import representatives from "./dataWorkSheet/representatives";
import comparision from "./dataWorkSheet/comparision";
import irradiations from "./dataWorkSheet/irradiations";
import coneCalculation from "./dataWorkSheet/coneCalculation";
import kskpol from "./dataWorkSheet/kskpol";

// coneFactors, Kv, HVL
import coneFactors from "./coneFactors/coneFactors";
import ks from "./ks/ks";
import hvl from "./hvl/hvl";

// api
import { queryTable2, queryResult, saveResult } from "@/api/api";

export default {
  data() {
    return {
      activeNames: ["2"],
      isSeen: true,

      form: {
        name: "",
        region: "",
        date1: "",
        date2: "",
        delivery: false,
        type: [],
        resource: "",
        desc: "",
      },
      active: 0,
      tableData: [
        {
          cone_name: "",
          beams_data: [
            {
              beam_no: "",
              kvp: "",
              nominal_hvl_mmA1: "",
              nominal_hvl_mmCu: "",
              measured_hvl_mmA1: "",
              measured_hvl_mmCu: "",
              mu_rho: "",
              bw: "",
              k_closed_cone: "",
              dose_rate: "",
              daily_output: "",
              k_combined_cone: "",
            },
          ],
        },
      ],
      currentDate: new Date(),
      kk1db: null,
      kk2db: null,
    };
  },
  methods: {
    getIDBdata(key) {},

    syncKK() {
      // get dbs info
      const kk1Req = indexedDB.open("KK1");
      kk1Req.onerror = (event) => {
        console.log("some error");
      };
      kk1Req.onsuccess = (event) => {
        this.kk1db = event.target.result;
        // console.log("test ok");
        // db table name = chamber name
        // db id = beam_no
        const chamberT = this.kk1db
          .transaction(`${this.coneCalculation.getChamberSn}`, "readwrite")
          .objectStore(`${this.coneCalculation.getChamberSn}`);

        // console.log(this.coneCalculation.getChamberSn);
        // console.log(this.getIDBdata("Filter 1"));
        // req.onsuccess = (event) => {
        //   req.result.ks;
        //   req.result.kpol;
        // };

        for (const cc of this.coneCalculation.tableData) {
          for (const coneCC of Array.from(cc.beams_data)) {
            const req = chamberT.get(`${coneCC.beam_no}`);
            req.onsuccess = (event) => {
              coneCC.ks = req.result.ks;
              coneCC.k_pol =
                req.result.kpol === undefined ? null : req.result.kpol;
            };
          }
        }
      };
      console.log(this.coneCalculation.tableData);
    },
    setKsAndKpol() {
      // console.log("setKsandKpol")
      // console.log(this.overviewTable.tableData)
      // console.log(this.ks.tableData)
      // console.log(this.coneCalculation.tableData)
      var coneFactorsData = this.coneCalculation.tableData;
      var ksData = this.ks.tableData;
      for (let each1 of coneFactorsData) {
        for (let each2 of ksData) {
          if (each1.basic_data.chamber_sn === each2.chamber) {
            for (let each3 of each1.beams_data) {
              for (let each4 of each2.beams_data) {
                if (each3.beam_no === each4.beam_id) {
                  each3.ks = each4.ks;
                  each3.k_pol = each4.kpol;
                }
              }
            }
            break;
          } else {
            for (let each3 of each1.beams_data) {
              each3.ks = null;
              each3.k_pol = null;
            }
          }
        }
      }
    },
    updateFacilityOutput() {
      var overviewData = this.overviewTable.tableData;
      var coneFactorsData = this.coneCalculation.tableData;
      for (let each1 of coneFactorsData) {
        for (let each2 of overviewData) {
          if (each1.cone_name === each2.cone_name) {
            for (let each3 of each1.beams_data) {
              for (let each4 of each2.beams_data) {
                if (each3.beam_no === each4.beam_no) {
                  if (!Number.isFinite(parseFloat(each4.daily_output))) {
                    each3.fac_output = each4.dose_rate;
                  } else {
                    each3.fac_output = each4.dose_rate * each4.daily_output;
                  }
                }
              }
            }
          }
        }
      }
    },

    async save() {
      this.$messageBox
        .confirm("Are you sure?", "Notice", {
          confirmButtonText: "Yes",
          cancelButtonText: "No",
          type: "warning",
        })
        .then(async () => {
          let overviewTableData = this.overviewTable.tableData;
          let ksTableData = this.ks.tableData;
          let coneCalculationTableData = this.coneCalculation.allTableData;
          let comparisionTableData = this.comparision.tableData;
          let hvlTableData = this.hvl.tables;
          let data = {
            overviewTableData: overviewTableData,
            ksTableData: ksTableData,
            coneCalculationTableData: coneCalculationTableData,
            comparisionTableData: comparisionTableData,
            referenceConeFactorsTableData: this.coneFactors.referenceTableData,
            coneFactorsTableData: this.coneFactors.cones,
            hvlTableData: hvlTableData,
          };
          const res = await saveResult(this.$route.params.auditNumber, data);
          if (res.success) {
            this.$message.success("save successful");
          } else {
            this.$message.error("save unsuccessful");
          }
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "cancel",
          });
        });
    },

    // Cone Calculation Data Visibility Modification
    async getPageData() {
      // get current path audit number as Get Request argument
      const res = await queryTable2(this.$route.params.auditNumber);
      if (res.success) {
        // data type transform and reading
        // then set tp the conCalculation.tabledata

        // extract attribute not from queryTable2 (WorkDataSheet2) with initialized default values
        const initializedElectron = [
          {
            with_m_1: "",
            with_m_2: "",
            with_m_3: "",
            with_avg: "",
            without_m_1: "",
            without_m_2: "",
            without_m_3: "",
            without_avg: "",
            diff: "",
          },
        ];
        const initializedKelec = null;
        const initializedWBV = "";
        const initializedCN = "";
        const initializedMeasId = "";
        const initializedTime = "";
        const initializedStdOff1 = 4.5;
        const initializedisLCorr = "";
        const initializedStdOff2 = "";

        const assignedConeCalculation = [];
        // iterate response from dataworksheet2
        for (const cc of res.data) {
          const aCCObj = new Object();
          Object.keys(cc).forEach((key) => {
            if (key === "coneName") {
              aCCObj["cone_name"] = cc[key];
            } else if (key === "basicData") {
              const basicObj = new Object();
              Object.keys(cc[key]).forEach((basickey) => {
                basicObj[this.lineOrHump(basickey, "toLine")] =
                  cc[key][basickey];
              });
              basicObj["electron_contamination_data"] = initializedElectron;
              basicObj["kelec"] = initializedKelec;
              basicObj["webline_bias_v"] = initializedWBV;
              basicObj["chamber_name"] = initializedCN;
              basicObj["meas_ID"] = initializedMeasId;
              basicObj["time"] = initializedTime;
              basicObj["standoff_1"] = initializedStdOff1;
              basicObj["isl_corr"] = initializedisLCorr;
              basicObj["standoff_2"] = initializedStdOff2;

              aCCObj[this.lineOrHump(key, "toLine")] = basicObj;
            } else {
              const beams = [];
              const selectedChamber =
                this.$store.getters["chamber/getChamberSn"];
              if (selectedChamber === "") {
                for (const beam of cc[key]) {
                  for (const chamberResults of beam["chamberResultList"]) {
                    const beamObj = new Object();
                    Object.keys(chamberResults).forEach((beamkey) => {
                      if (beamkey !== "nk") {
                        beamObj[this.lineOrHump(beamkey, "toLine")] =
                          chamberResults[beamkey];
                      } else {
                        beamObj["Nk"] = chamberResults[beamkey];
                      }
                    });
                    beamObj["beam_no"] = beam["beamNo"];
                    beamObj["kv"] = beam["kv"];
                    // other attributes not provided from Backend
                    // blue
                    beamObj["ks"] = null;
                    beamObj["k_pol"] = null;
                    beamObj["fac_output"] = 1.004;
                    beamObj["mu_min"] = null;

                    // yellow
                    beamObj["mu"] = null;
                    beamObj["temp"] = null;
                    beamObj["pressure"] = null;
                    beamObj["m_nc_1"] = null;
                    beamObj["m_nc_2"] = null;
                    beamObj["m_nc_3"] = null;

                    //green
                    beamObj["ktp"] = null;
                    beamObj["m_avg"] = null;
                    beamObj["acds_output"] = null;
                    beamObj["cone_diff"] = null;

                    // push beam
                    beams.push(beamObj);
                  }
                }
              }
              aCCObj[this.lineOrHump(key, "toLine")] = beams;
            }
          });
          assignedConeCalculation.push(aCCObj);
        }
        this.coneCalculation.allTableData = JSON.parse(
          JSON.stringify(assignedConeCalculation)
        ); //deep clone
        this.coneCalculation.tableData = assignedConeCalculation;

        for (var cone of this.coneCalculation.tableData) {
          cone.beams_data.sort(function (first, second) {
            return first.kv - second.kv;
          });
        }
      } else {
        this.getPageData();
      }

      // query all data result
      const res2 = await queryResult(this.$route.params.auditNumber);
      if (res2.success) {
        if (res2.data !== null) {
          this.overviewTable.tableData = res2.data.overviewTableData;
          this.ks.tableData = res2.data.ksTableData;
          this.coneCalculation.allTableData =
            res2.data.coneCalculationTableData;
          this.comparision.tableData = res2.data.comparisionTableData;
          this.coneFactors.referenceTableData =
            res2.data.referenceConeFactorsTableData;
          this.coneFactors.cones = res2.data.coneFactorsTableData;
          this.hvl.tables = res2.data.hvlTableData;
        }
      } else {
        this.getPageData();
      }
    },
    lineOrHump(name, type) {
      if (type === "toLine") {
        return name.replace(/([A-Z])/g, "_$1").toLowerCase();
      } else if (type === "toHump") {
        return name.replace(/\_(\w)/g, function (underScore, letter) {
          return letter.toUpperCase();
        });
      }
    },
  },
  watch: {
    "$store.state.chamber.chamberSn": function () {
      const selectedChamber = this.$store.getters["chamber/getChamberSn"];
      const partialTable = new Array();
      let allTables = Array.from(this.coneCalculation.allTableData); //just in case
      Object.keys(allTables).forEach((index) => {
        const tempObj = allTables[index];
        const partialConeTable = new Object();
        Object.keys(tempObj).forEach((key) => {
          if (key === "basic_data") {
            partialConeTable["basic_data"] = tempObj[key];
          } else if (key === "cone_name") {
            partialConeTable["cone_name"] = tempObj[key];
          } else if (key === "beams_data") {
            const beamArray = new Array();
            for (const beam of tempObj[key]) {
              if (selectedChamber === beam["chamber_sn"]) {
                beamArray.push(beam);
              }
            }
            partialConeTable["beams_data"] = beamArray;
          }
        });
        partialTable.push(partialConeTable);
      });
      this.coneCalculation.tableData = partialTable;
    },
  },
  mounted() {
    // // both-way hump and line transfer successful
    // console.log(this.lineOrHump("underScoreTest", "toLine"));
    // console.log(this.lineOrHump("under_score_test", "toHump"));

    this.overviewTable = this.$refs.overviewTable;

    this.ks = this.$refs.ks;
    this.coneCalculation = this.$refs.coneCalculation;
    this.coneFactors = this.$refs.coneFactors;
    this.comparision = this.$refs.comparision;
    this.hvl = this.$refs.hvl;
    this.getPageData();
  },

  components: {
    overviewTable,
    representatives,
    irradiations,
    comparision,
    coneCalculation,
    ks,
    kskpol,
    coneFactors,
    hvl,
  },
};
</script>

<style lang="scss" scoped></style>
