import { baseInstance } from "./request";
import { PAGE_SIZE } from "../common/js/config/query";

//get identification
export function getIdentification(audit_id) {
  return baseInstance({
    url: "/audit/identification/" + audit_id,
    method: "get",
  });
}

export function uploadRequest(file, orgfile) {
  return baseInstance({
    url: "/audit/upload",
    method: "post",
    body: file,
    headers: {
      "Content-Type": "multipart/form-data;",
    },
    transformRequest: [
      function () {
        return file;
      },
    ],
    onUploadProgress: (progressEvent) => {
      let num = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
      orgfile.onProgress({ percent: num });
    },
  });
}

export function uploadLookupReq(file, orgfile) {
  return baseInstance({
    url: "/lookup/upload",
    method: "post",
    body: file,
    headers: {
      "Content-Type": "multipart/form-data;",
    },
    transformRequest: [
      function () {
        return file;
      },
    ],
    onUploadProgress: (progressEvent) => {
      let num = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
      orgfile.onProgress({ percent: num });
    },
  });
}

// query cases
export function queryCases(
  audit_id,
  clinic,
  status,
  date,
  pageIndex,
  pageSize
) {
  return baseInstance({
    url: "/audit/case",
    method: "get",
    params: {
      auditId: audit_id,
      clinic: clinic,
      status: status,
      date: date,
      page: pageIndex,
      pageSize: pageSize,
    },
  });
}
// query table 1
export function queryTable1(auditNumber) {
  return baseInstance({
    url: "/audit/workDataSheet1/" + auditNumber,
    method: "get",
    data: {},
  });
}

// query table 2
export function queryTable2(auditNumber) {
  return baseInstance({
    url: "/audit/workDataSheet2/" + auditNumber,
    method: "get",
    data: {},
  });
}

// query result
export function queryResult(auditNumber) {
  return baseInstance({
    url: "/audit/dataFrontEnd/" + auditNumber,
    method: "get",
  });
}

// query warning
export function queryWarning(auditNumber) {
  return baseInstance({
    url: "/audit/warning/" + auditNumber,
    method: "get",
  });
}

// create cases
export function addCases() {
  return baseInstance({
    url: "/admin/user/admin",
    data: {},
  });
}

// update cases get ForData Object from (auditCases Index)
export function updateCases({ audit_id, clinics, date, status }) {
  return baseInstance({
    url: "/audit/case/" + audit_id,
    method: "post",
    params: {
      status: status,
    },
  });
}

// delete cases
export function deleteCases(auditId) {
  return baseInstance({
    url: "/audit/case/" + auditId,
    method: "delete",
    data: {
      auditId,
    },
  });
}

// save result
// argument data is {TableName1, TableName2 ...}
export function saveResult(auditNumber, data) {
  return baseInstance({
    url: "/audit/dataFrontEnd/" + auditNumber,
    method: "post",
    data: data,
  });
}
