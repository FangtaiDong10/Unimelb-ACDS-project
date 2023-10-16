<template>
  <div>
    <el-row>
      <el-col :span="8" v-for="(o, index) in tableData" :key="index">
        <el-card :body-style="{ padding: '0px' }">
          <el-table
            :data="tableData[0].data"
            style="width: 100%">
            <el-table-column
              :label= "tableData[index].chamber"
              align=center>
              <el-table-column
                prop="beam_id"
                label="Beam ID"
                v-if = "index == 0">
                <template slot-scope="scope">
                  {{tableData[index].data[tableData[0].data.indexOf(scope.row)].beam_id}}
                </template>
              </el-table-column>
              <el-table-column
                prop="ks"
                label="ks">
                <template slot-scope="scope">
                  {{tableData[index].data[tableData[0].data.indexOf(scope.row)].ks}}
                </template>
              </el-table-column>
              <el-table-column
                prop="kpol"
                label="kpol">
                <template slot-scope="scope">
                  <el-input
                    v-model="tableData[index].data[tableData[0].data.indexOf(scope.row)].kpol"
                    placeholder=""
                    class="manually"></el-input>
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
      name: 'kskpol',
      data () {
        return {
          tableData: [
            {
              chamber: 3587,
              data: [
                {
                  beam_id: 'Filter 1',
                  ks: 1,
                  kpol: 1
                }
              ]
            },
            {
              chamber: 1508,
              data: [
                {
                  beam_id: 'Filter 1',
                  ks: 1,
                  kpol: 1
                }
              ]
            }
          ]
        }
      }
    }
</script>

<style scoped>

</style>
