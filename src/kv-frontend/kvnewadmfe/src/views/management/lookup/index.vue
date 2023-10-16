<template>
  <div id="lookup" style="padding: 20px">
    <el-button style="margin-bottom: 20px" type="primary" @click="handleUpdate"
      >Upload</el-button
    >
    <el-collapse v-model="activeNames">
      <el-collapse-item
        name="1"
        v-model="activeNames"
        title="Basic Information"
      >
        <template slot="title">
          <h1 style="color: #3a71a8">BW</h1>
        </template>
        <bw ref="bw" />
      </el-collapse-item>
      <el-collapse-item name="2">
        <template slot="title">
          <h1 style="color: #3a71a8">Farmer</h1>
        </template>
        <farmer ref="farmer" />
      </el-collapse-item>
      <el-collapse-item name="3">
        <template slot="title">
          <h1 style="color: #3a71a8">Mu Rho AL</h1>
        </template>
        <murhoal ref="murhoal" />
      </el-collapse-item>
      <el-collapse-item name="4">
        <template slot="title">
          <h1 style="color: #3a71a8">Mu Rho Cu</h1>
        </template>
        <murhocu ref="murhocu" />
      </el-collapse-item>
      <el-collapse-item name="5">
        <template slot="title">
          <h1 style="color: #3a71a8">Plane Parallel</h1>
        </template>
        <planeParallel ref="planeParallel" />
      </el-collapse-item>
      <el-collapse-item name="6">
        <template slot="title">
          <h1 style="color: #3a71a8">Pstem</h1>
        </template>
        <pstem ref="pstem" />
      </el-collapse-item>
    </el-collapse>

    <div class="dialog-wrapper">
      <el-dialog
        title="Upload"
        :visible.sync="showDialog"
        :close-on-click-modal="false"
        width="600px"
        :limit="1"
        @closed="dialogClosed"
      >
        <div>
          <el-upload
            class="upload-demo"
            drag
            :limit="1"
            accept=".xls,.xlsx,.xlsm"
            ref="upload"
            :action="action"
            :http-request="uploadLookup"
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
            style="margin-left: 10px"
            size="small"
            type="success"
            @click="submitUpload"
            >submit</el-button
          >
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import murhoal from "./murhoal";
import murhocu from "./murhocu";
import planeParallel from "./planeParallel";
import farmer from "./farmer";
import bw from "./bw";
import pstem from "./pstem";
import { API_HOST } from "@/common/js/config/host";
import { uploadLookupReq } from "../../../api/api";
export default {
  name: "lookup",
  methods: {
    async getPageData() {
      this.$refs.bw.getPageData();
      this.$refs.farmer.getPageData();
      this.$refs.murhoal.getPageData();
      this.$refs.murhocu.getPageData();
      this.$refs.planeParallel.getPageData();
      this.$refs.pstem.getPageData();
    },
    submitUpload() {
      this.$refs.upload.submit();
    },

    uploadLookup(option) {
      console.log(option);
      // console.log(onUploadProgress);

      const file = option.file;
      // console.log(file)
      let fd = new FormData();
      fd.append("file", file);
      // console.log(fd);
      uploadLookupReq(fd, option)
        .then((res) => {
          this.$message.success("submit successful");
          this.$refs.upload.clearFiles();
          // this.toggleDialog();
          // this.showIdentification = true;
          this.getPageData();
          // this.identification = response.data;
          // this.$router.push('/management/identification/' + response.data.audit_ID)
          option.onSuccess();
        })
        .catch((error) => {
          this.$message.error(" submit failed ");
          this.$refs.upload.clearFiles();
        });
    },

    submitSuccess(response) {
      if (response.success === true) {
        this.$message.success("submit successful");
        this.$refs.upload.clearFiles();
        this.dialogClosed();
      } else {
        this.$message.error("submit failed ");
        this.$refs.upload.clearFiles();
      }
      this.getPageData();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    dialogClosed() {
      this.showDialog = false;
    },
    handleUpdate() {
      this.showDialog = true;
    },
  },
  data() {
    return {
      action: API_HOST + "/lookup",
      showDialog: false,
      // activeNames: ['1', '2', '3', '4']
    };
  },
  components: {
    murhoal,
    murhocu,
    planeParallel,
    farmer,
    bw,
    pstem,
  },
};
</script>

<style scoped></style>
