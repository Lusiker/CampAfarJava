<template>
  <div class="container">
    <div class="navheader borderbox" style="border-bottom: none">
      <img @click.stop="goBack" class="back" src="@/assets/img/icon-back.png" />
    </div>
    <div class="password-login-form">
      <div class="password-login-title">登录</div>
      <van-divider />
      <div class="item">
        <div class="name">{{loginMethod ? "邮箱" : "用户名"}}</div>
        <div class="input">
          <div v-if="loginMethod">
            <input
              v-model="loginForm.email"
              class="input-text"
              placeholder="请输入邮箱"
            />
          </div>
          <div v-else>
            <input
                v-model="loginForm.username"
                class="input-text"
                placeholder="请输入用户名"
            />
          </div>
        </div>
        <div class="item" @click.stop="changeMethod">
          <img
              :src="loginMethod ? emailIcon : usernameIcon"
              style="width: 20px; height: 20px"
          />
        </div>
      </div>
      <div class="item">
        <div class="name">密码</div>
        <div class="input">
          <input
            type="password"
            v-model="loginForm.password"
            class="input-text"
            placeholder="请输入密码"
          />
          <img
            @click.stop="clearPassword"
            v-show="loginForm.password"
            src="@/assets/img/new/close.png"
            style="width: 16px; height: 16px"
          />
        </div>
      </div>
      <div id = "register" @click="toregister()">
        <a><span style="color:cornflowerblue;">点击注册</span></a>
      </div>
    </div>
    <div class="box border-box mt-15 pl-60 pr-60">
      <div
        @click.stop="handleLogin"
        v-if="inputAvailable"
        class="btn-confirm active"
      >
        登录
      </div>
      <div v-else class="btn-confirm">登录</div>
    </div>
  </div>
</template>

<script>
import { Toast } from "vant";
export default {
  data() {
    return {
      url: this.$route.query.url || "",
      loginMethod: true,
      loading: false,
      loginForm: {
        email: "",
        username: "",
        password: "",
      },
      usernameIcon: require("@/assets/img/icon-me-h@2x.png"),
      emailIcon: require("@/assets/img/icon-email.png")
    };
  },
  mounted: function () {

  },
  computed: {
    inputAvailable() {
      if(this.loginMethod) {
        return this.loginForm.email.length !== 0 && this.loginForm.password.length !== 0
      } else {
        return this.loginForm.username.length !== 0 && this.loginForm.password.length !== 0
      }
    }
  },
  methods: {
    changeMethod() {
      if(this.loginMethod) {
        this.loginForm.email = ""
      } else {
        this.loginForm.username = ""
      }
      this.loginMethod = !this.loginMethod
    },
    toregister: function () {
      this.$router.push({
        path: "/register",
      });
    },
    // 处理注册登录
    handleLogin: function () {
      if(this.loading) {
        Toast.fail("登录进行中！")
        return;
      }


      this.loading = true
      // 对账号和密码进行校验
      if (!this.loginForm.password) {
        Toast.fail("密码不能为空！");
        this.loading = false
        return;
      }

      //进行登录
      if(this.loginMethod) {
        this.$request
            .post("login/email?email=" + this.loginForm.email + "&password=" + this.loginForm.password)
            .then((res) => {
              this.loading = false

              switch (res.stateEnum.state) {
                case 0: {
                  Toast.fail("登录成功！")
                  this.$store.dispatch('login', res.returnObject)

                  switch (this.$store.getters.intercepted){
                    case 'publish': {
                      this.$router.push({
                        path: '/publish',
                        replace: true
                      })
                      this.$store.dispatch('resetIntercept')
                      break
                    }
                    case 'users': {
                      this.$router.push({
                        path: '/users',
                        replace: true
                      })
                      this.$store.dispatch('resetIntercept')
                      break
                    }
                    default: {
                      this.$router.push({
                        path: '/member',
                        replace: true
                      })
                      this.$store.dispatch('resetIntercept')
                    }
                  }

                  break
                }
                case -1: {
                  if (res.specificCode === 1) {
                    Toast.fail("请求参数有误！")
                    return
                  } else if (res.specificCode === 2) {
                    Toast.fail("用户不存在或已注销！")
                    return
                  } else if (res.specificCode === 3) {
                    Toast.fail("密码错误")
                    return
                  } else {
                    Toast.fail("未知错误！")
                    return
                  }
                }
                case 1: {
                  Toast.fail("服务器出现错误！")
                  return
                }
                default: {
                  Toast.fail("未知错误！")
                  return
                }
              }
            })
      } else {
        this.$request
            .post("/login/usernameLogin", { userName: this.loginForm.username, userPassword: this.loginForm.password, })
            .then((res) => {
              this.loading = false
              if(res.code === 1) {
                Toast.fail("登录成功！")

                this.$store.dispatch('login', res.data)

                switch (this.$store.getters.intercepted){
                  case 'publish': {
                    this.$router.push({
                      path: '/publish',
                      replace: true
                    })
                    this.$store.dispatch('resetIntercept')
                    break
                  }
                  case 'users': {
                    this.$router.push({
                      path: '/users',
                      replace: true
                    })
                    this.$store.dispatch('resetIntercept')
                    break
                  }
                  default: {
                    this.$router.push({
                      path: '/member',
                      replace: true
                    })
                    this.$store.dispatch('resetIntercept')
                  }
                }
              } else if(res.code === 0) {
                if(res.msg === "登录失败") {
                  Toast.fail("登录失败！")
                } else if (res.msg === "账号已注销") {
                  Toast.fail("账号已注销！")
                } else {
                  Toast.fail("未知错误！")
                }
              } else {
                Toast.fail("未知错误！")
              }
            })
      }
    },
    goBack: function () {
      this.$router.go(-1);
    },
    clearPassword: function () {
      this.loginForm.password = "";
    },
  },
};
</script>

<style lang="less" scoped>
#page {
  background-color: #f8f8f8;
}
.container {
  position: absolute;
  left: 0;
  top: 0;
  overflow-y: scroll;
  overflow-x: hidden;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding-top: 50px;
  background: #fff;
}
.navheader {
  position: fixed;
  top: 0;
  width: 100%;
  height: 50px;
  background-color: #fff;
  z-index: 100;
  padding-left: 15px;
  padding-right: 15px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  border-bottom: 1px solid #f1f2f6;
}

.mr-5 {
  margin-right: 5px;
}

.mr-10 {
  margin-right: 10px;
}

.ml-5 {
  margin-left: 5px;
}

.ml-10 {
  margin-left: 10px;
}

.mt-10 {
  margin-top: 10px;
}

.mt-15 {
  margin-top: 15px;
}

.pl-30 {
  padding-left: 15px;
}

.pr-30 {
  padding-right: 15px;
}

.pl-60 {
  padding-left: 30px;
}

.pr-60 {
  padding-right: 30px;
}

.navheader .back {
  position: absolute;
  left: 15px;
  width: 19px;
  height: 19px;
}

.navheader .title {
  width: 72%;
  height: auto;
  font-size: 17px;
  font-weight: 500;
  text-align: center;
  color: #333;
  line-height: 22px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.box {
  width: 100%;
  height: auto;
  float: left;
  box-sizing: border-box;
}
.btn-confirm {
  width: 100%;
  height: 48px;
  background-color: rgba(#3ca7fa, 0.6);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 500;
  color: #ffffff;
  cursor: pointer;
  &.active {
    background: #3ca7fa;
  }
}

.password-login-form {
  width: 100%;
  height: auto;
  float: left;
  margin-top: 0px;
  background-color: white;
  box-sizing: border-box;
  padding: 30px 30px 0 30px;
  .password-login-title {
    width: 100%;
    height: 24px;
    font-size: 24px;
    font-weight: 500;
    color: #171923;
    line-height: 24px;
    box-sizing: border-box;
    margin-bottom: 30px;
  }

    .item {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    padding: 6px 0;
    display: flex;
    border-bottom: 1px solid #f4faff;
    margin-bottom: 20px;

    .name {
      min-width: 48px;
      height: auto;
      font-size: 16px;
      font-weight: 400;
      color: #333333;
      line-height: 36px;
      margin-right: 25px;
    }
    #register{
      font-size: 50px;
    }
      .input {
      flex: 1;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      box-sizing: border-box;
      align-items: center;

      .input-text {
        max-width: 200px;
        height: 36px;
        float: left;
        box-sizing: border-box;
        outline: none;
        color: #333333;
        font-size: 16px;
        border: none;
      }
    }
    .captcha {
      width: 90px;
      height: 36px;
      padding-top: 0px;
      margin-left: 19px;
      img {
        width: 90px;
        display: inline-block;
        overflow: hidden;
        position: relative;
      }
    }
  }
}
</style>
