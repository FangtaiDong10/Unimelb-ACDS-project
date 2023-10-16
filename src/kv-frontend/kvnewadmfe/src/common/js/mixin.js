import axios from "axios";
import { Message } from "element-ui";
import { dateFtt } from "./utils";

export const dataMixin = {
  data() {
    return {
      tableData: [],
      multipleSelection: [],
      showDialog: false,
      dialogTitle: "",
      totalNumbers: 0,
    };
  },
  methods: {
    handleSelectionChange(val) {
      const selectData = val.map((item) => item.id);
      this.multipleSelection = selectData;
    },
    handleDelete() {
      if (this.multipleSelection.length < 1) {
        this.$message.error("At least one item");
        return;
      }
      this.$messageBox
        .confirm("Are you sure?", "Notice", {
          confirmButtonText: "Yes",
          cancelButtonText: "No",
          type: "warning",
        })
        .then(() => {
          this.deletePageData && this.deletePageData();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "cancel",
          });
        });
    },

    // handle current page
    handleCurrentChange(currentPage) {
      this.pageIndex = currentPage;
      this.getPageData();
    },

    // handle current size
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getPageData();
    },

    handleSearch() {
      // handleSearch initialization
      this.pageIndex = 1;

      this.getPageData && this.getPageData();
    },

    // overwrote in main component
    handleCancel() {
      Object.keys(this.searchForm).forEach((key) => {
        if (key === "pageIndex") {
          this.searchForm[key] = 1;
        } else {
          this.searchForm[key] = "";
        }
      });
      this.getPageData && this.getPageData();
    },

    dialogClosed() {
      if (this.formData.id) {
        delete this.formData.id;
      }
      Object.keys(this.formData).forEach((key) => {
        this.formData[key] = "";
      });
    },
    toggleDialog() {
      this.showDialog = !this.showDialog;
    },

    formatTime(time) {
      return dateFtt("yyyy-MM-dd hh:ss:mm", new Date(time));
    },
    
    setFormData(data) {
      Object.keys(this.formData).forEach((key) => {
        this.formData[key] = data[key] || "";
      });
      this.formData.id = data.id;
    },
  },
};

export const uploadMixin = {
  data() {
    return {};
  },
  methods: {
    handleBeforeUpload(file) {
      let canUpload = true;
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        canUpload = false;
        this.$message.error("上传图片大小不能超过10MB!");
      }
      return canUpload;
    },

    async ossUpload(content) {
      let { type, name } = content.file;
      let splitArr = name.split(".");
      const postfix = splitArr[splitArr.length - 1];
      const oss = await getOssUrl(type, postfix);
      const url = oss.data;
      axios({
        url: url,
        method: "put",
        data: content.file,
        timeout: 0,
        headers: {
          "Content-Type": content.file.type,
        },
        onUploadProgress: (event) => {
          if (event.lengthComputable) {
            // 属性lengthComputable表示总共需要完成的工作量和当前工作量是否被检查到
            content.onProgress(event);
          }
        },
      })
        .then((response) => {
          if (response.status === 200) {
            content.onSuccess(url.split("?")[0]);
          } else {
            Message.error("文件上传失败");
          }
        })
        .catch((error) => {
          console.log(error);
          Message.error("文件上传失败");
        });
    },

    scaleImage(url, width = 100, height = 100) {
      return url + `?x-oss-process=image/resize,m_fixed,h_${height},w_${width}`;
    },
  },
};
