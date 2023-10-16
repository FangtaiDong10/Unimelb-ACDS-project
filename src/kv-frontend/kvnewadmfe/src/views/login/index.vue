<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :rules="loginRules"
      :model="loginForm"
      class="login-form"
      auto-complete="on"
    >

    <!--   ACDS Logo here     -->
   
      <img
        alt="logo"
        src="../../assets/system_only_logo.png"
        style="width: 220px; position: relative; top: 15px; margin-left: auto; margin-right: auto; display: block;"
      />


      <h1 class="title">ACDS Audit System</h1>

      <!-- Account Input ! -->
      <el-form-item prop="account">
        <el-input
          size="large"
          placeholder="Account"
          prefix-icon="el-icon-s-custom"
          v-model="loginForm.userName"
        >
        </el-input>
      </el-form-item>

      <!-- Password Input !-->
      <el-form-item prop="password">
        <el-input
          size="large"
          placeholder="Password"
          type="password"
          prefix-icon="el-icon-lock"
          v-model="loginForm.password"
          @keyup.enter.native="handleLogin"
        >
        </el-input>
      </el-form-item>

      <div>
        <el-button
          :loading="loading"
          type="primary"
          size="large"
          class="login-btn"
          @click.native.prevent="handleLogin"
        >
          Login
        </el-button>
      </div>

      <!-- <div>
        <el-button
          :loading="signuploading"
          type="primary"
          size="large"
          class="login-btn"
          @click.native.prevent="handleSignUp"
        >
          Sign up
        </el-button>
      </div> -->
    </el-form>
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      // TODO: REMOVE THE TEST INPUT
      loginForm: {
        userName: "",
        password: "",
      },

      loginRules: {
        userName: [
          {
            required: true,
            message: "Username cannot be NULL",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "Password cannot be NULL",
            trigger: "blur",
          },
        ],
      },

      loading: false,
      // sighuploading: false,

      showDialog: false,
    };
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((validate) => {
        if (validate) {
          this.loading = true;
          // Note: This action defined in action.js
          // TODO: change bypass_login into login

          // change back to Login
          this.$store.dispatch("auth/login", this.loginForm).then(
            () => {
              // user Object with username and token
              const user = this.$store.getters["auth/getUser"];
              localStorage.setItem("username", user)
              // console.log(user);
              
              const token = this.$store.getters["auth/token"];
              // console.log(token);
              // token set to requests and keep login state.
              localStorage.setItem("token", token);
              
              this.$router.replace("/");
              // console.log(this.$store.state.auth);
              // console.log(this.$store.getters['auth/getUser']);

              this.loading = false;
            },
            () => {
              this.loading = false;
            }
          );
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss">
/*重置element*/
$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

.login-container {
  .el-input {
    input {
      background: transparent;
      border: none;
      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  height: 100%;
  width: 100%;
  background-color: $bg;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;

    .title {
      margin-bottom: 30px;
      text-align: center;
      color: #fff;
    }

    .login-btn {
      margin-top: 20px;
      width: 100%;
    }
  }
}
</style>
