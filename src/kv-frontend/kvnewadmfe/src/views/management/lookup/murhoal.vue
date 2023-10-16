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
    </el-form>
    <el-table
      :data="tableData[date]"
      style="width: 100%; margin-top: 12px"
      class="murhoal">
      <el-table-column label="index" type="index" width="80" align="center"></el-table-column>
      <el-table-column
        prop="hvlAl"
        label="hvl al"
        width="200"
        align=center>
      </el-table-column>
      <el-table-column
        prop="murho"
        label="murho"
        width="200"
        align=center>
      </el-table-column>
      <el-table-column
        prop="murhoAlId"
        label="murho al id"
        width="200"
        align=center>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { lookup } from '../../../api/lookup'
    export default {
      name: 'murhoal',
      created () {
        this.getPageData()
      },
      methods: {
        async getPageData () {
          const res = await lookup('murho-al')
          let table = {}
          let data = res.data
          for (let row of data) {
            if (!(row.dateUpdated in table)) {
              table[row.dateUpdated] = []
            }
            table[row.dateUpdated].push(row)
          }
          this.tableData = table
          this.dateOptions = Object.keys(table)
          this.date = this.dateOptions[0]
        }
      },
      data () {
        return {
          date: '',
          dateOptions: [],
          tableData: {
          }
        }
      }
    }
</script>

<style scoped>
  .murhoal >>> table th {
    font-weight: bold;
    color: black;
    font-size: larger;
  }
  .murhoal >>> table td {
    font-weight: bold;
    color: #409EFF;
    font-size: larger;
  }
  .murhoal >>> table {
    white-space: normal;
    word-break: break-word;
  }
</style>
