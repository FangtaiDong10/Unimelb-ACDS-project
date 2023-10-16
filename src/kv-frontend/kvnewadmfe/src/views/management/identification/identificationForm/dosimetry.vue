<template>
  <div>
    <h2 style="float: left">Reference Dosimetry</h2>
    <br/>
    <el-descriptions class="margin-top" :column="2" border>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Protocol
        </template>
        <el-select :readonly="!isEdit" v-model="identification.reference_dosimetry.protocol" placeholder="please select">
          <el-option
            v-for="item in referenceProtocolOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          How was NK determined?
        </template>
        <el-select :readonly="!isEdit" v-model="identification.reference_dosimetry.nk" placeholder="please select">
          <el-option
            v-for="item in nkOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Comments
        </template>
        <el-input :readonly="!isEdit" v-model = identification.reference_dosimetry.comments></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <br/>
    <h2 style="float: left">KV Superficial Treatment - Reference Dosimetry</h2>
    <br/>
    <el-descriptions class="margin-top" :column="2" border>
      <el-descriptions-item label = "Protocol">
        <el-input :readonly="!isEdit" v-model = identification.kv_reference_dosimetry.protocol></el-input>
      </el-descriptions-item>
      <el-descriptions-item label = "Reference chamber">
        <el-input :readonly="!isEdit" v-model = identification.kv_reference_dosimetry.reference_chamber></el-input>
      </el-descriptions-item>
      <el-descriptions-item label = "in-air/in-phantom">
        <el-input :readonly="!isEdit" v-model = identification.kv_reference_dosimetry.air_phantom></el-input>
      </el-descriptions-item>
      <el-descriptions-item label = "Reference depth">
        <el-input :readonly="!isEdit" v-model = identification.kv_reference_dosimetry.reference_depth></el-input>
      </el-descriptions-item>
      <el-descriptions-item label = "Comments">
        <el-input :readonly="!isEdit" v-model = identification.kv_reference_dosimetry.comments></el-input>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
  export default {
    name: 'dosimetry',
    props: {
      identification: null,
      isEdit: null
    },
    data () {
      return {
        referenceProtocolOptions: [{
          value: 'TG-61',
          label: 'TG-61'
        }, {
          value: 'IPEMB 96+Addendum',
          label: 'IPEMB 96+Addendum'
        }, {
          value: 'TRS-398',
          label: 'TRS-398'
        }, {
          value: 'Other (Please describe below)',
          label: 'Other (Please describe below)'
        }],
        
        nkOptions: [{
          value: 'Calibration by ARPANSA',
          label: 'Calibration by ARPANSA'
        }, {
          value: 'Other (Please describe below)',
          label: 'Other (Please describe below)'
        }]
      }
    }
  }
</script>

<style scoped>

</style>
