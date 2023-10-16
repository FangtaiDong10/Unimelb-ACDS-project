<template>
  <div style="line-height: 60px; display: flex; margin-left: 10px">
    <div style="flex: 1">
      <span
        :class="collapseBtnClass"
        style="cursor: pointer; font-size: 20px"
        @click="collapse"
      ></span>
      <el-breadcrumb
        separator="/"
        style="display: inline-block; margin-left: 20px"
      >
        <el-breadcrumb-item :to="'/'">ACDS</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
        <!-- Dynamic breadcrumb  change: use v-for-->
        <!-- <el-breadcrumb-item>Management</el-breadcrumb-item> -->
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 100px; cursor: pointer">
      <span>Hello {{ currentUserName }}</span
      ><i class="el-icon-arrow-down" style="top: 5px; margin-left: 5px"></i>
      <el-dropdown-menu style="text-align: center" slot="dropdown">
        <el-dropdown-item>
          <el-button @click="changePw">Change Password</el-button>
        </el-dropdown-item>
        <el-dropdown-item style="margin-top: 10px">
          <el-button @click="logout">Log Out</el-button>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Function,
    pathName: String,
  },

  // handling BreadCrumb Issue
  computed: {
    // set currentPathName computed property
    currentPathName() {
      // console.log(this.$store.getters['breadcrumb/currentPathName'])
      return this.$store.getters["breadcrumb/currentPathName"];
    },
    currentUserName() {
      return window.localStorage.getItem("username");
    },
  },

  methods: {
    logout() {
      this.$store.dispatch("auth/logout", null);
      this.$router.replace("/auth");
    },
    changePw() {
      // this.$emit("changePw")]
      this.$router.replace("/changePw");
    },
  },

  // watch function for listening the event change
  // listening the event of route changing = listening the currentPathName var.
  watch: {
    currentPathName(newVal, oldVal) {
      // console.log(newVal)
    },
  },
};
</script>
