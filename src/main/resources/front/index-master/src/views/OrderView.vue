<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">我的订单</div>
    </div>
    <div class="body">
      <div class="order-item" v-for="item in mains" :key="item.id">
        <div class="title">
          <div class="time">{{ item.created_at | timeago }}</div>
          <div class="status">{{ item.status_text }}</div>
        </div>
        <div class="bottom-box">
          <div class="left">
            <div class="name">{{ item.goods[0].goods_name }}</div>
            <div class="payment">{{ item.payment_text }}</div>
          </div>
          <div class="right">
            <span>￥</span>
            {{ item.goods[0].goods_ori_charge }}
          </div>
        </div>
      </div>
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
      mains: {},
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
    let res = await this.$request.get("http://1.14.239.98/api/v2/member/orders?", {
      params: {
        page: 1,
        page_size: 500,
      },
    });
    this.mains = res.data.data;
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
  div.body {
    div.order-item {
      width: 100%;
      height: auto;
      overflow-y: scroll;
      float: left;
      box-sizing: border-box;
      padding: 0.4rem;
      background-color: #fff;
      border-bottom: 1px solid rgba(0, 0, 0, 0.05);
      div.title {
        width: 100%;
        height: auto;
        float: left;
        display: flex;
        padding-bottom: 0.4rem;
        div.time {
          flex: 1;
          font-size: 0.2667rem;
          font-weight: 400;
          color: #999;
          line-height: 0.2667rem;
        }

        div.status {
          flex: 1;
          text-align: right;
          font-size: 0.3467rem;
          font-weight: 400;
          color: #999;
          line-height: 0.3467rem;
        }
      }

      div.bottom-box {
        width: 100%;
        height: auto;
        float: left;
        display: flex;
        div.left {
          flex: 1;
          div.name {
            width: 100%;
            height: auto;
            float: left;
            font-size: 0.4rem;
            font-weight: 400;
            color: #333;
            line-height: 0.4rem;
            padding-bottom: 0.4rem;
          }

          div.payment {
            width: 100%;
            height: auto;
            float: left;
            font-size: 0.2667rem;
            font-weight: 400;
            color: #999;
            line-height: 0.2667rem;
          }
        }

        div.right {
          width: 3.2rem;
          height: auto;
          text-align: right;
          font-size: 0.5333rem;
          font-weight: 400;
          color: #333;
          line-height: 0.5333rem;
          padding-top: 0.2667rem;
          span {
            font-size: 0.2667rem;
          }
        }
      }
    }
  }
}
</style>
