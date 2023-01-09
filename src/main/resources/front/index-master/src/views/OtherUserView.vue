<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">用户信息</div>
    </div>
    <div class="user-avatar">
      <div class="value">
        <img :src="avatarSrc" alt="" />
      </div>
    </div>
    <div class="form-box">
      <div class="tit">账户信息</div>

      <div v-if="userLoaded" v-show="user !== null">
        <div class="group-item">
          <div class="name">昵称</div>
          <div class="value">
            <span>{{user.userName}}</span>
          </div>
        </div>
        <div class="group-item">
          <div class="name">邮箱</div>
          <div class="value">
            <span>{{user.userEmail}}</span>
          </div>
        </div>
        <div class="group-item">
          <div class="name">个人简介</div>
          <div class="value">
            <span>{{user.userIntroduction}}</span>
          </div>
        </div>
        <div class="group-item">
          <div class="name">上次登录</div>
          <div class="value">
            <span>{{user.lastLoginString}}</span>
          </div>
        </div>
        <div class="group-item" @click="showArticleList = !showArticleList">
          <div class="name">Ta的文章</div>
          <div class="value">
            <img src="../assets/img/icon-prev.png" class="arrow"/>
          </div>
        </div>

        <van-action-sheet class="articles" v-model="showArticleList" title="Ta的文章">
          <div v-if="userLoaded">
            <ArticleList
              :uid="user.userId"
            />
          </div>
        </van-action-sheet>
      </div>

      <div v-else>
        <van-col style="text-align: center;width: 100%;">
          <van-loading/>
          数据载入中
        </van-col>
      </div>
    </div>
  </div>
</template>

<script>
import { Toast } from 'vant';
import ArticleList from '@/components/ArticleList.vue';

export default {
  components: { ArticleList },
  data() {
    return {
      loading: false,
      pageLoading: true,
      finished: false,
      userLoaded: false,
      user: {},
      avatarSrc: '',
      targetUser: '',
      showArticleList: false
    }
  },
  computed: {
    loginState() {
        return this.$store.getters.loginState
    },
  },
  methods: {
    toBack: function () {
      this.$router.go(-1)
    },
  },
  beforeMount() {
    if(this.$store.getters.loginState) {
      //访问自己返回member
      if(this.$route.params.uid === this.$store.getters.user.userId) {
        this.$router.push("/member")
      }
    }

    if(!isNaN(this.$route.params.uid)) {
      //获取头像
      this.$request.get('/acquire/avatar?uid=' + this.$route.params.uid)
          .then(
              (res) => {
                switch (res.stateEnum.state) {
                  case 0: {
                    this.avatarSrc = res.returnObject
                    break
                  }
                  default: {
                    this.avatarSrc = this.$store.getters.defaultAvatar

                    return
                  }
                }
              }
          )

      //获取用户信息
      let currentUid = this.loginState ? this.$store.getters.user.userId : 0
      this.$request.get('/user?uidFrom=' + currentUid + '&uidTo=' + this.$route.params.uid)
          .then(
              (res) => {
                switch (res.stateEnum.state) {
                  case 0: {
                    this.user = res.returnObject
                    this.userLoaded = true
                    break
                  }
                  default: {
                    this.user = null
                    this.userLoaded = true
                    Toast.fail("用户数据载入失败")
                  }
                }
              }
          )
    } else {
      Toast.fail("用户不存在")
      this.userLoaded = true
      this.user = null
      this.avatarSrc = this.$store.getters.defaultAvatar
    }
  },
}
</script>
.
<style lang="less" scoped>

  .articles {
    overflow-y: hidden;
    width: 100%;
    height: 450px;
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
