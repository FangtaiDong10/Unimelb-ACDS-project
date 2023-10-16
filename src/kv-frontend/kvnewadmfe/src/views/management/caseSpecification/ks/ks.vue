<template>
  <div>
    <el-button
      type="primary"
      style="margin-bottom: 10px"
      @click="saveKsAndKpol1"
      >Save Table1</el-button
    >
    <el-button
      type="primary"
      style="margin-bottom: 10px"
      @click="saveKsAndKpol2"
      >Save Table2</el-button
    >
    <el-button type="danger" style="margin-bottom: 10px" @click="clearAll"
      >Refresh Page</el-button
    >
    <el-table
      v-for="table in tableData"
      :key="table.id"
      :data="table.beams_data"
      style="width: 100%; padding-bottom: 20px"
      class="overviewTable"
    >
      <!-- Chamber -->
      <el-table-column align="center">
        <template slot="header" slot-scope="{}">
          Chamber:
          <el-select v-model="table.chamber" placeholder="">
            <el-option
              v-for="item in chamberOptions"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
        </template>
      </el-table-column>

      <el-table-column prop="beamId" label="Beam No." align="center">
      </el-table-column>
      <el-table-column prop="energy" label="Energy" align="center">
      </el-table-column>
      <el-table-column label="Measured HVL" align="center">
        <el-table-column prop="measuredHvlMmAl" label="mm Al" align="center">
        </el-table-column>
        <el-table-column prop="measuredHvlMmCu" label="mm Cu" align="center">
        </el-table-column>
      </el-table-column>
      <el-table-column label="TP during dose measurement" align="center">
        <el-table-column prop="temperature" label="T(°C)" align="center">
          <template slot-scope="scope">
            <el-input
              class="manually"
              v-model="scope.row.temperature"
              placeholder=""
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="pressure" label="P (kPa)" align="center">
          <template slot-scope="scope">
            <el-input
              class="manually"
              v-model="scope.row.pressure"
              placeholder=""
            ></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="ktp" label="Ktp" align="center">
          <template slot-scope="scope">
            <p
              style="font-weight: bold; color: #30b08f"
              v-if="!isNaN(parseFloat(calculateKTP(scope.row)))"
            >
              {{ parseFloat(calculateKTP(scope.row)).toFixed(4) }}
            </p>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="Dose measurement" align="center">
        <el-table-column align="center">
          <template slot="header" slot-scope="{}">
            <span>V</span>
            <el-input
              class="manually"
              v-model="table.dose_measurement"
            ></el-input>
          </template>
          <el-table-column prop="dose_v1" label="(nC)" align="center">
            <template slot-scope="scope">
              <el-input
                class="manually"
                v-model="scope.row.dose_v1"
                placeholder=""
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="dose_v2" label="(nC)" align="center">
            <template slot-scope="scope">
              <el-input
                class="manually"
                v-model="scope.row.dose_v2"
                placeholder=""
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="dose_v3" label="(nC)" align="center">
            <template slot-scope="scope">
              <el-input
                class="manually"
                v-model="scope.row.dose_v3"
                placeholder=""
              ></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="m1" label="M1" align="center">
          <template slot-scope="scope">
            <p
              style="font-weight: bold; color: #30b08f"
              v-if="!isNaN(parseFloat(calculateM1(scope.row)))"
            >
              {{ parseFloat(calculateM1(scope.row)).toFixed(3) }}
            </p>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="Recombination measurement" align="center">
        <el-table-column align="center">
          <template slot="header" slot-scope="{}">
            <span>V</span><br />
            <p style="font-weight: bold; color: #30b08f">
              {{
                parseFloat(calculateRecombinationMeasurement(table)).toFixed(3)
              }}
            </p>
          </template>
          <el-table-column prop="recombination_v1" label="(nC)" align="center">
            <template slot-scope="scope">
              <el-input
                class="manually"
                v-model="scope.row.recombination_v1"
                placeholder=""
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="recombination_v2" label="(nC)" align="center">
            <template slot-scope="scope">
              <el-input
                class="manually"
                v-model="scope.row.recombination_v2"
                placeholder=""
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="recombination_v3" label="(nC)" align="center">
            <template slot-scope="scope">
              <el-input
                class="manually"
                v-model="scope.row.recombination_v3"
                placeholder=""
              ></el-input>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="m2" label="M2" align="center">
          <template slot-scope="scope">
            <p
              style="font-weight: bold; color: #30b08f"
              v-if="!isNaN(parseFloat(calculateM2(scope.row)))"
            >
              {{ parseFloat(calculateM2(scope.row)).toFixed(3) }}
            </p>
          </template>
        </el-table-column>
      </el-table-column>

      <!-- ks -->
      <el-table-column prop="ks" label="Ks" align="center">
        <template slot-scope="scope">
          <p style="font-weight: bold; color: #30b08f">
            {{ parseFloat(calculateKs(table, scope.row)).toFixed(3) }}
          </p>
        </template>
      </el-table-column>

      <!-- kpol -->
      <el-table-column prop="kpol" label="kpol" align="center">
        <template slot-scope="scope">
          <el-input
            class="manually"
            v-model="scope.row.kpol"
            placeholder=""
            @change="updateKsAndKpol"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getKs } from "@/api/ks";
export default {
  name: "ks",
  props: {
    updateKsAndKpol: {
      type: Function,
    },
  },
  data() {
    return {
      chamberOptions: ["3587", "5447", "5448", "1508", "858"],
      active: 0,
      kkdatabase1: null,
      kkdatabase2: null,
      t1: null,
      t2: null,
      version: 1,
      tableData: [
        {
          id: 1,
          chamber: "858",
          dose_measurement: 400,
          recombination_measurement: 100, // calculated by dose_measurement
          beams_data: [
            {
              // data within blue background
              beamId: "Filter 1",
              energy: 60,
              measuredHvlMmAl: 1.268,
              measuredHvlMmCu: null,
              // data within yellow background
              temperature: null,
              pressure: null,
              ktp: null,
              dose_v1: null,
              dose_v2: null,
              dose_v3: null,
              recombination_v1: null,
              recombination_v2: null,
              recombination_v3: null,
              kpol: null,
              // dara within green background
              m1: null,
              m2: null,
              ks: null,
            },
          ],
        },

        {
          id: 2,
          chamber: "5447",
          dose_measurement: 400,
          recombination_measurement: 100, // calculated by dose_measurement
          beams_data: [
            {
              // data within blue background
              beamId: null,
              energy: null,
              measuredHvlMmAl: null,
              measuredHvlMmCu: null,
              // data within yellow background
              temperature: null,
              pressure: null,
              ktp: null,
              dose_v1: null,
              dose_v2: null,
              dose_v3: null,
              recombination_v1: null,
              recombination_v2: null,
              recombination_v3: null,
              kpol: null,
              // dara within green background
              m1: null,
              m2: null,
              ks: null,
            },
          ],
        },
      ],
    };
  },
  methods: {
    saveKsAndKpol1() {
      // create db --> KK
      if (this.kkdatabase1) {
      }

      let kskpol = window.indexedDB.open("KK1", this.version);

      // console.log(kskpol);

      // handler success and get the KK database
      kskpol.onsuccess = (event) => {
        this.kkdatabase1 = event.target.result;
        console.log("successfully created temp db");
      };

      kskpol.onerror = (event) => {
        console.log("Some Error!");
      };

      // upgrade t1
      kskpol.onupgradeneeded = (event) => {
        this.kkdatabase1 = event.target.result;

        // create tables --> (t1, t2)

        this.t1 = this.kkdatabase1.createObjectStore(
          `${this.tableData[0].chamber}`,
          { keyPath: "id" }
        );

        // storing t1
        this.t1.transaction.oncomplete = (event) => {
          let t1Store = this.kkdatabase1
            .transaction([`${this.tableData[0].chamber}`], "readwrite")
            .objectStore(`${this.tableData[0].chamber}`);
          console.log("table1 ok");

          // get all following chambers beam data
          for (const filter of this.tableData[0].beams_data) {
            const newObject = new Object();
            Object.keys(filter).forEach((key) => {
              if (key === "beamId") {
                newObject["id"] = filter[key];
              }
              if (key === "kpol") {
                newObject["kpol"] = filter[key];
              }
              if (key === "ks") {
                newObject["ks"] = filter[key];
              }
            });
            t1Store.add(newObject);
          }
        };
      };
      // this.version += 1;
    },
    saveKsAndKpol2() {
      // console.log(this.version);
      if (this.kkdatabase2) {
      }

      let kskpol = window.indexedDB.open("KK2", this.version);

      kskpol.onsuccess = (event) => {
        this.kkdatabase2 = event.target.result;
        console.log("successfully created temp db");
      };

      kskpol.onerror = (event) => {
        // console.log("Some Error!");
      };

      // upgrade t2
      kskpol.onupgradeneeded = (event) => {
        this.kkdatabase2 = event.target.result;

        // create tables --> (t1, t2)

        this.t2 = this.kkdatabase2.createObjectStore(
          `${this.tableData[1].chamber}`,
          { keyPath: "id" }
        );
        // console.log(this.t2);

        // storing t2
        this.t2.transaction.oncomplete = (event) => {
          let t2Store = this.kkdatabase2
            .transaction([`${this.tableData[1].chamber}`], "readwrite")
            .objectStore(`${this.tableData[1].chamber}`);
          console.log("table2 ok");

          // get all following chambers beam data
          for (const filter of this.tableData[1].beams_data) {
            const newObject = new Object();
            Object.keys(filter).forEach((key) => {
              if (key === "beamId") {
                newObject["id"] = filter[key];
              }
              if (key === "kpol") {
                newObject["kpol"] = filter[key];
              }
              if (key === "ks") {
                newObject["ks"] = filter[key];
              }
            });
            t2Store.add(newObject);
          }
        };

        // this.version += 1;
      };
    },
    testing() {
      const Store = this.kkdatabase1
        .transaction("table1858", "readwrite")
        .objectStore("table1858");
      const request = Store.get("Filter 1");
      console.log(request);
    },
    clearAll(){
      location.reload()
    },
    
    next() {
      if (this.active++ >= 2) this.active = 2;
    },
    last() {
      if (this.active-- < 1) this.active = 0;
    },
    calculateRecombinationMeasurement(table) {
      let dose = table.dose_measurement;
      table.recombination_measurement = (dose / Math.abs(dose)) * 100;
      return (dose / Math.abs(dose)) * 100;
    },
    calculateM1(data) {
      let items = [data.dose_v1, data.dose_v2, data.dose_v3];
      let sum = 0;
      let count = 0;
      for (let i = 0; i < items.length; ++i) {
        if (items[i] !== "" && items[i] != null) {
          sum += parseFloat(items[i]);
          count += 1;
        }
      }
      if (count === 0) {
        data.m1 = null;
        return "";
      }
      data.m1 = sum / count;
      return sum / count;
    },
    calculateM2(data) {
      let items = [
        data.recombination_v1,
        data.recombination_v2,
        data.recombination_v3,
      ];
      let sum = 0;
      let count = 0;
      for (let i = 0; i < items.length; ++i) {
        if (items[i] !== "" && items[i] != null) {
          sum += parseFloat(items[i]);
          count += 1;
        }
      }
      if (count === 0) {
        data.m2 = null;
        return "";
      }
      data.m2 = sum / count;
      return (sum / count).toFixed(3);
    },
    calculateKs(table, data) {
      let v1 = table.dose_measurement;
      let v2 = table.recombination_measurement;
      let m1 = this.calculateM1(data);
      let m2 = this.calculateM2(data);

      if (v1 === "" || v2 === "" || m1 === "" || m2 === "") {
        data.ks = 1.0;
        this.updateKsAndKpol();
        return (1.0).toFixed(3);
      }
      data.ks = (1 - Math.pow(v1 / v2, 2)) / (m1 / m2 - Math.pow(v1 / v2, 2));
      this.updateKsAndKpol();
      return (
        (1 - Math.pow(v1 / v2, 2)) /
        (m1 / m2 - Math.pow(v1 / v2, 2))
      ).toFixed(3);
    },
    calculateKTP(data) {
      let res =
        ((parseFloat(data.temperature) + 273.15) / 293.15) *
        (101.325 / parseFloat(data.pressure));
      data.ktp = res;
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

    async getPageData() {
      const res = await getKs(this.$route.params.auditNumber);
      if (res.success) {
        this.tableData[0].beams_data = [];
        this.tableData[1].beams_data = [];
        for (let each of res.data) {
          this.tableData[0].beams_data.push(JSON.parse(JSON.stringify(each)));
          this.tableData[1].beams_data.push(JSON.parse(JSON.stringify(each)));
        }
        for (let each of this.tableData) {
          each.beams_data.sort(function (first, second) {
            return first.energy - second.energy;
          });
        }
      } else {
        this.getPageData();
      }
    },
  },

  mounted() {
    window.indexedDB.deleteDatabase("KK2");
    window.indexedDB.deleteDatabase("KK1");
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
  font-size: larger;
}
.overviewTable >>> table td {
  font-weight: bold;
  color: #409eff;
  font-size: larger;
}
</style>
