<template>
  <div>
    <h2 style="float: left">Beams <i @click="addBeam" style="color: #409EFF" class="point" v-if="isEdit">> Add Beam</i></h2>
    <br/>
    <el-table :data="identification.beam_data" style="width: 100%;" border>
      <el-table-column
        prop="beam_ID"
        label="Beam ID"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.beam_ID" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="nom_energy"
        label="Nom. Energy (kVp))"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.nom_energy" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="scd"
        label="SCD* (cm)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.scd" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="field_size_at_scd"
        label="Field size at SCD** (cm)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.field_size_at_scd" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="hvl_nominal_mm_ai"
        label="Nominal HVL (mm Al)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.hvl_measured_mm_al" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="hvl_nominal_mm_cu"
        label="Nominal HVL (mm Cu)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.hvl_nominal_mm_cu" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="hvl_measured_mm_ai"
        label="Measured HVL (mm Al)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.hvl_measured_mm_al" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="hvl_measured_mm_cu"
        label="Measured HVL (mm Cu)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.hvl_measured_mm_cu" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="Operation"
        style="color: #3A71A8 "
        v-if="isEdit">
        <template slot-scope="scope">
          <el-button :readonly="!isEdit" type="danger" size="mini" @click.prevent="removeBeam(scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: 'beams',
    props: {
      identification: null,
      isEdit: null
    },
    methods: {
      addBeam () {
        this.identification.beams.push({
          role: '',
          title: '',
          first_name: '',
          last_name: '',
          phone: '',
          email: ''
        })
      },
      removeBeam (item) {
        const index = this.identification.beams.indexOf(item)
        if (index !== -1) {
          this.identification.beams.splice(index, 1)
          this.identification.reference_cone.splice(index, 1)
        }
      }
    }
  }
</script>

<style scoped>
.point:hover {
  cursor: pointer;
  background-color: gold;
}
</style>
