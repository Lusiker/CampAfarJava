<!--  -->
<template>
  <v-container class="article-global">
    <div class="nav-box">
      <img class="title-img" src="../assets/img/icon-back.png" alt="" @click="$router.go(-1)" />
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
          class="banner"
          :src="bannerSrc"
          width="100%"
          height="300px"
          fit="fill"
        ></van-image>
      </div>
    </div>

    <v-card
        class="author-info"
        width="100%"
        height="auto"
        tile
        border-radius="0px"
        v-if="!this.articleNotExist"
    >
      <v-overlay
          absolute
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
              <v-btn
                  :color="'#da5a22'"
                  class="profile-btn"
                  @click="toUserProfile"
              >
                <span class="btn-info">
                  用户资料
                </span>
              </v-btn>
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

    </div>
    <div v-else>
      <div v-if="!articleLoaded && !articleNotExist">
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
      <div v-else>
        <v-card
            class="title-info"
            elevation="5"
        >
          <h1>{{article.articleTitle}}</h1>
          <v-divider/>
          <h3>创建于:{{createdAt}}</h3>
          <h3>浏览量:{{article.articleViewCount}}</h3>
        </v-card>
        <v-divider/>
        <v-card  height="200px">

          <div class="dialog-content">
            <div class="dialog" v-html="article.articleDetail"></div>
            <v-overlay
                :absolute="true"
                :value="notPurchased"
            >
              <v-btn
                  :color="'#3aafd9'"
              >
                <span style="font-size: 14px">前往购买</span>
              </v-btn>
            </v-overlay>
          </div>

        </v-card>
      </div>
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
      commentText: '',
      articleNotExist: false,
      notPurchased: false,
    };
  },
  computed: {
    createdAt() {
      if(this.article === {}) {
        return ''
      }

      let t = new Date(this.article.articleCreatedAt)
      let result = t.getFullYear() + "年" + t.getMonth() +"月" + t.getDate() + "日  " + t.getHours() + ":"
      if(t.getMinutes() < 10) {
        result += "0" + t.getMinutes() + ":"
      } else {
        result += t.getMinutes() + ":"
      }

      if(t.getSeconds() < 10) {
        result += "0" + t.getSeconds()
      } else {
        result += t.getSeconds()
      }

      return result
    }
  },
  methods: {
    toUserProfile() {
      this.$router.push('/user/' + this.article.userId)
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
              Toast.fail("读取信息失败")
              this.loadUserFailed = true
            }
          }
        }
      )
    }
  },
  mounted() {
    this.articleId = this.$route.params.aid;
    if(isNaN(Number(this.articleId))) {
      this.bannerLoading = false
      this.contentLoading = false
      this.content = '<div style="text-align: center"><strong>文章不存在</strong></div>'
      this.bannerSrc = this.$store.getters.bannerArticleNotExistSrc
      this.articleNotExist = true
    } else {
      this.$request.get("/acquire/articleBanner?aid=" + this.articleId)
          .then(
              (res) => {
                switch(res.stateEnum.state) {
                  case 0: {
                    this.bannerLoading = false
                    this.bannerSrc = res.returnObject
                    if(this.bannerSrc ===  null) {
                      this.bannerSrc = this.$store.getters.defaultBannerSrc
                    }

                    break
                  }
                  case -1: {
                    if(res.specificCode === 2) {
                      this.bannerLoading = false
                      this.articleNotExist = true
                      this.bannerSrc = this.$store.getters.bannerArticleNotExistSrc
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
                    if(res.specificCode === 1) {
                      //尚未购买
                      this.notPurchased = true
                    }

                    break
                  }
                  case -1: {
                    if(res.specificCode === 3) {
                      this.bannerLoading = false
                      this.articleNotExist = true
                    }

                    break
                  }
                  default: {
                    Toast.fail("读取文章信息失败")
                    this.articleLoaded = true
                    this.articleNotExist = true
                  }
                }
              }
          )
    }
  }
};
</script>

<style scoped>
  .banner {
    border: 1px solid grey;
  }
  .author-info {
    margin-top: 10px;
  }
  .profile-btn {
    position:absolute;
    top: 10px;
    right:10px
  }
  .title-info {
    padding: 10px;
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .title {
    font-size: 220px;
  }
  .dialog-content {
    height: 100%;
    width: 100%;
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
    color: white;
  }
  .tabs {
    padding-top: 10px;
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
