<!--  -->
<template>
  <div class="container">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="toBack" />
      <div class="text">VIP会员</div>
    </div>
    <div class="info">
      <div class="img">
        <img :src="information.avatar" alt="" />
      </div>
      <div class="info-right">
        <div class="info-text">
          <div class="top">
            您的{{ information.role.name }}{{ information.role_expired_at }}到期
          </div>
          <div class="bottom">购买后将会有效期顺延</div>
        </div>
      </div>
    </div>
    <div class="info-box">
      <div
        class="info-item"
        v-for="(item, index) in price"
        :key="item.id"
        @click="tochoice(index, item.id, item.name, item.charge)"
      >
        <div class="name">{{ item.name }}</div>
        <div class="price">
          <span class="small">￥</span>
          {{ item.charge }}
        </div>
        <div class="desc">
          <div>{{ item.desc_rows[0] }}</div>
        </div>
      </div>
    </div>
    <div class="btn" @click="toPay">购买会员</div>
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
      information: {
        role: {},
      },
      price: {},
      id: 0,
      name: "",
      charge: 0,
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
    tochoice: function (index, id, name, charge) {
      let lis = document.getElementsByClassName("info-item");
      for (let i = 0; i < lis.length; i++) {
        lis[i].className = "info-item";
      }
      lis[index].className = "info-item active";
      this.id = id;
      this.name = name;
      this.charge = charge;
    },
    toPay: function () {
      this.$router.push({
        path: "/checkstand",
        query: {
          id: this.id,
          name: this.name,
          charge: this.charge,
        },
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await this.$request.get("http://1.14.239.98/api/v2/member/detail");
    this.information = res.data;
    let result = await this.$request.get("http://1.14.239.98/api/v2/roles");
    this.price = result.data;
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
  div.info {
    width: 100%;
    height: auto;
    float: left;
    display: flex;
    padding: 0.5333rem 0.4rem;
    box-sizing: border-box;
    margin-top: 0.4rem;
    margin-bottom: 0.4rem;
    background-color: #fff;
    margin-top: 15px;
    margin-bottom: 15px;
    div.img {
      width: 1.28rem;
      height: 1.28rem;
      border-radius: 50%;
      img {
        width: 1.28rem;
        height: 1.28rem;
        border-radius: 50%;
      }
    }

    div.info-right {
      padding-top: 0.1067rem;
      margin-left: 0.4rem;
      flex: 1;
      font-size: 0.3467rem;
      font-weight: 400;
      color: #333;
      line-height: 0.5333rem;
      div.info-text {
        font-size: 0.3467rem;
        font-weight: 400;
        color: #333;
        line-height: 0.5333rem;
      }
    }
  }
  div.info-box {
    width: 100%;
    height: auto;
    float: left;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    box-sizing: border-box;
    padding: 0.4rem;
    div.info-item {
      width: calc(50% - 5px);
      height: auto;
      float: left;
      padding: 0.4rem;
      box-sizing: border-box;
      border-radius: 0.2133rem;
      margin-bottom: 0.4rem;
      border: 1px solid #fff;
      background-color: #fff;
      div.name {
        width: 100%;
        height: auto;
        float: left;
        font-size: 0.3733rem;
        font-weight: 600;
        color: #e1a500;
        line-height: 0.3733rem;
        margin-bottom: 0.5333rem;
      }

      div.price {
        width: 100%;
        height: auto;
        float: left;
        font-size: 0.7467rem;
        font-weight: 600;
        color: #333;
        line-height: 0.64rem;
        margin-bottom: 0.4rem;
        span.small {
          font-size: 0.48rem;
        }
      }

      div.desc {
        width: 100%;
        height: auto;
        float: left;
        font-size: 0.32rem;
        font-weight: 400;
        color: #666;
        line-height: 0.48rem;
        div {
          font-size: 0.32rem;
          font-weight: 400;
          color: #666;
          line-height: 0.48rem;
        }
      }
    }
    div.active {
      border: 1px solid #e1a500;
      background: #ffeded;
    }
  }
  div.btn {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    width: 100%;
    height: 1.3067rem;
    background-color: #cea200;
    line-height: 1.3067rem;
    text-align: center;
    color: #fff;
    font-size: 0.4267rem;
  }
  
}
</style>
