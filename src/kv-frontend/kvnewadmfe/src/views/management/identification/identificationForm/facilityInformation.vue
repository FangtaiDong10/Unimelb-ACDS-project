<template>
  <div>
    <el-descriptions class="margin-top" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Radiation Oncology Organisation
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.organisation_name"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Radiation Oncology Service
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.oncology_service"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Radiation Oncology Facility
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.oncology_facility"
        ></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <br />
    <h2 style="float: left">
      Facility Representative <i
        @click="addRepresentative"
        style="color: #409eff"
        class="point"
        v-if="isEdit"
      >
       > Add Representative</i
      >
    </h2>
    <br />
    <el-descriptions
      class="margin-top"
      :column="3"
      border
      v-for="representative in identification.facility_information
        .facility_representatives"
      :key="representative.key"
    >
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Title
        </template>
        <el-input :readonly="!isEdit" v-model="representative.title"></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          First Name
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="representative.first_name"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Last Name
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="representative.last_name"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Role
        </template>
        <el-input :readonly="!isEdit" v-model="representative.role"></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Phone
        </template>
        <el-input :readonly="!isEdit" v-model="representative.phone"></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Email
        </template>
        <el-input :readonly="!isEdit" v-model="representative.email"></el-input>
      </el-descriptions-item>
      <el-descriptions-item v-if="isEdit">
        <template slot="label">
          <i></i>
          Operation
        </template>
        <el-button
          type="danger"
          size="mini"
          @click.prevent="removeRepresentative(representative)"
          >Delete</el-button
        >
      </el-descriptions-item>
    </el-descriptions>
    <br />
    <h2>Physical Address of Facility</h2>
    <el-descriptions class="margin-top" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Postcode
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="
            identification.facility_information.physical_address.post_code
          "
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          State
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="
            identification.facility_information.physical_address.state_name
          "
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Suburb
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.physical_address.suburb"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Street Address
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.physical_address.street"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Location
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="
            identification.facility_information.physical_address.building
          "
        ></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <br />
    <h2>Reporting Address</h2>
    <el-descriptions class="margin-top" :column="3" border>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Postcode
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="
            identification.facility_information.reporting_address.post_code
          "
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          State
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="
            identification.facility_information.reporting_address.state_name
          "
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Suburb
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.reporting_address.suburb"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Street Address
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="identification.facility_information.reporting_address.street"
        ></el-input>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i></i>
          Location
        </template>
        <el-input
          :readonly="!isEdit"
          v-model="
            identification.facility_information.reporting_address.building
          "
        ></el-input>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  name: "facilityInformation",
  props: {
    identification: null,
    isEdit: null,
  },
  methods: {
    addRepresentative() {
      this.identification.facility_information.facility_representatives.push({
        beam_ID: "",
        ssd: "",
        depth: "",
        geometry_shape: "",
        geometry_measurement: "",
        open_closed: "",
        thickness: "",
        dosp_ssd: "",
      });
    },
    removeRepresentative(item) {
      const index =
        this.identification.facility_information.facility_representatives.indexOf(
          item
        );
      if (index !== -1) {
        this.identification.facility_information.facility_representatives.splice(
          index,
          1
        );
      }
    },
  },
};
</script>

<style scoped>
.point:hover {
  cursor: pointer;
  background-color: gold;
}
</style>
