<!--  -->
<template>
  <div class="container" style="padding-top: 0px">
    <div class="title-box">
      <img src="../assets/img/icon-back.png" alt="" @click="goBack" />
      <div class="text">设置</div>
    </div>
    <div class="mask"></div>
    <div class="group-box" style="padding-top: 50px">
      <div class="group-item">
        <div class="name" @click="goTouser">用户协议</div>
      </div>
      <div class="group-item">
        <div class="name" @click="goTopro">隐私协议</div>
      </div>
      <div class="group-item">
        <div class="name" @click="goaboutus">关于我们</div>
      </div>
    </div>
    <div class="btn-box" v-if="token">
      <div class="btn" @click="deletToken">安全退出</div>
    </div>
    <div class="btn-box" v-else></div>
    <div class="bottom-text">
      <div class="item">Copyright 2022.</div>
      <div class="item">{{ data.webname }}版权所有</div>
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
      data: {
        token: [],
      },
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    goTouser: function () {
      window.open(this.data.user_protocol);
    },
    goTopro: function () {
      window.open(this.data.user_private_protocol);
    },
    goaboutus: function () {
      window.open(this.data.aboutus);
    },
    goBack: function () {
      this.$router.push({
        path: "/member",
      });
    },
    deletToken: function () {
      this.token = localStorage.clear();

      this.$router.push({
        path: "/member",
      });
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    this.token = localStorage.getItem("h5-token");
    let res = await this.$request.get("http://1.14.239.98/api/v2/other/config");
    this.data = res.data;
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
<style lang="less">
div.container {
  box-sizing: border-box;
  height: 100vh;
  background: #f6f6f6;
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
    line-height: 1.3333rem;
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
  div.mask {
    width: 100%;
    height: 0.4rem;
    background-color: #f3f6f9;
  }
  div.group-box {
    width: 100%;
    height: auto;
    float: left;
    background-color: #fff;
    div.group-item {
      width: 100%;
      height: auto;
      float: left;
      display: flex;
      box-sizing: border-box;
      padding: 0.2133rem 0.4rem;
      border-bottom: 1px solid rgba(0, 0, 0, 0.03);
      div.name {
        width: 4rem;
        height: auto;
        float: left;
        line-height: 0.96rem;
        font-size: 0.4rem;
        color: #333;
      }
    }
  }
  div.btn-box {
    width: 100%;
    height: auto;
    float: left;
    box-sizing: border-box;
    div.btn {
      width: 100%;
      height: auto;
      float: left;
      box-sizing: border-box;
      height: 1.0667rem;
      line-height: 1.0667rem;
      border-radius: 0.5333rem;
      text-align: center;
      color: #fff;
      background-color: #3ca7fa;
      font-size: 0.4rem;
    }
  }
  div.bottom-text {
    width: 100%;
    height: auto;
    float: left;
    padding-top: 3.2rem;
    text-align: center;
    background-color: #f6f6f6;
    padding-bottom: 8rem;
    div.item {
      font-size: 0.2667rem;
      color: rgba(0, 0, 0, 0.4);
      line-height: 0.5333rem;
    }
  }
}
</style>
