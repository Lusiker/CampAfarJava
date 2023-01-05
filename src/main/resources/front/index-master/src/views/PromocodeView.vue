<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">我的邀请码</div>
    </div>
    <div class="body-box">
      <div class="top-box">
        <div class="left">
          <div class="value">{{ mains.code }}</div>
          <div class="title">我的邀请码</div>
          <div class="btn">复制</div>
        </div>
        <div class="right">
          <div class="value">￥0</div>
          <div class="title">邀请奖励金</div>
        </div>
      </div>
      <div class="bottom-box">
        <p>邀请者分享邀请链接给其他人下单成功后，将的到如下对应金额奖励：</p>
        <p>邀请人奖励：0元</p>
        <p>被邀请人奖励：0元</p>
        <p>订单抽成：0%</p>
      </div>
    </div>
    <div class="tab">
      <van-tabs v-model:active="active" line-width="20px">
        <van-tab title=" 邀请记录 "
          ><div class="img-box">
            <img src="../assets/img/img-placeholder.png" alt="" /></div
        ></van-tab>
        <van-tab title=" 资金明细 "
          ><div class="img-box">
            <img src="../assets/img/img-placeholder.png" alt="" /></div
        ></van-tab>
        <van-tab title=" 提现明细 "
          ><div class="img-box">
            <img src="../assets/img/img-placeholder.png" alt="" /></div
        ></van-tab>
      </van-tabs>
    </div>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import Vue from "vue";
import { Tab, Tabs } from "vant";
Vue.use(Tab);
Vue.use(Tabs);
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      mains: {},
      active: 0,
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    toBack: function () {
      this.$router.push({
        path: "/member",
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await this.$request.get("http://1.14.239.98/api/v2/member/promoCode");
    this.mains = res.data;
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
div.container {
  box-sizing: border-box;
  padding-top: 50px;
  background: #f6f6f6;
  height: 100vh;
  overflow-y: scroll;
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
  div.body-box {
    width: 100%;
    max-height: 9.4667rem;
    background: #1784ed;
    padding: 0.8rem 0.4rem 0.5333rem 0.4rem;
    div.top-box {
      width: 9.2rem;
      background: linear-gradient(180deg, #fff6cf, #ffeaa1);
      border-radius: 4px;
      margin: 0 auto;
      display: flex;
      flex-direction: row;
      div.left {
        width: 50%;
        display: flex;
        flex-direction: column;
        padding-top: 0.8rem;
        padding-bottom: 0.8rem;
        padding-left: 0.4rem;
        box-sizing: border-box;
        div.value {
          width: 100%;
          font-size: 0.5333rem;
          font-weight: 500;
          color: #333;
          line-height: 0.5333rem;
          margin-bottom: 0.5333rem;
          text-align: center;
        }

        div.title {
          display: inline-block;
          width: auto;
          height: 0.3467rem;
          font-size: 0.3467rem;
          font-weight: 400;
          color: #666;
          line-height: 0.3467rem;
          margin-left: 1.3333rem;
        }

        div.btn {
          width: 2.56rem;
          height: 0.8rem;
          border-radius: 0.4rem;
          border: 1px solid #1784ed;
          cursor: pointer;
          margin-top: 0.5333rem;
          font-size: 0.3733rem;
          font-weight: 400;
          color: #1784ed;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-left: 1.3333rem;
        }
      }

      div.right {
        width: 50%;
        display: flex;
        flex-direction: column;
        padding-top: 0.8rem;
        padding-bottom: 0.8rem;
        padding-right: 0.4rem;
        box-sizing: border-box;
        div.value {
          width: 100%;
          font-size: 0.5333rem;
          font-weight: 500;
          color: #333;
          line-height: 0.5333rem;
          margin-bottom: 0.5333rem;
          text-align: center;
        }

        div.title {
          display: inline-block;
          width: auto;
          height: 0.3467rem;
          font-size: 0.3467rem;
          font-weight: 400;
          color: #666;
          line-height: 0.3467rem;
          margin-left: 1.3333rem;
        }
      }
    }

    div.bottom-box {
      width: 100%;
      height: 3.1733rem;
      display: flex;
      flex-direction: column;
      margin-top: 0.5867rem;
      p {
        width: 100%;
        font-size: 0.3467rem;
        font-weight: 400;
        color: #fff;
        line-height: 0.5067rem;
        margin-bottom: 0.2133rem;
      }
    }
  }
  div.tab {
    width: 100%;
    display: flex;
    flex-direction: column;
    div.img-box {
      width: 100%;
      height: auto;
      float: left;
      text-align: center;
      padding-top: 1.3333rem;
      padding-bottom: 1.3333rem;
      img {
        width: 3.2rem;
        height: 3.2rem;
      }
    }
  }
}
</style>
