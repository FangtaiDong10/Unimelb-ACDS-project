import axios from "axios";

// initialize request object
const request = axios.create({
  // Attention!! The prefix '/ api' is added to the global unification,
  // that is to say, all interfaces will be prefixed with '/ api'.
  // Don't add '/ api' when writing interfaces in the page,
  // otherwise there will be two '/ API',
  // such as' / api / api / user '.
  baseURL: "http://back.kv-redback.tk:5000",
  timeout: 5000,
});

// request interceptor
// handling the request before sending to the Back-End
// like add TOKEN to every request --> for encrytion
request.interceptors.request.use(
  (config) => {
    //   set request header --> Content-Type
    config.headers["Content-Type"] = "application/json;charset=utf-8";

    // config.headers['token'] = user.token;  // set request header
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// response interceptor which comming from Back-End.
// The results can be processed uniformly after the interface responds.
request.interceptors.response.use(
  (response) => {

    //  receive response DATA and assign them to variable res
    let res = response.data;
    
    // if the return-back response is File type  --> directly return
    if (response.config.responseType === "blob") {
      return res;
    }

    // String DATA returned by compatible server  --> Do JSON.parse()
    if (typeof res === "string") {
      res = res ? JSON.parse(res) : res;
    }
    return res;
  },

  // if error, print error Log
  (error) => {
    console.log("err" + error); // for debug
    return Promise.reject(error);
  }
);

// export request object
export default request;
