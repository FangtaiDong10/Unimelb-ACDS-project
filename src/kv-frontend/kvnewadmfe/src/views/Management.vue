<template>
  <el-container style="height: 100vh">
    <!--Aside Bar-->
    <el-aside
      :width="sideWidth + 'px'"
      style="
        background-color: rgb(238, 241, 246);
        height: 100%;
        box-shadow: 2px 0 6px rgb(0 21 41);
      "
    >
      <Aside :isCollapsed="isCollapsed" :logoTextShow="logoTextShow"></Aside>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc">
        <Header
          :collapseBtnClass="collapseBtnClass"
          :collapse="collapse"
        ></Header>
      </el-header>

      <el-main>
        <!-- Current Page child route will be appeared in the router-view -->
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
// import request from "@/utils/request";
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
  name: "HomeView",
  data() {
    return {
      collapseBtnClass: "el-icon-s-fold",
      isCollapsed: false,
      sideWidth: 200,
      logoTextShow: true,
    };
  },

  components: {
    Aside,
    Header,
  },

  methods: {
    collapse() {
      // @click triggering this method (fold)
      this.isCollapsed = !this.isCollapsed;
      if (this.isCollapsed) {
        this.sideWidth = 64;
        this.collapseBtnClss = "el-icon-s-unfold";
        this.logoTextShow = false;
      } else {
        // button back to unfolder status (unfold)
        this.sideWidth = 200;
        this.collapseBtnClss = "el-icon-s-fold";
        this.logoTextShow = true;
      }
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.ml5 {
  margin-left: 5px;
}

.mr5 {
  margin-right: 5px;
}

.pd10 {
  padding: 10px;
}

body {
  height: 100%;
  -moz-osx-font-smoothing: grayscale;
  -webkit-font-smoothing: antialiased;
  text-rendering: optimizeLegibility;
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB,
    Microsoft YaHei, Arial, sans-serif;
}
</style>
