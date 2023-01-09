<template>
  <div class="container">
    <div class="top-box">
      <img src="../assets/img/营地.png" alt="" class="logo">
      <div class="title">远行营地</div>
    </div>
    <div class="greyblank"></div>

    <div class="banner">
      <van-swipe :autoplay="3000" indicator-color="#007aff">
        <van-swipe-item v-for="item in slides" :key="item.src">
          <a :href="item.href">
            <img :src="item.src" alt="" />
          </a>
        </van-swipe-item>
      </van-swipe>
    </div>

    <div class="gzh-box">
      <div class="gzh-left">
        <div class="img">
          <img src="../assets/img/logo.png" alt="">
        </div>
        <div class="info">
          <span class="name"> 文章创作 </span>
          <span class="desc"> 点亮篝火，照亮他人前行的路 </span>
        </div>
      </div>
      <div class="gzh-right">
        <div class="btn" @click="topublish">发布</div>
      </div>
    </div>

    <div class="section-box">
      <div class="section-box-title">
        <div class="section-box-title-text" v-if="change">热门文章</div>
        <div class="section-box-title-text" v-else>最新发布</div>
        <div class="section-box-title-more" @click.stop="tochange()">
          <span v-if="change">查看最新发布</span>
          <span v-else>查看热门文章</span>
        </div>
      </div>


      <div class="section-box-body" v-if="change">
        <div v-if="loaded1">
          <v-list style="width: 100%;">
            <Article v-for="item in mains1" :item="item" :get-date-string="getDateString" :key="item.articleId"/>
          </v-list>
        </div>
      </div>

      <div class="section-box-body" v-else>
        <div v-if="loaded2">
        <v-list style="width: 100%;">
          <Article v-for="item in mains2" :item="item" :get-date-string="getDateString" :key="item.articleId"/>
        </v-list>
        </div>
      </div>
    </div>
    <kp-foot-nav type="home"></kp-foot-nav>
  </div>
</template>

<script>
import Article from '@/components/Article.vue';
import Vue from "vue";
import { Swipe, SwipeItem, Lazyload } from "vant";
Vue.use(Swipe);
Vue.use(SwipeItem);
Vue.use(Lazyload);
import KPBlank from "@/components/KPBlank.vue";
import KPCourseItem from "@/components/KPCourseItem.vue";
import KPFootNav from "@/components/kp-foot-nav.vue";

export default {
  components: {
    Article,
    KPBlank,
    KPCourseItem,
    "kp-foot-nav": KPFootNav,
  },
  data() {
    return {
      loaded1: false,
      loaded2: false,
      change: true,
      blocks: [],
      slides: [{src:require('../assets/img/banner1.jpg')}, {src:require('../assets/img/banner2.jpg')}, {src:require('../assets/img/banner3.jpg')}],
      mains1: [],//热门
      mains2: [],//最新
    };
  },
  mounted() {

    this.$request.get('/popularArticles')
        .then(
            (res) => {
              //干事情
              this.mains1 = res.returnObject.records;
              this.loaded1 = true
            }
        )

    this.$request.get('/latestArticles')
        .then(
            (res) => {
              //干事情
              this.mains2 = res.returnObject.records;
              this.loaded2 = true
            }
        )
  },
  methods: {
    topublish: function () {
      this.$router.push({
        path: "/publish",
      });
    },
    tochange: function () {
      this.change=!this.change;
    },
    getDateString(rawDate) {
      let t = new Date(rawDate)
      let result = t.getFullYear() + "年" + (t.getMonth() + 1) +"月" + t.getDate() + "日  " + t.getHours() + ":"
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
};
</script>
<style lang="less" scoped>
@font-face {
    font-family: SmileySans-Oblique;
    src: url("../assets/SmileySans-Oblique.ttf");
    font-weight: normal;
    font-style: normal;
}
.container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #f3f6f9;
  overflow-x: hidden;
  overflow-y: scroll;
  padding-top: 0px;

  div.top-box {
        width: 100%;
        height: 1.3333rem;
        background: #fff;
        display: flex;
        flex-direction: row;
        align-items: center;
        .logo{
          height: 30px;
          width: 30px;
          vertical-align: middle;
        }
        div.title {
          font-family: SmileySans-Oblique;
          width: 72%;
          height: 0.48rem;
          font-size: 0.62rem;
          font-weight: 500;
          color: #171923;
          line-height: 0.48rem;
          // vertical-align: middle;
          display: inline-block;
        }}

  .navTap {
    width: 100%;
    height: 1.3333rem;
    background-color: #fff;
    position: relative;
    display: flex;
    padding: 0.2133rem 0.4rem;
    .search {
      width: 0.4267rem;
      height: 0.4267rem;
      position: absolute;
      top: 0.4533rem;
      left: 0.9867rem;
      cursor: pointer;
    }
    .input {
      width: 100%;
      height: 0.9067rem;
      background: #f5f5f5;
      border-radius: 0.4533rem;
      // outline: none;
      border: none;
      padding-left: 1.3333rem;
      font-size: 0.3733rem;
    }
  }
  .banner {
    width: 100%;
    margin-top: 5px;
    //padding: 0.2667rem 0.4rem;
    height: 4.5rem;
    overflow: hidden;
    background: #fff;
    a {
      display: block;
    }
    img {
      width: 100%;
      height: 100%;
    }
  }
  .navs {
    display: flex;
    //padding: 0.133333rem;
    background-color: white;
    //padding: 0.6667rem 0.4rem 0.5333rem 0.4rem;
    .navs-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      img {
        height: 1.333333rem;
        width: 1.333333rem;
      }

      .name {
        margin-top: 10px;
      }
    }
  }
  .gzh-box {
    width: 100%;
    height: auto;
    margin-top: 0.2667rem;
    background: #fff;
    box-sizing: border-box;
    padding: 0.4rem;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-bottom: 10px;
    border-radius: 15px;
    div.mask {
      position: fixed;
      z-index: 999;
      top: 0;
      right: 0;
      left: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.6);
      height: 100vh;
      div.modal {
        position: fixed;
        z-index: 999;
        width: 5.3333rem;
        height: 6.2933rem;
        top: calc(50% - 3.1467rem);
        left: calc(50% - 2.6667rem);
        background-color: #fff;
        border-radius: 0.2133rem;
        overflow: hidden;
        -webkit-animation: window-open 0.3s;
        animation: window-open 0.3s;
        display: flex;
        flex-direction: column;
        img.topright {
          position: absolute;
          top: 0.08rem;
          right: 0;
          width: 1.7067rem;
          height: 1.7867rem;
        }

        div.title {
          width: 100%;
          margin-top: 0.5333rem;
          height: 0.3733rem;
          font-size: 0.3733rem;
          font-weight: 500;
          color: #333;
          line-height: 0.3733rem;
        }

        div.body {
          width: 100%;
          margin-top: 0.5333rem;
          height: 3.2rem;
          display: flex;
          justify-content: center;
          div.code {
            width: 3.2rem;
            height: 3.2rem;
            img {
              width: 3.2rem;
              height: 3.2rem;
            }
          }
        }

        div.tip {
          width: 100%;
          margin-top: 0.5333rem;
          height: 0.32rem;
          font-size: 0.32rem;
          font-weight: 400;
          color: #999;
          line-height: 0.32rem;
          text-align: center;
        }
      }
    }
    .gzh-left {
      display: flex;
      flex-direction: row;
      .img {
        width: 1.3333rem;
        height: 1.3333rem;
        margin-right: 0.2667rem;
        img {
          width: 1.3333rem;
          height: 1.3333rem;
        }
      }

      .info {
        display: flex;
        flex-direction: column;
        .name {
          width: 100%;
          height: 0.5333rem;
          font-size: 0.4rem;
          font-weight: 500;
          color: #171923;
          line-height: 0.5333rem;
          margin-top: 0.1333rem;
          overflow: hidden;
        }

        .desc {
          width: 100%;
          height: 0.32rem;
          font-size: 0.32rem;
          font-weight: 400;
          color: #999;
          line-height: 0.32rem;
          margin-top: 0.2133rem;
          overflow: hidden;
        }
      }
    }

    .gzh-right {
      width: 1.5467rem;
      display: flex;
      align-items: center;
      text-align: center;
      .btn {
        width: 100%;
        height: 0.64rem;
        background: #3ca7fa;
        border-radius: 0.32rem;
        font-size: 0.3733rem;
        font-weight: 400;
        color: #fff;
        justify-content: center;
      }
    }
  }
  div.section-box {
    width: 100%;
    // margin-top: 0.2667rem;
    background: #fff;
    padding: 0.6667rem 0.4rem 0.1333rem 0.4rem;
    display: flex;
    flex-direction: column;
    border-radius: 15px;
    div.section-box-title {
      width: 100%;
      height: 0.5067rem;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.6667rem;
      div.section-box-title-text {
        height: 0.5067rem;
        font-size: 0.5067rem;
        font-weight: 600;
        color: #333;
        line-height: 0.5067rem;
      }

      div.section-box-title-more {
        display: inline-block;
        width: auto;
        height: 0.3467rem;
        font-size: 0.3467rem;
        font-weight: 400;
        color: #999;
        line-height: 0.3467rem;
        cursor: pointer;
      }
    }

    .section-box-body {
      width: 100%;
      .item {
        margin-bottom: 0.4rem;
        .item-comp {
          display: flex;
          flex-direction: row;
          justify-content: space-between;
          .img {
            width: 3.2rem;
            height: 2.4rem;
            margin-right: 0.2667rem;
            img {
              width: 3.2rem;
              height: 2.4rem;
              border-radius: 0.1067rem;
            }
          }

          .text {
            flex: 1;
            padding-top: 0.1333rem;
            .text-title {
              width: 100%;
              height: 1.0667rem;
              font-size: 0.4rem;
              font-weight: 500;
              color: #171923;
              line-height: 0.5333rem;
              margin-bottom: 0.4rem;
            }

            .text-info {
              width: 100%;
              height: auto;
              display: flex;
              align-items: center;
              .text-info-left {
                flex: 1;
                text-align: left;
                font-size: 0.32rem;
                font-weight: 400;
                color: #999;
                line-height: 0.32rem;
              }

              .text-info-right {
                flex: 1;
                text-align: right;
                font-size: 0.48rem;
                line-height: 0.48rem;
                font-weight: 600;
                color: #ff5858;
                .charge {
                  font-size: 0.3733rem;
                  margin-right: -0.1333rem;
                }
              }
              .free {
                flex: 1;
                text-align: right;
                font-size: 0.3733rem;
                line-height: 0.48rem;
                font-weight: 600;
                color: #52c41a;
              }
            }
          }
        }
      }
    }
  }
  .footer-box {
    width: 100%;
    height: auto;
    float: left;
    padding-top: 1.3333rem;
    padding-bottom: 1.3333rem;
    text-align: center;
margin-bottom: 1.4133rem;
    .inline-img {
      display: inline-block;
      img {
        width: 3.0667rem;
        height: 1.12rem;
      }
    }
  }
}
</style>
