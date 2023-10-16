<template>
  <div>
    <h2 style="float: left">Applicator/Cone {{identification.cone[0].cone_id_alt}}<i @click="addConeBeam(0)" style="color: #409EFF" class="point" v-if="isEdit"> > Add Beam Data</i></h2>
    <br/>
    <el-descriptions
      direction="vertical"
      :column="11"
      border>
      <el-descriptions-item label="SSD (cm)">
        <el-input :readonly="!isEdit" v-model = identification.cone[0].ssd></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Nominal Dose Output Type">
        <el-select :readonly="!isEdit" v-model="identification.cone[0].dose_output_unit" placeholder="please select">
          <el-option
            v-for="item in nominalDoseOutputTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Closed/Open Cone">
        <el-select :readonly="!isEdit" v-model="identification.cone[0].open_closed" placeholder="please select">
          <el-option
            v-for="item in closeOpenOPtions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="End Thickness (mm)">
        <el-input :readonly="!isEdit" v-model = identification.cone[0].end_thickness :disabled="identification.cone[0].open_closed !== 'Close'"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Shape">
        <el-select :readonly="!isEdit" v-model="identification.cone[0].shape" placeholder="please select">
          <el-option
            v-for="item in shapeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Size Diameter (cm)" v-if="identification.cone[0].shape === 'Circular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[0].field_diameter></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Square Area (cm2)" v-if="identification.cone[0].shape === 'Square'">
        <el-input :readonly="!isEdit" v-model = identification.cone[0].field_area></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Dimension 1 (cm)" v-if="identification.cone[0].shape === 'Rectangular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[0].field_dimension_1></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Dimension 2 (cm)" v-if="identification.cone[0].shape === 'Rectangular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[0].field_dimension_2></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <el-table :data="identification.cone[0].beams_data" class="table" style="width: 100%" border>
      <el-table-column
        prop="beam_ID"
        label="Beam ID"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.beam_id" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="dosp_at_ssd"
        label="DOSP* at SSD, z = 0? (y/n)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-select :readonly="!isEdit" v-model="scope.row.dosp_ssd" placeholder="please select">
            <el-option
              v-for="item in yesOrNoOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="nominal_dose_output_value"
        label="Nominal Dose Output Value"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.nom_dose_output" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="Operation"
        style="color: #3A71A8 "
        v-if="isEdit">
        <template slot-scope="scope">
          <el-button :readonly="!isEdit" type="danger" size="mini" @click.prevent="removeConeBeam(0, scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br/>
    <h2 style="float: left">Applicator/Cone {{identification.cone[1].cone_id_alt}}<i @click="addConeBeam(1)" style="color: #409EFF" class="point" v-if="isEdit"> > Add Beam Data</i></h2>
    <br/>
    <el-descriptions
      direction="vertical"
      :column="11"
      border>
      <el-descriptions-item label="SSD (cm)">
        <el-input :readonly="!isEdit" v-model = identification.cone[1].ssd></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Nominal Dose Output Type">
        <el-select :readonly="!isEdit" v-model="identification.cone[1].dose_output_unit" placeholder="please select">
          <el-option
            v-for="item in nominalDoseOutputTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Closed/Open Cone">
        <el-select :readonly="!isEdit" v-model="identification.cone[1].open_closed" placeholder="please select">
          <el-option
            v-for="item in closeOpenOPtions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="End Thickness (mm)">
        <el-input :readonly="!isEdit" v-model = identification.cone[1].end_thickness :disabled="identification.cone[1].open_closed !== 'Close'"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Shape">
        <el-select :readonly="!isEdit" v-model="identification.cone[1].shape" placeholder="please select">
          <el-option
            v-for="item in shapeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Size Diameter (cm)" v-if="identification.cone[1].shape === 'Circular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[1].field_diameter></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Square Area (cm2)" v-if="identification.cone[1].shape === 'Square'">
        <el-input :readonly="!isEdit" v-model = identification.cone[1].field_area></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Dimension 1 (cm)" v-if="identification.cone[1].shape === 'Rectangular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[1].field_dimension_1></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Dimension 2 (cm)" v-if="identification.cone[1].shape === 'Rectangular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[1].field_dimension_2></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <el-table :data="identification.cone[1].beams_data" style="width: 100%;" border>
      <el-table-column
        prop="beam_ID"
        label="Beam ID"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.beam_id" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="dosp_at_ssd"
        label="DOSP* at SSD, z = 0? (y/n)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-select :readonly="!isEdit" v-model="scope.row.dosp_ssd" placeholder="please select">
            <el-option
              v-for="item in yesOrNoOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="nominal_dose_output_value"
        label="Nominal Dose Output Value"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.nom_dose_output" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="Operation"
        style="color: #3A71A8 "
        v-if="isEdit">
        <template slot-scope="scope">
          <el-button :readonly="!isEdit" type="danger" size="mini" @click.prevent="removeConeBeam(1, scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <br/>
    <h2 style="float: left">Applicator/Cone {{identification.cone[2].cone_id_alt}}<i @click="addConeBeam(2)" style="color: #409EFF" class="point" v-if="isEdit"> > Add Beam Data</i></h2>
    <br/>
    <el-descriptions
      direction="vertical"
      :column="11"
      border>
      <el-descriptions-item label="SSD (cm)">
        <el-input :readonly="!isEdit" v-model = identification.cone[2].ssd></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Nominal Dose Output Type">
        <el-select :readonly="!isEdit" v-model="identification.cone[2].dose_output_unit" placeholder="please select">
          <el-option
            v-for="item in nominalDoseOutputTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Closed/Open Cone">
        <el-select :readonly="!isEdit" v-model="identification.cone[2].open_closed" placeholder="please select">
          <el-option
            v-for="item in closeOpenOPtions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="End Thickness (mm)">
        <el-input :readonly="!isEdit" v-model = identification.cone[2].end_thickness :disabled="identification.cone[2].open_closed !== 'Close'"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Shape">
        <el-select :readonly="!isEdit" v-model="identification.cone[2].shape" placeholder="please select">
          <el-option
            v-for="item in shapeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Size Diameter (cm)" v-if="identification.cone[2].shape === 'Circular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[2].field_diameter></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Square Area (cm2)" v-if="identification.cone[2].shape === 'Square'">
        <el-input :readonly="!isEdit" v-model = identification.cone[2].field_area></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Dimension 1 (cm)" v-if="identification.cone[2].shape === 'Rectangular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[2].field_dimension_1></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="Dimension 2 (cm)" v-if="identification.cone[2].shape === 'Rectangular'">
        <el-input :readonly="!isEdit" v-model = identification.cone[2].field_dimension_2></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <el-table :data="identification.cone[2].beams_data" style="width: 100%;" border>
      <el-table-column
        prop="beam_ID"
        label="Beam ID"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.beam_id" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        prop="dosp_at_ssd"
        label="DOSP* at SSD, z = 0? (y/n)"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-select :readonly="!isEdit" v-model="scope.row.dosp_ssd" placeholder="please select">
            <el-option
              v-for="item in yesOrNoOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
        prop="nominal_dose_output_value"
        label="Nominal Dose Output Value"
        style="color: #3A71A8 ">
        <template slot-scope="scope">
          <el-input :readonly="!isEdit" v-model="scope.row.nom_dose_output" placeholder=""></el-input>
        </template>
      </el-table-column>
      <el-table-column
        label="Operation"
        style="color: #3A71A8 "
        v-if="isEdit">
        <template slot-scope="scope">
          <el-button :readonly="!isEdit" type="danger" size="mini" @click.prevent="removeConeBeam(2, scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    name: 'cones',
    props: {
      identification: null,
      isEdit: null
    },
    data () {
      return {
        yesOrNoOptions: [{
          value: 'yes',
          label: 'yes'
        }, {
          value: 'no',
          label: 'no'
        }],
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
        }],
        nominalDoseOutputTypeOptions: [{
          value: 'Gy/min',
          label: 'Gy/min'
        }, {
          value: 'cGy/min',
          label: 'cGy/min'
        }, {
          value: 'Gy/MU',
          label: 'Gy/MU'
        }, {
          value: 'cGy/MU',
          label: 'cGy/MU'
        }]
      }
    },
    methods: {
      addConeBeam (i) {
        this.identification.applicator_cones[i].beams_data.push({
          beam_ID: '',
          dosp_at_ssd: '',
          nominal_dose_output_value: ''
        })
      },
      removeConeBeam (i, item) {
        const index = this.identification.applicator_cones[i].beams_data.indexOf(item)
        if (index !== -1) {
          this.identification.applicator_cones[i].beams_data.splice(index, 1)
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
