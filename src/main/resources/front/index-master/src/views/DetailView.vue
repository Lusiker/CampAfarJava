<!--  -->
<template>
  <v-container class="article-global">
    <div class="nav-box">
      <img class="title-img" src="../assets/img/icon-back.png" alt="" @click="goToHome()" />
      <div class="title">知识详情</div>
    </div>


    <div class="main-img">
      <div class="loading" v-if="bannerLoading">
        <van-row>
          <van-col span="8"></van-col>
          <van-col span="8">
            <van-loading>加载中...</van-loading>
          </van-col>
          <van-col span="8"></van-col>
        </van-row>
      </div>
      <div  v-else>
        <van-image
          :src="bannerSrc"
          width="100%"
          height="300px"
          fit="fill"
        ></van-image>
      </div>
    </div>

    <v-card
        class="mt-3 author-info"
        width="100%"
        height="auto"
        tile
        border-radius="0px"
    >
      <v-overlay
          :absolute="true"
          :value="!userLoaded"
      >
        <van-loading/>
        用户信息读取中
      </v-overlay>
      <div v-show="userLoaded">
        <div v-if="loadUserFailed">
          <v-btn @click="getUserCardInfo">重新加载</v-btn>
        </div>
        <div v-else>
          <div class="card-title">
            <van-row clas="info-row">
              <van-col span="4">
                <v-avatar size="50">
                  <img
                      alt="user"
                      :src="author.userAvatar"
                  >
                </v-avatar>
              </van-col>
              <van-col>
                <span class="user-name">
                  {{author.userName}}
                </span>
                <div class="user-email">
                  {{author.userEmail}}
                </div>
              </van-col>
              <v-btn class="btn"><span class="btn-info">用户资料</span></v-btn>
            </van-row>
          </div>
          <v-divider/>
          <v-card-text>
            <div class="user-name">
              {{author.userIntroduction}}
            </div>
          </v-card-text>
        </div>
      </div>
    </v-card>

    <v-tabs v-model="active" :color="'#be2929'" class="tabs">
      <v-tab><span class="tab-text">文章</span></v-tab>
      <v-tab><span class="tab-text">评论</span></v-tab>
    </v-tabs>
    <v-divider/>
    <div v-if="active === 1">
      <div v-if="!articleLoaded">
        <van-col>
          <v-progress-circular
              :size="70"
              :width="7"
              :color="'#be2929'"
              indeterminate
          ></v-progress-circular>
          文章加载中
        </van-col>
      </div>
    </div>
    <div v-else>
      <v-sheet>
        <h1>{{article.articleTitle}}</h1>
        <v-divider/>
        <div class="dialog-content">

          <div class="dialog" v-html="article.articleDetail"></div>
        </div>
      </v-sheet>
    </div>
    </v-container>
</template>

<script>
import { Toast } from 'vant';

export default {
  data() {
    return {
      articleId: -1,
      bannerLoading: true,
      bannerSrc: '',
      contentLoading: true,
      content: '',
      active: 0,
      comments: [],
      userLoaded: false,
      loadUserFailed: false,
      articleLoaded: false,
      article: {},
      author: {},
      commentText: ''
    };
  },
  methods: {
    goToHome: function () {
      this.$router.push({
        path: "/",
      });
    },

    tologin: function () {
      this.$router.push({
        path: "/login-password",
      });
    },

    getUserCardInfo() {
      this.$request.get("/user/cardInfo?uid=" + this.article.userId).then(
        (res) => {
          this.userLoaded = true
          switch (res.stateEnum.state) {
            case 0: {
              this.author = res.returnObject

              break
            }
            default: {
              Toast.fail("读取文章信息失败")
              this.loadUserFailed = true
            }
          }
        }
      )
    }
  },
  computed: {
    chipInfo() {
      if(this.userLoaded) {
        switch (this.author.userState) {
          case "RESTRICTED": return "受限制"
          case "NORMAL": return "正常"
          case "BANNED": return "被封禁"
          case "LOGOFF": return "已注销"
          default: return "未知状态"
        }
      } else {
        return "..."
      }
    }
  },
  mounted() {
    this.articleId = this.$route.params.aid;
    if(isNaN(Number(this.articleId))) {
      this.bannerLoading = false
      this.contentLoading = false
      this.content = '<div style="text-align: center"><strong>文章不存在</strong></div>'
      this.bannerSrc = this.$store.getters.bannerArticleNotExistSrc
    } else {
      this.$request.get("/acquire/articleBanner?aid=" + this.articleId)
          .then(
              (res) => {
                switch(res.stateEnum.state) {
                  case 0: {
                    this.bannerLoading = false
                    this.bannerSrc = res.returnObject
                    if(this.bannerSrc.length === 0) {
                      this.bannerSrc = this.$store.getters.defaultBannerSrc
                    }

                    break
                  }
                  default: {
                    this.bannerLoading = false
                    this.bannerSrc = this.$store.getters.defaultBannerSrc
                  }
                }
              }
          )

      let uid = this.$store.getters.loginState ? this.$store.getters.user.userId : 0
      this.$request.get("/article?uid=" + uid + "&aid=" + this.articleId)
          .then(
              (res) => {
                this.articleLoaded = true
                switch (res.stateEnum.state) {
                  case 0: {
                    this.article = res.returnObject
                    this.getUserCardInfo()
                    break
                  }
                  default: {
                    Toast.fail("读取文章信息失败")
                  }
                }
              }
          )
    }
  }
};
</script>

<style scoped>
  .title {
    font-size: 220px;
  }
  .dialog-content {
    height: 100%;
    width: 100%;
    overflow: scroll;
  }
  .dialog >>> img,p,span {
    width: 100%;
  }
  .card-title {
    height: 60px;
    width: 100%;
    padding-left: 10px;
    padding-top: 5px;
  }
  .user-name {
    font-size: 15px;
    padding-left: 10px;
  }
  .user-email {
    font-size: 12px;
    padding-left: 10px;
    font-weight: lighter;
  }
  .btn-info {
    font-size: 10px;
  }
  .tabs {
    width: 100%;
  }
  .tab-text {
    font-size: 0.33rem;
  }
  .loading {
    padding-top: 80px;
    width: 100%;
    height: 300px;
    text-align: center;
    align-items: center;
  }
  .nav-box {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 1.3333rem;
    background-color: #fff;
    z-index: 100;

    display: flex;
    flex-direction: row;
    justify-content: center;
    line-height: 1.3333rem;
  }
  .title-img {
    position: absolute;
    left: 0.2rem;
    width: 0.5067rem;
    height: 0.5067rem;
    top: 0.4267rem;
  }
  .title {
        width: 72%;
        height: auto;
        font-size: 0.4533rem;
        font-weight: 500;
        text-align: center;
        color: #333;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
  }
</style>
