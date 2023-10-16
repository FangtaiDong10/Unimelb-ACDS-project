<template>
  <div>
    <h2 style="float: left">Reference Cone<i @click="addReferenceConeBeam" style="color: #409EFF" class="point" v-if="isEdit"> > Add Beam Data</i></h2>
    <br/>
    <el-table :data="identification.reference_cone" style="width: 100%;" border>
      <el-table-column
        prop="beam_ID"
        label="Beam ID"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.reference_cone_id_alt" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="ssd"
        label="Meas. SSD (cm)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.ssd" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="depth"
        label="Meas. Depth Zref (cm)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.depth" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="geometry_shape"
        label="Cone Geometry Shape"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-select :readonly="!isEdit" v-model="scope.row.geometry_shape" placeholder="please select">
            <el-option
              v-for="item in shapeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="geometry_measurement"
        label="Cone Geometry size"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.geometry_measurement" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="dosp_ssd"
        label="DOSP at SSD, z = 0? (y/n)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.dosp_ssd" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="open_closed"
        label="Closed/Open Cone"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-select :readonly="!isEdit" v-model="scope.row.open_closed" placeholder="please select">
            <el-option
              v-for="item in closeOpenOPtions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="thickness"
        label="thickness (mm)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.thickness" placeholder="" :disabled="scope.row.open_closed !== 'Close'"></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="Operation"
        style="color: #3A71A8 "
        v-if="isEdit">
        <template slot-scope="scope">
          <el-button :readonly="!isEdit" type="danger" size="mini" @click.prevent="removeReferenceConeBeam(scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: 'referenceCone',
    props: {
      identification: null,
      isEdit: null
    },
    data () {
      return {
        shapeOptions: [{
          value: 'Circular',
          label: 'Circular'
        }, {
          value: 'Square',
          label: 'Square'
        }, {
          value: 'Rectangular',
          label: 'Rectangular'
        }],
        closeOpenOPtions: [{
          value: 'Open',
          label: 'Open'
        }, {
          value: 'Close',
          label: 'Close'
        }]
      }
    },
    methods: {
      addReferenceConeBeam () {
        this.identification.reference_cone.beams_data.push({
          beam_ID: '',
          ssd: '',
          depth: '',
          geometry_shape: '',
          geometry_measurement: '',
          open_closed: '',
          thickness: '',
          dosp_ssd: ''
        })
      },
      removeReferenceConeBeam (item) {
        const index = this.identification.reference_cone.beams_data.indexOf(item)
        if (index !== -1) {
          this.identification.reference_cone.beams_data.splice(index, 1)
        }
      }
    }
  }
</script>

<style scoped>
.point:hover{
  cursor: pointer;
  background-color: gold;
}
</style>
