<template>
    <div>
      <el-row>
        <el-col :span="8" v-for="(o, index) in 3" :key="index">
          <el-card :body-style="{ padding: '0px' }">
            <el-table
              :data="tableData[0].cone_data"
              style="width: 100%">
              <el-table-column
                :label= "tableData[index].cone_name"
                align=center>
                <el-table-column
                  prop="beam_id"
                  label="Beam ID"
                >
                  <template slot-scope="scope">
                    {{tableData[index].cone_data[tableData[0].cone_data.indexOf(scope.row)].beam_id}}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="gymu"
                  label="Setting for 1 Gy Mu"
                  >
                  <template slot-scope="scope">
                    {{tableData[index].cone_data[tableData[0].cone_data.indexOf(scope.row)].gymu}}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="usedMu"
                  label="Setting Used Mu"
                  align=center>
                  <template slot-scope="scope">
                    <el-input size="mini" v-model="tableData[index].cone_data[tableData[0].cone_data.indexOf(scope.row)].usedMu" placeholder=""></el-input>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="fac"
                  label="Fac Dose to OSLDs (Gy)"
                  align=center>
                  <template slot-scope="scope">
                    {{tableData[index].cone_data[tableData[0].cone_data.indexOf(scope.row)].fac}}
                  </template>
                </el-table-column>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
</template>

<script>
    export default {
      name: 'irradiations',
      data () {
        return {
          tableData: [
            {
              cone_name: 'Cone 1 - F',
              cone_data: [
                {
                  gymu: '59.7609',
                  usedMu: '1',
                  fac: '1'
                }
              ]
            },
            {
              cone_name: 'Cone 2 - K',
              cone_data: [
                {
                  gymu: '52.7609',
                  usedMu: '2',
                  fac: '2'
                }
              ]
            },
            {
              cone_name: 'Cone 3 - E',
              cone_data: [
                {
                  gymu: '49.7609',
                  usedMu: '3',
                  fac: '3'
                }
              ]
            }
          ]
        }
      },
      methods: {

      }
    }
</script>

<style scoped>

</style>
