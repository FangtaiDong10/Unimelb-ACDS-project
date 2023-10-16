<template>
  <div class="assets-page">
    <div class="search-wrapper">
      <el-form :model="searchForm" :inline="true">
        <slot name="search"></slot>
        <el-form-item>
          <el-button @click="handleSearch" type="primary">Search</el-button>
          <el-button @click="handleCancelSearch" type="warning"
            >Reset</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <div class="operate-wrapper">
      <el-button type="primary" @click="handleAdd" v-if="canAdd">Add</el-button>
      <!-- <el-button type="danger" @click="handleDelete" v-if="canDelete"
        >Delete</el-button
      > -->
    </div>
    <div class="table-wrapper">
      <slot name="table"> </slot>
    </div>
    <!-- pagination in Assets-Page component -->
    <!-- <pagination :total="total" :current-page="currentPage" @current-change="currentChange"></pagination> -->
    <!-- Pagination-->
    <div style="padding: 20px 0">
      <!--          temperary delete
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page.sync="currentPage4"-->
      <el-pagination
        :total="total"
        :current-page="currentPage"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
// import Pagination from "@/components/pagination/pagination.vue";

export default {
  name: "assets-page",
  props: {
    total: {
      type: Number,
      default: null,
    },
    searchForm: {
      type: Object,
      default: null,
    },

    currentPage: {
      type: Number,
      default: 1,
    },
    pageSize: {
      type: Number,
      default: 10,
    },

    canAdd: {
      type: Boolean,
      default: true,
    },
    // canDelete: {
    //   type: Boolean,
    //   default: true,
    // },
  },
  methods: {
    handleCurrentChange(currentPage) {
      this.$emit("current-change", currentPage);
    },
    handleSizeChange(pageSize) {
      this.$emit("size-change", pageSize);
    },
    handleAdd() {
      this.$emit("data-add");
    },
    // handleDelete() {
    //   this.$emit("data-delete");
    // },
    handleSearch() {
      this.$emit("data-search");
    },
    handleCancelSearch() {
      this.$emit("data-cancel");
    },
  },
  // components: {
  //   Pagination,
  // },
};
</script>

<style lang="scss" scoped>
.assets-page {
  padding-top: 30px;
  .table-wrapper {
    margin-top: 30px;
  }
}
</style>
