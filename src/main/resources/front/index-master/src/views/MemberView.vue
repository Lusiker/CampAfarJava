<!-- -->
<template>
  <div class="container" style="padding-top: 0px">
    <div class="user-icon">
      <div class="left-icon" @click="tomessage">
        <img src="../assets/img/icon-message@2x.png" alt="" />
      </div>
      <div class="right-icon" @click="tosetting">
        <img src="../assets/img/icon-setting@2x.png" alt="" />
      </div>
    </div>

    <div class="user-info" v-if="loginState" @click="goTousers">
      <div class="user-avatar">
        <van-loading v-if="avatarLoading" />
        <img :src="avatarSrc" alt="" v-else/>
      </div>
      <div class="user-body">
        <div class="name">{{ user.userName }}</div>
        <div class="email">{{user.userEmail}}</div>
        <div class="level">经验值：{{ user.userExp }}</div>

      </div>
      <div class="user-back">
        <img src="../assets/img/icon-prev.png" alt="" style="width: 12px; height: 12px" />
      </div>
    </div>
    <div class="user-info" @click="tologin" v-else>
      <div class="user-avatar">
        <img src="../assets/img/default_avatar.png" alt="" />
      </div>
      <div class="user-body">
        <div class="login-button">请登录</div>
      </div>
      <div class="user-back">
        <img src="../assets/img/icon-prev.png" alt="" style="width: 12px; height: 12px" />
      </div>
    </div>

    <div class="user-info" v-show="loginState">
      <div class="info">
        <van-cell-group inset>
          <van-cell title="用户余额" :value="user.userPoint" >
          </van-cell>
          <van-cell title="用户状态" :value="userState" >
          </van-cell>
        </van-cell-group>
      </div>
    </div>

    <div class="user-item">

      <div class="banner-title">我的账号</div>
      <van-divider/>
      <div class="banner-body">
        <div class="grid-box">
          <div class="grid-item" @click="toOrder">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/icon-list@2x.png" alt="" />
              </div>
              <div class="text">我的订单</div>
            </div>
          </div>
          <div class="grid-item" @click="toMyArticle">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/icon-list@2x.png" alt="" />
              </div>
              <div class="text">我的文章</div>
            </div>
          </div>
          <!-- <div class="grid-item" @click="toMyQuestion">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/icon-money@2x.png" alt="" />
              </div>
              <div class="text">我的问答</div>
            </div>
          </div> -->
          <div class="grid-item" @click="totestotheruser">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/钱包.png" alt="" />
              </div>
              <div class="text">test用户信息跳转</div>
            </div>
          </div>


          <div class="grid-item" @click="totestpurchase">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/钱.png" alt="" />
              </div>
              <div class="text">test购买跳转</div>
            </div>
          </div>


          <div class="grid-item" @click="totestTrolley">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/钱.png" alt="" />
              </div>
              <div class="text">购物车</div>
            </div>
          </div>


        </div>
      </div>
    </div>


    <kp-foot-nav type="member"></kp-foot-nav>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
// a.1 引入vant 轮播图相关组件

import KPFootNav from "@/components/kp-foot-nav.vue";

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "kp-foot-nav": KPFootNav,
  },
  data() {
    //这里存放数据

    return {
      collapsed: true,
      avatarLoading: true,
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
    userState() {
      switch (this.$store.getters.user.userState){
        case "RESTRICTED": return "受限制(尚未激活)"
        case "NORMAL": return "正常"
        case "BANNED": return "被封禁"
        case "LOGOFF": return "已注销"
        default: return "未知状态"
      }
    }
  },
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    tologin: function () {
      this.$router.push({
        path: "/login-password",
      });
    },
    toMyQuestion: function () {
      if (this.token) {
        this.$router.push({
          path: "/member/promocode",
        });
      } else {
        this.$router.push({
          path: "/login-password",
        });
      }
    },
    goTousers: function () {
      if (this.loginState) {
        this.$router.push({
          path: "/users",
        });
      } else {
        this.$router.push({
          path: "/login-password",
        });
      }
    },
    toOrder: function () {
      // if (this.token) {
      //   this.$router.push({
      //     path: "/order",
      //   });
      // } else {
      //   this.$router.push({
      //     path: "/login-password",
      //   });
      // }
      this.$router.push({
         path: "/order",
         });
    },
    toMyArticle: function(){
      this.$router.push({
          path: "/myarticle",
        });
    },
    totestTrolley: function(){
      this.$router.push({
        path: "/trolley",
      });
    },
    totestotheruser: function(){
      this.$router.push({
        path: "/otheruser",
      });
    },
    totestpurchase: function(){
      this.$router.push({
        path: "/purchase",
      });
    },
    tosetting: function () {
      this.$router.push({
        path: "/setting",
      });
    },
    tomessage: function () {
      if (this.token) {
        this.$router.push({
          path: "/message",
        });
      } else {
        this.$router.push({
          path: "/login-password",
        });
      }
    },
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {
    //若已登录，请求头像
    if(this.loginState) {
      if(!this.$store.getters.avatarHasCached) {
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

<style lang="less">
div.info {
  width: 100%;
  align-items: start;
}

div.container {
  background: #f3f6f9;
  div.user-icon {
    width: 100%;
    background: #fff;
    height: auto;
    float: left;
    box-sizing: border-box;
    padding: 0.5333rem 0.4rem 0 0.4rem;
    display: flex;
    justify-content: space-between;
    div.left-icon {
      position: relative;
      width: 0.6933rem;
      height: 0.6933rem;
      img {
        width: 0.6933rem;
        height: 0.6933rem;
      }
    }

    div.right-icon {
      position: relative;
      width: 0.6933rem;
      height: 0.6933rem;
      img {
        width: 0.6933rem;
        height: 0.6933rem;
      }
    }
  }
  div.user-info {
    width: 100%;
    background: #fff;
    height: auto;
    float: left;
    box-sizing: border-box;
    padding: 0.6667rem 0.4rem 0.0rem 0.4rem;
    display: flex;
    align-items: center;
    div.user-avatar {
      width: auto;
      height: 1.7067rem;
      box-sizing: border-box;
      padding-right: 0.4rem;
      img {
        width: 1.7067rem;
        height: 1.7067rem;
        border-radius: 50%;
      }
    }

    div.user-body {
      flex: 1;
      height: 2.3rem;
      div.login-button {
        width: 100%;
        height: auto;
        float: left;
        font-size: 0.4267rem;
        font-weight: 400;
        color: rgba(0, 0, 0, 0.7);
        line-height: 1.7067rem;
      }
      div.name {
        width: 100%;
        height: auto;
        float: left;
        box-sizing: border-box;
        padding-top: 0.2667rem;
        padding-bottom: 0.2rem;
        font-size: 0.4267rem;
        font-weight: 600;
        color: #333;
        line-height: 0.4267rem;
      }
      div.email {
        width: 100%;
        height: auto;
        float: left;
        font-weight: lighter;
      }
      div.level {
        width: 100%;
        height: auto;
        float: left;
        font-size: 0.3467rem;
        padding-top: 0.15rem;
        font-weight: 400;
        color: #666;
        line-height: 0.3467rem;
      }
    }
  }
  div.user-img {
    width: 100%;
    height: 1.5rem;
    float: left;
    background: #fff;
    text-align: center;
    img {
      height: 1.5rem;
    }
  }
  div.user-item {
    width: 100%;
    height: auto;
    float: left;
    background-color: #fff;
    box-sizing: border-box;
    padding: 0.8rem 0.4rem;
    margin-bottom: 0.2667rem;
    div.banner-title {
      width: 100%;
      height: auto;
      float: left;
      margin-bottom: 0.6667rem;
      font-size: 0.4rem;
      font-weight: 600;
      color: #333;
      line-height: 0.4rem;
    }

    div.banner-body {
      width: 100%;
      height: auto;
      div.grid-box {
        display: grid;
        gap: 0.5333rem;
        grid-template-columns: repeat(4, minmax(0, 1fr));
        box-sizing: border-box;
        padding-left: 0.4rem;
        padding-right: 0.4rem;
        div.grid-item {
          div.icon {
            width: 100%;
            height: auto;
            float: left;
            text-align: center;
            div.icon-img {
              margin-bottom: 0.2667rem;
              display: inline-block;
              img {
                width: 0.8rem;
                height: 0.8rem;
              }
            }

            div.text {
              width: 100%;
              height: auto;
              float: left;
              font-size: 0.32rem;
              font-weight: 400;
              color: #333;
              line-height: 0.32rem;
              text-align: center;
            }
          }
        }
      }
    }
  }
  div.power {
    background-color: #f3f6f9;
    width: 100%;
    height: 3.7867rem;
    float: left;
    padding-top: 1.3333rem;
    padding-bottom: 5.3333rem;
    text-align: center;
    div.img {
      img {
        width: 3.0667rem;
        height: 1.12rem;
      }
    }
  }
}
</style>
