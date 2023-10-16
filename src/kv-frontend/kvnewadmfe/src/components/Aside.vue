<template>
  <el-menu
    :default-openeds="['1']"
    style="height: 100%; overflow-x: hidden"
    background-color="rgb(48,65,86)"
    text-color="#fff"
    active-text-color="#ffd04b"
    :collapse-transition="false"
    :collapse="isCollapsed"
    router
  >
    <!--   ACDS Logo here     -->
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img
        alt=""
        src="../assets/system_only_logo.png"
        style="width: 55px; position: relative; top: 15px"
      />
      <b style="color: aliceblue" v-show="logoTextShow"
        ><span style="margin-left: 1px"></span>ACDS System</b
      >
    </div>

    <!--Home Page-->`
    <el-menu-item index="/home">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">Home</span>
      </template>
    </el-menu-item>

    <!--Management 2-->
    <el-submenu index="2" >
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">Management</span>
      </template>
      <el-menu-item index="/auditcases">
        <i class="el-icon-s-custom"></i>
        <span slot="title">Audit Cases</span>
      </el-menu-item>
      <el-menu-item index="/lookup">
        <i class="el-icon-files"></i>
        <span slot="title">Lookup Table</span>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapsed: Boolean,
    logoTextShow: Boolean,
  },
};
</script>

<style scoped>
</style>
