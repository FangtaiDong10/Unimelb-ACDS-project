<template>
  <div>
    <el-table
      :data="repsData"
      style="width: 100%; margin-top: 12px">
      <el-table-column
        prop="a_reps_name"
        label="ACDS Reps"
        width="250"
        align=center>
        <template slot-scope="scope">
          <el-input size="mini" v-model="scope.row.a_reps_name" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="f_reps_name"
        label="Facility Reps"
        width="250"
        align=center>
        <template slot-scope="scope">
          <el-input size="mini" v-model="scope.row.f_reps_name" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="add_reps"
        align="center"
        width="150">
        <template slot="header">
          <el-button @click="addReps" icon="el-icon-plus" size="mini" type="primary" plain>Add</el-button>
        </template>
        <template slot-scope="scope">
          <el-button @click="deleteReps(scope.row)" icon="el-icon-plus" size="mini" type="danger" plain>Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
    export default {
      name: 'representatives',
      data () {
        return {
          active: 0,
          repsData: [
            [{
              a_reps_name: '',
              f_reps_name: ''
            }]
          ]
        }
      },
      methods: {
        deleteReps (rep) {
          console.log(rep)
          const index = this.repsData.indexOf(rep)
          if (index !== -1) {
            this.repsData.splice(index, 1)
          }
        },
        addReps () {
          this.repsData.push({
            a_reps_name: '',
            f_reps_name: ''
          })
        }
      }
    }
</script>

<style scoped>

</style>
