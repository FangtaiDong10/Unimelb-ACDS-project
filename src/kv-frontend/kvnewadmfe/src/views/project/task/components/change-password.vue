<template>
  <div>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      status-icon
      style="margin-top: 50px"
      label-width="140px"
      class="demo-ruleForm"
    >
      <el-form-item><h1>Please Input New Password</h1></el-form-item>
      <el-form-item></el-form-item>
      <el-form-item label="Password" prop="pass">
        <el-input
          style="width: 300px"
          v-model="ruleForm.pass"
          type="password"
          autocomplete="off"
          show-password
        />
      </el-form-item>
      <el-form-item label="Confirm" prop="checkPass">
        <el-input
          style="width: 300px"
          v-model="ruleForm.checkPass"
          type="password"
          autocomplete="off"
          show-password
        /> </el-form-item
      ><el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')"
          >Change</el-button
        >
        <el-button @click="resetForm('ruleForm')">Reset</el-button>
      </el-form-item></el-form
    >
  </div>
</template>
<script>
import { Message } from "element-ui";
import { changePassword } from "@/api/user";
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please input the password"));
      } else {
        if (this.ruleForm.checkPass !== "") {
          this.$refs.ruleForm.validateField("checkPass");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please input the password again"));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error("Two inputs don't match!"));
      } else {
        callback();
      }
    };
    return {
      dialogFormVisible: false,
      ruleForm: { pass: "", checkPass: "" },
      rules: {
        pass: [{ validator: validatePass, trigger: "blur" }],
        checkPass: [{ validator: validatePass2, trigger: "blur" }],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          Message.success("Successfully change the Password, Please Login!");
          const currentUser = window.localStorage.getItem("username");
          const newPw = this.ruleForm.pass;
          await changePassword(currentUser, newPw);
          this.$store.dispatch("auth/logout", null);
          this.$router.replace("/auth");
        } else {
          console.log("error ");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>
