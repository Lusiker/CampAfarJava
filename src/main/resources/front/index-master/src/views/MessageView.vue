<!--  -->
<template>
  <div class="container">
    <div class="navbox">
      <img src="../assets/img/icon-back.png" alt="" @click="toback" />
      <div class="text">我的消息</div>
    </div>
    <div class="item" v-for="item in messageItems" :key="item.id">
      <div class="contend" v-text="item.data.message"></div>
      <div class="date">{{ item.created_at | timeago }}</div>
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
      messageItems: [],
    };
  },
  //监听属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    toback: function () {
      this.$router.go(-1);
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  async created() {
    let res = await this.$request.get(
      "http://1.14.239.98/api/v2/member/messages?page=1&page_size=20"
    );
    this.messageItems = res.data.data;
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
  background: #f6f6f6;
  padding-top: 50px;
  height: 100vh;
  overflow-y: scroll;
  div.navbox {
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
  div.item {
    width: 100%;
    height: auto;
    float: left;
    padding: 0.2667rem 0.4rem;
    background-color: #fff;
    border-bottom: 1px solid rgba(0, 0, 0, 0.05);
    div.contend {
      width: 100%;
      height: auto;
      float: left;
      font-size: 0.3733rem;
      color: rgba(0, 0, 0, 0.3);
      line-height: 0.5333rem;
      margin-bottom: 0.2667rem;
      word-break: break-all;
    }

    div.date {
      width: 100%;
      height: auto;
      float: left;
      font-size: 0.2667rem;
      color: rgba(0, 0, 0, 0.3);
    }
  }
}
</style>
