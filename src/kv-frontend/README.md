# KV-Frontend

This is the frontend of Project VIOLET.

## Requirements

- Node.js

   

## Config

See **kv-frontend/kvnewadmfe/.env.prod**

For more, see [Configuration Reference](https://cli.vuejs.org/config/).



## Code Structure

In the frontend we have put different components to different directory to help manage the project. All documents are stored in the "/scr" folder. The structure is as following while the symbol — means the next level of the folder, same as "./"

- **api**: folder with different api requests. all kinds of api are import request.js as the main Axios baseIntance api instance. 
- **assets**: ACDS logo, which is designed and used for the ACDS system side bar component. 
- **common**:
  - **js**:
    - **config**: this folder contains the basic configuration of HOST information and basic routes,js 
    - **utils**: this folder contains the form handler object and index.js which is used for the dataFtt() date formating function. 
    - **mixins.js**: an importance document which contain the dataMixin function and uploadMixin function, which is used for the common functions integration to enhance the reusability of important functions of code. 
  - **scss**: folder with SCSS global css styling for different Vue components. 
- **components**: the folder with re-used components such as Aside.vue and Header.vue 
- **plugins**: element-ui small plugins injection. 
- **router**: for managing the different routers with parents and children URLs 
- **store**: an important folder for Vuex to manage different modules status with standard state, mutations, getters and actions(handling async requests)
- **views**: main folder for router-view page appearance vue components 
  - **login**: login entry point 
  - **management**: management pages for fulfilling clients' requirements 
    - **auditCases**: Table for different auditcases input from clients. 
    - **caseSpecification**: this folder contains four components: 1 coneFactors; 2 dataWorkSheet; 3 hvl; 4 ks and all are injected in the index.vue for the purpose of fulfilling measurement and calculation services. 
    - **identification**: all components are integrated in the index.vue component with audit information, facility information, identification form, dosimetry and beams and cones table. 
    - **lookup**: for client checking different table data parameters including six tables: bw.vue; farmer.vue; Al murho; Cu murho; plane parallel and pstem 
  - **project**: with the asset page component used to ensure the function of auditcase table's searching and pagination. 
- **App.vue**: for finally integrations of all router view pages components. 
- **main.js**: for mounting the Vue application to the DOM.



## Setup
```shell
$ cd kv-frontend/kvnewadmfe
$ npm install
```

### Compiles and hot-reloads for production
```shell
$ npm run serve-prod
```

### Compiles and minifies for production
```shell
$ npm run build
```
