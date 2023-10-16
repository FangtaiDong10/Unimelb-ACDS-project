<template>
  <div>
    <h2 align="center">Reference Cone</h2>
    <br/>
    <el-table :data="referenceTableData" style="width: 100%;" border class="coneFactorTable">
      <el-table-column
        prop="kv"
        label="kv"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.kv" placeholder="" class="manually"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="temp"
        label="temp"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.temp" placeholder="" class="manually"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="pressure"
        label="pressure"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.pressure" placeholder="" class="manually"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="ktp"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <p style="font-weight: bold; color: #30B08F" v-if="!isNaN(parseFloat(calculateKTP(scope.row)))">{{parseFloat(calculateKTP(scope.row)).toFixed(6)}}</p>
        </template>
      </el-table-column>
      <el-table-column
        prop="m_1"
        label="m_1"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.m_1" placeholder="" class="manually"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="m_2"
        label="m_2"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.m_2" placeholder="" class="manually"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="m_3"
        label="m_3"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <el-input v-model="scope.row.m_3" placeholder="" class="manually"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="mavg"
        label="mavg"
        style="color: #3A71A8 "
        align="center">
        <template slot-scope="scope">
          <p style="font-weight: bold; color: #30B08F" v-if="!isNaN(parseFloat(calculateMavg(scope.row)))">{{parseFloat(calculateMavg(scope.row)).toFixed(6)}}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="Operation"
        style="color: #3A71A8 "
        align="center">
        <template slot="header">
          <el-button type="primary" size="mini" @click.prevent="addReferenceData()">Add</el-button>
        </template>

        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click.prevent="removeReferenceData(scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-for="cone in cones">
      <h1 align="center" style="padding-top: 50px">{{cone.cone_name}}</h1>
      <br/>
      <el-table :data="cone.coneTableData" style="width: 100%;" border class="coneFactorTable">
        <el-table-column
          prop="kv"
          label="kv"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.kv" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="temp"
          label="temp"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.temp" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="pressure"
          label="pressure"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.pressure" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="ktp"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <p style="font-weight: bold; color: #30B08F" v-if="!isNaN(parseFloat(calculateKTP(scope.row)))">{{parseFloat(calculateKTP(scope.row)).toFixed(6)}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="m_1"
          label="m_1"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.m_1" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="m_2"
          label="m_2"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.m_2" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          prop="m_3"
          label="m_3"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.m_3" placeholder="" class="manually"></el-input>
          </template>
        </el-table-column>
        <el-table-column
          label="M Average"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <p style="font-weight: bold; color: #30B08F" v-if="!isNaN(parseFloat(calculateMavg(scope.row)))">{{parseFloat(calculateMavg(scope.row)).toFixed(6)}}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="Cone Factor"
          style="color: #3A71A8 "
          align="center">
          <template slot-scope="scope">
            <p style="font-weight: bold; color: #30B08F" v-if="!isNaN(parseFloat(calculateConeFactor(scope.row)))">{{parseFloat(calculateConeFactor(scope.row)).toFixed(6)}}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="Operation"
          style="color: #3A71A8 "
          align="center">
          <template slot="header">
            <el-button type="primary" size="mini" @click.prevent="addConeData(cone)">Add</el-button>
          </template>

          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click.prevent="removeConeData(cone, scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'beams',
    data () {
      return {
        cones: [
          {
            cone_name: 'cone 1',
            dose_type: '',
            coneTableData: [
              {
                // yellow
                kv: null,
                temp: null,
                pressure: null,
                m_1: null,
                m_2: null,
                m_3: null
              }
            ]
          },
          {
            cone_name: 'cone 2',
            dose_type: '',
            coneTableData: [
              {
                // yellow
                kv: null,
                temp: null,
                pressure: null,
                m_1: null,
                m_2: null,
                m_3: null
              }
            ]
          },
          {
            cone_name: 'cone 3',
            dose_type: '',
            coneTableData: [
              {
                // yellow
                kv: null,
                temp: null,
                pressure: null,
                m_1: null,
                m_2: null,
                m_3: null
              }
            ]
          }
        ],
        referenceTableData: [
          {
            // yellow
            kv: null,
            temp: null,
            pressure: null,
            m_1: null,
            m_2: null,
            m_3: null
          }
        ],
        isEdit: true
      }
    },
    methods: {
      calculateKTP (data) {
        if (parseFloat(data.temp) && parseFloat(data.pressure)) {
          return (parseFloat(data.temp) + 273.15) / 293.15 * (101.325 / parseFloat(data.pressure))
        }
        return ''
      },
      calculateMavg (data) {
        let items = [data.m_1, data.m_2, data.m_3]
        let sum = 0
        let count = 0
        if (this.calculateKTP(data) === '') {
          return ''
        }
        for (let i = 0; i < items.length; ++i) {
          if (items[i] !== '' && items[i] != null) {
            sum += parseFloat(items[i])
            count += 1
          }
        }
        return (sum) / count * this.calculateKTP(data)
      },
      calculateConeFactor (data) {
        let kv = data.kv
        let mavg = this.calculateMavg(data)
        if (mavg === 0 || mavg === null || mavg === '') {
          return ''
        }
        for (var ref_data of this.referenceTableData) {
          if (kv === ref_data.kv) {
            if (this.calculateMavg(ref_data) === '' || this.calculateMavg(ref_data) === null) {
              return ''
            }
            return mavg / this.calculateMavg(ref_data)
          }
        }
        return null
      },
      addConeData (cone) {
        cone.coneTableData.push({
          // yellow
          kv: '',
          temp: '',
          pressure: '',
          m_1: '',
          m_2: '',
          m_3: '',
          // green
          ktp: '',
          mavg: ''
        })
      },
      removeConeData (cone, item) {
        this.$messageBox.confirm('Are you sure?', 'Notice', {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning'
        }).then(() => {
          const index = cone.coneTableData.indexOf(item)
          if (index !== -1) {
            cone.coneTableData.splice(index, 1)
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: 'cancel'
          })
        })
      },
      addReferenceData () {
        this.referenceTableData.push({
          // yellow
          kv: '',
          temp: '',
          pressure: '',
          m_1: '',
          m_2: '',
          m_3: '',
          // green
          ktp: '',
          mavg: ''
        })
      },
      removeReferenceData (item) {
        this.$messageBox.confirm('Are you sure?', 'Notice', {
          confirmButtonText: 'Yes',
          cancelButtonText: 'No',
          type: 'warning'
        }).then(() => {
          const index = this.referenceTableData.indexOf(item)
          if (index !== -1) {
            this.referenceTableData.splice(index, 1)
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
  .coneFactorTable >>> table th {
    font-weight: bold;
    color: black;
    font-size: larger;
  }
  .coneFactorTable >>> table td {
    font-weight: bold;
    color: #409EFF;
    font-size: larger;
  }
  .coneFactorTable >>> table {
    white-space: normal;
    word-break: break-word;
  }
</style>
