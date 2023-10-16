<template>
  <div>
    <!-- first table in row1 on DataWorkSheet -->
    <el-table
      :data="tableData[active].beams_data"
      style="width: 100%"
      class="overviewTable"
    >
      <!-- Beam No. -->
      <el-table-column prop="beam_no" label="Beam No." align="center">
      </el-table-column>

      <!-- KVp -->
      <el-table-column prop="kvp" label="KVp" align="center"> </el-table-column>

      <!-- Nominal HVL -->
      <el-table-column label="Nominal HVL" align="center">
        <el-table-column prop="nominal_hvl_mmAl" label="mm Al" align="center">
        </el-table-column>
        <el-table-column prop="nominal_hvl_mmCu" label="mm Cu" align="center">
        </el-table-column>
      </el-table-column>

      <!-- Measured HVL -->
      <el-table-column label="Measured HVL" align="center">
        <el-table-column prop="measured_hvl_mmAl" label="mm Al" align="center">
        </el-table-column>
        <el-table-column prop="measured_hvl_mmCu" label="mm Cu" align="center">
        </el-table-column>
      </el-table-column>

      <!-- mu/rho -->
      <el-table-column prop="mu_rho" label="mu/rho" align="center">
        <template
          slot-scope="scope"
          v-if="scope.row.mu_rho !== '' && scope.row.mu_rho !== null"
        >
          {{ parseFloat(scope.row.mu_rho).toFixed(3) }}
        </template>
      </el-table-column>

      <!-- SSD 30 or 50 -->
      <el-table-column align="center">
        <template slot="header" slot-scope="{}">
          {{ tableData[active].cone_name }}<br />
          <p style="font-weight: bold; color: #409eff">
            {{ tableData[active].open }} SSD: {{ tableData[active].ssd }}
          </p>
        </template>

        <!-- Bw -->
        <el-table-column
          prop="bw_combined"
          label="Bw"
          style="background-color: #3a71a8"
          align="center"
        >
          <template
            slot-scope="scope"
            v-if="!isNaN(parseFloat(scope.row.bw_combined))"
          >
            {{ parseFloat(scope.row.bw_combined).toFixed(3) }}
          </template>
        </el-table-column>

        <!-- k closed cone -->
        <el-table-column
          prop="k_closed_cone"
          label="k closed cone"
          align="center"
        >
          <template
            slot-scope="scope"
            v-if="!isNaN(parseFloat(scope.row.k_closed_cone))"
          >
            {{ parseFloat(scope.row.k_closed_cone).toFixed(3) }}
          </template>
        </el-table-column>

        <!-- k combined cone -->
        <el-table-column
          prop="k_combined_cone"
          label="k combined cone"
          align="center"
        >
          <template slot-scope="scope">
            <p
              style="font-weight: bold; color: #30b08f"
              v-if="
                !isNaN(
                  parseFloat(
                    calculate_combined_cone(
                      scope.row.bw_combined,
                      scope.row.k_closed_cone,
                      scope.row.mu_rho
                    )
                  )
                )
              "
            >
              {{
                parseFloat(
                  calculate_combined_cone(
                    scope.row.bw_combined,
                    scope.row.k_closed_cone,
                    scope.row.mu_rho
                  )
                ).toFixed(3)
              }}
            </p>
          </template>
        </el-table-column>

        <!-- Daily output -->
        <el-table-column
          prop="daily_output"
          label="Daily output"
          align="center"
        >
          <template slot-scope="scope">
            <el-input
              :disabled="
                scope.row.dose_rate == '' || scope.row.dose_rate == null
              "
              v-model="scope.row.daily_output"
              placeholder=""
              class="manually"
              @change="updateFacilityOutput"
            ></el-input>
          </template>
        </el-table-column>

        <!-- Dose Rate (nom) cGy/MU -->
        <el-table-column
          prop="dose_rate"
          label="Dose Rate (nom) cGy/MU"
          align="center"
        >
          <template slot="header" slot-scope="{}">
            Dose Rate (nom) <br />
            <p align="center" style="font-weight: bold; color: #409eff">
              {{ tableData[active].nominal_dose_output_type }}
            </p>
          </template>
          <template slot-scope="scope">
            <p
              style="font-weight: bold; color: #30b08f"
              v-if="!isNaN(parseFloat(calculate_dose_rate(scope.row)))"
            >
              {{ parseFloat(calculate_dose_rate(scope.row)).toFixed(3) }}
            </p>
          </template>
        </el-table-column>

        <!-- OSLD Irradiations-->
        <el-table-column label="OSLD Irradiations" align="center">
          <!-- column 1 -->
          <el-table-column
            prop="gymu"
            label="Setting for 1 Gy MU"
            align="center"
          >
            <template slot-scope="scope">
              <p
                style="font-weight: bold; color: #30b08f"
                v-if="
                  !(
                    isNaN(parseFloat(calculate_gymu(scope.row.dose_rate))) ||
                    !isFinite(parseFloat(calculate_gymu(scope.row.dose_rate)))
                  )
                "
              >
                {{ parseFloat(calculate_gymu(scope.row.dose_rate)).toFixed(3) }}
              </p>
            </template>
          </el-table-column>

          <!-- column 2 -->
          <el-table-column prop="gymu" label="Setting Used MU" align="center">
            <template slot-scope="scope">
              <el-input
                :disabled="
                  scope.row.dose_rate == '' || scope.row.dose_rate == null
                "
                v-model="scope.row.used_mu"
                placeholder=""
                class="manually"
              ></el-input>
            </template>
          </el-table-column>

          <!-- column 3 -->
          <el-table-column
            prop="fac_dose"
            label="Fac Dose to OSLDs"
            align="center"
          >
            <template slot-scope="scope">
              <p
                style="font-weight: bold; color: #30b08f"
                v-if="
                  !isNaN(
                    parseFloat(
                      calculate_fac_dose_to_OSLDs(tableData[active], scope.row)
                    )
                  )
                "
              >
                {{
                  parseFloat(
                    calculate_fac_dose_to_OSLDs(tableData[active], scope.row)
                  ).toFixed(3)
                }}
              </p>
            </template>
          </el-table-column>
        </el-table-column>
      </el-table-column>
    </el-table>

    <!-- Cone Switch button -->
    <el-button
      size="medium"
      style="margin-top: 20px; margin-left: 45%"
      icon="el-icon-d-arrow-left"
      @click="last"
      circle
    ></el-button>
    <b>{{ "\xa0\xa0" + tableData[active].cone_name + "\xa0\xa0" }}</b>
    <el-button
      size="medium"
      style="margin-top: 12px"
      icon="el-icon-d-arrow-right"
      @click="next"
      circle
    ></el-button>
  </div>
</template>

<script>
import { queryTable1, queryResult } from "@/api/api";
export default {
  name: "overviewTable",
  props: {
    updateFacilityOutput: {
      type: Function,
    },
  },
  data() {
    return {
      active: 0,
      tableData: [
        {
          cone_name: "F",
          open: "Open",
          ssd: "30",
          nominal_dose_output_type: "cGy/MU",
          beams_data: [
            {
              beam_no: "",
              kvp: "",
              nominal_hvl_mmAl: "",
              nominal_hvl_mmCu: "",
              measured_hvl_mmAl: "",
              measured_hvl_mmCu: "",
              mu_rho: "",
              bw_combined: "",
              k_closed_cone: "",
              dose_rate: "",
              daily_output: "",
              k_combined_cone: "",
              // OSLD part
              gymu: "",
              used_mu: "",
              fac_dose: "",
            },
          ],
        },
      ],
    };
  },
  methods: {
    next() {
      if (this.active++ >= 2) this.active = 2;
    },
    last() {
      if (this.active-- < 1) this.active = 0;
    },

    calculate_dose_rate(data) {
      if (!Number.isFinite(parseFloat(data.daily_output))) {
        this.updateFacilityOutput();
        return data.dose_rate;
      } else {
        this.updateFacilityOutput();
        return data.dose_rate * data.daily_output;
      }
      // if (data.daily_output === null || data.daily_output === '') {
      //   return data.dose_rate
      // } else {
      //   alert()
      //   data.dose_rate = data.daily_output * data.dose_rate
      //   return data.dose_rate
      // }
    },
    calculate_combined_cone(a, b, c) {
      return a * b * c;
    },
    calculate_gymu(doseRate) {
      return 60 / doseRate;
    },

    calculate_fac_dose_to_OSLDs(table, data) {
      let type = table.nominal_dose_output_type;
      let res = null;
      if (type === "cGy/MU") {
        res = data.dose_rate * 100 * data.used_mu;
      } else if (type === "Gy/min") {
        res = data.dose_rate * (data.used_mu / 60);
      } else if (type === "Gy/MU") {
        res = data.dose_rate * data.used_mu;
      } else if (type === "cGy/min") {
        res = data.dose_rate * 100 * (data.used_mu / 60);
      }
      return res;
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

    // getPageData Modification for queryTable1
    async getPageData() {
      const res = await queryTable1(this.$route.params.auditNumber);
      if (res.success) {
        const assignedTableData = [];
        const coneDataList = res.data;
        for (const cone of coneDataList) {
          const aConeObj = new Object();
          Object.keys(cone).forEach((key) => {
            if (key !== "beamsData") {
              aConeObj[this.lineOrHump(key, "toLine")] = cone[key];
            } else {
              aConeObj["beams_data"] = [];
              for (const beam of cone[key]) {
                const aBeamObj = new Object();
                Object.keys(beam).forEach((key) => {
                  if (key === "nominalHvlMmAl") {
                    aBeamObj["nominal_hvl_mmAl"] = beam[key];
                  } else if (key === "nominalHvlMmCu") {
                    aBeamObj["nominal_hvl_mmCu"] = beam[key];
                  } else if (key === "measuredHvlMmAl") {
                    aBeamObj["measured_hvl_mmAl"] = beam[key];
                  } else if (key === "measuredHvlMmCu") {
                    aBeamObj["measured_hvl_mmCu"] = beam[key];
                  } else if (key === "chamberResultList") {
                    // for the common values, use the first instead
                    aBeamObj["mu_rho"] = beam[key][0]["muRho"];
                    aBeamObj["bw_combined"] = beam[key][0]["bwCombined"];
                    aBeamObj["k_closed_cone"] = beam[key][0]["kClosedCone"];
                    aBeamObj["k_combined_cone"] = beam[key][0]["kCombinedCone"];
                  } else {
                    aBeamObj[this.lineOrHump(key, "toLine")] = beam[key];
                  }
                });
                aConeObj["beams_data"].push(aBeamObj);
              }
            }
          });
          assignedTableData.push(aConeObj);
        }
        // Dispatch beam data to Vuex at least the (dose_rate) --> for the queryTable2's fac_output attribute
        this.$store.commit("overview/setBeams", assignedTableData);
        this.tableData = this.$store.getters["overview/currentBeams"];
        for (var cone of this.tableData) {
          cone.beams_data.sort(function (first, second) {
            return first.kvp - second.kvp;
          });
        }
      } else {
        this.getPageData();
      }
      // }
    },
  },
  mounted() {
    this.getPageData();
  },
};
</script>

<style scoped>
.manually >>> input {
  font-weight: bold;
  color: coral;
  font-size: larger;
}
.overviewTable >>> table th {
  font-weight: bold;
  color: black;
  font-size: small;
}
.overviewTable >>> table td {
  font-weight: bold;
  color: #409eff;
  font-size: larger;
}
.overviewTable >>> table {
  white-space: normal;
  word-break: break-word;
}
</style>
