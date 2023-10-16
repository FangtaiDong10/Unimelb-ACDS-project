<template>
  <div>
    <!--Search Bars-->
    <div style="padding: 10px 0">
      <el-input
        style="width: 200px"
        placeholder="Search Audit Number..."
        suffix-icon="el-icon-s-custom"
        v-model="auditId"
      ></el-input>
      <el-input
        style="width: 200px"
        placeholder="Search Clinics..."
        suffix-icon="el-icon-house"
        class="ml5"
        v-model="clinic"
      ></el-input>
      <el-input
        style="width: 200px"
        placeholder="Select Status..."
        suffix-icon="el-icon-star-off"
        class="ml5"
        v-model="status"
      ></el-input>

      <!-- Search Button -->
      <el-button class="ml5" type="primary" @click="load">Search</el-button>

      <!-- Reset Button -->
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <!--CRUD template here-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd"
        >Add <i class="el-icon-circle-plus"></i
      ></el-button>

      <!-- Double check for batch delete manipulation -->
      <el-popconfirm
        style="margin-left: 5px"
        confirm-button-text="Delete"
        cancel-button-text="Cancel"
        title="Are you sure to batch delete all selected data?"
        @confirm="delBatch"
      >
        <el-button type="danger" slot="reference"
          >Delete All <i class="el-icon-remove-outline"></i
        ></el-button>
      </el-popconfirm>

      <el-upload
        action="http://localhost:9090/user/import"
        :show-file-list="false"
        :on-success="handleExcelImportSuccess"
        :accept="xlsx"
        style="display: inline-block"
      >
        <el-button type="primary" style="margin-left: 5px"
          >Import <i class="el-icon-bottom"></i
        ></el-button>
      </el-upload>

      <el-button type="primary" @click="exp" style="margin-left: 5px"
        >Export <i class="el-icon-top"></i
      ></el-button>
    </div>

    <!--Main Table-->
    <el-table
      :data="tableData"
      border
      stripe
      :header-cell-class-name="headerBg"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>

      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column
        prop="username"
        label="UserName"
        width="140"
      ></el-table-column>
      <el-table-column
        prop="nickname"
        label="NickName"
        width="120"
      ></el-table-column>
      <el-table-column prop="email" label="Email"> </el-table-column>
      <el-table-column prop="phone" label="Phone"> </el-table-column>
      <el-table-column prop="address" label="Address"> </el-table-column>

      <!--Manipulate in the table column-->
      <el-table-column label="Manipulation">
        <template slot-scope="scope">
          <!-- Edit button in the column -->
          <el-button
            @click="handleEdit(scope.row)"
            style="background-color: lightskyblue"
            >Edit <i class="el-icon-edit"></i
          ></el-button>

          <!-- Delete button in the column -->
          <!-- Not user properties
                :icon="InfoFilled"
                icon-color="red" -->
          <el-popconfirm
            style="margin-left: 5px"
            confirm-button-text="Delete"
            cancel-button-text="Cancel"
            title="Are you sure to delete this data?"
            @confirm="handleDel(scope.row.id)"
          >
            <el-button type="danger" slot="reference"
              >Delete <i class="el-icon-delete"></i
            ></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--Home Pagination-->
    <div style="padding: 20px 0">
      <!--          temperary delete
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page.sync="currentPage4"-->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </div>

    <!--Dialog  Initial dialogFormVisible is equal false)-->
    <el-dialog
      :visible.sync="dialogFormVisible"
      title="User Information"
      width="30%"
    >
      <el-form label-width="80px" size="small">
        <el-form-item label="Username">
          <el-input v-model="form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Nickname">
          <el-input v-model="form.nickname" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Phone">
          <el-input v-model="form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Address">
          <el-input v-model="form.address" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="save">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Audit",
  data() {
    return {
      tableData: [],
      total: 0,
      
      pageNum: 1,
      pageSize:10,

      username: "",
      email: "",
      address: "",

      // Dialog
      form: {},
      dialogFormVisible: false,

      // CheckBox
      multipleSelection: [],
      headerBg: "tHeaderBg",
    };
  },
  created() {
    this.load();
  },
  methods: {
    // encapsulate fetching method to load different paras data from back-end
    load() {
      this.request
        .get("/user/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.username,
            email: this.email,
            address: this.address,
          },
        })
        .then((res) => {
          // console.log(res);
          this.tableData = res.records;
          this.total = res.total;
        });
    },

    // Saving what user input to database
    save() {
      // the res (object) in .then is the object fetched from Back-End
      // Back-End return True or False
      this.request.post("/user", this.form).then((res) => {
        if (res) {
          this.$message.success("Save Successfully");
          // close the dialog
          this.dialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("Save Failed");
        }
      });
    },

    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },

    handleEdit(row) {
      // put row data into form object
      this.form = row;
      this.dialogFormVisible = true;
    },

    handleDel(id) {
      this.request.delete("/user/" + id).then((res) => {
        if (res) {
          this.$message.success("Delete Successfully");
          this.load();
        } else {
          this.$message.error("Delete Failed");
        }
      });
    },

    handleSelectionChange(val) {
      // console.log(val);
      // assign selections and para-val is an object catched by checkbox selection
      this.multipleSelection = val;
    },

    // handling the val which stored in multipleSelection --> transdering to ids List
    delBatch() {
      let ids = this.multipleSelection.map((v) => v.id); // [{},{},{}] --> [1,2,3]
      this.request.post("/user/del/batch", ids).then((res) => {
        if (res) {
          this.$message.success("Batch Delete Successfully");
          this.load();
        } else {
          this.$message.error("Batch Delete Failed -- Please Select First");
        }
      });
    },

    reset() {
      this.username = "";
      this.email = "";
      this.address = "";
      this.load();
    },

    handleSizeChange(pageSize) {
      // console.log(pageSize);
      this.pageSize = pageSize;
      this.load();
    },

    handleCurrentChange(pageNum) {
      // console.log(pageNum);
      this.pageNum = pageNum;
      this.load();
    },

    exp() {
      window.open("http://localhost:9090/user/export");
    },

    handleExcelImportSuccess() {
      this.$message.success("Import Successfully")
      this.load()
    },
  },
};
</script>

<style scoped>
.tHeaderBg {
  background: lightgray !important;
}

</style>
