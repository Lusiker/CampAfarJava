<template>
    <div class="container">
      <div class="navheader borderbox" style="border-bottom: none">
        <img @click.stop="goBack" class="back" src="@/assets/img/icon-back.png" />
      </div>
      <div class="password-login-form">
        <div class="password-login-title">注册</div>
        <van-divider />
        <div class="item">
          <div class="name">邮箱  </div>
          <div class="input">
            <input
              v-model="registerForm.email"
              class="input-text"
              placeholder="请输入邮箱"
            />
            <img
              @click.stop="clearEmail"
              v-show="registerForm.email"
              src="@/assets/img/new/close.png"
              style="width: 16px; height: 16px"
            />
          </div>
        </div>
        <van-notice-bar
            :text="'邮箱不合法'"
            v-if="!validateEmail(registerForm.email) && registerForm.email.length !== 0"
        />
        <div class="item">
          <div class="name">密码  </div>
          <div class="input">
            <input
              type="password"
              v-model="registerForm.password"
              class="input-text"
              placeholder="请输入密码"
            />
            <img
              @click.stop="clearPassword"
              v-show="registerForm.password"
              src="@/assets/img/new/close.png"
              style="width: 16px; height: 16px"
            />
          </div>
        </div>
        <van-notice-bar
            :text="'注意：密码长度应在6到20位之间（当前' + registerForm.password.length + ')'"
            v-if="registerForm.password.length !== 0 || registerForm.passwordRepeat.length !== 0"
        />
        <div class="item">
          <div class="name">重复密码</div>
          <div class="input">
            <input
                type="password"
                v-model="registerForm.passwordRepeat"
                class="input-text"
                placeholder="请再次输入密码"
            />
            <img
                @click.stop="clearPasswordRepeat"
                v-show="registerForm.passwordRepeat"
                src="@/assets/img/new/close.png"
                style="width: 16px; height: 16px"
            />
          </div>
        </div>
        <van-notice-bar
          :text="'两次输入密码不一致'"
          v-if="registerForm.passwordRepeat !== registerForm.password"
        />
        <div  @click="$router.push('/login-password')">
          <a>返回登录</a>
        </div>
      </div>

      <div class="box border-box mt-15 pl-60 pr-60">
        <div
          @click.stop="handleLogin"
          v-if="registerForm.email && registerForm.password && registerForm.passwordRepeat"
          class="btn-confirm active"
        >
          注册
        </div>
        <div v-else class="btn-confirm">注册</div>
      </div>
    </div>
  </template>

<script>
  import { Toast } from "vant";
  export default {
    data() {
      return {
        url: this.$route.query.url || "",
        loading: false,
        registerForm: {
          email: "",
          password: "",
          passwordRepeat: "",
        },
      };
    },
    mounted: function () {
      // console.log(this.$route);
    },
    computed: {},
    methods: {
      // 处理注册登录
      handleLogin: function () {
        if(this.loading) {
          Toast.fail("注册进行中！")
          return;
        }
        // 对账号和密码进行校验
        this.loading = true
        if (!this.registerForm.password) {
          Toast.fail("密码不能为空！")
          this.loading = false
          return;
        }
        if(this.registerForm.passwordRepeat !== this.registerForm.password){
          Toast.fail("两次输入密码不一致！")
          this.loading = false
          return;
        }
        if (!this.validateEmail(this.registerForm.email)) {
          Toast.fail("邮箱格式不符合要求！")
          this.loading = false
          return;
        }

        //进行注册
        this.$request
          .post("/register?email=" + this.registerForm.email + "&password=" + this.registerForm.password)
          .then((res) => {
            this.loading = false

            switch (res.stateEnum.state) {
              case 0: {
                Toast.success("注册成功，前往登录界面！")
                this.$router.push('/login-password')
                return
              }
              case -1: {
                if(res.specificCode === 1) {
                  Toast.fail("请求参数有误！")
                  return
                } else if(res.specificCode === 2) {
                  Toast.fail("此邮箱已被使用！")
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
                break
              }
            }
          });
      },
      goBack: function () {
        // window.history.go(-1)
        // 表示返回上一页
        this.$router.go(-1);
      },
      validateEmail(email){
        return String(email)
            .toLowerCase()
            .match(
                /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            );
      },
      clearEmail: function () {
        this.registerForm.email = "";
      },
      clearPassword: function () {
        this.registerForm.password = "";
      },
      clearPasswordRepeat() {
        this.registerForm.passwordRepeat = "";
      }
    },
  };
  </script>

<style lang="less" scoped>
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

  <style>
  #page {
    background-color: #f8f8f8;
  }
  </style>
