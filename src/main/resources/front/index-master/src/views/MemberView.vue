<!-- -->
<template>
  <div class="container" style="padding-top: 0">
    <div class="user-icon">
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
                <img src="../assets/img/icon-check-n.png" alt="" />
              </div>
              <div class="text">我的订单</div>
            </div>
          </div>

          <div class="grid-item" @click="showArticleList = !showArticleList">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/icon-list@2x.png" alt="" />
              </div>
              <div class="text">我的文章</div>
            </div>
          </div>

          <div class="grid-item" @click="showChargeDrawer = !showChargeDrawer">
            <div class="icon">
              <div class="icon-img">
                <img src="../assets/img/钱.png" alt="" />
              </div>
              <div class="text">充值</div>
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

    <van-action-sheet v-model="showChargeDrawer" title="充值">
      <van-col class="submit-button">
        <van-row>
          <van-field label="输入充值金额" type="number" :disabled="!hasActivate" v-model="chargeValue"></van-field>
          <van-notice-bar
              :text="'您尚未激活账号，无法进行此操作'"
              v-if="!hasActivate"
          />
          <van-notice-bar
              :text="'注意：金额应大于零'"
              v-if="chargeValue != 0 && chargeValue.length > 0"
          />
        </van-row>
        <br/><br/>
        <van-divider/>
        <van-row>
          <div><van-button
              :disabled="!chargeAmountValid"
              :color="'#6598e5'"
              block
              @click="charge"
          >充值</van-button></div>
        </van-row>
      </van-col>
    </van-action-sheet>

    <van-action-sheet class="articles" v-model="showArticleList" title="我的文章">
      <div v-if="loginState">
        <ArticleList
            :uid="user.userId"
        />
      </div>
    </van-action-sheet>

    <kp-foot-nav type="member"></kp-foot-nav>
  </div>
</template>

<script>
import KPFootNav from "@/components/kp-foot-nav.vue";
import { Toast } from 'vant';
import ArticleList from '@/components/ArticleList.vue';

export default {
  components: {
    "kp-foot-nav": KPFootNav,
    ArticleList
  },
  data() {
    return {
      showArticleList: false,
      collapsed: true,
      avatarLoading: true,
      showChargeDrawer: false,
      chargeValue: '0.00'
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
        case "RESTRICTED": return "受限制" + (this.$store.getters.user.userHasActivated ? '' : '(尚未激活)')
        case "NORMAL": return "正常"
        case "BANNED": return "被封禁"
        case "LOGOFF": return "已注销"
        default: return "未知状态"
      }
    },
    chargeAmountValid() {
      if(isNaN(this.chargeValue)) {
        return false
      }

      return Number(this.chargeValue) > 0;
    },
    hasActivate() {
      return this.$store.getters.user.userHasActivated
    },
  },
  methods: {
    charge() {
      if(this.loginState) {
        this.$request.post('/user/charge?uid=' + this.user.userId + '&value=' + this.chargeValue).then(
          (res) => {
            switch (res.stateEnum.state) {
              case 0 :{
                Toast.success("充值成功")
                this.$store.dispatch('setNewPoint', res.returnObject)
                this.chargeValue = '0.00'
                this.showChargeDrawer = false

                return
              }
              default: {
                Toast.fail("充值失败")
              }
            }
          }
        )
      }
    },
    tologin: function () {
      this.$router.push({
        path: "/login-password",
      });
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
  },
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
};
</script>

<style lang="less">
div.info {
  width: 100%;
  align-items: start;
}
.submit-button {
  width: 100%;
  text-align: center;
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
