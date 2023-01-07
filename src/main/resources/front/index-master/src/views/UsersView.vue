<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">个人中心</div>
    </div>

    <div class="user-avatar">
      <div class="value">
        <img :src="avatarSrc" alt="" />
        <input type="file" accept="image/*" class="input-avatar" @change="changeAvatar($event)" />
      </div>
      <div class="text">点击更换头像</div>
    </div>

    <div class="form-box">
      <div class="tit">账户信息</div>
      <div class="group-item" @click="showChangeNameDrawer = !showChangeNameDrawer">
        <div class="name">昵称</div>
        <div class="value">
          <span>{{ user.userName }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>

      <div class="group-item" @click="showChangeEmailDrawer = !showChangeEmailDrawer">
        <div class="name">更换邮箱</div>
        <div class="value">
          <span>{{ user.userEmail }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>

      <div class="group-item" @click="showChangeUserIntroductionDrawer = !showChangeUserIntroductionDrawer">
        <div class="name">更换个人简介</div>
        <div class="value">
          <span>{{ user.userIntroduction }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>

      <div class="group-item" @click="showActivateUserDrawer = !showActivateUserDrawer">
        <div class="name">激活邮箱</div>
        <div class="value">
          <span>当前状态:{{ user.userHasActivated ? '已激活' : '未激活' }}</span
          ><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>

      <div class="group-item" @click="showChangePasswordDrawer = !showChangePasswordDrawer">
        <div class="name">修改密码</div>
        <div class="value">
          <span class="un">设置密码</span><img src="../assets/img/icon-prev.png" class="arrow" />
        </div>
      </div>

      <div class="group-item">
        <van-divider/>
        <van-button type="danger" block @click="logout">退出登录</van-button>
      </div>
    </div>


    <van-action-sheet v-model="showChangeNameDrawer" title="更改用户名">
      <van-col class="submit-button">
        <van-row>
          <van-field label="输入新用户名" :placeholder="user.userName" v-model="newUserName"></van-field>
          <van-notice-bar
              :text="'注意：用户名不应超过30（当前' + newUserName.length + ')'"
              v-if="newUserName.length !== 0"
          />
        </van-row>
        <br/><br/>
        <van-divider/>
        <van-row>
        <div><van-button
            :disabled="newUserName.length ===0 || newUserName.length > 30"
            :color="'#6598e5'"
            block
            @click="changeUserName"
        >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>

    <van-action-sheet v-model="showChangeEmailDrawer" title="更改邮箱">
      <van-col class="submit-button">
        <van-row>
          <van-field label="输入新邮箱" :placeholder="user.userEmail" v-model="newUserEmail"></van-field>
          <van-notice-bar
              :text="'注意：邮箱不应超过50（当前' + newUserEmail.length + ')'"
              v-if="newUserName.length !== 0"
          />
        </van-row>
        <br/><br/>
        <van-divider/>
        <van-row>
          <div><van-button
              :disabled="newUserEmail.length === 0 || newUserEmail.length > 50"
              :color="'#6598e5'"
              block
              @click="changeUserEmail"
          >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>

    <van-action-sheet v-model="showChangeUserIntroductionDrawer" title="更改个人简介">
      <van-col class="submit-button">
        <van-row>
          <van-field label="输入个人简介" :placeholder="user.userIntroduction" v-model="newUserIntroduction"></van-field>
          <van-notice-bar
              :text="'注意：个人简介不应超过250（当前' + newUserIntroduction.length + ')'"
              v-if="newUserIntroduction.length !== 0"
          />
        </van-row>
        <br/><br/>
        <van-divider/>
        <van-row>
          <div><van-button
              :disabled="newUserIntroduction.length ===0 || newUserIntroduction.length > 250"
              :color="'#6598e5'"
              block
              @click="changeUserIntroduction"
          >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>

    <van-action-sheet v-model="showActivateUserDrawer" title="激活邮箱">
      <van-col class="submit-button">
        <van-row>
          <van-field label="输入激活码" v-model="activateCode">
            <template #button>
              <van-button
                :disabled="sendButtonDisabled"
                @click="sendCode"
              >{{sendButtonText}}</van-button>
            </template>
          </van-field>
        </van-row>
        <van-divider/>
        <van-row>
          <div><van-button
              :disabled="activateCode.length ===0 || activateCode.length > 4"
              :color="'#6598e5'"
              block
              @click="submitCode"
          >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>

    <van-action-sheet v-model="showChangePasswordDrawer" title="更换密码">
      <van-col class="submit-button">
        <van-row>
          <van-field
              v-model="oldPassword"
              label="输入旧密码"
              :type="showPassword ? 'text' : 'password'"
              :right-icon="showPassword ? eyeSrc : eycClosedSrc"
              @click-right-icon="showPassword = !showPassword"
          />
          <van-field
              v-model="newPassword"
              label="输入新密码"
              :type="showPassword ? 'text' : 'password'"
          />
          <van-field
              label="重复新密码"
              :type="showPassword ? 'text' : 'password'"
              v-model="newPasswordRepeat"></van-field>
          <van-notice-bar
              :text="'注意：密码长度介于6到20之间（当前' + newPassword.length + ')'"
              v-if="newPassword.length !== 0"
          />
        </van-row>
        <br/><br/>
        <van-divider/>
        <van-row>
          <div><van-button
              :disabled="newPassword.length === 0 || newPassword !== newPasswordRepeat || newPassword.length > 20 || newPassword.length < 6"
              :color="'#6598e5'"
              block
              @click="changePassword"
          >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>
  </div>

</template>

<script>
import { Notify, Toast } from 'vant';

export default {
  data() {
    return {
      file: "",
      showChangeNameDrawer: false,
      newUserName: '',
      showChangeEmailDrawer: false,
      newUserEmail: '',
      showChangeUserIntroductionDrawer: false,
      newUserIntroduction: '',
      showActivateUserDrawer: false,
      showChangePasswordDrawer: false,
      oldPassword: '',
      newPassword: '',
      newPasswordRepeat: '',
      activateCode: '',
      codeSent: null,
      countDown: 0,
      intervalCountDown : null,
      showPassword: false,
      eyeSrc: require("../assets/eye.png"),
      eycClosedSrc: require("../assets/eye-off.png")
    };
  },
  computed: {
    loginState() {
      return this.$store.getters.loginState
    },
    user() {
      return this.$store.getters.user
    },
    avatarSrc() {
      return this.$store.getters.avatar
    },
    sendButtonDisabled() {
      return this.user.userHasActivated || this.codeSent !== null;
    },
    sendButtonText() {
      if(this.codeSent === null) {
        return "发送"
      } else {
        return this.countDown
      }
    }
  },
  methods: {
    toastUpdateFail() {
      Toast.fail("更新失败")
    },
    logout(){
      this.$store.dispatch('logout')
      localStorage.removeItem("codeStart")
      this.$router.push({
        path: "/member",
      })
    },
    toBack: function () {
      this.$router.push({
        path: "/member",
      });
    },
    changeAvatar: function (event) {
      if(this.loginState) {
        this.file = event.target.files[0];
        let formData = new FormData();
        formData.append("file", this.file);
        this.$request.post("/upload/avatar?uid=" + this.user.userId, formData)
            .then(() => {
              location.reload();
            });
      }
    },
    changeUserName() {
      if(this.loginState) {
        this.$request.post('/user/update?uid=' + this.user.userId + "&userName=" + this.newUserName).then(
            (res) => {
              switch (res.stateEnum.state) {
                case 0: {
                  Notify({
                    message: "用户名修改成功",
                    duration: 2500,
                    type: 'primary'
                  })
                  this.$store.dispatch('changeUserName', this.newUserName)
                  this.newUserName = ''
                  this.showChangeNameDrawer = false
                  break
                }
                default: {
                  this.toastUpdateFail()
                }
              }
            }
        )
      }
    },
    changeUserEmail() {
      if(this.loginState) {
        this.$request.post('/user/update?uid=' + this.user.userId + "&userEmail=" + this.newUserEmail).then(
            (res) => {
              switch (res.stateEnum.state) {
                case 0: {
                  Notify({
                    message: "邮箱修改成功",
                    duration: 2500,
                    type: 'primary'
                  })
                  this.$store.dispatch('changeUserEmail', this.newUserEmail)
                  this.newUserEmail = ''
                  this.showChangeEmailDrawer = false
                  break
                }
                default: {
                  this.toastUpdateFail()
                }
              }
            }
        )
      }
    },
    changeUserIntroduction() {
      if(this.loginState) {
        this.$request.post('/user/update?uid=' + this.user.userId + "&userIntroduction=" + this.newUserIntroduction).then(
            (res) => {
              switch (res.stateEnum.state) {
                case 0: {
                  Notify({
                    message: "个人简介修改成功",
                    duration: 2500,
                    type: 'primary'
                  })
                  this.$store.dispatch('changeUserIntroduction', this.newUserIntroduction)
                  this.newUserIntroduction = ''
                  this.showChangeUserIntroductionDrawer = false
                  break
                }
                default: {
                  this.toastUpdateFail()
                }
              }
            }
        )
      }
    },
    sendCode() {
      if(this.loginState) {
        let now = new Date()
        if (this.codeSent !== null) {
          //仍保有发送时间
          if (now.getTime() - this.codeSent.getTime() < 60000) {
            //小于60秒
            Toast.fail("请求过于频繁")

            return
          }
        }

        this.$request.post("/user/activate?uid=" + this.user.userId)
            .then(
                (res) => {
                  switch (res.stateEnum.state) {
                    case 0: {
                      Notify({
                        message: "激活码发送成功，请注意查收",
                        duration: 2500,
                        type: 'primary'
                      })

                      return
                    }
                    case -1: {
                      switch (res.specificCode) {
                        case 1:{
                          Toast.fail("请求参数有误")
                          this.resetCountDown()
                          return
                        }
                        case 2: {
                          Toast.fail("用户不存在")
                          this.resetCountDown()
                          return
                        }
                        case 3: {
                          Toast.fail("用户已激活")
                          this.resetCountDown()
                          return
                        }
                        default: {
                          Toast.fail("未知错误")
                          this.resetCountDown()
                          return
                        }
                      }
                    }
                    case -3: {
                      Toast.fail("请求过于频繁")
                      this.resetCountDown()
                      return
                    }
                    case 1: {
                      Toast.fail("服务器错误")
                      this.resetCountDown()
                      return
                    }
                    default: {
                      Toast.fail("未知错误")
                      this.resetCountDown()
                      return
                    }
                  }
                }
            )

        //尚未发送或者已超过60秒
        this.countDown = 60
        this.codeSent = now
        localStorage.setItem("codeStart", this.codeSent.getTime()
            .toString())
        this.intervalCountDown = setInterval(() => {
          this.countDown = this.countDown - 1
          if (this.countDown === 0) {
            this.codeSent = null
            this.countDown = 60
            clearInterval(this.intervalCountDown)
            this.intervalCountDown = null
          }
        }, 1000)
      }
    },
    resetCountDown() {
      this.codeSent = null
      this.countDown = 60
      clearInterval(this.intervalCountDown)
      this.intervalCountDown = null
      localStorage.removeItem("codeStart")
    },
    submitCode() {
      if(this.loginState) {
        if(this.activateCode.length !== 4) {
          Toast.fail("验证码不合法")

          return
        }

        this.$request.post("/user/checkActivate?uid=" + this.user.userId + "&code=" + this.activateCode)
            .then(
                (res) => {
                  switch (res.stateEnum.state) {
                    case 0: {
                      Notify({
                        message: "用户激活成功",
                        duration: 2500,
                        type: 'primary'
                      })
                      this.$store.dispatch('updateUserActivationState')
                      this.showActivateUserDrawer = false
                      this.activateCode = ''

                      return
                    }
                    case -1: {
                      switch (res.specificCode) {
                        case 1:{
                          Toast.fail("请求参数有误")
                          return
                        }
                        case 2: {
                          Toast.fail("用户不存在")
                          return
                        }
                        case 3: {
                          Toast.fail("用户已激活")
                          return
                        }
                        case 4: {
                          Toast.fail("验证码错误")
                          return
                        }
                        case 5: {
                          Toast.fail("验证码未发送或已过期")
                          return
                        }
                        default: {
                          Toast.fail("未知错误")
                          return
                        }
                      }
                    }
                    case 1: {
                      Toast.fail("服务器错误")
                      return
                    }
                    default: {
                      Toast.fail("未知错误")
                      return
                    }
                  }
                }
            )
      }
    },
    changePassword() {
      if(this.loginState) {
        if(this.newPassword.length < 6 || this.newPassword.length > 20 || this.newPassword !== this.newPasswordRepeat) {
          Toast.fail("输入有误")

          return
        }

        this.$request.post("/user/password?uid=" + this.user.userId + "&oldPassword=" + this.oldPassword + "&newPassword=" + this.newPassword)
            .then(
                (res) => {
                  switch(res.stateEnum.state) {
                    case 0: {
                      Notify({
                        message: "修改成功",
                        duration: 2500,
                        type: 'primary'
                      })
                      this.showChangePasswordDrawer = false

                      return
                    }
                    case -1: {
                      switch (res.specificCode) {
                        case 1: {
                          Toast.fail("参数有误")
                          return
                        }
                        case 2: {
                          Toast.fail("旧密码错误")
                          return
                        }
                        case 3: {
                          Toast.fail("用户不存在或已注销")
                          return
                        }
                        default: {
                          Toast.fail("未知错误")
                          return
                        }
                      }
                    }
                    case 1: {
                      Toast.fail("服务器错误")
                      return
                    }
                    default: {
                      Toast.fail("未知错误")
                      return
                    }
                  }
                }
            )
      }
    }
  },
  mounted() {
    let prevTime = Number(localStorage.getItem("codeStart"))
    if(prevTime !== null) {
      let now = new Date()
      this.countDown = 60 - Math.round(((now.getTime() - prevTime) / 1000))
      if(this.countDown > 0) {
        this.codeSent = new Date(prevTime)
        this.intervalCountDown = setInterval(() => {
          this.countDown = this.countDown - 1
          if (this.countDown === 0) {
            this.codeSent = null
            this.countDown = 60
            clearInterval(this.intervalCountDown)
            this.intervalCountDown = null
          }
        },1000)
      } else{
        this.codeSent = null
        this.countDown = 0
        this.intervalCountDown = null
        localStorage.removeItem("codeStart")
      }
    }

    if(this.loginState) {
      if (!this.$store.getters.avatarHasCached) {
        this.$request.get('/acquire/avatar?uid=' + this.user.userId)
            .then(
                (res) => {
                  switch (res.stateEnum.state) {
                    case 0: {
                      this.$store.dispatch('cacheAvatar', res.returnObject)
                      this.avatarLoading = false

                      break
                    }
                    default: {
                      this.avatarLoading = false

                      return
                    }
                  }
                }
            )
      } else {
        this.avatarLoading = false
      }
    } else {
      this.$store.dispatch('intercept', 'users')
      this.$router.push('/login-password')
    }
  },
  beforeDestroy() {
    clearInterval(this.intervalCountDown)
  },
};
</script>
<style lang="less" scoped>
 .submit-button {
   width: 100%;
   text-align: center;
 }
div.container {
  box-sizing: border-box;
  padding-top: 50px;
  background: #fff;
  div.title-box {
    position: fixed;
    top: 0;
    width: 100%;
    height: 1.3333rem;
    background-color: #fff;
    z-index: 100;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    border-bottom: 1px solid #f1f2f6;
    img {
      position: absolute;
      left: 0.4rem;
      width: 0.5067rem;
      height: 0.5067rem;
    }

    div.text {
      width: 72%;
      height: auto;
      font-size: 0.4533rem;
      font-weight: 500;
      text-align: center;
      color: #333;
      line-height: 0.5867rem;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  div.user-avatar {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 0.8rem;
    margin-bottom: 1.3333rem;
    div.value {
      width: 1.7067rem;
      height: 1.7067rem;
      float: left;
      position: relative;
      img {
        width: 1.7067rem;
        height: 1.7067rem;
        border-radius: 50%;
      }

      input.input-avatar {
        position: absolute;
        top: 0;
        left: 0;
        opacity: 0;
        width: 1.7067rem;
        height: 1.7067rem;
        border-radius: 50%;
      }
    }

    div.text {
      width: auto;
      height: 0.3467rem;
      font-size: 0.3467rem;
      font-weight: 400;
      color: #666;
      line-height: 0.3467rem;
      margin-top: 0.4rem;
    }
  }
  div.form-box {
    width: 100%;
    height: auto;
    float: left;
    margin-bottom: 1.3333rem;
    background-color: #fff;
    div.tit {
      width: 100%;
      height: auto;
      float: left;
      box-sizing: border-box;
      padding-left: 0.5333rem;
      font-size: 0.4267rem;
      font-weight: 600;
      color: #333;
      line-height: 0.4267rem;
      margin-bottom: 0.2667rem;
    }

    div.group-item {
      width: 100%;
      height: 1.4933rem;
      float: left;
      display: flex;
      box-sizing: border-box;
      padding: 0.2133rem 0.5333rem;
      border-bottom: 1px solid #f3f6f9;
      div.name {
        width: 4rem;
        height: 1.0667rem;
        float: left;
        line-height: 1.0667rem;
        font-size: 0.4267rem;
        color: #333;
      }

      div.value {
        flex: 1;
        height: 1.0667rem;
        text-align: right;
        font-size: 0.3733rem;
        color: #666;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        span {
          text-align: right;
          font-size: 0.3733rem;
          color: #666;
        }

        img.arrow {
          display: inline-block;
          width: 0.32rem;
          height: 0.32rem;
          margin-left: 0.4rem;
        }
      }
    }
  }
}
</style>
