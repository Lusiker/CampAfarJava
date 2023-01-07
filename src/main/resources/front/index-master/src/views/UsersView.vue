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
              :disabled="newUserEmail.length ===0 || newUserEmail.length > 50"
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
              <van-button>获取激活码</van-button>
            </template>
          </van-field>
        </van-row>
        <van-divider/>
        <van-row>
          <div><van-button
              :disabled="activateCode.length ===0 || activateCode.length > 4"
              :color="'#6598e5'"
              block
              @click="sendCode"
          >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>

    <van-action-sheet v-model="showChangePasswordDrawer" title="更换密码">
      <van-col class="submit-button">
        <van-row>
          <van-field label="输入新密码" type="password" v-model="newPassword"></van-field>
          <van-field label="重复新密码" type="password" v-model="newPasswordRepeat"></van-field>
          <van-notice-bar
              :text="'注意：密码长度2介于6到20之间（当前' + newPassword.length + ')'"
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
              @click="changeUserEmail"
          >提交</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>
  </div>

</template>

<script>
import { Toast } from 'vant';

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
      newPassword: '',
      newPasswordRepeat: '',
      activateCode: '',

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
    }
  },
  watch: {},
  methods: {
    toastUpdateFail() {
      Toast.fail("更新失败")
    },
    logout(){
      this.$store.dispatch('logout')
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
    requestActivateCode() {

    },
    verifyActivateCode() {

    }
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
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
    }
  },
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发
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
