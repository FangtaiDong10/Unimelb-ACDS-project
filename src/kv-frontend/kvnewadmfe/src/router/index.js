import store from "@/store";
import { Breadcrumb } from "element-ui";
import Vue from "vue";
import VueRouter from "vue-router";
import UserAuth from "@/views/login/index.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/home",
    name: "Home",
    component: () => import("../views/Management.vue"),
    children: [],
  },

  {
    path: "/",
    name: "Management",
    redirect: (to) => {
      if (store.getters["auth/isAuthenticated"]) {
        return { path: "/auditcases" };
      } else {
        return { path: "/auth" };
      }
    },
    // redirect: "/auditcases",
    component: () => import("../views/Management.vue"),
    children: [
      // testing purpose
      {
        path: "audit",
        name: "Managment  /  Audit Cases",
        component: () => import("../views/Audit.vue"),
      },

      {
        path: "/auditcases",
        name: "Management  /  AuditCases",
        component: () => import("@/views/management/auditCases/index.vue"),
      },

      // Identification
      {
        path: "/audit/identification/:auditNumber",
        name: "Audit / Identification",
        component: () => import("@/views/management/identification/"),
      },

      // Case Specification
      {
        path: "/audit/caseSpecification/:auditNumber",
        name: "Audit / CaseSpecification",
        backPath: "/auditcases",
        meta: {
          backPath: "/auditcases",
          title: "Case Specification",
        },
        component: () => import("@/views/management/caseSpecification/"),
      },

      // Lookup Table
      {
        path: "lookup",
        name: "Management  /  Lookup Table",
        component: () => import("@/views/management/lookup/"),
      },

      // Change Password
      {
        path: "changePw",
        name: "Change Password",
        component: () =>
          import("@/views/project/task/components/change-password.vue"),
      },
    ],
  },

  {
    path: "/auth",
    name: "auth",
    component: UserAuth,
  },

  // {
  //   path: "/about",
  //   name: "about",
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

// for router, do the router guard, which record the situation of the route
router.beforeEach((to, from, next) => {
  // set destination name to the localStorage variable
  // for implementing it in Header component

  localStorage.setItem("currentPathName", to.name);

  // for trigger the data update in store
  store.commit("breadcrumb/setPath");

  // keeping login
  store.commit("auth/setLoginState", localStorage.getItem("token"));

  // protect not back to /auth once login state activated
  if (store.getters["auth/isAuthenticated"] && to.path === "/auth") {
    next("/");
  }

  // start URLs protection
  if (to.path === "/" && !store.getters["auth/isAuthenticated"]) {
    // release route
    console.log(store.getters["auth/isAuthenticated"]);
    next();
  } else if (to.path !== "/" && to.path !== "/auth") {
    // if refresh the page, data in vuex will automatically be cleared --> next(false)
    if (!store.getters["auth/isAuthenticated"]) {
      console.log("not root or auth path and not authenticated");
      next("/");
    } else {
      // isAuthenticated & URL not to root Or auth
      next();
    }
  } else {
    // console.log(from, to);
    console.log(store.getters["auth/isAuthenticated"]);
    // console.log("auth");
    next();
  }
});

export default router;
