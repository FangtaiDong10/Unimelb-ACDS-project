<template>
  <div>
    <h2 style="float: left">Audit Information</h2>
    <br/>
    <el-descriptions class="margin-top" :column="3" border :inline = 'true'>
      <!-- Audit Number -->
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Audit Number
        </template>
        <el-input v-model = identification.audit_information.audit_id :readonly="!isEdit"></el-input>
      </el-descriptions-item>

      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Audit Date
        </template>
        <el-date-picker
          v-model=" identification.audit_information.date"
          type="date"
          :readonly="!isEdit"
          placeholder="Choose Date">
        </el-date-picker>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Facility ID
        </template>
        <el-input :readonly="!isEdit" v-model = identification.audit_information.facility_id></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <br/>
    <h2 style="float: left">Treatment Machine</h2>
    <br/>
    <el-descriptions class="margin-top" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          KV Unit Manufacturer
        </template>
        <el-input :readonly="!isEdit" v-model = identification.treatment_machine.manufacturer></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          KV Unit Model
        </template>
        <el-input :readonly="!isEdit" v-model = identification.treatment_machine.unit_model></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Unit Serial Number
        </template>
        <el-input :readonly="!isEdit" v-model = identification.treatment_machine.unit_serial_number></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Local Name
        </template>
        <el-input :readonly="!isEdit" v-model = identification.treatment_machine.local_name></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Tube Insert Type
        </template>
        <el-input :readonly="!isEdit" v-model = identification.treatment_machine.tube_insert_type></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Tube Serial Number
        </template>
        <el-input :readonly="!isEdit" v-model = identification.treatment_machine.tube_serial_number></el-input>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
  export default {
    name: 'auditInformation',
    props: {
      identification: null,
      isEdit: null
    }
  }
</script>

<style scoped>

</style>
