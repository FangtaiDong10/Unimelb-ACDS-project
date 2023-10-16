<template>
  <div>
    <el-table :data="tableData" style="width: 100%; margin-top: 12px">
      <el-table-column prop="attr" label="" width="100" align="center">
      </el-table-column>
      <el-table-column
        prop="fac_input"
        label="Facility"
        width="85"
        align="center"
      >
        <template slot-scope="scope">
          <el-input
            size="mini"
            v-model="scope.row.fac_input"
            placeholder=""
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="acds_input" label="ACDS" width="85" align="center">
        <template slot-scope="scope">
          <el-input
            size="mini"
            v-model="scope.row.acds_input"
            placeholder=""
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="diff" label="% Diff" width="85" align="center">
        <template slot-scope="scope">
          {{ calculateDiff(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="uncertainty"
        label="% Uncertainty"
        width="120"
        align="center"
      >
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "comparision",
  data() {
    return {
      tableData: [
        {
          attr: "Temp. (°C)",
          fac_input: "",
          acds_input: "",
          diff: "",
          uncertainty: "",
        },
        {
          attr: "Pres. (kPa)",
          fac_input: "",
          acds_input: "",
          diff: "",
          uncertainty: "",
        },
      ],
    };
  },
  methods: {
    calculateDiff(data) {
      if (data.attr === "Temp. (°C)") {
        if (data.fac_input === "" || data.acds_input === "") {
          return null;
        }
        return (
          (parseFloat(data.fac_input) + 273.15) /
            (parseFloat(data.acds_input) + 273.15) -
          1
        ).toFixed(3);
      } else {
        if (data.fac_input === "" || data.acds_input === "") {
          return null;
        }
        return (
          parseFloat(data.fac_input) / parseFloat(data.acds_input) -
          1
        ).toFixed(3);
      }
    },
  },
};
</script>

<style scoped>

</style>
