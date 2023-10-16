<template>
  <div>
    <el-button type="primary" @click.prevent="addTable()">Add table</el-button>
    <el-card v-for="table in tables">
      <h1 align="center" style="padding-top: 50px"></h1>
      <br/>
      <el-descriptions
        direction="vertical"
        :column="11"
        border>
        <el-descriptions-item label="Chamber">
          <el-input v-model = table.chamber></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="kvp">
          <el-input v-model = table.kvp></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="fac_hvl">
          <el-input v-model = table.fac_hvl @change="calculateHvlValue1(table)"></el-input>
        </el-descriptions-item>
      </el-descriptions>
      <el-table :data="table.data" style="width: 100%;" border class="hvl">
        <el-table-column
          prop="hvl_value"
          label="hvl_value"
          style="color: #3A71A8 "
          align="center">
          <template slot="header" slot-scope= "{}">
            <el-select v-model="table.hvl_type" placeholder="please select">
              <el-option
                v-for="item in hvl_type"
                :key="item"
                :label="item"
                :value="item">
              </el-option>
            </el-select>
          </template>
          <template slot-scope="scope">
            <el-input v-model="scope.row.hvl_value" placeholder="" class="manually" v-if="table.data.indexOf(scope.row) > 0"></el-input>
            <p v-else>{{scope.row.hvl_value}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="m1"
          label="m1"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.m1" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="m2"
          label="m2"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.m2" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="m3"
          label="m3"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.m3" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="M (nC)"
          prop="m_avg"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <p style="font-weight: bold; color: #30B08F" v-if="!isNaN(parseFloat(calculateMavg(scope.row)))">{{parseFloat(calculateMavg(scope.row)).toFixed(6)}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="ratio"
          label="Ratio"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <p v-if="!isNaN(parseFloat(calculateRatio(table, scope.row)))">{{parseFloat(calculateRatio(table, scope.row)).toFixed(3)}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="abs_diff"
          label="ABS diff"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <p v-if="!isNaN(parseFloat(calculateABSDiff(scope.row)))">{{parseFloat(calculateABSDiff(scope.row) * 100).toFixed(3)}}%</p>
          </template>
        </el-table-column>
        <el-table-column
          label="Operation"
          style="color: #3A71A8 "
          align="center">
          <template slot="header">
            <el-button type="primary" size="mini" @click.prevent="addTableData(table)">Add</el-button>
          </template>

          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click.prevent="removeTableData(table, scope.row)" v-if="table.data.indexOf(scope.row) > 0">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-descriptions
        direction="vertical"
        :column="4"
        border>
        <el-descriptions-item label="Ratio 1">
          <p v-if="!isNaN(parseFloat(calculateRatio1(table)))">{{parseFloat(calculateRatio1(table)).toFixed(8)}}</p>
        </el-descriptions-item>
        <el-descriptions-item label="Ratio 2">
          <p v-if="!isNaN(parseFloat(table.ratio2))">{{parseFloat(table.ratio2).toFixed(8)}}</p>
        </el-descriptions-item>
        <el-descriptions-item label="Filt. P1">
          <p v-if="!isNaN(parseFloat(table.filtP1))">{{table.filtP1}}</p>
        </el-descriptions-item>
        <el-descriptions-item label="Filt. P2">
          <p v-if="!isNaN(parseFloat(table.filtP2))">{{table.filtP2}}</p>
        </el-descriptions-item>
      </el-descriptions>
      <el-descriptions
        direction="vertical"
        :column="4"
        border>
        <el-descriptions-item :label="'HVL ' + table.hvl_type">
          <p v-if="!isNaN(parseFloat(calculateHvlValue1(table)))">{{parseFloat(calculateHvlValue1(table)).toFixed(2)}}</p>
        </el-descriptions-item>
        <el-descriptions-item label="Diff">
          <p v-if="!isNaN(parseFloat(table.hvl_diff1))">{{parseFloat(table.hvl_diff1 * 100).toFixed(2)}}%</p>
        </el-descriptions-item>
        <el-descriptions-item :label="'HVL ' + table.hvl_type">
          <p v-if="!isNaN(parseFloat(table.hvl_value2))">{{parseFloat(table.hvl_value2).toFixed(2)}}</p>
        </el-descriptions-item>
        <el-descriptions-item label="Diff">
          <p v-if="!isNaN(parseFloat(table.hvl_diff2))">{{parseFloat(table.hvl_diff2 * 100).toFixed(2)}}%</p>
        </el-descriptions-item>
      </el-descriptions>
      <el-button type="danger" @click.prevent="removeTable(table)">Remove table</el-button>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'hvl',
    data () {
      return {
        tables: [{
          kvp: null,
          hvl_type: 'mm Al',
          chamber: null,
          ratio1: null,
          ratio2: null,
          filtP1: null,
          filtP2: null,
          fac_hvl: null,
          hvl_value1: null,
          hvl_diff1: null,
          hvl_value2: null,
          hvl_diff2: null,
          data: [
            {
              hvl_value: '0',
              m1: null,
              m2: null,
              m3: null,
              m_avg: null,
              ratio: null,
              abs_diff: null
            }
          ]
        }],
        hvl_type: [
          'mm Al',
          'mm Cu'
        ],
        isEdit: true
      }
    },
    methods: {
      calculateMavg (data) {
        let items = [data.m1, data.m2, data.m3]
        let sum = 0
        let count = 0
        for (let i = 0; i < items.length; ++i) {
          if (items[i] !== '' && items[i] != null) {
            sum += parseFloat(items[i])
            count += 1
          }
        }
        data.m_avg = (sum) / count
        return (sum) / count
      },
      calculateRatio (table, data) {
        data.ratio = data.m_avg / table.data[0].m_avg
        return data.ratio
      },
      calculateABSDiff (data) {
        data.abs_diff = data.ratio - 0.5
        return data.abs_diff
      },
      calculateRatio1 (table) {
        var ratio1 = null
        var ratio2 = null
        var filtP1 = null
        var filtP2 = null
        var all_value = [0.5]

        for (let each of table.data) {
          all_value.push(each.ratio)
        }
        all_value.sort().reverse()
        let flagIndex = all_value.indexOf(0.5)
        if (flagIndex > 0) {
          ratio1 = all_value[flagIndex - 1]
          for (let each of table.data) {
            if (each.ratio === table.ratio1) {
              filtP1 = each.hvl_value
            }
          }
        }
        if (flagIndex < all_value.length) {
          ratio2 = all_value[flagIndex + 1]
          for (let each of table.data) {
            if (each.ratio === table.ratio2) {
              filtP2 = each.hvl_value
            }
          }
        }
        table.ratio1 = ratio1
        table.ratio2 = ratio2
        table.filtP1 = filtP1
        table.filtP2 = filtP2
        return table.ratio1
      },
      calculateHvlValue1 (table) {
        table.hvl_value1 = parseFloat((table.filtP1 - table.filtP2) / (table.ratio1 - table.ratio2) * (0.5 - table.ratio1)) + parseFloat(table.filtP1)
        table.hvl_diff1 = table.fac_hvl / table.hvl_value1 - 1
        let hvl_value2 = null
        if (Math.abs(table.ratio1 - 0.5) > Math.abs(table.ratio2 - 0.5)) {
          hvl_value2 = (table.filtP2 * Math.log(0.5))
          for (let each of table.data) {
            if (each.ratio === table.ratio2) {
              hvl_value2 = hvl_value2 / Math.log(each.m_avg / table.data[0].m_avg)
            }
          }
        } else {
          hvl_value2 = (table.filtP1 * Math.log(0.5))
          for (let each of table.data) {
            if (each.ratio === table.ratio1) {
              hvl_value2 = hvl_value2 / Math.log(each.m_avg / table.data[0].m_avg)
            }
          }
        }

        table.hvl_value2 = hvl_value2
        table.hvl_diff2 = table.fac_hvl / table.hvl_value2 - 1
        return table.hvl_value1
      },
      addTable () {
        this.tables.push({
          kvp: null,
          hvl_type: 'mm Al',
          chamber: null,
          ratio1: null,
          ratio2: null,
          filtP1: null,
          filtP2: null,
          fac_hvl: null,
          hvl_value1: null,
          hvl_diff1: null,
          hvl_value2: null,
          hvl_diff2: null,
          data: [
            {
              hvl_value: '0',
              m1: null,
              m2: null,
              m3: null,
              m_avg: null,
              ratio: null,
              abs_diff: null
            }
          ]
        })
      },
      removeTable (table) {
        this.$messageBox.confirm('Are you sure?', 'Notice', {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning'
        }).then(() => {
          const index = this.tables.indexOf(table)
          if (index !== -1) {
            this.tables.splice(index, 1)
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: 'cancel'
          })
        })
      },
      addTableData (table) {
        table.data.push({
          hvl_value: '',
          m1: '',
          m2: '',
          m3: '',
          m_avg: '',
          ratio: '',
          abs_diff: ''
        })
      },
      removeTableData (table, item) {
        this.$messageBox.confirm('Are you sure?', 'Notice', {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning'
        }).then(() => {
          const index = table.data.indexOf(item)
          if (index !== -1) {
            table.data.splice(index, 1)
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: 'cancel'
          })
        })
      }
    }
  }
</script>

<style scoped>
  .manually >>> input {
    font-weight: bold;
    color: coral;
    font-size: larger;
  }
  .hvl >>> table th {
    font-weight: bold;
    color: black;
    font-size: larger;
  }
  .hvl >>> table td {
    font-weight: bold;
    color: #409EFF;
    font-size: larger;
  }
  .hvl >>> table {
    white-space: normal;
    word-break: break-word;
  }
</style>
