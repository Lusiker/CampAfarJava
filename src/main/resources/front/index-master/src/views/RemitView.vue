<!--  -->
<template>
  <div class="container">
    <div class="head">
      <img src="../assets/img/icon-back.png" alt="" @click.stop="back" />
      <div class="title">手动打款支付</div>
    </div>
    <div class="order-box">
      <div class="order-id">
        订单号：<span>{{ message.orderId }}</span>
      </div>
      <div class="price">
        需支付：<span
          >￥<strong>{{ message.charge }}</strong></span
        >
      </div>
    </div>
    <div class="pay-box">
      <div class="text">收款信息<span>（手动打款需后台验证后交付订单，请耐心等待）</span></div>
      <div class="code-box" v-html="info.text"></div>
    </div>
    <div class="btn">
      <div class="text" @click="tomember">我已完成支付</div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    //这里存放数据
    return {
      message: {},
      info: {},
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    back: function () {
      this.$router.push({
        path: "/Member",
      });
    },
    tomember: function () {
      this.$router.push({
        path: "/",
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await this.$route.query;
    this.message = res;
    let resData = await this.$request.get("/api/v2/order/pay/handPay");
    this.info = resData.data;
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
  box-sizing: border-box;
  padding-top: 1.333333rem;
  background: #f6f6f6;
  position: absolute;
  left: 0;
  top: 0;
  overflow-y: scroll;
  overflow-x: hidden;
  width: 100%;
  height: 100%;
  .head {
    position: fixed;
    top: 0;
    width: 100%;
    height: 1.333333rem;
    background-color: #fff;
    z-index: 100;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    display: flex;
    flex-direction: row;
    justify-content: center;
    border-bottom: 0.026667rem solid #f1f2f6;
    align-items: center;
    img {
      position: absolute;
      left: 0.4rem;
      width: 0.506667rem;
      height: 0.506667rem;
    }
    .title {
      width: 72%;
      height: auto;
      font-size: 0.453333rem;
      font-weight: 500;
      text-align: center;
      color: #333;
      line-height: 0.586667rem;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .order-box {
    width: 100%;
    height: 2.8rem;
    background: #fff;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0.533333rem 0.4rem;
    margin-top: 0.266667rem;
    .order-id {
      width: 100%;
      height: 0.4rem;
      font-size: 0.4rem;
      font-weight: 600;
      color: #333;
      line-height: 0.4rem;
      margin-bottom: 0.88rem;
      span {
        margin-left: 0.266667rem;
        font-weight: 400;
        color: #666;
      }
    }
    .price {
      width: 100%;
      height: 0.533333rem;
      font-size: 0.4rem;
      font-weight: 600;
      color: #333;
      line-height: 0.533333rem;
      span {
        height: 0.533333rem;
        font-size: 0.373333rem;
        font-weight: 600;
        color: #ff5858;
        line-height: 0.533333rem;
        strong {
          font-size: 0.48rem;
        }
      }
    }
  }
  .pay-box {
    width: 100%;
    height: auto;
    background: #fff;
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    padding: 0.533333rem 0.4rem;
    margin-top: 0.266667rem;
    .text {
      width: 100%;
      height: 0.4rem;
      font-size: 0.4rem;
      font-weight: 600;
      color: #333;
      line-height: 0.4rem;
      span {
        font-size: 0.32rem;
        line-height: 0.4rem;
        font-weight: 400;
      }
    }
    .code-box {
      width: 100%;
      height: auto;
      font-size: 0.4rem;
      font-weight: 400;
      color: #666;
      line-height: 0.64rem;
      margin-top: 0.533333rem;
      p {
        margin: 0;
        display: block;
      }
    }
  }
  .btn {
    width: 100%;
    box-sizing: border-box;
    height: 1.226667rem;
    padding: 0 0.4rem;
    margin-top: 0.8rem;
    display: flex;
    justify-content: center;
    .text {
      width: 100%;
      height: 1.226667rem;
      background: #3ca7fa;
      border-radius: 0.106667rem;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 0.426667rem;
      font-weight: 400;
      color: #fff;
    }
  }
}
</style>
