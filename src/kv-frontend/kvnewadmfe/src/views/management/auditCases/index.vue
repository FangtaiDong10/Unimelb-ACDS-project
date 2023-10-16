<template>
  <div class="class">
    <!-- assets page component -->
    <assets-page
      :total="totalNumbers"
      :search-form="searchForm"
      :page-size="pageSize"
      :currentPage="pageIndex"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
      @data-add="showAdd"
      @data-cancel="handleCancel"
      @data-delete="handleDelete"
      @data-search="handleSearch"
    >
      <!-- Audit Number Search -->
      <el-form-item label="Audit Number" slot="search">
        <el-input
          v-model="searchForm.audit_id"
          placeholder="Audit Number"
          suffix-icon="el-icon-s-custom"
        ></el-input>
      </el-form-item>

      <!-- Clinic Search -->
      <el-form-item label="Clinic" slot="search">
        <el-input
          v-model="searchForm.clinic"
          placeholder="Clinics"
          suffix-icon="el-icon-house"
        ></el-input>
      </el-form-item>

      <!-- Status Selection -->
      <el-form-item label="Status" slot="search">
        <el-select
          v-model="searchForm.status"
          clearable
          placeholder="Status"
          suffix-icon="el-icon-star-off"
        >
          <el-option
            :label="item"
            :value="item"
            v-for="item in statusOption"
            :key="item"
          ></el-option>
        </el-select>
      </el-form-item>

      <!-- Date Selection -->
      <el-form-item label="Date" slot="search">
        <el-date-picker
          v-model="searchForm.date"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="Choose Date"
          suffix-icon="el-icon-star-calendar"
        >
        </el-date-picker>
      </el-form-item>

      <!-- El Table -->
      <div slot="table">
        <!-- table settings @handleSelectionChange not in methods -->
        <el-table
          ref="multipleTable"
          :data="tableData"
          style="width: 100%"
          :border="true"
          @selection-change="handleSelectionChange"
        >
          <!-- selection box column-->
          <!-- <el-table-column
            type="selection"
            width="55"
            align="center"
          ></el-table-column> -->

          <!-- index column-->
          <el-table-column
            label="index"
            type="index"
            :index="indexMethod"
            width="55"
            align="center"
          ></el-table-column>

          <!-- Audit Number auditID in tableData-->
          <el-table-column
            label="Audit Number"
            prop="audit_id"
            align="center"
          ></el-table-column>

          <!-- Clinic in tableData-->
          <el-table-column
            label="Clinic"
            prop="clinic"
            align="center"
          ></el-table-column>

          <!-- status in tableData-->
          <el-table-column
            label="Status"
            prop="status"
            align="center"
          ></el-table-column>

          <!-- Date column interpolation scope(tableData).row.date attribute -->
          <el-table-column label="Date" align="center">
            <template slot-scope="scope">
              {{ formatDate(scope.row.date) }}
            </template>
          </el-table-column>

          <!-- Operation column with Edit Button @click to mixin with tableData row object-->
          <el-table-column label="Operation" width="250" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="medium"
                @click="showEdit(scope.row)"
                >Edit</el-button
              >
            </template>
          </el-table-column>

          <!-- Calculation column with <Router Link> changing the page location -->
          <el-table-column label="Calculation" width="200" align="center">
            <template slot-scope="scope">
              <router-link
                :to="'/audit/caseSpecification/' + scope.row.audit_id"
                class="link"
              >
                <el-button type="primary" size="medium">Calculation</el-button>
              </router-link>
            </template>
          </el-table-column>

          <!-- Identification column with <Router Link> changing the page location -->
          <el-table-column label="Identification" width="200" align="center">
            <template slot-scope="scope">
              <router-link
                :to="'/audit/identification/' + scope.row.audit_id"
                class="link"
              >
                <el-button type="primary" size="medium"
                  >Identification</el-button
                >
              </router-link>
            </template>
          </el-table-column>

          <!-- Warning column with queryWarning method pass the : dataTable argument audit_id-->
          <el-table-column label="Warning" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="primary"
                size="medium"
                @click="queryWarning(scope.row.audit_id)"
                >Warning</el-button
              >
            </template>
          </el-table-column>
          <el-table-column label="Operations" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="medium"
                @click="deletePageData(scope.row.audit_id)"
                >Delete</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </assets-page>

    <!-- V-IF different dialogs appearance shown on the page -->
    <div class="dialog-wrapper">
      <el-dialog
        :title="dialogTitle"
        :visible.sync="showDialog"
        :close-on-click-modal="false"
        width="600px"
        :limit="1"
        @closed="dialogClosed"
      >
        <!-- dialog for adding new -> New Case -->
        <div v-if="dialogTitle === 'New Case'">
          <el-upload
            class="upload-demo"
            drag
            :limit="1"
            accept=".xls,.xlsx,.xlsm"
            ref="upload"
            :action="action"
            :http-request="tokenReq"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-success="submitSuccess"
            :auto-upload="false"
          >
            <div class="el-upload__tip" slot="tip">Only excel file</div>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              Drag here, or<em> click to upload</em>
            </div>
          </el-upload>
          <el-button
            style="margin-top: 10px"
            size="small"
            type="success"
            @click="submitUpload"
            >submit</el-button
          >
        </div>

        <!-- dialog for changing status -> Edit Status -->
        <div v-if="dialogTitle === 'Edit Status'">
          <el-form :model="formData" label-width="100px">
            <el-form-item label="Status">
              <el-select
                v-model="formData.status"
                clearable
                placeholder="Please select"
              >
                <el-option
                  :label="item"
                  :value="item"
                  v-for="item in statusOption"
                  :key="item"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <el-button @click="save" type="primary">Confirm</el-button>
        </div>

        <!-- dialog for warning notification -->
        <div v-if="dialogTitle === 'Warning'">
          <el-table
            ref="multipleTable"
            :data="warningTable"
            style="width: 100%"
            :border="true"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              label="cone_id"
              prop="cone_id"
              align="center"
            ></el-table-column>
            <el-table-column
              label="beam_id"
              prop="beam_id"
              align="center"
            ></el-table-column>
            <el-table-column
              label="message"
              prop="message"
              align="center"
            ></el-table-column>
          </el-table>
        </div>
      </el-dialog>

      <!-- identificationForm dialog -->
      <!-- <el-dialog
        title="Identification"
        :visible.sync="showIdentification"
        :close-on-click-modal="false"
        width="1400px"
        :limit="1"
        @closed="dialogClosed"
      >
        <identificationForm
          :identification="identification"
          :is-edit="isEdit"
        />
      </el-dialog> -->
    </div>
  </div>
</template>

<script>
import { dataMixin, uploadMixin } from "@/common/js/mixin";
import routes from "@/common/js/config/routes";

import AssetsPage from "@/views/project/task/components/assets-page";
import identificationForm from ".././identification/identificationForm/identificationForm";

import { dateFtt } from "@/common/js/utils";
import {
  queryCases,
  updateCases,
  deleteCases,
  queryWarning,
  uploadRequest,
} from "../../../api/api";
import { API_HOST } from "@/common/js/config/host";

export default {
  // mixins include handleCurrentPage, and add handleCurrentSize
  mixins: [dataMixin, uploadMixin],
  data() {
    return {
      isEdit: false,

      identification: {
        audit_information: {
          date: "", //
          audit_id: "asd", //
          facility_id: "",
        },
        facility_information: {
          organisation_name: "",
          oncology_service: "",
          oncology_facility: "",
          facility_representatives: [
            {
              role: "",
              title: "",
              first_name: "",
              last_name: "",
              phone: "",
              email: "",
            },
          ],
          physical_address: {
            building: "", //
            street: "", //
            suburb: "",
            state_name: "",
            post_code: "",
          },
          reporting_address: {
            building: "",
            street: "",
            suburb: "",
            state_name: "",
            post_code: "",
          },
        },
        treatment_machine: {
          manufacturer: "",
          unit_model: "",
          unit_serial_number: "",
          local_name: "",
          tube_insert_type: "",
          tube_serial_number: "",
        },
        reference_dosimetry: {
          protocol: "",
          nk: "",
          comments: "",
        },
        kv_reference_dosimetry: {
          protocol: "",
          reference_chamber: "",
          air_phantom: "",
          reference_depth: "",
          comments: "",
        },
        beam_data: [
          {
            //
            beam_id: "", //
            beam_id_alt: "", // new add
            nom_energy: "",
            scd: "",
            field_size_at_scd: "",
            hvl_nominal_mm_al: "",
            hvl_nominal_mm_cu: "",
            hvl_measured_mm_al: "",
            hvl_measured_mm_cu: "",
          },
        ],
        reference_cone: {
          beams_data: [
            {
              nom_energy: "", // new add
              reference_cone_id_alt: "",
              ssd: "",
              depth: "",
              geometry_shape: "",
              geometry_measurement: "",
              open_closed: "",
              thickness: "",
              dosp_ssd: "", //
            },
          ],
        },
        cone: [
          {
            cone_id: "", // new
            cone_id_alt: "", // new
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "", //
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "", //
                dosp_ssd: "", //
                nom_dose_output: "", //
              },
            ],
          },
          {
            cone_id: "", // new
            cone_id_alt: "", // new
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "", //
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "", //
                dosp_ssd: "", //
                nom_dose_output: "", //
              },
            ],
          },
          {
            cone_id: "", // new
            cone_id_alt: "", // new
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "", //
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "", //
                dosp_ssd: "", //
                nom_dose_output: "", //
              },
            ],
          },
        ],
      },

      showIdentification: false,
      action: API_HOST + "audit/upload",
      name: "",

      pageIndex: 1,
      pageSize: 10,

      searchForm: {
        audit_id: "",
        clinic: "",
        status: "",
        date: "",
      },

      formData: {
        audit_id: "",
        clinics: "",
        date: "",
        status: "",
      },

      tableData: [
        {
          audit_id: "",
          clinic: "",
          date: "",
          status: "",
        },
      ],

      excel: {
        url: "",
        size: "",
      },

      formStudents: [],

      statusOption: ["Progressing", "Finished"],

      warningTable: [],
    };
  },
  created() {
    this.getPageData();
  },

  methods: {
    async queryWarning(auditNumber) {
      const res = await queryWarning(auditNumber);
      if (res.success) {
        // modify query warning
        const warnings = [];
        for (const warning of res.data) {
          const newObj = new Object();
          Object.keys(warning).forEach((key) => {
            if (key === "coneId") {
              newObj["cone_id"] = warning[key];
            } else if (key === "beamId") {
              newObj["beam_id"] = warning[key];
            } else {
              newObj[key] = warning[key];
            }
          });
          warnings.push(newObj);
        }
        this.warningTable = warnings;
        this.dialogTitle = "Warning";
        this.toggleDialog();
      } else {
        this.queryWarning();
      }
    },

    // async/await function of getIdentification

    tokenReq(option) {
      // console.log(option);
      // console.log(onUploadProgress);

      const file = option.file;
      // console.log(file)
      let fd = new FormData();
      fd.append("file", file);
      // console.log(fd);

      uploadRequest(fd, option)
        .then((response) => {
          this.$message.success(
            // response.data.audit_information.audit_ID +
            " submit successful"
          );
          this.$refs.upload.clearFiles();
          this.toggleDialog();
          this.showIdentification = true;

          // this.identification = response.data;
          // this.$router.push('/management/identification/' + response.data.audit_ID)
          option.onSuccess();
          this.getPageData();
        })
        .catch((error) => {
          this.$message.error(" submit failed ");
          this.$refs.upload.clearFiles();
        });
    },

    // Modify for refreshing the audit cases
    submitSuccess(response) {
      if (response.success === true) {
        this.$message.success(
          // response.data.audit_information.audit_ID +
          " submit successful"
        );
        this.$refs.upload.clearFiles();
        this.toggleDialog();
        this.showIdentification = true;

        // this.identification = response.data;
        // this.$router.push('/management/identification/' + response.data.audit_ID)
      } else {
        // this.$message.error(" submit failed ");
        this.$refs.upload.clearFiles();
      }

      this.getPageData();
    },

    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    formatDate(date) {
      return dateFtt("yyyy-MM-dd", new Date(date));
    },
    showAdd() {
      this.dialogTitle = "New Case";
      this.toggleDialog();
    },
    showEdit(data) {
      this.dialogTitle = "Edit Status";
      this.setFormData(data);
      this.toggleDialog();
    },

    async save() {
      if (!this._checkForm()) {
        return;
      }
      // console.log(this.formData);
      await updateCases(this.formData);
      this.$message.success("update successful");
      this.getPageData();
      this.toggleDialog();
    },

    async handleCancel() {
      // clear properties and refresh the page in curent index and size
      (this.searchForm.audit_id = ""),
        (this.searchForm.clinic = ""),
        (this.searchForm.status = ""),
        (this.searchForm.date = ""),
        this.pageIndex,
        this.pageSize;

      const res = await queryCases(
        this.searchForm.audit_id,
        this.searchForm.clinic,
        this.searchForm.status,
        this.searchForm.date,
        this.pageIndex,
        this.pageSize
      );
      if (res.success) {
        // assign response data to this.tableData
        // console.log(res.data.list);

        // reconstruct the response data
        const newtableDatas = [];
        const newlist = res.data.list;
        for (const obj of newlist) {
          const newtableData = {
            audit_id: obj.auditId,
            clinic: obj.clinic,
            status: obj.status,
            date: obj.date,
          };
          newtableDatas.push(newtableData);
        }

        this.tableData = newtableDatas;
        this.totalNumbers = res.data.total;

        this.pageIndex = res.data.page;
        this.pageSize = res.data.pageSize;
      } else {
        this.getPageData();
      }
    },

    // Edit and Eeconstruct the request gettting from utils(axios request)
    async getPageData() {
      const res = await queryCases(
        this.searchForm.audit_id,
        this.searchForm.clinic,
        this.searchForm.status,
        this.searchForm.date,
        this.pageIndex,
        this.pageSize
      );
      if (res.success) {
        // assign response data to this.tableData
        // console.log(res.data.list);

        // reconstruct the response data
        const newtableDatas = [];
        const newlist = res.data.list;
        for (const obj of newlist) {
          const newtableData = {
            audit_id: obj.auditId,
            clinic: obj.clinic,
            status: obj.status,
            date: obj.date,
          };
          newtableDatas.push(newtableData);
        }

        this.tableData = newtableDatas;
        this.totalNumbers = res.data.total;

        this.pageIndex = res.data.page;
        this.pageSize = res.data.pageSize;
      } else {
        this.getPageData();
      }
    },

    async deletePageData(auditId) {
      const res = await deleteCases(auditId);
      if (res.success) {
        this.$message.success("delete successful");
        this.getPageData();
      } else {
        this.$message.success("delete failed!");
        this.getPageData();
      }
    },

    _checkForm() {
      if (!this.formData.status) {
        this.$message.error("Please select status");
        return false;
      }
      return true;
    },
  },

  components: {
    AssetsPage,
    identificationForm,
  },

  computed: {
    routes() {
      return routes.student;
    },
    indexMethod() {
      return (this.pageIndex - 1) * this.pageSize + 1;
    },
  },
};
</script>

<style lang="scss" scoped>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.class {
  padding: 20px;

  .student-icon {
    font-size: 28px;
  }
}
</style>
