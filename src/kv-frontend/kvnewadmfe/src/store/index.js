import Vue from "vue";
import Vuex from "vuex";

import baseRequest from "@/api/request";
import { loginRequest } from "@/api/user";
Vue.use(Vuex);

// auth module
const moduleAuth = {
  namespaced: true,
  state: { user: null, token: null },
  // change state
  mutations: {
    setUser(state, payload) {
      state.user = payload;
    },
    setToken(state, payload) {
      state.token = payload;
    },
    setLoginState(state, payload) {
      state.token = payload;
    },
  },
  getters: {
    getUser(state) {
      return state.user;
    },
    token(state) {
      return state.token;
    },
    isAuthenticated(state) {
      return !!state.token;
    },
  },

  actions: {
    loginBypass(context, payload) {
      context.commit("setUser", payload);
      // console.log(payload);
    },

    async login(context, payload) {
      // de-structure in the user api parameter
      const response = await loginRequest(payload);

      if (!response.success) {
        console.log("fail to authenticate");
        const error = new Error(
          responseData.message || "Failed to authenticate."
        );
        throw error;
      }
      
      // console.log(response);
      
      // let token = document.cookie.split(";")[1].split("=")[1];
      // let username = document.cookie.split(";")[0].split("=")[1];

      let token = response.data
      // console.log(token, username);

      context.commit("setUser", payload.userName);
      context.commit("setToken", token);
    },

    // log out
    logout(context, payload) {
      // console.log(payload);
      context.commit("setUser", payload);
      context.commit("setToken", payload);
      window.localStorage.clear();
    },
  },
};
// breadcrumb module
const moduleBreadcrumb = {
  namespaced: true,
  state() {
    return {
      currentPathName: "",
    };
  },
  mutations: {
    setPath(state) {
      //localStorage.getItem("currentPathName")
      state.currentPathName = localStorage.getItem("currentPathName");
    },
  },
  getters: {
    currentPathName(state) {
      return state.currentPathName;
    },
  },
};

// beams_data
const moduleOverviewTable = {
  namespaced: true,
  state() {
    return {
      currentBeams: [],
    };
  },
  getters: {
    currentBeams(state) {
      return state.currentBeams;
    },
  },
  mutations: {
    setBeams(state, payload) {
      state.currentBeams = payload;
    },
  },
};

//chambers overview
const chambersInOverviewTable = {
  namespaced: true,
  state() {
    return {
      chamberSn: "",
    };
  },
  getters: {
    getChamberSn(state) {
      return state.chamberSn;
    },
  },
  mutations: {
    SELECTED_CHAMBER(state, payload) {
      state.chamberSn = payload;
    },
  },
  actions: {
    setChamberSn(context, serialNumber) {
      context.commit("SELECTED_CHAMBER", serialNumber);
    },
  },
};

// identification module
const moduleIdentification = {
  namespaced: true,
  state() {
    return {
      // identification object in auditCases index
      identification: {
        // audit_information
        audit_information: {
          date: "", //
          audit_id: "asd", //
          facility_id: "",
        },

        // facility_information
        facility_information: {
          organisation_name: "",
          oncology_service: "",
          oncology_facility: "",
          facility_representatives: [
            {
              role: "",
              title: "",
              first_name: "",
              last_name: "",
              phone: "",
              email: "",
            },
          ],
          physical_address: {
            building: "", //
            street: "", //
            suburb: "",
            state_name: "",
            post_code: "",
          },
          reporting_address: {
            building: "",
            street: "",
            suburb: "",
            state_name: "",
            post_code: "",
          },
        },

        // treatment_machine
        treatment_machine: {
          manufacturer: "",
          unit_model: "",
          unit_serial_number: "",
          local_name: "",
          tube_insert_type: "",
          tube_serial_number: "",
        },

        // reference_dosimetry
        reference_dosimetry: {
          protocol: "",
          nk: "",
          comments: "",
        },

        //kv_reference_dosimetry
        kv_reference_dosimetry: {
          protocol: "",
          reference_chamber: "",
          air_phantom: "",
          reference_depth: "",
          comments: "",
        },

        // beam_data
        beam_data: [
          {
            //
            beam_id: "", //
            beam_id_alt: "", // new add
            nom_energy: "",
            scd: "",
            field_size_at_scd: "",
            hvl_nominal_mm_al: "",
            hvl_nominal_mm_cu: "",
            hvl_measured_mm_al: "",
            hvl_measured_mm_cu: "",
          },
        ],

        // reference_cone
        reference_cone: {
          beams_data: [
            {
              nom_energy: "", // new add
              reference_cone_id_alt: "",
              ssd: "",
              depth: "",
              geometry_shape: "",
              geometry_measurement: "",
              open_closed: "",
              thickness: "",
              dosp_ssd: "", //
            },
          ],
        },

        // cone
        cone: [
          {
            cone_id: "", // new
            cone_id_alt: "", // new
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "", //
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "", //
                dosp_ssd: "", //
                nom_dose_output: "", //
              },
            ],
          },
          {
            cone_id: "", // new
            cone_id_alt: "", // new
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "", //
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "", //
                dosp_ssd: "", //
                nom_dose_output: "", //
              },
            ],
          },
          {
            cone_id: "", // new
            cone_id_alt: "", // new
            open_closed: "",
            end_thickness: "",
            ssd: "",
            dose_output_unit: "", //
            shape: "",
            field_diameter: "",
            field_area: "",
            field_dimension_1: "",
            field_dimension_2: "",
            beams_data: [
              {
                beam_id: "", //
                dosp_ssd: "", //
                nom_dose_output: "", //
              },
            ],
          },
        ],
      },
    };
  },

  // getters of identification
  getters: {
    identification(state) {
      // return identification object
      return state.identification;
    },
  },

  // mutation of identification
  mutations: {
    setIdentification(state, payload) {
      return (state.identification = payload);
    },
  },
};

const store = new Vuex.Store({
  modules: {
    breadcrumb: moduleBreadcrumb,
    overview: moduleOverviewTable,
    identification: moduleIdentification,
    auth: moduleAuth,
    chamber: chambersInOverviewTable,
  },
});

export default store;
