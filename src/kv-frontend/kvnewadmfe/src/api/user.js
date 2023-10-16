import { baseInstance } from "./request";
import store from "@/store";

// login api (data = body(in http))
export function loginRequest({ userName, password }) {
  return baseInstance({
    url: "/login",
    data: {
      username: userName,
      password,
    },
  });
}

export function changePassword(username, password) {
  return baseInstance({
    url: "/password",
    method: "post",
    data: {
      username,
      password,
    },
  });
}
