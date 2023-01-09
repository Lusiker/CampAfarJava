<!--  -->
<template>
  <v-container class="article-global">
    <div class="nav-box">
      <img class="title-img" src="../assets/img/icon-back.png" alt="" @click="$router.push('/knowledge')" />
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

    <div v-if="articleLoaded">
      <UserInfoCard
        :uid="article.userId"
        :article-not-exist="articleNotExist"
      />
    </div>

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
                @click="createPurchase"
            >
              <span style="font-size: 14px">前往购买</span>
            </v-btn>
          </v-overlay>
        </div>

      </v-card>
    </div>

  </v-container>
</template>

<script>
import { Toast } from 'vant';
import UserInfoCard from '@/components/UserInfoCard.vue';

export default {
  components: { UserInfoCard },
  data() {
    return {
      articleId: -1,
      bannerLoading: true,
      bannerSrc: '',
      contentLoading: true,
      content: '',
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
    createPurchase() {
      if(this.$store.getters.loginState) {
        this.$request.post('/purchase/create?uid=' + this.$store.getters.user.userId + "&type=article&tid=" + this.articleId + "&value=" + this.article.articlePrice)
            .then(
                (res) => {
                  switch (res.stateEnum.state) {
                    case 0: {
                      Toast.success('订单创建成功')

                      this.$router.push('/order')
                      break
                    }
                    case 1: {
                      Toast.fail('服务器错误')
                      break
                    }
                    case -1: {
                      switch (res.specificCode) {
                        case 1: {
                          Toast.fail('请求错误')

                          break
                        }
                        case 2: {
                          Toast.fail(this.$store.getters.user.userHasActivated ? '用户不存在或已注销' : '用户未激活')

                          break
                        }
                        case 3: {
                          Toast.fail('用户已购买过此项目')

                          break
                        }
                        case 4: {
                          Toast.fail('用户有尚未支付的订单')
                          this.$router.push('/order')
                        }
                      }

                      break
                    }
                    default: {
                      Toast.fail('未知错误')

                      break
                    }
                  }
                }
            )
      } else {
        this.$store.dispatch('intercept','article/' + this.articleId)
        this.$router.push('/login-password')
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
  .article-global {
    height: 100%;
    overflow: scroll;
  }

  .banner {
    border: 1px solid grey;
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
