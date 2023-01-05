<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">收银台</div>
    </div>
    <div class="body-box">
      <div class="title">商品信息</div>
      <div class="item">
        <div class="img">
          <img src="../assets/img/default-vip.png" alt="" />
        </div>
        <div class="info">
          <div class="name">{{ message.name }}</div>
          <div class="lable">VIP会员</div>
          <div class="price">￥{{ message.charge }}</div>
        </div>
      </div>
    </div>
    <div class="boder-box">
      <div class="title">使用优惠码</div>
      <img src="../assets/img/icon-prev.png" alt="" />
    </div>
    <div class="border-box">
      <div class="left-box">
        <img src="../assets/img/payhand.png" alt="" />
        <span>{{ info.name }}</span>
        <div class="right-box">
          <img src="../assets/img/selected.png" alt="" />
        </div>
      </div>
    </div>
    <div class="bottom-box">
      <div class="left-box">
        总计￥
        <span class="price">{{ message.charge }}</span>
      </div>
      <div class="btn" @click="toBBPay">立即支付</div>
    </div>
  </div>
</template>

<script>
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
      order_id: {},
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
        path: "/Vipcenter",
      });
    },
    toBBPay: function () {
      this.$router.push({
        path: "/Remit",
        query: {
          orderId: this.order_id,
          charge: this.message.charge,
        },
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await this.$route.query;
    this.message = res;
    let data = await this.$request.get("http://1.14.239.98/api/v2/order/payments?scene=h5");
    this.info = data.data[0];
    let resData = await this.$request.post("/api/v2/order/role", {
      role_id: this.message.id,
    });
    this.order_id = resData.data.order_id;
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
    height: 4.3733rem;
    float: left;
    background: #fff;
    margin-top: 0.2667rem;
    padding: 0.4rem;

    div.title {
      width: 100%;
      height: 0.3733rem;
      font-size: 0.3733rem;
      font-weight: 400;
      color: #333;
      line-height: 0.3733rem;
      margin-bottom: 0.8rem;
    }

    div.item {
      width: 100%;
      height: 2rem;
      display: flex;
      flex-direction: row;
      div.img {
        width: 2.6667rem;
        height: 2rem;
        border-radius: 2px;
        margin-right: 0.4rem;
        img {
          width: 2.6667rem;
          height: 2rem;
          border-radius: 2px;
        }
      }

      div.info {
        width: 66%;
        height: 2rem;
        display: flex;
        flex-direction: column;
        div.name {
          width: 100%;
          height: 0.4rem;
          font-size: 0.4rem;
          font-weight: 600;
          color: #333;
          line-height: 0.4rem;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          margin-top: 0.0533rem;
          margin-bottom: 0.2667rem;
        }

        div.lable {
          height: 0.3467rem;
          font-size: 0.3467rem;
          font-weight: 400;
          color: #ff4d4f;
          line-height: 0.3467rem;
          margin-bottom: 0.48rem;
        }

        div.price {
          height: 0.4rem;
          font-size: 0.4rem;
          font-weight: 600;
          color: #fd3968;
          line-height: 0.4rem;
        }
      }
    }
  }
  div.boder-box {
    width: 100%;
    height: 1.52rem;
    float: left;
    background: #fff;
    margin-top: 0.2667rem;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding-left: 0.4rem;
    padding-right: 0.4rem;
    cursor: pointer;
    div.title {
      display: inline-block;
      width: auto;
      height: 0.4rem;
      font-size: 0.4rem;
      font-weight: 400;
      color: #333;
      line-height: 0.4rem;
    }

    img {
      width: 0.5067rem;
      height: 0.5067rem;
    }
  }
  div.border-box {
    width: 100%;
    float: left;
    height: auto;
    background: #fff;
    margin-top: 0.2667rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0.8rem 0.4rem;
    div.left-box {
      width: 100%;
      height: 0.8rem;
      display: flex;
      flex-direction: row;
      align-items: center;
      position: relative;
      img {
        width: 0.8rem;
        height: 0.8rem;
        margin-right: 0.4rem;
      }

      span {
        height: 0.48rem;
        font-size: 0.48rem;
        font-weight: 400;
        color: #333;
        line-height: 0.48rem;
      }
      div.right-box {
        width: 0.64rem;
        height: 0.64rem;
        position: absolute;
        right: 0;
        top: 0.08rem;

        img {
          width: 0.64rem;
          height: 0.64rem;
        }
      }
    }
  }
  div.bottom-box {
    width: 100%;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 1.3067rem;
    background: #fff;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    div.left-box {
      height: 0.64rem;
      font-size: 0.4267rem;
      font-weight: 500;
      color: #ff4d4f;
      line-height: 0.64rem;
      margin-left: 0.4rem;
      span.price {
        font-size: 0.64rem;
      }
    }

    div.btn {
      width: 3.84rem;
      height: 1.3067rem;
      background: #000;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 0.4267rem;
      font-weight: 500;
      color: #fff;
    }
  }
}
</style>
