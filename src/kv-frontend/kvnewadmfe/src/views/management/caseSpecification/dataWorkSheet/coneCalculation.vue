<template>
  <div>
    <!-- Cone Caculation (Third collapse table column) -->
    <el-container>
      <el-main>
        <!-- First Row in ConeCalculation -->
        <el-row>
          <!-- ConeCalculation Left Part (First Col)-->
          <el-col :span="12">
            <el-card class="box-card">
              <el-descriptions
                class="margin-top"
                :column="1"
                border
                :inline="true"
              >
                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Electrometer SN:
                  </template>
                  <el-select
                    v-model="tableData[active].basic_data.electrometer_sn"
                    placeholder="please select"
                    class="manually"
                  >
                    <el-option
                      v-for="item in electrometerSNOption"
                      :key="item"
                      :label="item"
                      :value="item"
                    >
                    </el-option>
                  </el-select>
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Channel:
                  </template>
                  <el-select
                    v-model="tableData[active].basic_data.channel"
                    placeholder="please select"
                    class="manually"
                  >
                    <el-option
                      v-for="item in channelOption"
                      :key="item"
                      :label="item"
                      :value="item"
                    >
                    </el-option>
                  </el-select>
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Kelec:
                  </template>
                  <el-input
                    :readonly="true"
                    v-model="tableData[active].basic_data.kelec"
                    class="fetched"
                    >{{ calculateKelec(tableData[active]) }}</el-input
                  >
                </el-descriptions-item>
                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    WebLine Range
                  </template>
                  <el-select
                    v-model="tableData[active].basic_data.webline_range"
                    placeholder="please select"
                    class="manually"
                  >
                    <el-option
                      v-for="item in webLineRangeOption"
                      :key="item"
                      :label="item"
                      :value="item"
                    >
                    </el-option>
                  </el-select>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>

          <!-- ConeCalculation Right Part (Second Col)-->
          <el-col :span="12">
            <el-card class="box-card">
              <el-descriptions
                class="margin-top"
                :column="1"
                border
                :inline="true"
              >
                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Meas ID:
                  </template>
                  <el-input
                    v-model="tableData[active].basic_data.meas_ID"
                    class="manually"
                  ></el-input>
                </el-descriptions-item>

                <!-- Left Second Attribute -->
                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Chamber:
                  </template>
                  <el-select
                    v-model="chamberSn"
                    placeholder="please select"
                    class="manually"
                  >
                    <el-option
                      v-for="item in chamberOption"
                      :key="item"
                      :label="item"
                      :value="item"
                    >
                    </el-option>
                  </el-select>
                  <el-input
                    v-if="this.getChamberSn != ''.toString()"
                    :readonly="true"
                    v-model="chamberLookupTable[this.getChamberSn]['name']"
                    class="fetched"
                    >></el-input
                  >
                </el-descriptions-item>

                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Webline Bias V:
                  </template>
                  <h2 v-if="this.getChamberSn === ''.toString()">
                    please select a chamber
                  </h2>
                  <el-input
                    v-else
                    :readonly="true"
                    v-model="
                      chamberLookupTable[this.getChamberSn]['weblineBiasV']
                    "
                    class="fetched"
                    >></el-input
                  >
                </el-descriptions-item>

                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Time
                  </template>
                  <el-input
                    v-model="tableData[active].basic_data.time"
                    class="manually"
                  ></el-input>
                </el-descriptions-item>

                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    ISL Corr:
                  </template>
                  <el-input
                    :readonly="true"
                    v-model="parseFloat(calculateISLCorr()).toFixed(3)"
                    @change="calculateStandoff"
                    class="manually"
                    v-if="!isNaN(parseFloat(calculateISLCorr()))"
                  ></el-input>
                </el-descriptions-item>

                <el-descriptions-item>
                  <template slot="label">
                    <i></i>
                    Standoff (mm):
                  </template>
                  <el-row>
                    <el-col :span="12"
                      ><el-input
                        v-model="tableData[active].basic_data.standoff_1"
                        @change="calculateStandoff"
                        class="manually"
                      ></el-input
                    ></el-col>
                    <el-col :span="12">
                      <el-input
                        :readonly="true"
                        v-model="
                          parseFloat(
                            tableData[active].basic_data.standoff_2
                          ).toFixed(3)
                        "
                        @change="calculateStandoff"
                        class="fetched"
                        v-if="
                          !isNaN(
                            parseFloat(tableData[active].basic_data.standoff_2)
                          )
                        "
                      ></el-input>
                    </el-col>
                  </el-row>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
        </el-row>

        <!-- Second Row of ConeCalculation -->
        <el-row v-if="chamberSn != ''.toString()">
          <el-card>
            <!-- switch button -->
            <el-switch
              v-model="
                tableData[active].basic_data.electron_contamination_check
              "
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="Electron Contamination Check"
            >
            </el-switch>

            <!-- v-if table -->
            <el-table
              v-if="tableData[active].basic_data.electron_contamination_check"
              :data="tableData[active].basic_data.electron_contamination_data"
              class="coneCalculation"
            >
              <!-- With Filter -->
              <el-table-column label="With Filtration" align="center">
                <el-table-column label="M(nc)" align="center">
                  <template slot-scope="scope">
                    <el-row>
                      <el-col :span="8"
                        ><el-input
                          v-model="scope.row.with_m_1"
                          class="manually"
                        ></el-input
                      ></el-col>
                      <el-col :span="8"
                        ><el-input
                          v-model="scope.row.with_m_2"
                          class="manually"
                        ></el-input
                      ></el-col>
                      <el-col :span="8"
                        ><el-input
                          v-model="scope.row.with_m_3"
                          class="manually"
                        ></el-input
                      ></el-col>
                    </el-row>
                  </template>
                </el-table-column>
                <el-table-column label="Avg" align="center" prop="with_avg">
                  <template slot-scope="scope">
                    <p style="font-weight: bold; color: #30b08f">
                      {{ parseFloat(calculateWithAvg(scope.row)).toFixed(3) }}
                    </p>
                  </template>
                </el-table-column>
              </el-table-column>

              <!-- Without Filter -->
              <el-table-column label="Without Filtration" align="center">
                <el-table-column label="M(nc)" align="center">
                  <template slot-scope="scope">
                    <el-row>
                      <el-col :span="8"
                        ><el-input
                          v-model="scope.row.without_m_1"
                          class="manually"
                        ></el-input
                      ></el-col>
                      <el-col :span="8"
                        ><el-input
                          v-model="scope.row.without_m_2"
                          class="manually"
                        ></el-input
                      ></el-col>
                      <el-col :span="8"
                        ><el-input
                          v-model="scope.row.without_m_3"
                          class="manually"
                        ></el-input
                      ></el-col>
                    </el-row>
                  </template>
                </el-table-column>
                <el-table-column label="Avg" align="center" prop="without_avg">
                  <template slot-scope="scope">
                    <p style="font-weight: bold; color: #30b08f">
                      {{
                        parseFloat(calculateWithoutAvg(scope.row)).toFixed(3)
                      }}
                    </p>
                  </template>
                </el-table-column>
              </el-table-column>

              <!-- %diff Column -->
              <el-table-column label="%diff" align="center" prop="diff">
                <template slot-scope="scope">
                  <p
                    style="font-weight: bold; color: #c03639"
                    v-if="calculateDiff(scope.row) > 0.05"
                  >
                    {{ parseFloat(calculateDiff(scope.row) * 100).toFixed(2) }}%
                    <br />Electron Filtration Required for this cone!
                  </p>
                  <p style="font-weight: bold; color: #30b08f" v-else>
                    {{ parseFloat(calculateDiff(scope.row) * 100).toFixed(2) }}%
                    <br />No Filtration Required
                  </p>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
          <el-card
            ><el-button @click="synchronizeKK"
              >Synchronize Ks and Kpol values</el-button
            ></el-card
          >
        </el-row>

        <!-- Data Visibility Table Row -->
        <h2 v-if="chamberSn === ''.toString()">please select a chamber</h2>
        <el-row v-else>
          <el-card>
            <el-table
              :data="tableData[active].beams_data"
              style="background-color: #ffffff !important"
              class="coneCalculation"
            >
              <!-- (MU) -->
              <el-table-column prop="mu" label="(MU)" align="center">
                <template slot-scope="scope">
                  <el-input
                    size="mini"
                    v-model="scope.row.mu"
                    placeholder=""
                    class="manually"
                  ></el-input>
                </template>
              </el-table-column>

              <!-- Beam No. -->
              <el-table-column prop="beam_no" label="Beam No." align="center">
              </el-table-column>

              <!-- kv -->
              <el-table-column prop="kv" label="kv" align="center">
              </el-table-column>

              <!-- Nk (...) -->
              <el-table-column prop="Nk" label="Nk (cGy/nC)" align="center">
                <template slot-scope="scope" v-if="scope.row.Nk !== null">
                  {{ parseFloat(scope.row.Nk).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- mu/rho -->
              <el-table-column prop="mu_rho" label="mu/rho" align="center">
                <template slot-scope="scope" v-if="scope.row.mu_rho !== null">
                  {{ parseFloat(scope.row.mu_rho).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- Bw -->
              <el-table-column
                prop="bw"
                label="Bw"
                align="center"
                style="background-color: #3a71a8"
              >
                <template slot-scope="scope" v-if="scope.row.bw !== null">
                  {{ parseFloat(scope.row.bw).toFixed(3) }}
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
                  v-if="scope.row.k_closed_cone !== null"
                >
                  {{ parseFloat(scope.row.k_closed_cone).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- ks -->
              <el-table-column prop="ks" label="ks" align="center">
                <template slot-scope="scope" v-if="scope.row.ks !== null">
                  {{ parseFloat(scope.row.ks).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- kpol -->
              <el-table-column prop="k_pol" label="kpol" align="center">
                <template slot-scope="scope" v-if="scope.row.k_pol !== null">
                  {{ parseFloat(scope.row.k_pol).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- Temp (C) -->
              <el-table-column prop="temp" label="Temp (C)" align="center">
                <template slot-scope="scope">
                  <el-input
                    size="mini"
                    v-model="scope.row.temp"
                    placeholder=""
                    class="manually"
                  ></el-input>
                </template>
              </el-table-column>

              <!-- Pressure (kPa) -->
              <el-table-column
                prop="pressure"
                label="Pressure (kPa)"
                align="center"
              >
                <template slot-scope="scope">
                  <el-input
                    size="mini"
                    v-model="scope.row.pressure"
                    placeholder=""
                    class="manually"
                  ></el-input>
                </template>
              </el-table-column>

              <!-- kTP -->
              <el-table-column prop="ktp" label="kTP" align="center">
                <template slot-scope="scope">
                  <p
                    style="font-weight: bold; color: #30b08f"
                    v-if="!isNaN(parseFloat(calculateKTP(scope.row)))"
                  >
                    {{ parseFloat(calculateKTP(scope.row)).toFixed(4) }}
                  </p>
                </template>
              </el-table-column>

              <!-- M -->
              <el-table-column label="M" align="center">
                <el-table-column prop="m_nc_1" label="(nC)" align="center">
                  <template slot-scope="scope">
                    <el-input
                      size="mini"
                      v-model="scope.row.m_nc_1"
                      placeholder=""
                      class="manually"
                    ></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="m_nc_2" label="(nC)" align="center">
                  <template slot-scope="scope">
                    <el-input
                      size="mini"
                      v-model="scope.row.m_nc_2"
                      placeholder=""
                      class="manually"
                    ></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="m_nc_3" label="(nC)" align="center">
                  <template slot-scope="scope">
                    <el-input
                      size="mini"
                      v-model="scope.row.m_nc_3"
                      placeholder=""
                      class="manually"
                    ></el-input>
                  </template>
                </el-table-column>
              </el-table-column>

              <!-- M avg(nC) -->
              <el-table-column prop="m_avg" label="M avg (nC)" align="center">
                <template slot-scope="scope">
                  <p
                    style="font-weight: bold; color: #30b08f"
                    v-if="!isNaN(parseFloat(calculateMAvg(scope.row)))"
                  >
                    {{ parseFloat(calculateMAvg(scope.row)).toFixed(3) }}
                  </p>
                </template>
              </el-table-column>

              <!-- Pstem, air -->
              <el-table-column
                prop="pstem_air"
                label="Pstem,air"
                align="center"
              >
                <template
                  slot-scope="scope"
                  v-if="scope.row.pstem_air !== null"
                >
                  {{ parseFloat(scope.row.pstem_air).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- ACDS output cGy/MU -->
              <el-table-column
                prop="acds_output"
                label="ACDS output cGy/MU"
                align="center"
              >
                <template slot-scope="scope">
                  <p
                    style="font-weight: bold; color: #30b08f"
                    v-if="
                      !isNaN(
                        parseFloat(
                          calculateACDSOutput(tableData[active], scope.row)
                        )
                      )
                    "
                  >
                    {{
                      parseFloat(
                        calculateACDSOutput(tableData[active], scope.row)
                      ).toFixed(3)
                    }}
                  </p>
                </template>
              </el-table-column>

              <!-- Facility output cGy/MU -->
              <el-table-column
                prop="fac_output"
                label="Facility output cGy/MU"
                align="center"
              >
                <template
                  slot-scope="scope"
                  v-if="!isNaN(parseFloat(scope.row.fac_output))"
                >
                  {{ parseFloat(scope.row.fac_output).toFixed(3) }}
                </template>
              </el-table-column>

              <!-- %Diff -->
              <el-table-column prop="cone_diff" label="%Diff" align="center">
                <template slot-scope="scope">
                  <p
                    style="font-weight: bold; color: #30b08f"
                    v-if="scope.row.acds_output > 0"
                  >
                    {{ (calculateOutputDiff(scope.row) * 100).toFixed(2) }}%
                  </p>
                </template>
              </el-table-column>

              <!-- MU/min -->
              <el-table-column prop="mu_min" label="MU/min" align="center">
              </el-table-column>
            </el-table>

            <!-- Cone Swift button -->
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
          </el-card>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "coneCalculation",
  props: {},
  data() {
    return {
      chamberSn: "",
      // data of first row left
      kelecLookupTable: {
        194: {
          1: 1.001,
          2: "",
        },
        195: {
          1: 1.0,
          2: "",
        },
        259: {
          1: 1.0,
          2: "",
        },
        175: {
          1: 1.0,
          2: "",
        },
        2226: {
          1: 0.999,
          2: "",
        },
        180307: {
          1: 1.0,
          2: "",
        },
        68964006: {
          1: 1.003,
          2: 1.002,
        },
        68964016: {
          1: 1.004,
          2: 1.003,
        },
        68964017: {
          1: 1.003,
          2: 1.003,
        },
        W230363002: {
          1: 0.999,
          2: 0.999,
        },
        W230363003: {
          1: 0.999,
          2: 0.999,
        },
        "24793 6005": {
          1: "",
          2: "",
        },
      },
      electrometerSNOption: [
        "194",
        "195",
        "259",
        "175",
        "2226",
        "180307",
        "68964006",
        "68964016",
        "68964017",
        "W230363002",
        "W230363003",
        "24793 6005",
      ],
      channelOption: [1, 2],
      webLineRangeOption: ["High", "Medium", "Low"],

      // data of first row right
      chamberLookupTable: {
        3587: {
          name: "PTW 30013",
          weblineBiasV: "-300V (CEP)",
        },
        5447: {
          name: "PTW 30013",
          weblineBiasV: "+400 V (CEN)",
        },
        5448: {
          name: "PTW 30013",
          weblineBiasV: "+400 V (CEN)",
        },
        1508: {
          name: "PTW 23342",
          weblineBiasV: "-300V (CEP)",
        },
        858: {
          name: "PTW 23344",
          weblineBiasV: "-300 V (CEP)",
        },
      },
      chamberOption: ["3587", "5447", "5448", "1508", "858"],
      allTableData: [
        {
          cone_name: null,
          basic_data: {
            // data with blue background
            kelec: null,
            webline_bias_v: "",
            electron_contamination_check: false,
            chamber_name: "",
            ssd: 30,
            dose_type: "cGy/MU",

            // data with yellow background
            electrometer_sn: "194",
            channel: 1,
            webline_range: "High",
            meas_ID: "",
            chamber_sn: "3587",
            time: "",
            standoff_1: 4.5,

            // data with green background
            isl_corr: "",
            standoff_2: "",

            // electron contamination table
            electron_contamination_data: [
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
            ],
          },

          beams_data: [
            {
              // data with blue background
              beam_no: null,
              kv: 60,
              Nk: 48.461,
              mu_rho: 1.018,
              bw: 1.18,
              k_closed_cone: 1.0,
              ks: null,
              k_pol: null,
              pstem_air: 1.0,
              fac_output: 1.004,
              mu_min: null, // last column not sure

              // data with yellow background
              mu: 200,
              temp: 20.24,
              pressure: 102.26,
              m_nc_1: 33.01,
              m_nc_2: 32.94,
              m_nc_3: null,

              // data with green background
              ktp: null,
              m_avg: null,
              acds_output: null,
              cone_diff: null,
            },
          ],
        },
      ],

      // table data for global --> comming from caseSpecification index
      tableData: [
        {
          cone_name: null,
          basic_data: {
            // data with blue background
            kelec: null,
            webline_bias_v: "",
            electron_contamination_check: false,
            chamber_name: "",
            ssd: 30,
            dose_type: "cGy/MU",

            // data with yellow background
            electrometer_sn: "194",
            channel: 1,
            webline_range: "High",
            meas_ID: "",
            chamber_sn: "3587",
            time: "",
            standoff_1: 4.5,

            // data with green background
            isl_corr: "",
            standoff_2: "",

            // electron contamination table
            electron_contamination_data: [
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
            ],
          },

          beams_data: [
            {
              // data with blue background
              beam_no: null,
              kv: 60,
              Nk: 48.461,
              mu_rho: 1.018,
              bw: 1.18,
              k_closed_cone: 1.0,
              ks: null,
              k_pol: null,
              pstem_air: 1.0,
              fac_output: 1.004,
              mu_min: null, // last column not sure

              // data with yellow background
              mu: 200,
              temp: 20.24,
              pressure: 102.26,
              m_nc_1: 33.01,
              m_nc_2: 32.94,
              m_nc_3: null,

              // data with green background
              ktp: null,
              m_avg: null,
              acds_output: null,
              cone_diff: null,
            },
          ],
        },
      ],

      active: 0,
      // kk1db: null,
      // kk2db: null,
    };
  },
  computed: {
    getChamberSn() {
      return this.$store.getters["chamber/getChamberSn"];
    },
  },
  watch: {
    chamberSn: function (val, oldVal) {
      this.$store.dispatch("chamber/setChamberSn", val);
    },
  },
  mounted() {
    this.calculateISLCorr();
    this.calculateStandoff();
  },
  methods: {
    //synchronize ks and kpol
    synchronizeKK() {
      this.$emit("sync-kv-and-kpol");
    },

    next() {
      if (this.active++ >= 2) this.active = 2;
    },
    last() {
      if (this.active-- < 1) this.active = 0;
    },

    // mounted hook methods x2
    calculateISLCorr() {
      for (var cone of this.tableData) {
        cone.basic_data.isl_corr = Math.pow(
          (1 + 0.1 / cone.basic_data.ssd) / (1 + 0.1 / 30),
          2
        );
      }
      return this.tableData[this.active].basic_data.isl_corr;
    },
    calculateStandoff() {
      for (var cone of this.tableData) {
        cone.basic_data.standoff_2 = Math.pow(
          (10 * cone.basic_data.ssd + parseFloat(cone.basic_data.standoff_1)) /
            (10 * cone.basic_data.ssd),
          2
        );
      }
    },

    calculateWithAvg(data) {
      data.with_avg = this.calculateAvg(
        data.with_m_1,
        data.with_m_2,
        data.with_m_3
      );
      return data.with_avg;
    },
    calculateWithoutAvg(data) {
      data.without_avg = this.calculateAvg(
        data.without_m_1,
        data.without_m_2,
        data.without_m_3
      );
      return data.without_avg;
    },
    calculateMAvg(data) {
      console.log(data);
      data.m_avg = this.calculateAvg(data.m_nc_1, data.m_nc_2, data.m_nc_3);
      return data.m_avg;
    },
    calculateAvg(a, b, c) {
      let items = [a, b, c];
      let sum = 0;
      let count = 0;
      for (let i = 0; i < items.length; ++i) {
        if (items[i] !== "" && items[i] != null) {
          sum += parseFloat(items[i]);
          count += 1;
        }
      }
      if (count === 0) {
        return "";
      }

      return sum / count;
    },
    calculateKelec(table) {
      table.basic_data.kelec =
        this.kelecLookupTable[table.basic_data.electrometer_sn][
          table.basic_data.channel
        ];
      return table.basic_data.kelec;
    },
    calculateDiff(data) {
      data.diff = data.without_avg / data.with_avg - 1;
      return data.diff;
    },
    calculateOutputDiff(data) {
      data.cone_diff = (data.fac_output - data.acds_output) / data.acds_output;
      return data.cone_diff;
    },
    calculateKTP(data) {
      data.ktp =
        ((parseFloat(data.temp) + 273.15) / 293.15) *
        (101.325 / parseFloat(data.pressure));
      return data.ktp;
    },
    calculateACDSOutput(table, data) {
      let ISL = table.basic_data.isl_corr;
      let Standoff = table.basic_data.standoff_1;
      let Ssd = table.basic_data.ssd;
      if (Standoff === "") {
         Standoff = 1;
       } else {
         Standoff = Math.pow(
           (10 * Ssd + table.basic_data.standoff_1) / (10 * Ssd),
           2
         );
       }
      let Nk = data.Nk;
      let Murho = data.mu_rho;
      let Bw = data.bw;
      let kClosedCone = data.k_closed_cone;
      let Ks = data.ks;
      if (Ks === "" || Ks === null) {
        Ks = 1;
      }
      let Kelec = table.basic_data.kelec;
      let Kpol = data.k_pol;
      let KTP = data.ktp;
      let Mavg = data.m_avg;
      let Pstem = data.pstem_air;
      let F = null;
      if (table.basic_data.dose_type === "Gy/min") {
        F = 60 / data.mu;
      } else if (table.basic_data.dose_type === "cGy/MU") {
        F = 100 / data.mu;
      }
      data.acds_output =
        ((ISL *
          Standoff *
          Nk *
          Murho *
          Bw *
          kClosedCone *
          Ks *
          Kelec *
          Kpol *
          KTP *
          Mavg) /
          1000) *
        F *
        Pstem;
      return data.acds_output;
    },
  },
};
</script>

<style scoped>
.box-card {
  height: 375px;
}

.manually >>> input {
  font-weight: bold;
  color: coral;
  font-size: larger;
}
.fetched >>> input {
  font-weight: bold;
  color: #409eff;
  font-size: larger;
}
.coneCalculation >>> table th {
  font-weight: bold;
  color: black;
  font-size: larger;
}
.coneCalculation >>> table td {
  font-weight: bold;
  color: #409eff;
  font-size: larger;
}
.coneCalculation >>> table {
  white-space: normal;
  word-break: break-word;
}
</style>
