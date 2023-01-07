<!--  -->
<template>
  <div class="container">
    <div class="navTap">
      <img src="@/assets/img/icon-search.png" alt="" class="search" />
      <input type="text" placeholder="搜索关键词" class="input" />
    </div>
    <div class="banner">
      <van-swipe :autoplay="3000" indicator-color="#007aff">
        <van-swipe-item v-for="item in slides" :key="item.src">
          <a :href="item.href">
            <img :src="item.src" alt="" />
          </a>
        </van-swipe-item>
      </van-swipe>
    </div>
    <div class="navs">
      <div
        :navs="navs"
        @click.stop="goToDetail(item)"
        v-for="item in navs.items"
        :key="item.href"
        class="navs-item"
      >
        <img :src="item.src" alt="" />
        <span class="name">{{ item.name }}</span>
      </div>
    </div>
    <div class="blank">
      <KPBlank :height="blank.height" :bgcolor="blank.bgcolor"></KPBlank>
    </div>
    <div class="gzh-box">
      <div class="mask" v-show="showQRcode" @click="hide">
        <div class="modal" @click.stop="show">
          <!-- <img src="../assets/img/topright.png" alt="" class="topright" /> -->
          <!-- <div class="title">关注公众号</div> -->
          <!-- <div class="body">
            <div class="code">
              <img :src="h5GzhV1.qrcode" alt="" />
            </div>
          </div> -->
          <!-- <div class="tip">长按二维码识别或截图保存</div> -->
        </div>
      </div>
      <div class="gzh-left">
        <div class="img">
          <!-- <img :src="h5GzhV1.logo" alt="" /> -->
          <img src="../assets/img/apple.png" alt="">
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
        <div class="section-box-title-text">知识列表</div>
        <div class="section-box-title-more" @click.stop="goToAllcourse()">
          <span>查看全部</span>
        </div>
      </div>
      <div class="section-box-body">
        <KPCourseItem v-for="i in mains.items" :key="i.id" :item="i"></KPCourseItem>
      </div>
    </div>
    <div class="footer-box">
      <!-- <div class="inline-img">
        <img src="../assets/img/watermark@2x.png" alt="" />
      </div> -->
    </div>
    <kp-foot-nav type="home"></kp-foot-nav>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
// a.1 引入vant 轮播图相关组件
import Vue from "vue";
import { Swipe, SwipeItem, Lazyload } from "vant";
Vue.use(Swipe);
Vue.use(SwipeItem);
Vue.use(Lazyload);
import KPBlank from "@/components/KPBlank.vue";
import KPCourseItem from "@/components/KPCourseItem.vue";
import KPFootNav from "@/components/kp-foot-nav.vue";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    KPBlank,
    KPCourseItem,
    "kp-foot-nav": KPFootNav,
  },
  data() {
    //这里存放数据
    return {
      blocks: [],
      slides: [],
      navs: [],
      blank: {},
      h5GzhV1: [],
      mains: [],
      showQRcode: false,
      tables: [
        {
          src: require("../assets/img/icon-home-h@2x.png"),
          text: "首页",
        },
        {
          src: require("../assets/img/马上提问1.png"),
          text: "知识",
        },
        {
          src: require("../assets/img/icon-study-n@2x.png"),
          text: "问答",
        },
        {
          src: require("../assets/img/icon-me-n@2x.png"),
          text: "我的",
        },
      ],
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    topublish: function () {
      this.$router.push({
        path: "/publish",
      });
    },
    goToAllcourse: function () {
      this.$router.push({
        path: "/vod",
      });
    },
    goToDetail: function (item) {
      this.$router.push({
        path: "/vod/detail",
        query: {
          id: item.href.split("?")[1].split("=")[1],
        },
      });
    },

    show: function () {
      this.showQRcode = true;
    },
    hide: function () {
      this.showQRcode = false;
    },
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
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
    margin-top: 0;
    padding: 0.2667rem 0.4rem;
    height: 3.6rem;
    overflow: hidden;
    background: #fff;
    a {
      display: block;
    }
    img {
      width: 100%;
      height: 3.0667rem;
    }
  }
  .navs {
    display: flex;
    padding: 0.133333rem;
    background-color: white;
    padding: 0.6667rem 0.4rem 0.5333rem 0.4rem;
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
          text-align: center;
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
        border-radius: 50%;
        overflow: hidden;
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
