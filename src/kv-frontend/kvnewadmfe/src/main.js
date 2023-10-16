import Vue from "vue";

import App from "./App.vue";
import router from "./router";
import store from "./store/index";
import ElementUI from "element-ui";
import "./plugins/element.js";
import "./common/scss/index.scss";

import locale from "element-ui/lib/locale/lang/en";
import "element-ui/lib/theme-chalk/index.css";

// import request from "@/utils/request";

Vue.use(ElementUI, { locale, size: "small" });
Vue.config.productionTip = false;

// for testing api
// Vue.prototype.request = request;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
