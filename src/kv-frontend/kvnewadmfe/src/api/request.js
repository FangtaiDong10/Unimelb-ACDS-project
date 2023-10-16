import axios from "axios";
import { Message } from "element-ui";
import { API_HOST } from "@/common/js/config/host";
import store from "@/store";
import { setTimeout } from "core-js";
import router from "@/router"; 

axios.defaults.retry = 4;
axios.defaults.retryDelay = 1000;

const baseInstance = axios.create({
  baseURL: API_HOST,
  timeout: 50000,
  method: "post",
  withCredentials: true,
});

baseInstance.interceptors.request.use(
  (config) => {
    let tokenWTF = window.localStorage.getItem("token");
    if (tokenWTF) {
      config.headers.token = tokenWTF;
      return config;
    }
    return config;
  },
  (error) => {
    console.log(error);
    Message.error(
      "Network Error on sending request, Please wait. Details: " + error.message
    );
    return Promise.reject(error);
  }
);

baseInstance.interceptors.response.use(
  (response) => {
    const res = response.data;

    if (res.success) {
      return res;
    } else {
      // Message.error(res.message || 'Network Error when requesting url Please wait')
      return false;
    }
  },
  async (error) => {
    // console.log(error,typeof error,error.response.status);

    if (error.response.status === 401) {
      store.commit("auth/setToken", null);
      window.localStorage.clear();
      router.replace("/");
    }

    Message.error(
      "Network Error on receiving request, Please wait. Detail: " +
        error.message
    ); // promise

    var config = error.config;
    var backoff = new Promise(function (resolve) {
      setTimeout(function () {
        resolve();
      }, config.retryDelay || 1);
    });
    await backoff;

    // window.location.assign("https://kv-redback.tk/");
    return axios(config);
  }
);

export { baseInstance };
