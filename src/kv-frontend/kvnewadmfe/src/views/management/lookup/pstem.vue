<template>
  <div>
    <el-form ref="form" label-width="150px">
      <el-form-item label="Date">
        <el-select v-model="date" placeholder="">
          <el-option
            v-for="item in dateOptions"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="Diameter" v-if ="chambersOptions[date]">
        <el-select v-model="chamber" placeholder="">
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
      class="planeParallel">
      <el-table-column label="index" type="index" width="80" align="center"></el-table-column>
      <el-table-column
        prop="beamPpChamberId"
        label="beam pp chamber id"
        width="200"
        align=center>
      </el-table-column>
      <el-table-column
        prop="hvlMeasuredMmAl"
        label="Hvl Measured (mm al)"
        width="250"
        align=center>
      </el-table-column>
      <el-table-column
        prop="pstemOption"
        label="Pstem Option"
        width="250"
        align=center>
      </el-table-column>
      <el-table-column
        prop="pstemValue"
        label="Pstem Value"
        width="250"
        align=center>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { lookup } from '../../../api/lookup'
  export default {
    name: 'pstem',
    created () {
      this.getPageData()
    },
    methods: {
      async getPageData () {
        const res = await lookup('pstem')
        let table = {}
        let data = res.data
        for (let row of data) {
          if (!(row.dateUpdated in table)) {
            table[row.dateUpdated] = {}
          }
          if (!(row.diameter in table[row.dateUpdated])) {
            table[row.dateUpdated][row.diameter] = []
          }
          table[row.dateUpdated][row.diameter].push(row)
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
  .planeParallel >>> table th {
    font-weight: bold;
    color: black;
    font-size: larger;
  }
  .planeParallel >>> table td {
    font-weight: bold;
    color: #409EFF;
    font-size: larger;
  }
  .planeParallel >>> table {
    white-space: normal;
    word-break: break-word;
  }
</style>
