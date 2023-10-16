<template>
  <div>
    <el-form ref="form" label-width="120px">
      <el-form-item label="Date">
        <el-select
          v-model="date"
          placeholder="Please select"
          @change="changeDate"
        >
          <el-option
            v-for="item in dateOptions"
            :key="item"
            :label="item"
            :value="item"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Type">
        <el-select
          v-model="type"
          placeholder="Please select"
          @change="changeType"
        >
          <el-option
            v-for="item in typeOptions[date]"
            :key="item"
            :label="item"
            :value="item"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="SSD">
        <el-select
          v-model="ssd"
          placeholder="Please select"
          v-if="ssdOptions[date]"
        >
          <el-option
            v-for="item in ssdOptions[date][type]"
            :key="item"
            :label="item"
            :value="item"
          >
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-table
      v-if="tableData[date]"
      :data="tableData[date][type][ssd]"
      style="width: 100%; margin-top: 12px"
      class="bw"
    >
      <el-table-column
        label="index"
        type="index"
        width="80"
        align="center"
      ></el-table-column>
      <!-- TODO: The prop attribute name is BAD -->
      <el-table-column prop="hvl" label="Hvl" width="100" align="center">
      </el-table-column>
      <el-table-column prop="1" label="1" width="100" align="center">
      </el-table-column>
      <el-table-column prop="2" label="2" width="100" align="center">
      </el-table-column>
      <el-table-column prop="3" label="3" width="100" align="center">
      </el-table-column>
      <el-table-column prop="5" label="5" width="100" align="center">
      </el-table-column>
      <el-table-column prop="10" label="10" width="100" align="center">
      </el-table-column>
      <el-table-column prop="15" label="15" width="100" align="center">
      </el-table-column>
      <el-table-column prop="20" label="20" width="100" align="center">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { lookup } from "../../../api/lookup";
export default {
  name: "bw",
  created() {
    this.getPageData();
  },
  methods: {
    changeDate() {
      this.type = this.typeOptions[this.date][0];
      this.changeType();
    },
    changeType() {
      this.ssd = this.ssdOptions[this.date][this.type][0];
    },
    async getPageData() {
      const res = await lookup("bw");
      let table = {};
      let newTable = {};
      let data = res.data;
      // console.log(data) // TODO: Debug command, remove it!
      for (let row of data) {
        if (!(row.dateUpdated in table)) {
          table[row.dateUpdated] = {};
          newTable[row.dateUpdated] = {};
        }
        if (!(row.type in table[row.dateUpdated])) {
          table[row.dateUpdated][row.type] = {};
          newTable[row.dateUpdated][row.type] = {};
        }
        if (!(row.ssd in table[row.dateUpdated][row.type])) {
          table[row.dateUpdated][row.type][row.ssd] = {};
          newTable[row.dateUpdated][row.type][row.ssd] = [];
        }
        if (row.type === "al") {
          if (!(row.hvlAl in table[row.dateUpdated][row.type][row.ssd])) {
            table[row.dateUpdated][row.type][row.ssd][row.hvlAl] = [];
          }
          table[row.dateUpdated][row.type][row.ssd][row.hvlAl].push(row);
        } else if (row.type === "cu") {
          if (!(row.hvlCu in table[row.dateUpdated][row.type][row.ssd])) {
            table[row.dateUpdated][row.type][row.ssd][row.hvlCu] = [];
          }
          table[row.dateUpdated][row.type][row.ssd][row.hvlCu].push(row);
        }
      }
      for (var tempDate in table) {
        for (var type in table[tempDate]) {
          for (var ssd in table[tempDate][type]) {
            newTable[tempDate][type][ssd] = [];
            for (var hvl in table[tempDate][type][ssd]) {
              let item = {
                hvl: hvl,
              };
              for (var each of table[tempDate][type][ssd][hvl]) {
                item[each.diameter] = each.bw;
              }
              newTable[tempDate][type][ssd].push(item);
            }
          }
        }
      }
      this.tableData = newTable;

      this.dateOptions = Object.keys(table);
      this.date = this.dateOptions[0];
      for (var d of this.dateOptions) {
        this.typeOptions[d] = Object.keys(table[d]);
        this.ssdOptions[d] = {};
        for (var t of this.typeOptions[d]) {
          this.ssdOptions[d][t] = Object.keys(table[d][t]);
        }
      }
      this.type = this.typeOptions[this.date][0];
      this.ssd = this.ssdOptions[this.date][this.type][0];
    },
  },
  data() {
    return {
      date: "",
      type: "",
      ssd: "",
      dateOptions: [],
      typeOptions: {},
      ssdOptions: {},
      tableData: {},
    };
  },
};
</script>

<style scoped>
.bw >>> table th {
  font-weight: bold;
  color: black;
  font-size: larger;
}
.bw >>> table td {
  font-weight: bold;
  color: #409eff;
  font-size: larger;
}
.bw >>> table {
  white-space: normal;
  word-break: break-word;
}
</style>
