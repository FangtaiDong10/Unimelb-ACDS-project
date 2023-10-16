<template>
  <div>
    <el-form ref="form" label-width="120px">
      <el-form-item label="Date">
        <el-select v-model="date" placeholder="Please select">
          <el-option
            v-for="item in dateOptions"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Chamber SN" v-if="chambersOptions[date]">
        <el-select v-model="chamber" placeholder="Please select">
          <el-option
            v-for="item in chambersOptions[date]"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-table
      v-if ="tableData[date]"
      :data="tableData[date][chamber]"
      style="width: 100%; margin-top: 12px"
      class="farmer">
      <el-table-column label="index" type="index" width="80" align="center"></el-table-column>
      <el-table-column
        prop="beamFarmerId"
        label="beam Id"
        width="100"
        align=center>
      </el-table-column>
      <el-table-column
        prop="kv"
        label="Nominal kVp"
        width="150"
        align=center>
      </el-table-column>
      <el-table-column
        prop="hvlMeasuredMmCu"
        label="Hvl Measured (mm cu)"
        width="250"
        align=center>
      </el-table-column>
      <el-table-column
        prop="hvlMeasuredMmAl"
        label="Hvl Measured (mm al)"
        width="250"
        align=center>
      </el-table-column>
      <el-table-column
        prop="nkValue"
        label="NK (mGy/nC)"
        width="250"
        align=center>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { lookup } from '../../../api/lookup'
  export default {
    name: 'farmer',
    created () {
      this.getPageData()
    },
    methods: {
      async getPageData () {
        const res = await lookup('farmer')
        let table = {}
        let data = res.data
        for (let row of data) {
          if (!(row.dateUpdated in table)) {
            table[row.dateUpdated] = {}
          }
          if (!(row.chamberSn in table[row.dateUpdated])) {
            table[row.dateUpdated][row.chamberSn] = []
          }
          table[row.dateUpdated][row.chamberSn].push(row)
        }
        for (var date in table) {
          for (var chamber in table[date]) {
            table[date][chamber].sort(function (first, second) {
              return first.kv - second.kv
            })
          }
        }

        this.tableData = table

        this.dateOptions = Object.keys(table)
        this.date = this.dateOptions[0]
        for (var each of this.dateOptions) {
          this.chambersOptions[each] = Object.keys(table[each])
        }
        this.chamber = this.chambersOptions[this.date][0]
      }
    },
    data () {
      return {
        date: '',
        chamber: '',
        dateOptions: [],
        chambersOptions: {},
        tableData: {
        }
      }
    }
  }
</script>

<style scoped>
  .farmer >>> table th {
    font-weight: bold;
    color: black;
    font-size: larger;
  }
  .farmer >>> table td {
    font-weight: bold;
    color: #409EFF;
    font-size: larger;
  }
  .farmer >>> table {
    white-space: normal;
    word-break: break-word;
  }
</style>
