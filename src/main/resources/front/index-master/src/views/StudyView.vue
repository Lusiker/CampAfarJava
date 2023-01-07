<!-- -->
<template>
  <div class="container" v-if="token" style="padding-top: 0px">
    <van-tabs v-model:active="active" line-width="20px">
      <van-tab title="订阅课程">
        <div class="mask"></div>
        <div class="body-box">
          <div
            class="body-item"
            v-for="item in subscriptions"
            :key="item.id"
            @click="todetail(item)"
          >
            <div class="left">
              <img :src="item.thumb" alt="" />
            </div>
            <div class="right">
              <div class="top">{{ item.title }}</div>
              <div class="bottom">订阅时间 {{ item.published_at | timeago }}</div>
            </div>
          </div>
        </div>
      </van-tab>
      <van-tab title="收藏课程">
        <div class="mask"></div>
        <div class="body-box">
          <div class="body-item" v-for="item in collects" :key="item.id" @click="todetail(item)">
            <div class="left">
              <img :src="item.thumb" alt="" />
            </div>
            <div class="right">
              <div class="top">{{ item.title }}</div>
              <div class="bottom">订阅时间 {{ item.published_at | timeago }}</div>
            </div>
          </div>
        </div>
      </van-tab>
    </van-tabs>
    <kp-foot-nav type="study"></kp-foot-nav>
  </div>
  <div class="container" v-else>
    <div class="img-box">
      <img src="../assets/img/img-placeholder.png" alt="" />
    </div>
    <div class="btn-box">
      <div class="btn" @click="tologin">登录查看</div>
    </div>
    <kp-foot-nav type="study"></kp-foot-nav>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
// a.1 引入vant 轮播图相关组件

import KPFootNav from "@/components/kp-foot-nav.vue";
import Vue from "vue";
import { Tab, Tabs } from "vant";

Vue.use(Tab);
Vue.use(Tabs);
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    "kp-foot-nav": KPFootNav,
  },
  data() {
    //这里存放数据

    return {
      token: [],
      active: 0,
      subscriptions: {},
      collects: {},
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    tologin: function () {
      this.$router.push({
        path: "/login-password",
      });
    },
    todetail: function (item) {
      this.$router.push({
        path: "/vod/detail",
        query: {
          id: item.id,
        },
      });
    },
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang="less" scoped>
div.container {
  div.img-box {
    width: 100%;
    height: auto;
    float: left;
    text-align: center;
    padding-bottom: 1.3333rem;
    img {
      width: 3.2rem;
      height: 3.2rem;
    }
  }

  div.btn-box {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    div.btn {
      width: 100%;
      height: 1.2267rem;
      box-sizing: border-box;
      line-height: 1.2267rem;
      background: #3ca7fa;
      border-radius: 0.1067rem;
      float: left;
      text-align: center;
      color: #fff;
      font-size: 0.4267rem;
      font-weight: 500;
    }
  }
  div.mask {
    width: 100%;
    height: 0.2667rem;
    background-color: #f3f6f9;
  }
  div.body-box {
    width: 100%;
    height: 100vh;
    overflow-y: scroll;
    float: left;
    background-color: #fff;
    box-sizing: border-box;
    padding: 0.4rem;
    div.body-item {
      width: 100%;
      height: auto;
      float: left;
      margin-bottom: 0.6667rem;
      display: flex;
      div.left {
        margin-right: 10px;
        img {
          width: 3.2rem;
          height: 2.4rem;
          border-radius: 0.1067rem;
        }
      }

      div.right {
        flex: 1;
        padding-top: 0.1333rem;
        div.top {
          width: 100%;
          height: 1.0667rem;
          float: left;
          font-size: 0.4rem;
          font-weight: 500;
          color: #171923;
          line-height: 0.5333rem;
          text-overflow: -o-ellipsis-lastline;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          line-clamp: 2;
          -webkit-box-orient: vertical;
          margin-bottom: 0.5867rem;
        }

        div.bottom {
          width: 100%;
          height: auto;
          float: left;
          font-size: 0.32rem;
          font-weight: 400;
          color: #999;
          line-height: 0.32rem;
        }
      }
    }
  }
}
</style>
