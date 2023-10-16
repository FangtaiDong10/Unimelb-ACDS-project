<template>
  <el-collapse v-model="activeNames">
    <!-- Basic Information -->
    <el-collapse-item name="1" v-model="activeNames" title="Basic Information">
      <template slot="title">
        <h1>Basic Information</h1>
      </template>
      <audit-information :identification="identification" :is-edit="isEdit" />
    </el-collapse-item>

    <!-- Facility Information -->
    <el-collapse-item name="2">
      <template slot="title">
        <h1>Facility Information</h1>
      </template>
      <facility-information
        :identification="identification"
        :is-edit="isEdit"
      />
    </el-collapse-item>

    <!-- Dosimetry -->
    <el-collapse-item name="3">
      <template slot="title">
        <h1>Dosimetry</h1>
      </template>
      <dosimetry :identification="identification" :is-edit="isEdit" />
    </el-collapse-item>

    <!-- Beams and Cones -->
    <el-collapse-item name="4">
      <template slot="title">
        <h1>Beams and Cones</h1>
      </template>
      <beams :identification="identification" :is-edit="isEdit" />
      <reference-cone :identification="identification" :is-edit="isEdit" />
      <cones :identification="identification" :is-edit="isEdit" />
    </el-collapse-item>
  </el-collapse>
</template>

<script>
import auditInformation from "./auditInformation";
import facilityInformation from "./facilityInformation";
import dosimetry from "./dosimetry";
import beams from "./beamsAndCones/beams";
import referenceCone from "./beamsAndCones/referenceCone";
import cones from "./beamsAndCones/cones";
export default {
  name: "identificationForm",
  props: {
    identification: null,
    isEdit: null,
  },
  methods: {},
  data() {
    return {
      activeNames: ["1", "2", "3", "4"],
    };
  },
  components: {
    auditInformation,
    facilityInformation,
    dosimetry,
    beams,
    referenceCone,
    cones,
  },
};
</script>

<style scoped>
h1 {
  color: #3a71a8;
  font-size: xx-large;
}
</style>
